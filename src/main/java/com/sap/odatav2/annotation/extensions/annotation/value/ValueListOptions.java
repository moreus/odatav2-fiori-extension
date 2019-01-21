package com.sap.odatav2.annotation.extensions.annotation.value;

public enum ValueListOptions {
    FIXEDVALUES("fixed-value"),
    STANDARD("standard");

    private final String value;

    private ValueListOptions(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

