package org.dnltsk.d2d.challenge.parse;

import org.dnltsk.d2d.challenge.model.Trip;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observable;

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




}