package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.model.DailyStats;
import org.dnltsk.d2d.challenge.model.DailyStatsResponse;
import org.dnltsk.d2d.challenge.write.DbInitiator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CREATED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TripsWebIT {

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

    @Test
    public void number_of_inserts_of_smallTestData_is_correct() throws SQLException {
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/trips", TestDataRepository.smallTestData, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        int numTrips = 0;
        int numGridCells= 0;
        int numRegions = 0;
        int numDatasources = 0;
        try (Connection conn = dbPool.openJdbcConnection()){
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

    @Test
    public void number_of_inserts_of_largeTestData_is_correct() throws SQLException {
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/trips", TestDataRepository.largeTestData, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);

        int numTrips = 0;
        int numGridCells= 0;
        int numRegions = 0;
        int numDatasources = 0;
        try (Connection conn = dbPool.openJdbcConnection()){
            numTrips = count(conn, "SELECT count(*) as num from public.trips");
            numGridCells = count(conn, "SELECT count(*) as num from public.grid_cells");
            numRegions = count(conn, "SELECT count(*) as num from public.regions");
            numDatasources = count(conn, "SELECT count(*) as num from public.datasources");
        }
        assertThat(numTrips).as("number of trips")
            .isEqualTo(100);
        assertThat(numGridCells).as("number of grid_cells")
            .isEqualTo(32);
        assertThat(numRegions).as("number of regions")
            .isEqualTo(3);
        assertThat(numDatasources).as("number of datasources")
            .isEqualTo(5);
    }

    @Test
    public void dailyStats_of_largeTestData_is_correct() throws SQLException {
        testRestTemplate.postForEntity("/trips", TestDataRepository.largeTestData, Void.class);

        String query = "?region=prague"
            + "&minLat=0.0"
            + "&minLon=0.0"
            + "&maxLat=0.0"
            + "&maxLon=0.0";
        DailyStatsResponse response = testRestTemplate.getForObject("/stats" + query, DailyStatsResponse.class);

        assertThat(response.getDailyStats()).hasSize(1);
        DailyStats dailyStats = response.getDailyStats().get(0);
        assertThat(dailyStats.getDayOfWeek()).isEqualTo(DayOfWeek.TUESDAY);
        assertThat(dailyStats.getNumberOfTrips()).isEqualTo(1);
    }

    private int count(Connection conn, String query) throws SQLException {
        ResultSet resultSet = conn.createStatement().executeQuery(query);
        resultSet.next();
        return resultSet.getInt("num");
    }

}
