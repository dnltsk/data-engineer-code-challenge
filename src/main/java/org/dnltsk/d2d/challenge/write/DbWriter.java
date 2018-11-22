package org.dnltsk.d2d.challenge.write;

import lombok.extern.slf4j.Slf4j;
import org.dnltsk.d2d.challenge.model.GridCell;
import org.dnltsk.d2d.challenge.model.Trip;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class DbWriter {

    public void insertRegions(List<String> regions, Connection conn) {
        log.info("inserting regions: " + regions.toString());
        try {
            for (String region : regions) {
                conn.createStatement().executeUpdate(
                    "INSERT INTO public.regions (name)\n" +
                        "VALUES(\n" +
                        " '" + region + "'\n" +
                        ") ON CONFLICT DO NOTHING;"
                );
            }
        } catch (SQLException e) {
            log.error("Failed to insert regions", e);
        }
    }

    public void insertDatasources(List<String> datasources, Connection conn) {
        log.info("inserting datasources: " + datasources);
        try {
            for (String datasource : datasources) {
                conn.createStatement().executeUpdate(
                    "INSERT INTO public.datasources (name)\n" +
                        "VALUES(\n" +
                        " '" + datasource + "'\n" +
                        ") ON CONFLICT DO NOTHING;"
                );
            }
        } catch (SQLException e) {
            log.error("Failed to insert datasources", e);
        }
    }

    public void insertGridCells(List<GridCell> gridCells, Connection conn) {
        log.info("inserting gridCells: " + gridCells);
        try {
            for (GridCell gridCell: gridCells) {
                conn.createStatement().executeUpdate(
                    "INSERT INTO public.grid_cells (x, y, geom)\n" +
                        "VALUES(\n" +
                        "  "+gridCell.getXCenter()+", " +
                        "  "+gridCell.getYCenter()+", " +
                        "  ST_GeomFromText('"+gridCell.getGeom().toText()+"', 4326)\n" +
                        ") ON CONFLICT DO NOTHING;"
                );
            }
        } catch (SQLException e) {
            log.error("Failed to insert gridCells", e);
        }
    }

    public void insertChunkOfTrips(List<Trip> trips, Connection conn) {
        log.info("inserting trips: " + trips.size());

        for (Trip trip : trips) {
            log.debug("insert trip: " + trip);
            try {
                conn.createStatement().executeUpdate(
                    "INSERT INTO public.trips(\n" +
                        "  region_fk, origin_tile_fk, destination_tile_fk, datasource_fk, datetime, ingested_at, weekday, hour_of_day, origin_geom, destination_geom)\n" +
                        "VALUES (\n" +
                        "    (SELECT id FROM public.regions WHERE name = 'prague'),\n" +
                        "    (SELECT id FROM public.grid_cells WHERE x = "+trip.getOriginGridCell().getXCenter()+" AND y = "+trip.getOriginGridCell().getYCenter()+"),\n" +
                        "    (SELECT id FROM public.grid_cells WHERE x = "+trip.getDestinationGridCell().getXCenter()+" AND y = "+trip.getDestinationGridCell().getYCenter()+"),\n" +
                        "    (SELECT id FROM public.datasources WHERE name = 'funny_car'),\n" +
                        "    '"+trip.getDatetime()+"',\n" +
                        "    current_timestamp,\n" +
                        "    1,\n" +
                        "    1,\n" +
                        "    ST_GeomFromText('"+trip.getOrigin().toText()+"', 4326),\n" +
                        "    ST_GeomFromText('"+trip.getDestination().toText()+"', 4326)\n" +
                        ");"
                );
            } catch (SQLException e) {
                log.error("Failed to insert single trip", e);
            }
        }
    }


}
