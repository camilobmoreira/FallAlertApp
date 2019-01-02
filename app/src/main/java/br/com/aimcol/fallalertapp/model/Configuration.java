package br.com.aimcol.fallalertapp.model;

import lombok.Getter;

public enum Configuration {

    MIN_TIME_TO_NOTIFY_AGAIN(30000L),
    CUSTOM_MSG_FOR_FALL_EVENT(null),
    DATE_FORMAT("MM/dd/yyyy");

    @Getter
    private Object defaultValue;

    Configuration(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
