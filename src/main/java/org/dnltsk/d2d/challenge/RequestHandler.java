package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.db.DbManager;
import org.dnltsk.d2d.challenge.model.DailyStats;
import org.dnltsk.d2d.challenge.model.DailyStatsResponse;
import org.dnltsk.d2d.challenge.model.StatsRequest;
import org.dnltsk.d2d.challenge.model.Trip;
import org.dnltsk.d2d.challenge.parse.TripParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestHandler {

    @Autowired
    private TripParser parser;

    @Autowired
    private DbManager dbManager;

    public DailyStatsResponse loadDailyStats(StatsRequest statsRequest) {
        List<DailyStats> dailyStats = dbManager.loadDailyStats(statsRequest);
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
