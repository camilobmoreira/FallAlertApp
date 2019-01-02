package br.com.aimcol.fallalertapp.model;

import lombok.Data;

public @Data class UserConfiguration {
    private Configuration configuration;
    private Object value;
    private Class type;
}
