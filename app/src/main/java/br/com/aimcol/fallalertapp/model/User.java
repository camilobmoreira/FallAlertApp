package br.com.aimcol.fallalertapp.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class User {

    public static final String USER_JSON = "userJson";

    private String key;
    private String email;
    private Person person;
    private Map<Configuration, Object> configurations = new HashMap<>();
}
