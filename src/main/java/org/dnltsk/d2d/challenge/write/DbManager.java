package org.dnltsk.d2d.challenge.write;

import lombok.extern.slf4j.Slf4j;
import org.dnltsk.d2d.challenge.DatabasePool;
import org.dnltsk.d2d.challenge.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DbManager {

    @Autowired
    private DatabasePool databasePool;

    @Autowired
    private DbWriter writer;

    public void insertTrips(List<Trip> trips) {

        try (Connection conn = databasePool.openJdbcConnection()) {

            List<String> distinctRegions = trips.stream()
                .map(Trip::getRegion)
                .distinct()
                .collect(Collectors.toList());
            writer.insertRegions(distinctRegions, conn);

            List<String> distinctDatasources = trips.stream()
                .map(Trip::getDatasource)
                .distinct()
                .collect(Collectors.toList());
            writer.insertDatasources(distinctDatasources, conn);

            Observable.just(trips)
                .flatMapIterable(t -> t)
                .buffer(10)
                .toBlocking()
                .subscribe(t -> writer.insertChunkOfTrips(t, conn));

            conn.commit();

        } catch (SQLException e) {
            log.error("failed to insert whole trips!", e);
        }
    }

}
