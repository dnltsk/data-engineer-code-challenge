package org.dnltsk.d2d.challenge.parse;

import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BboxConverterTest {

    private BboxConverter converter = new BboxConverter();

    @Test
    public void conversion_works_straightforward() throws ParseException {
        Float minLat = 53.45f;
        Float minLon = 10.15f;
        Float maxLat = 53.55f;
        Float maxLon = 10.25f;

        Polygon polygon = converter.convert(minLon, minLat, maxLon, maxLat);

        assertThat(polygon.toText()).isEqualTo("POLYGON ((10.15 53.45, 10.15 53.55, 10.25 53.55, 10.25 53.45, 10.15 53.45))");
    }
}