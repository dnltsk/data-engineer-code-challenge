package org.dnltsk.d2d.challenge.parse;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import org.dnltsk.d2d.challenge.model.GridCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GridCellCalculator {

    @Autowired
    private BboxConverter bboxConverter;

    private final static float cellSize = 0.1f;


    public GridCell calcGridCell(Point point) throws ParseException {
        int xCenter = (int) Math.round(point.getX() / cellSize);
        int yCenter = (int) Math.round(point.getY() / cellSize);
        Polygon rect = generateCellPolygon(xCenter, yCenter);

        return GridCell.builder()
            .xCenter(xCenter)
            .yCenter(yCenter)
            .geom(rect)
            .build();
    }

    private Polygon generateCellPolygon(int xCenter, int yCenter) throws ParseException {
        float xGeom = (float) xCenter * cellSize;
        float yGeom = (float) yCenter * cellSize;
        float halfSize = cellSize / 2.0f;
        return bboxConverter.convert(
            (yGeom - halfSize),
            (xGeom - halfSize),
            (xGeom + halfSize),
            (yGeom + halfSize)
        );
    }

}
