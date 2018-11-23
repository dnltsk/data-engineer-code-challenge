package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.model.DailyStats;
import org.dnltsk.d2d.challenge.model.DailyStatsResponse;
import org.dnltsk.d2d.challenge.model.Trip;
import org.dnltsk.d2d.challenge.parse.TripParser;
import org.dnltsk.d2d.challenge.write.DbManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestHandler {

    @Autowired
    private TripParser parser;

    @Autowired
    private DbManager dbManager;

    public DailyStatsResponse loadDailyStats(String region) {
        List<DailyStats> dailyStats = dbManager.loadDailyStats(region);
        DailyStatsResponse response = new DailyStatsResponse();
        response.setDailyStats(dailyStats);
        return response;
    }

    public void handleTripsAsCsv(String tripsAsCsv) {

        List<Trip> trips = parser.parse(tripsAsCsv)
            .toList()
            .toBlocking()
            .single();

        dbManager.insertTrips(trips);

    }

}
