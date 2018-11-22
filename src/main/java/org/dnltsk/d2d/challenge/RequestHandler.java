package org.dnltsk.d2d.challenge;

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

    public void handleTripsAsCsv(String tripsAsCsv) {

        List<Trip> trips = parser.parse(tripsAsCsv)
            .map(trip -> {
                return trip;
            })
            .toList()
            .toBlocking()
            .single();

        dbManager.inserteTrips(trips);
    }

}
