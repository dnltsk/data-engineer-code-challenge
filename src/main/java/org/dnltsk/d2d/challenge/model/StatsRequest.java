package org.dnltsk.d2d.challenge.model;

import com.vividsolutions.jts.geom.Polygon;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsRequest {

    private String region;
    private Polygon bbox;

}
