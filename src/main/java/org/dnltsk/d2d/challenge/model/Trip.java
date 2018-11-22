package org.dnltsk.d2d.challenge.model;

import com.vividsolutions.jts.geom.Point;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Trip {

    private String region;
    private Point origin;
    private GridCell originGridCell;
    private Point destination;
    private GridCell destinationGridCell;
    private String datasource;
    private Instant datetime;

}
