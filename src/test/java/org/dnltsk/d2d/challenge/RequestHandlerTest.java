package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.parse.TripParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.dnltsk.d2d.challenge.TestDataRepository.smallTestData;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class RequestHandlerTest {

    @Mock
    private TripParser parser;

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
}