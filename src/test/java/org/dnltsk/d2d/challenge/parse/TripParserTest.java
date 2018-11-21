package org.dnltsk.d2d.challenge.parse;

import org.dnltsk.d2d.challenge.model.Trip;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observable;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dnltsk.d2d.challenge.TestDataRepository.smallTestData;

@RunWith(SpringRunner.class)
public class TripParserTest {

    @InjectMocks
    private TripParser parser;

    @Test
    public void number_of_rows_is_correct() {
        Observable<Trip> trips = parser.parse(smallTestData);
        assertThat(trips.toList().toBlocking().single().size()).isEqualTo(2);
        trips.test();
    }

    @Test
    public void trip_is_parsed_correctly() {
        Observable<Trip> trips = parser.parse(smallTestData);
        Trip trip = trips.first().toBlocking().single();
        assertThat(trip.getRegion()).isEqualTo("prague");
        assertThat(trip.getOriginAsWkt()).isEqualTo("POINT (14.4973794438195 50.00136875782316)");
        assertThat(trip.getDestinationAsWkt()).isEqualTo("POINT (14.43109483523328 50.04052930943246)");
        assertThat(trip.getDatetime()).isEqualTo(Instant.parse("2018-05-28T09:03:40Z"));
        assertThat(trip.getDatasource()).isEqualTo("funny_car");
    }


}