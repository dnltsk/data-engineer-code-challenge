package org.dnltsk.d2d.challenge.parse;

import org.dnltsk.d2d.challenge.model.GridCell;
import org.dnltsk.d2d.challenge.model.Trip;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observable;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dnltsk.d2d.challenge.TestDataRepository.smallTestData;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TripParserTest {

    @InjectMocks
    private TripParser parser;

    @Mock
    private GridCellCalculator gridCellCalculator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(gridCellCalculator.calcGridCell(any()))
            .thenReturn(GridCell.builder().build());
    }

    @Test
    public void number_of_rows_is_correct() {
        Observable<Trip> trips = parser.parse(smallTestData);
        assertThat(trips.toList().toBlocking().single().size()).isEqualTo(2);
        trips.test();
    }

    @Test
    public void trip_is_parsed_correctly() {
        Observable<Trip> trips = parser.parse(smallTestData);
        Trip trip = trips.first().toBlocking().single();
        assertThat(trip.getRegion()).isEqualTo("prague");
        assertThat(trip.getOrigin().toText()).isEqualTo("POINT (14.4973794438195 50.00136875782316)");
        assertThat(trip.getOriginGridCell()).isNotNull();
        assertThat(trip.getDestination().toText()).isEqualTo("POINT (14.43109483523328 50.04052930943246)");
        assertThat(trip.getDestinationGridCell()).isNotNull();
        assertThat(trip.getDatetime()).isEqualTo(Instant.parse("2018-05-28T09:03:40Z"));
        assertThat(trip.getDayOfWeek()).isEqualTo(1); //monday
        assertThat(trip.getHourOfDay()).isEqualTo(9);
        assertThat(trip.getDatasource()).isEqualTo("funny_car");
    }


}