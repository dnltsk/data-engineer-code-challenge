package org.dnltsk.d2d.challenge.model;

import com.vividsolutions.jts.geom.Polygon;
import lombok.Builder;
import lombok.Data;


/**
 * GridCell describes the a specific location on a grid
 */
@Data
@Builder
public class GridCell {

    private Integer xCenter;
    private Integer yCenter;

    /**
     * cell geometry around xCenter and yCenter
     */
    private Polygon geom;


}
