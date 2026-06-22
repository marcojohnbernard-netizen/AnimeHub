package com.animehub.model;

public enum WatchStatus {
    PLAN_TO_WATCH("Plan to Watch"),
    WATCHING("Currently Watching"),
    COMPLETED("Completed"),
    DROPPED("Dropped");

    private final String label;

    WatchStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
