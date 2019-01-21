package com.sap.odatav2.annotation.extensions.annotation.validation.base;

import java.util.List;

public class ODataOperationValidators {
    private List<AnnotationValidator> validatorList;

    public ODataOperationValidators(List<AnnotationValidator> validatorList) {
        this.validatorList = validatorList;
    }

    public List<AnnotationValidator> getValidatorList() {
        return this.validatorList;
    }
}
