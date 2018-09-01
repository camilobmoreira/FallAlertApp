package br.com.aimcol.fallalertapp.model;

import lombok.Data;

@Data
public abstract class Person {
    private String name;
    private String type;

    public Person(String type) {
        this.type = type;
    }
}
