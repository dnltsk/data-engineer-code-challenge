package org.dnltsk.d2d.challenge;

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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class HttpControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void POST_trips_should_respond_with_CREATED() {
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/trips", TestDataRepository.smallTestData, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
    }
}