package org.dnltsk.d2d.challenge.parse;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;
import lombok.extern.slf4j.Slf4j;
import org.dnltsk.d2d.challenge.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.io.StringReader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Objects;

@Service
@Slf4j
public class TripParser {

    @Autowired
    private GridCellCalculator gridCellCalculator;

    private WKTReader wktReader = new WKTReader();

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd HH:mm:ss")
        .toFormatter()
        .withZone(ZoneId.of("UTC"));

    public Observable<Trip> parse(String tripsAsCsv) {
        CSVReader csvReader = new CSVReaderBuilder(new StringReader(tripsAsCsv))
            .withSkipLines(1) // skip csv header
            .build();
        return Observable
            .from(csvReader)
            .map(csvRow -> {
                    log.debug("parsing " + Arrays.toString(csvRow));
                    try {
                        Point origin = (Point) wktReader.read(csvRow[1]);
                        Point destination = (Point) wktReader.read(csvRow[2]);
                        Instant datetime = formatter.parse(csvRow[3], Instant::from);
                        ZonedDateTime zonedDatetime = datetime.atZone(ZoneId.of("UTC"));
                        return Trip.builder()
                            .region(csvRow[0].toLowerCase())
                            .origin(origin)
                            .originGridCell(gridCellCalculator.calcGridCell(origin))
                            .destination(destination)
                            .destinationGridCell(gridCellCalculator.calcGridCell(destination))
                            .datasource(csvRow[4].toLowerCase())
                            .datetime(datetime)
                            .dayOfWeek(zonedDatetime.getDayOfWeek().getValue())
                            .hourOfDay(zonedDatetime.getHour())
                            .build();
                    } catch (Exception e) {
                        log.error("Failed to parse csv row: " + Arrays.toString(csvRow), e);
                        return null;
                    }
                }
            ).filter(Objects::nonNull);
    }
}