package org.dnltsk.d2d.challenge.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SwaggerConfigurationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void SwaggerUI_is_available() {
        ResponseEntity<Void> response = testRestTemplate.getForEntity("/swagger-ui.html", Void.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }
}