package com.matteoveroni.appifywebsite.events;

/**
 * @Author: Matteo Veroni
 */

public class EventDisplayError {

    private final String errorDescription;

    public EventDisplayError(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorDescription() {
        return this.errorDescription;
    }
}
