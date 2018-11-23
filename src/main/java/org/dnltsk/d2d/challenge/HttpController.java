package org.dnltsk.d2d.challenge;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dnltsk.d2d.challenge.model.DailyStatsResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RequestHandler requestHandler;

    @ApiOperation(value = "post new trips as csv")
    @PostMapping("/trips")
    public ResponseEntity<Void> postTrips(
        @ApiParam(name = "tripsAsCsv", value = "csv to upload.<ul><li>header like 'region,origin_coord,destination_coord,datetime,datasource' is expected</li><li>first row is getting ignored</li></ul>")
        @RequestBody String tripsAsCsv
    ) throws URISyntaxException {
        requestHandler.handleTripsAsCsv(tripsAsCsv);
        return ResponseEntity.created(new URI("")).build();

        /* // My attempt for an async approach (results in too many open db connection of course 😞)

        Observable.just(tripsAsCsv)
            .subscribeOn(Schedulers.newThread())
            .doOnNext(csv -> {
                requestHandler.handleTripsAsCsv(csv);
            }).subscribe();
        return ResponseEntity.accepted().build();

        */
    }

    @ApiOperation(value = "get statistics")
    @GetMapping("/stats")
    public ResponseEntity<DailyStatsResponse> postTrips(
        @ApiParam(name = "region", value = "region to filter (case-insensitive)", defaultValue = "prague")
        @RequestParam String region,
        @ApiParam(name = "minLat", value = "minLat/minY of bbox filter", defaultValue = "52.0")
        @RequestParam Float minLat,
        @ApiParam(name = "minLon", value = "minLon/minX of bbox filter", defaultValue = "13.0")
        @RequestParam Float minLon,
        @ApiParam(name = "maxLat", value = "maxLat/maxY of bbox filter", defaultValue = "53.0")
        @RequestParam Float maxLat,
        @ApiParam(name = "maxLon", value = "maxLon/maxX of bbox filter", defaultValue = "14.0")
        @RequestParam Float maxLon
    ) {
        return ResponseEntity.ok(requestHandler.loadDailyStats(region));
    }

}
