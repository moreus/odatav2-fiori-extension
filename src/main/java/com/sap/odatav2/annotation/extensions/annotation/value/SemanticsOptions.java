package com.sap.odatav2.annotation.extensions.annotation.value;

public enum SemanticsOptions {
    UNITOFMEASURE("unit-of-measure"),
    CURRENCYCODE("currency-code"),
    EMAIL("email"),
    URL("url"),
    NAME("name"),
    GIVENNAME("givenname"),
    MIDDLENAME("middlename"),
    FAMILYNAME("familyname"),
    NICKNAME("nickname"),
    NOTE("note"),
    SUFFIX("suffix"),
    TEL("tel");

    private final String value;

    private SemanticsOptions(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
