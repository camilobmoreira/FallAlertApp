package br.com.aimcol.fallalertapp.model;

import java.util.List;

import lombok.Data;

@Data
public class Elderly extends Person {

    public static final String ELDERLY_JSON = "elderlyJson";

//  private LocalDate birthday;
    private List<Caregiver> caregivers;
}
