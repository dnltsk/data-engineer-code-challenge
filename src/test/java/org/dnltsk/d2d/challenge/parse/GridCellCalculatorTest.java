package org.dnltsk.d2d.challenge.parse;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.dnltsk.d2d.challenge.model.GridCell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class GridCellCalculatorTest {

    @InjectMocks
    private GridCellCalculator calculator;

    @Mock
    private BboxConverter converter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void center_and_geom_of_x_0_y_0_is_calculated_correctly() throws ParseException {
        Point point = (Point) new WKTReader().read("POINT(0 0)");

        GridCell gridCell = calculator.calcGridCell(point);

        assertThat(gridCell.getXCenter()).isEqualTo(0);
        assertThat(gridCell.getYCenter()).isEqualTo(0);

        ArgumentCaptor<Float> minLat = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> minLon = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> maxLat = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> maxLon = ArgumentCaptor.forClass(Float.class);
        verify(converter).convert(minLat.capture(), minLon.capture(), maxLat.capture(), maxLon.capture());
        assertThat(minLat.getValue()).isEqualTo(-0.05f);
        assertThat(minLon.getValue()).isEqualTo(-0.05f);
        assertThat(maxLat.getValue()).isEqualTo(0.05f);
        assertThat(maxLon.getValue()).isEqualTo(0.05f);
    }

    @Test
    public void center_and_geom_of_x_2p3_y_2p3_is_calculated_correctly() throws ParseException {
        Point point = (Point) new WKTReader().read("POINT(2.3 2.3)");

        GridCell gridCell = calculator.calcGridCell(point);

        assertThat(gridCell.getXCenter()).isEqualTo(23);
        assertThat(gridCell.getYCenter()).isEqualTo(23);

        ArgumentCaptor<Float> minLat = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> minLon = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> maxLat = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> maxLon = ArgumentCaptor.forClass(Float.class);
        verify(converter).convert(minLat.capture(), minLon.capture(), maxLat.capture(), maxLon.capture());
        assertThat(minLat.getValue()).isEqualTo(2.25f);
        assertThat(minLon.getValue()).isEqualTo(2.25f);
        assertThat(maxLat.getValue()).isEqualTo(2.35f);
        assertThat(maxLon.getValue()).isEqualTo(2.35f);
    }

    @Test
    public void center_and_geom_of_x_minus2p3_y_minus2p3_is_calculated_correctly() throws ParseException {
        Point point = (Point) new WKTReader().read("POINT(-2.3 -2.3)");

        GridCell gridCell = calculator.calcGridCell(point);

        assertThat(gridCell.getXCenter()).isEqualTo(-23);
        assertThat(gridCell.getYCenter()).isEqualTo(-23);

        ArgumentCaptor<Float> minLat = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> minLon = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> maxLat = ArgumentCaptor.forClass(Float.class);
        ArgumentCaptor<Float> maxLon = ArgumentCaptor.forClass(Float.class);
        verify(converter).convert(minLat.capture(), minLon.capture(), maxLat.capture(), maxLon.capture());
        assertThat(minLat.getValue()).isEqualTo(-2.35f);
        assertThat(minLon.getValue()).isEqualTo(-2.35f);
        assertThat(maxLat.getValue()).isEqualTo(-2.25f);
        assertThat(maxLon.getValue()).isEqualTo(-2.25f);
    }

}