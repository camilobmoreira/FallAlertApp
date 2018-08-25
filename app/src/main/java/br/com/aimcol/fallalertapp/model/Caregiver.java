package br.com.aimcol.fallalertapp.model;

import java.util.Map;

import lombok.Data;

@Data
public class Caregiver {
    //String key;
    String name;
    Map<ContactType, String> contacts;
}
