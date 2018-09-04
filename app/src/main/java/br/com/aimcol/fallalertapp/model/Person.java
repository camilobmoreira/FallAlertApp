package br.com.aimcol.fallalertapp.model;

import lombok.Data;

@Data
public abstract class Person {
    private transient String type;

    private String name;

    public Person(String type) {
        this.type = type;
    }
}
