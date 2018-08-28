package br.com.aimcol.fallalertapp.model;

import java.util.List;

import lombok.Data;

@Data
public class Elderly {

    public static final String ELDERLY_JSON = "elderlyJson";

    private String key;
    private String name;
//  private LocalDate birthday;
    private List<Caregiver> caregivers;
}
