package org.dnltsk.d2d.challenge.parse;

import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.stereotype.Service;

@Service
public class BboxConverter {

    private WKTReader wktReader = new WKTReader();

    public Polygon convert(Float minLat, Float minLon, Float maxLat, Float maxLon) throws ParseException {
        String ll = minLon + " " + minLat;
        String ul = minLon + " " + maxLat;
        String ur = maxLon + " " + maxLat;
        String lr = maxLon + " " + minLat;
        String wkt = "POLYGON((" + ll + ", " + ul + ", " + ur + ", " + lr + ", " + ll + "))";
        return (Polygon) wktReader.read(wkt);
    }

}
