package com.sap.odatav2.annotation.extensions.annotation.value;

public enum DisplayFormatOptions {
    DATE("Date"),
    NONNEGATIVE("NonNegative"),
    UPPERCASE("UpperCase");

    private final String value;

    private DisplayFormatOptions(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
