package org.dnltsk.d2d.challenge.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
@Slf4j
public class DbInitiator {

    @Autowired
    private DatabasePool databasePool;


    public void resetDb() throws SQLException {
        log.info("resetDb..");
        Connection conn = this.databasePool.openJdbcConnection();
        try {
            sanitizeDb(conn);
            createRegions(conn);
            createDatasources(conn);
            createCells(conn);
            createTrips(conn);
            conn.commit();
            log.info("resetDb.. done!");
        } catch (SQLException e) {
            log.info("resetDb.. failed!");
            throw e;
        } finally {
            conn.close();
        }
    }

    private void sanitizeDb(Connection conn) throws SQLException {
        conn.createStatement().executeUpdate("ALTER EXTENSION postgis UPDATE");
        conn.createStatement().executeUpdate("DROP TABLE IF EXISTS public.trips");
        conn.createStatement().executeUpdate("DROP TABLE IF EXISTS public.grid_cells");
        conn.createStatement().executeUpdate("DROP TABLE IF EXISTS public.regions");
        conn.createStatement().executeUpdate("DROP TABLE IF EXISTS public.datasources");
    }

    private void createRegions(Connection conn) throws SQLException {
        conn.createStatement().executeUpdate(
            "CREATE TABLE IF NOT EXISTS public.regions\n" +
                "(\n" +
                "    id serial,\n" +
                "    name VARCHAR(256) NOT NULL UNIQUE,\n" +
                "    PRIMARY KEY (id)\n" +
                ")"
        );
    }

    private void createDatasources(Connection conn) throws SQLException {
        conn.createStatement().executeUpdate(
            "CREATE TABLE IF NOT EXISTS public.datasources\n" +
                "(\n" +
                "    id serial,\n" +
                "    name VARCHAR(256) NOT NULL UNIQUE,\n" +
                "    PRIMARY KEY (id)\n" +
                ");"
        );
    }

    private void createCells(Connection conn) throws SQLException {
        conn.createStatement().executeUpdate(
            "CREATE TABLE IF NOT EXISTS grid_cells\n" +
                "(\n" +
                "    id serial,\n" +
                "    x integer NOT NULL ,\n" +
                "    y integer NOT NULL ,\n" +
                "    PRIMARY KEY (id),\n" +
                "    UNIQUE (x, y)\n" +
                ")"
        );
        conn.createStatement().executeQuery(
            "SELECT AddGeometryColumn ('public', 'grid_cells', 'geom', 4326, 'POLYGON', 2)"
        );
        conn.createStatement().executeUpdate(
            "CREATE INDEX grid_cells_gix ON grid_cells USING GIST (geom)"
        );
    }

    private void createTrips(Connection conn) throws SQLException {
        conn.createStatement().executeUpdate(
            "CREATE TABLE IF NOT EXISTS public.trips\n" +
                "(\n" +
                "    id bigserial,\n" +
                "    region_fk integer NOT NULL REFERENCES public.regions(id),\n" +
                "    origin_cell_fk integer NOT NULL REFERENCES public.grid_cells(id),\n" +
                "    destination_cell_fk integer NOT NULL REFERENCES public.grid_cells(id),\n" +
                "    datasource_fk integer NOT NULL REFERENCES public.datasources(id),\n" +
                "    datetime timestamptz NOT NULL,\n" +
                "    ingested_at timestamptz NOT NULL,\n" +
                "    day_of_week integer NOT NULL,\n" +
                "    hour_of_day integer NOT NULL,\n" +
                "    PRIMARY KEY (id)\n" +
                ")"
        );

        conn.createStatement().executeQuery("SELECT AddGeometryColumn ('public', 'trips', 'origin_geom', 4326, 'POINT', 2)");
        conn.createStatement().executeUpdate("CREATE INDEX trips_origin_gix ON trips USING GIST (origin_geom)");

        conn.createStatement().executeQuery("SELECT AddGeometryColumn ('public', 'trips', 'destination_geom', 4326, 'POINT', 2)");
        conn.createStatement().executeUpdate("CREATE INDEX trips_destination_gix ON trips USING GIST (destination_geom)");

    }


}
