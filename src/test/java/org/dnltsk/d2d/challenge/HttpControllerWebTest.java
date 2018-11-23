package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.model.Stats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NoDbConfiguration.class, webEnvironment = RANDOM_PORT)
public class HttpControllerWebTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void POST_trips_should_respond_with_CREATED() {
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/trips", TestDataRepository.smallTestData, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    public void GET_stats_should_respond_with_OK() {

        String query = "?region=dummy-region"
            + "&minLat=0.0"
            + "&minLon=0.0"
            + "&maxLat=0.0"
            + "&maxLon=0.0";

        ResponseEntity<Stats> response = testRestTemplate.getForEntity("/stats" + query, Stats.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void actuator_metrics_endpoint_is_available() {
        ResponseEntity<Void> response = testRestTemplate.getForEntity("/actuator/metrics" , Void.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void actuator_info_endpoint_is_available() {
        ResponseEntity<Void> response = testRestTemplate.getForEntity("/actuator/info" , Void.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void actuator_health_endpoint_is_available() {
        ResponseEntity<Void> response = testRestTemplate.getForEntity("/actuator/health" , Void.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}