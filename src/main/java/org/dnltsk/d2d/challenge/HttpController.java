package org.dnltsk.d2d.challenge;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class HttpController {

    @PostMapping("/trips")
    public ResponseEntity<Void> postTrips(@RequestBody String tripsCsv) throws URISyntaxException {
        return ResponseEntity.created(new URI("/trip/1")).build();
    }


}
