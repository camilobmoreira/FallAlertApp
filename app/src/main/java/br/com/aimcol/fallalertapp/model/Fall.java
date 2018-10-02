package br.com.aimcol.fallalertapp.model;

import java.util.Date;

import lombok.Data;

@Data
public class Fall {
    private Long latitude;
    private Long longitude;
    private Date date;
}
