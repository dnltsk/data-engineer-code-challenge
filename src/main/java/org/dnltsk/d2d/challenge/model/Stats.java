package org.dnltsk.d2d.challenge.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class Stats {

    private Double weeklyAverageNumberOfTrips;

}
