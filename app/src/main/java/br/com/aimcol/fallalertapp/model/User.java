package br.com.aimcol.fallalertapp.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class User {

    public static final String USER_JSON = "userJson";

    private String key;
    private String email;
    private Person person;
    private List<UserConfiguration> configurations = new ArrayList<>();
}
