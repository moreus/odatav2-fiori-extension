package com.sap.odatav2.annotation.extensions.annotation.value;

public enum FilterRestriction {
    SINGLEVALUE("single-value"),
    MULTIVALUE("multi-value"),
    INTERVAL("interval");
    private final String value;

    private FilterRestriction(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
