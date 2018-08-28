package br.com.aimcol.fallalertapp.model;

import java.util.List;

import lombok.Data;

@Data
public class Caregiver {
    //String key;
    String name;
    List<Contact> contacts;
}
