package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.parse.TripParser;
import org.dnltsk.d2d.challenge.write.DbWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestHandler {

    @Autowired
    private TripParser parser;

    @Autowired
    private DbWriter writer;

    public void handleTripsAsCsv(String tripsAsCsv) {
        parser.parse(tripsAsCsv)
            .buffer(10)
            .forEach(trips -> {
                writer.insertTrip(trips);
            });
    }

}
