package org.dnltsk.d2d.challenge.parse;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.dnltsk.d2d.challenge.model.GridCell;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GridCellCalculatorTest {

    private GridCellCalculator calculator = new GridCellCalculator();

    @Test
    public void center_and_geom_of_x_0_y_0_is_calculated_correctly() throws ParseException {
        Point point = (Point) new WKTReader().read("POINT(0 0)");

        GridCell gridCell = calculator.calcGridCell(point);

        assertThat(gridCell.getXCenter()).isEqualTo(0);
        assertThat(gridCell.getYCenter()).isEqualTo(0);
        assertThat(gridCell.getGeom().toText()).isEqualTo("POLYGON ((-0.05 -0.05, -0.05 0.05, 0.05 0.05, 0.05 -0.05, -0.05 -0.05))");
    }

    @Test
    public void center_and_geom_of_x_2p3_y_2p3_is_calculated_correctly() throws ParseException {
        Point point = (Point) new WKTReader().read("POINT(2.3 2.3)");

        GridCell gridCell = calculator.calcGridCell(point);

        assertThat(gridCell.getXCenter()).isEqualTo(23);
        assertThat(gridCell.getYCenter()).isEqualTo(23);
        assertThat(gridCell.getGeom().toText()).isEqualTo("POLYGON ((2.25 2.25, 2.25 2.35, 2.35 2.35, 2.35 2.25, 2.25 2.25))");
    }

    @Test
    public void center_and_geom_of_x_minus2p3_y_minus2p3_is_calculated_correctly() throws ParseException {
        Point point = (Point) new WKTReader().read("POINT(-2.3 -2.3)");

        GridCell gridCell = calculator.calcGridCell(point);

        assertThat(gridCell.getXCenter()).isEqualTo(-23);
        assertThat(gridCell.getYCenter()).isEqualTo(-23);
        assertThat(gridCell.getGeom().toText()).isEqualTo("POLYGON ((-2.35 -2.35, -2.35 -2.25, -2.25 -2.25, -2.25 -2.35, -2.35 -2.35))");
    }
}