package org.dnltsk.d2d.challenge.parse;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.dnltsk.d2d.challenge.model.Trip;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.io.StringReader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Service
public class TripParser {

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd HH:mm:ss")
        .toFormatter()
        .withZone(ZoneId.of("UTC"));

    public Observable<Trip> parse(String tripsAsCsv) {
        CSVReader csvReader = new CSVReaderBuilder(new StringReader(tripsAsCsv))
            .withSkipLines(1)
            .build();
        return Observable
            .from(csvReader)
            .map(csvRow -> {
                return Trip.builder()
                    .region(csvRow[0].toLowerCase())
                    .originAsWkt(csvRow[1])
                    .destinationAsWkt(csvRow[2])
                    .datetime(formatter.parse(csvRow[3], Instant::from))
                    .datasource(csvRow[4].toLowerCase())
                    .build();
                }
            );

    }
}
