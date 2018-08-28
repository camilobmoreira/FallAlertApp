package br.com.aimcol.fallalertapp.model;

import java.util.List;

import lombok.Data;

@Data
public class Caregiver {
    public static final String CAREGIVER_JSON = "caregiverJson";

    //private String key;
    private String name;
    private List<Contact> contacts;
}
