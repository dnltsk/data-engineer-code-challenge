package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.model.DailyStats;
import org.dnltsk.d2d.challenge.model.DailyStatsResponse;
import org.dnltsk.d2d.challenge.parse.TripParser;
import org.dnltsk.d2d.challenge.write.DbManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dnltsk.d2d.challenge.TestDataRepository.smallTestData;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RequestHandlerTest {

    @InjectMocks
    private RequestHandler requestHandler;

    @Spy
    private TripParser parser;

    @Mock
    private DbManager manager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void incoming_csv_is_forwarded_to_parser() {
        requestHandler.handleTripsAsCsv(smallTestData);
        verify(parser).parse(smallTestData);
    }

    @Test
    public void small_csv_is_forwarded_to_writer() {
        requestHandler.handleTripsAsCsv(smallTestData);
        verify(manager).insertTrips(any());
    }

    @Test
    public void loaded_dailyStats_are_wrapped_into_a_response_object() {
        List<DailyStats> dummyStats = Arrays.asList(new DailyStats(), new DailyStats());
        when(manager.loadDailyStats("dummy-region")).thenReturn(dummyStats);

        DailyStatsResponse dailyStatsResponse = requestHandler.loadDailyStats("dummy-region");

        assertThat(dailyStatsResponse.getDailyStats()).isEqualTo(dummyStats);
    }
}