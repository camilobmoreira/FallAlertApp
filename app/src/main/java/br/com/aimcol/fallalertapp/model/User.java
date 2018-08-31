package br.com.aimcol.fallalertapp.model;

import lombok.Data;

@Data
public class User {

    public static final String USER_JSON = "userJson";

    private String id;
    private String email;
    private Person person;
}
