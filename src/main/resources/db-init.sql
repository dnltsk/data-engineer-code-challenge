DROP TABLE IF EXISTS public.trips;
DROP TABLE IF EXISTS public.tiles_z_10;
DROP TABLE IF EXISTS public.regions;
DROP TABLE IF EXISTS public.datasources;

CREATE TABLE IF NOT EXISTS public.tiles_z_10
(
    id serial,
    x integer NOT NULL,
    y integer NOT NULL,
    PRIMARY KEY (id)
);
SELECT AddGeometryColumn ('public', 'tiles_z_10', 'geom', 4326, 'POLYGON', 2);
CREATE INDEX tiles_z_10_gix ON tiles_z_10 USING GIST (geom);
CREATE TABLE IF NOT EXISTS public.regions
(
    id serial,
    name VARCHAR(256) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS public.datasources
(
    id serial,
    name VARCHAR(256),
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS public.trips
(
    id bigserial,
    region_fk integer NOT NULL REFERENCES public.regions(id),
    origin_tile_fk integer NOT NULL REFERENCES public.tiles_z_10(id),
    destination_tile_fk integer NOT NULL REFERENCES public.tiles_z_10(id),
    datasource_fk integer NOT NULL REFERENCES public.datasources(id),
    datetime timestamptz NOT NULL,
    ingested_at timestamptz NOT NULL,
    weekday integer NOT NULL,
    hour_of_day integer NOT NULL,
    PRIMARY KEY (id)
);
SELECT AddGeometryColumn ('public', 'trips', 'origin_geom', 4326, 'POINT', 2);
CREATE INDEX trips_origin_gix ON trips USING GIST (origin_geom);
SELECT AddGeometryColumn ('public', 'trips', 'destination_geom', 4326, 'POINT', 2);
CREATE INDEX trips_destination_gix ON trips USING GIST (destination_geom);