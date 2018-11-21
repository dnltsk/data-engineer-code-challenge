package org.dnltsk.d2d.challenge.write;

import lombok.extern.slf4j.Slf4j;
import org.dnltsk.d2d.challenge.DatabasePool;
import org.dnltsk.d2d.challenge.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class DbWriter {

    @Autowired
    private DatabasePool databasePool;


    public void insertTrip(List<Trip> trips) {
        log.info("inserting trips: "+trips.size());
        try {
            Connection conn = databasePool.openJdbcConnection();
            trips.forEach(
                trip -> {
                    log.debug("insert trip: " + trip);
                    try {
                        conn.createStatement().executeUpdate(
                            "INSERT INTO public.regions (name)\n" +
                                "VALUES(\n" +
                                " '" + trip.getRegion() + "'\n" +
                                ") ON CONFLICT DO NOTHING;"
                        );

                        conn.createStatement().executeUpdate(
                            "INSERT INTO public.datasources (name)\n" +
                                "VALUES(\n" +
                                " '" + trip.getDatasource() + "'\n" +
                                ") ON CONFLICT DO NOTHING;"
                        );

                        conn.createStatement().executeUpdate(
                            "INSERT INTO public.tiles_z_10 (x, y, geom)\n" +
                                "VALUES(\n" +
                                " 0, 0, ST_GeomFromText('POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))', 4326)\n" +
                                ") ON CONFLICT DO NOTHING;"
                        );

                        conn.createStatement().executeUpdate(
                            "INSERT INTO public.tiles_z_10 (x, y, geom)\n" +
                                "VALUES(\n" +
                                "  0, 0, ST_GeomFromText('POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))', 4326)\n" +
                                ") ON CONFLICT DO NOTHING;"
                        );

                        conn.createStatement().executeUpdate(
                            "INSERT INTO public.trips(\n" +
                                "  region_fk, origin_tile_fk, destination_tile_fk, datasource_fk, datetime, ingested_at, weekday, hour_of_day, origin_geom, destination_geom)\n" +
                                "VALUES (\n" +
                                "    (SELECT id FROM public.regions WHERE name = 'prague'),\n" +
                                "    (SELECT id FROM public.tiles_z_10 WHERE x = 0 AND y = 0),\n" +
                                "    (SELECT id FROM public.tiles_z_10 WHERE x = 0 AND y = 0),\n" +
                                "    (SELECT id FROM public.datasources WHERE name = 'funny_car'),\n" +
                                "    current_timestamp,\n" +
                                "    current_timestamp,\n" +
                                "    1,\n" +
                                "    1,\n" +
                                "    ST_GeomFromText('POINT (30 10)', 4326),\n" +
                                "\tST_GeomFromText('POINT (30 10)', 4326)\n" +
                                ");"
                        );

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            );
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
