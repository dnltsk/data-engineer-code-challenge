package org.dnltsk.d2d.challenge.parse;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.dnltsk.d2d.challenge.model.GridCell;
import org.springframework.stereotype.Service;

@Service
public class GridCellCalculator {

    private final static float cellSize = 0.1f;

    private WKTReader wktReader = new WKTReader();

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
        float halfSize = (float) cellSize / 2.0f;
        String ll = (xGeom - halfSize) + " " + (yGeom - halfSize);
        String ul = (xGeom - halfSize) + " " + (yGeom + halfSize);
        String ur = (xGeom + halfSize) + " " + (yGeom + halfSize);
        String lr = (xGeom + halfSize) + " " + (yGeom - halfSize);
        String wkt = "POLYGON((" + ll + ", " + ul + ", " + ur + ", " + lr + ", " + ll + "))";
        return (Polygon) wktReader.read(wkt);
    }

}
