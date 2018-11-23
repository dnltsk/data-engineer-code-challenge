package org.dnltsk.d2d.challenge.db;

import lombok.extern.slf4j.Slf4j;
import org.dnltsk.d2d.challenge.model.DailyStats;
import org.dnltsk.d2d.challenge.model.GridCell;
import org.dnltsk.d2d.challenge.model.StatsRequest;
import org.dnltsk.d2d.challenge.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DbManager {

    private final static int chunkSize = 10;

    @Autowired
    private DatabasePool databasePool;

    @Autowired
    private DbWriter writer;

    @Autowired
    private DbReader reader;

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public List<DailyStats> loadDailyStats(StatsRequest statsRequest) {
        try (Connection conn = databasePool.openJdbcConnection()) {
            return reader.selectDailyStats(conn, statsRequest);
        } catch (SQLException e) {
            log.error("failed to load daily stats", e);
            return Collections.emptyList();
        }
    }

    public void insertTrips(List<Trip> trips) {

        try (Connection conn = databasePool.openJdbcConnection()) {
            insertDistinctRegions(trips, conn);
            insertDistinctDatasources(trips, conn);
            insertDistinctGridCells(trips, conn);
            insertTrips(trips, conn);
            conn.commit();
        } catch (SQLException e) {
            log.error("failed to insert whole trips!", e);
        }
    }

    private void insertTrips(List<Trip> trips, Connection conn) {
        Observable.just(trips)
            .flatMapIterable(t -> t)
            .buffer(chunkSize)
            .toBlocking()
            .subscribe(t -> writer.insertChunkOfTrips(t, conn));
    }

    private void insertDistinctRegions(List<Trip> trips, Connection conn) {
        List<String> distinctRegions = trips.stream()
            .map(Trip::getRegion)
            .distinct()
            .collect(Collectors.toList());
        writer.insertRegions(distinctRegions, conn);
    }

    private void insertDistinctDatasources(List<Trip> trips, Connection conn) {
        List<String> distinctDatasources = trips.stream()
            .map(Trip::getDatasource)
            .distinct()
            .collect(Collectors.toList());
        writer.insertDatasources(distinctDatasources, conn);
    }

    private void insertDistinctGridCells(List<Trip> trips, Connection conn) {
        List<GridCell> distinctGridCenters = trips.stream()
            .map(trip -> {
                if (trip.getOriginGridCell() != null && trip.getDestinationGridCell() != null) {
                    return Arrays.asList(trip.getOriginGridCell(), trip.getDestinationGridCell());
                }
                return null;
            })
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(distinctByKey(gridCell -> gridCell.getXCenter() + "," + gridCell.getYCenter()))
            .collect(Collectors.toList());
        writer.insertGridCells(distinctGridCenters, conn);
    }

}
