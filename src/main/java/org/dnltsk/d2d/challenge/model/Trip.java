package org.dnltsk.d2d.challenge.model;

import lombok.Data;

import java.time.Instant;

@Data
//@AllArgsConstructor
public class Trip {

    private String region;
    private String originAsWkt;
    private String destinationAsWkt;
    private String datasource;
    private Instant datetime;

}
