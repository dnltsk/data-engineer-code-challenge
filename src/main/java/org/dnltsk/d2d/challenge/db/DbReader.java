package org.dnltsk.d2d.challenge.db;

import lombok.extern.slf4j.Slf4j;
import org.dnltsk.d2d.challenge.model.DailyStats;
import org.dnltsk.d2d.challenge.model.StatsRequest;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DbReader {

    public List<DailyStats> selectDailyStats(Connection conn, StatsRequest statsRequest) {
        try {
            String bboxWkt = statsRequest.getBbox().toText();
            ResultSet resultSet = conn.createStatement().executeQuery(
                "SELECT DISTINCT \n" +
                    "   t.day_of_week, count(*) num\n" +
                    "FROM \n" +
                    "   public.trips t, public.regions r\n" +
                    "WHERE \n" +
                    "   t.region_fk = r.id\n" +
                    "   AND r.name = '"+statsRequest.getRegion()+"'\n" +
                    "   AND ST_Contains(\n" +
                    "       ST_GeomFromText('"+bboxWkt+"', 4326),\n" +
                    "       t.origin_geom\n" +
                    "   )\n" +
                    "   AND ST_Contains(\n" +
                    "       ST_GeomFromText('"+bboxWkt+"', 4326),\n" +
                    "       t.destination_geom\n" +
                    "   )\n" +
                    "GROUP BY \n" +
                    "   t.day_of_week, r.name");

            log.info("SELECT DISTINCT \n" +
                "   t.day_of_week, count(*) num\n" +
                "FROM \n" +
                "   public.trips t, public.regions r\n" +
                "WHERE \n" +
                "   t.region_fk = r.id\n" +
                "   AND r.name = '"+statsRequest.getRegion()+"'\n" +
                "   AND ST_Contains(\n" +
                "       ST_GeomFromText('"+bboxWkt+"', 4326),\n" +
                "       t.origin_geom\n" +
                "   )\n" +
                "   AND ST_Contains(\n" +
                "       ST_GeomFromText('"+bboxWkt+"', 4326),\n" +
                "       t.destination_geom\n" +
                "   )\n" +
                "GROUP BY \n" +
                "   t.day_of_week, r.name");

            List<DailyStats> dailyStats = new ArrayList<>();
            while (resultSet.next()) {
                dailyStats.add(
                    new DailyStats(
                        DayOfWeek.of(resultSet.getInt("day_of_week")),
                        resultSet.getInt("num")
                    )
                );
            }
            return dailyStats;

        } catch (SQLException e) {
            log.error("Failed to select stats", e);
            return Collections.emptyList();
        }
    }

}
