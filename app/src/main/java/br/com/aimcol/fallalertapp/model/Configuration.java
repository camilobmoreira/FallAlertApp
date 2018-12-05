package br.com.aimcol.fallalertapp.model;

import lombok.Getter;

public enum Configuration {

    MIN_TIME_TO_NOTIFY_AGAIN("Min time to send another notification"),
    CUSTOM_MSG_FOR_FALL_EVENT("Message to send to caregiver when a fall happens");

    @Getter
    private String text;

    Configuration(String text) {
        this.text = text;
    }
}
