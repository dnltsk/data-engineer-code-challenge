package org.dnltsk.d2d.challenge.parse;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.dnltsk.d2d.challenge.model.Trip;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.io.StringReader;
import java.util.Arrays;

@Service
public class TripParser {

    public Observable<Trip> parse(String tripsAsCsv) {
        CSVReader csvReader = new CSVReaderBuilder(new StringReader(tripsAsCsv))
            .withSkipLines(1)
            .build();
        return Observable
            .from(csvReader)
            .map(csvRow -> {
                    System.out.println(Arrays.toString(csvRow));
                    return new Trip();
                }
            );

    }
}
