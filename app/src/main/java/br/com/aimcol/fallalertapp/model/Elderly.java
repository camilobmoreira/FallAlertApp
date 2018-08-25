package br.com.aimcol.fallalertapp.model;

import java.util.List;

import lombok.Data;

@Data
public class Elderly {
    String key;
    String name;
//    LocalDate birthday;
    List<Caregiver> caregivers;
}
