package br.com.aimcol.fallalertapp.model;

import java.util.List;

import lombok.Data;

@Data
public class Elderly extends Person {

    public static final String ELDERLY_JSON = "elderlyJson";

    public Elderly() {
        super(Elderly.class.getSimpleName());
    }

    private Long birthDateAsLong;
    private List<Caregiver> caregivers;
}
