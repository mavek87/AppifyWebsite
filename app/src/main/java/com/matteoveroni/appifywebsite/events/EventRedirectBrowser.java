package com.matteoveroni.appifywebsite.events;

/**
 * @Author: Matteo Veroni
 */

public class EventRedirectBrowser {
    private final String url;

    public EventRedirectBrowser(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}
