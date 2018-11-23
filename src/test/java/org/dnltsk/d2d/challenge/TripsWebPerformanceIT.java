package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.write.DbInitiator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CREATED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TripsWebPerformanceIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DbInitiator dbInitiator;

    @Autowired
    private DatabasePool dbPool;

    @Before
    public void setUp() throws SQLException {
        dbInitiator.resetDb();
    }

    @Test @Ignore("because it's not really valuable")
    public void processing_of_ten_thousand_trips() throws SQLException {
        Observable.range(0, 100)
            .map(i -> {
                ResponseEntity<Void> response = testRestTemplate.postForEntity("/trips", TestDataRepository.largeTestData, Void.class);
                assertThat(response.getStatusCode()).isEqualTo(CREATED);
                return i;
            }).
            toList()
            .toBlocking().first();

        int numTrips = 0;
        int numGridCells = 0;
        int numRegions = 0;
        int numDatasources = 0;
        try (Connection conn = dbPool.openJdbcConnection()) {
            numTrips = count(conn, "SELECT count(*) as num from public.trips");
            numGridCells = count(conn, "SELECT count(*) as num from public.grid_cells");
            numRegions = count(conn, "SELECT count(*) as num from public.regions");
            numDatasources = count(conn, "SELECT count(*) as num from public.datasources");
        }
        assertThat(numTrips).as("number of trips")
            .isEqualTo(2);
        assertThat(numGridCells).as("number of grid_cells")
            .isEqualTo(4);
        assertThat(numRegions).as("number of regions")
            .isEqualTo(2);
        assertThat(numDatasources).as("number of datasources")
            .isEqualTo(2);
    }

    private int count(Connection conn, String query) throws SQLException {
        ResultSet resultSet = conn.createStatement().executeQuery(query);
        resultSet.next();
        return resultSet.getInt("num");
    }

}
