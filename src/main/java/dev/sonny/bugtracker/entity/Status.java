package dev.sonny.bugtracker.entity;

public enum Status {
    OPEN("OPEN"),
    CLOSED("CLOSED");

    private String value;
    private Status(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}