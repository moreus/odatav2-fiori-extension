package com.sap.odatav2.annotation.extensions.annotation.value;

import java.util.Arrays;

public enum FieldControlOptions {
    HIDDEN(0),
    READONLY(1),
    OPTIONAL(3),
    REQUIRED(7);

    private int value;

    FieldControlOptions(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static FieldControlOptions getFieldControlValue(int value) {
        return (FieldControlOptions)Arrays.stream(values()).filter((fieldControlOption) -> {
            return fieldControlOption.getValue() == value;
        }).findFirst().orElseGet(() -> {
            return OPTIONAL;
        });
    }
}