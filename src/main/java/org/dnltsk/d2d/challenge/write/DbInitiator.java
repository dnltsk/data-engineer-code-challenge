package org.dnltsk.d2d.challenge.write;

import com.github.davidmoten.rx.jdbc.Database;
import lombok.extern.slf4j.Slf4j;
import org.dnltsk.d2d.challenge.DatabasePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

@Service
@Slf4j
public class DbInitiator {

    @Autowired
    private DatabasePool databasePool;


    public void resetDb() {
        log.info("resetDb..");
        Database db = this.databasePool.getDatabase();

        db.update(
            "ALTER EXTENSION postgis UPDATE;")
            .count().toBlocking();

        Observable<Integer> deleteTrips = db.update(
            "DROP TABLE IF EXISTS public.trips")
            .count();
        Observable<Integer> deleteTiles = db.update(
            "DROP TABLE IF EXISTS public.tiles_z_10")
            .dependsOn(deleteTrips)
            .count();

        Observable<Integer> deleteRegions = db.update(
            "DROP TABLE IF EXISTS public.regions")
            .dependsOn(deleteTrips)
            .count();

        Observable<Integer> deleteDatasources = db.update(
            "DROP TABLE IF EXISTS public.datasources")
            .dependsOn(deleteTrips)
            .count();

        Observable<Integer> createTiles = db.update(
            "CREATE TABLE IF NOT EXISTS tiles_z_10\n" +
                "(\n" +
                "    id serial,\n" +
                "    x integer,\n" +
                "    y integer,\n" +
                "    PRIMARY KEY (id)\n" +
                ")")
            .dependsOn(deleteTiles)
            .count();
        Observable<Integer> addGeomColumnTiles = db.select(
            "SELECT AddGeometryColumn ('public', 'tiles_z_10', 'geom', 4326, 'POLYGON', 2)")
            .dependsOn(createTiles)
            .count();
        Observable<Integer> createTilesGix = db.update(
            "CREATE INDEX tiles_z_10_gix ON tiles_z_10 USING GIST (geom)")
            .dependsOn(createTiles)
            .count();

        Observable<Integer> createRegions = db.update(
            "CREATE TABLE IF NOT EXISTS public.regions\n" +
                "(\n" +
                "    id serial,\n" +
                "    name VARCHAR(256) NOT NULL,\n" +
                "    PRIMARY KEY (id)\n" +
                ")")
            .dependsOn(deleteRegions)
            .count();

        Observable<Integer> createDatasources = db.update(
            "CREATE TABLE IF NOT EXISTS public.datasources\n" +
                "(\n" +
                "    id serial,\n" +
                "    name VARCHAR(256),\n" +
                "    PRIMARY KEY (id)\n" +
                ");")
            .dependsOn(deleteDatasources)
            .count();

        Observable<Integer> createTrips = db.update(
            "CREATE TABLE IF NOT EXISTS public.trips\n" +
                "(\n" +
                "    id bigserial,\n" +
                "    region_fk integer NOT NULL REFERENCES public.regions(id),\n" +
                "    origin_tile_fk integer NOT NULL REFERENCES public.tiles_z_10(id),\n" +
                "    destination_tile_fk integer NOT NULL REFERENCES public.tiles_z_10(id),\n" +
                "    datasource_fk integer NOT NULL REFERENCES public.datasources(id),\n" +
                "    datetime timestamptz NOT NULL,\n" +
                "    ingested_at timestamptz NOT NULL,\n" +
                "    weekday integer NOT NULL,\n" +
                "    hour_of_day integer NOT NULL,\n" +
                "    PRIMARY KEY (id)\n" +
                ")")
            .dependsOn(deleteTrips)
            .dependsOn(createTiles)
            .dependsOn(createRegions)
            .dependsOn(createDatasources)
            .count();
        Observable<Integer> createTripsOriginGeom = db.select(
            "SELECT AddGeometryColumn ('public', 'trips', 'origin_geom', 4326, 'POINT', 2)")
            .dependsOn(createTrips)
            .count();
        Observable<Integer> createOriginGix = db.update(
            "CREATE INDEX trips_origin_gix ON trips USING GIST (origin_geom)")
            .dependsOn(createTripsOriginGeom)
            .count();
        Observable<Integer> createTripsDestinationGeom = db.select(
            "SELECT AddGeometryColumn ('public', 'trips', 'destination_geom', 4326, 'POINT', 2)")
            .dependsOn(createTrips)
            .count();
        Observable<Integer> createDestinationGix = db.update(
            "CREATE INDEX trips_destination_gix ON trips USING GIST (destination_geom)")
            .dependsOn(createTripsDestinationGeom)
            .count();

        db.select(
            "select count(*) from public.trips")
            .dependsOn(createOriginGix)
            .dependsOn(createDestinationGix)
            .getAs(String.class)
            .toList()
            .toBlocking()
            .single();

        log.info("resetDb.. done");

        db.close();
    }

}
