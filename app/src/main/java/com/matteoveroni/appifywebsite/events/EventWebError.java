package com.matteoveroni.appifywebsite.events;

/**
 * @Author: Matteo Veroni
 */

public class EventWebError {
    private final String errorDescription;

    public EventWebError(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorDescription() {
        return this.errorDescription;
    }
}
