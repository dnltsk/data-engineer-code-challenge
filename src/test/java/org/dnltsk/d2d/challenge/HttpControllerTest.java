package org.dnltsk.d2d.challenge;

import com.vividsolutions.jts.io.ParseException;
import org.dnltsk.d2d.challenge.model.StatsRequest;
import org.dnltsk.d2d.challenge.parse.BboxConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dnltsk.d2d.challenge.TestDataRepository.smallTestData;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class HttpControllerTest {

    @Mock
    private RequestHandler requestHandler;

    @Spy
    private BboxConverter bboxConverter;

    @InjectMocks
    private HttpController httpController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void incoming_csv_is_forwarded_to_parser() throws URISyntaxException {
        httpController.postTrips(smallTestData);
        verify(requestHandler).handleTripsAsCsv(smallTestData);
    }

    @Test
    public void incoming_request_parameters_are_preprocess_as_expected() throws ParseException {
        httpController.getStats("DUMMY-Region", 0.0f, 0.0f, 1.0f, 1.0f);

        ArgumentCaptor<StatsRequest> statsRequest = ArgumentCaptor.forClass(StatsRequest.class);
        verify(requestHandler).loadDailyStats(statsRequest.capture());
        assertThat(statsRequest.getValue().getRegion()).isEqualTo("dummy-region");
        assertThat(statsRequest.getValue().getBbox().isValid())
            .as("bbox geometry is valid")
            .isTrue();
    }
}