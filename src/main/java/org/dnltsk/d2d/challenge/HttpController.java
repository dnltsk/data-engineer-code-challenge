package org.dnltsk.d2d.challenge;

import com.vividsolutions.jts.io.ParseException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dnltsk.d2d.challenge.model.DailyStatsResponse;
import org.dnltsk.d2d.challenge.model.StatsRequest;
import org.dnltsk.d2d.challenge.parse.BboxConverter;
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

    @Autowired
    private BboxConverter bboxConverter;

    @ApiOperation(value = "post new trips as csv")
    @PostMapping("/trips")
    public ResponseEntity<Void> postTrips(
        @ApiParam(name = "tripsAsCsv", value = "csv to upload." +
            "\n\nheader is expected (first row is getting ignored)." +
            "\n\nsee sample csv at https://github.com/dnltsk/data-engineer-code-challenge/blob/master/trips.csv")
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
    public ResponseEntity<DailyStatsResponse> getStats(
        @ApiParam(name = "region", value = "region to filter (case-insensitive)", defaultValue = "prague")
        @RequestParam String region,
        @ApiParam(name = "minLat", value = "minLat/minY of bbox filter", defaultValue = "53.0")
        @RequestParam Float minLat,
        @ApiParam(name = "minLon", value = "minLon/minX of bbox filter", defaultValue = "10.0")
        @RequestParam Float minLon,
        @ApiParam(name = "maxLat", value = "maxLat/maxY of bbox filter", defaultValue = "54.0")
        @RequestParam Float maxLat,
        @ApiParam(name = "maxLon", value = "maxLon/maxX of bbox filter", defaultValue = "11.0")
        @RequestParam Float maxLon
    ) throws ParseException {
        StatsRequest statsRequest = StatsRequest.builder()
            .region(region.toLowerCase())
            .bbox(bboxConverter.convert(minLon, minLat, maxLon, maxLat))
            .build();
        return ResponseEntity.ok(requestHandler.loadDailyStats(statsRequest));
    }

}
