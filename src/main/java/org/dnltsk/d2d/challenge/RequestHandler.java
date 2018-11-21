package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.model.Trip;
import org.dnltsk.d2d.challenge.parse.TripParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

@Service
public class RequestHandler {

    @Autowired
    private TripParser parser;

    public void handleTripsAsCsv(String tripsAsCsv){
        Observable<Trip> parsedTrips = parser.parse(tripsAsCsv);
    }

}
