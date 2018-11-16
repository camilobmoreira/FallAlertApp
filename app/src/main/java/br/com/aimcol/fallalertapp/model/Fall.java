package br.com.aimcol.fallalertapp.model;

import lombok.Data;

@Data
public class Fall {
    public static final String FALL_JSON = "fallJson";

    private Double latitude;
    private Double longitude;
    private Long timeInMillis;
}
