package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.parse.TripParser;
import org.dnltsk.d2d.challenge.write.DbWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import static org.dnltsk.d2d.challenge.TestDataRepository.largeTestData;
import static org.dnltsk.d2d.challenge.TestDataRepository.smallTestData;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class RequestHandlerTest {

    @Spy
    private TripParser parser;

    @Mock
    private DbWriter writer;

    @InjectMocks
    private RequestHandler requestHandler;

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
        verify(writer).insertTrip(any());
    }

    @Test
    public void large_csv_is_forwarded_in_chunks_to_writer() {
        requestHandler.handleTripsAsCsv(largeTestData);
        verify(writer, times(10)).insertTrip(any());
    }
}