package org.dnltsk.d2d.challenge.write;

import org.dnltsk.d2d.challenge.DatabasePool;
import org.dnltsk.d2d.challenge.model.Trip;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class DbManagerTest {

    @InjectMocks
    private DbManager manager;

    @Mock
    private DatabasePool databasePool;

    @Mock
    private DbWriter writer;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        when(databasePool.openJdbcConnection()).thenReturn(mock(Connection.class));
    }

    @Test
    public void distinct_regions_are_extracted() {
        List<Trip> trips = Arrays.asList(
            Trip.builder().region("region-foo").build(),
            Trip.builder().region("region-foo").build(),// <- duplication
            Trip.builder().region("region-bar").build(),
            Trip.builder().region("region-bar").build() // <- duplication
        );
        manager.insertTrips(trips);

        ArgumentCaptor<List<String>> argument = ArgumentCaptor.forClass((Class<List<String>>) (Class) List.class);
        verify(writer).insertRegions(argument.capture(), any());
        assertThat(argument.getValue()).containsExactly("region-foo", "region-bar");
    }

    @Test
    public void distinct_datasources_are_extracted() {
        List<Trip> trips = Arrays.asList(
            Trip.builder().datasource("ds-foo").build(),
            Trip.builder().datasource("ds-foo").build(),// <- duplication
            Trip.builder().datasource("ds-bar").build(),
            Trip.builder().datasource("ds-bar").build() // <- duplication
        );

        manager.insertTrips(trips);

        ArgumentCaptor<List<String>> argument = ArgumentCaptor.forClass((Class<List<String>>) (Class) List.class);
        verify(writer).insertDatasources(argument.capture(), any());
        assertThat(argument.getValue()).containsExactly("ds-foo", "ds-bar");
    }

    @Test
    public void large_number_of_trips_are_inserted_in_chunks() {
        List<Trip> lotsOfTrips = Observable.range(1, 35)
            .map(i -> Trip.builder().build())
            .toList().toBlocking().single();

        manager.insertTrips(lotsOfTrips);

        verify(writer, times(4)).insertChunkOfTrips(any(), any());
    }
}