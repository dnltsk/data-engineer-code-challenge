package org.dnltsk.d2d.challenge;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "post new trips as csv")
    @PostMapping("/trips")
    public ResponseEntity<Void> postTrips(
        @ApiParam(name = "tripsCsv", value="csv to upload")
        @RequestBody String tripsCsv
    ) throws URISyntaxException {
        return ResponseEntity.created(new URI("/trip/1")).build();
    }

    @ApiOperation(value = "get statistics")
    @GetMapping("/stats")
    public ResponseEntity<Stats> postTrips(
        @ApiParam(name = "region", value="region to filter (case-insensitive)", defaultValue = "prague")
        @RequestParam String region,
        @ApiParam(name = "minLat", value="minLat/minY of bbox filter", defaultValue = "52.0")
        @RequestParam Double minLat,
        @ApiParam(name = "minLon", value="minLon/minX of bbox filter", defaultValue = "13.0")
        @RequestParam Double minLon,
        @ApiParam(name = "maxLat", value="maxLat/maxY of bbox filter", defaultValue = "53.0")
        @RequestParam Double maxLat,
        @ApiParam(name = "maxLon", value="maxLon/maxX of bbox filter", defaultValue = "14.0")
        @RequestParam Double maxLon
    ) {
        return ResponseEntity.ok(new Stats());
    }

}
