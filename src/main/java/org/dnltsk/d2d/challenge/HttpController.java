package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.model.Stats;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class HttpController {

    @PostMapping("/trips")
    public ResponseEntity<Void> postTrips(
        @RequestBody String tripsCsv
    ) throws URISyntaxException {
        return ResponseEntity.created(new URI("/trip/1")).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Stats> postTrips(
        @RequestParam String region,
        @RequestParam Double minLat,
        @RequestParam Double minLon,
        @RequestParam Double maxLat,
        @RequestParam Double maxLon
    ) {
        return ResponseEntity.ok(new Stats());
    }

}
