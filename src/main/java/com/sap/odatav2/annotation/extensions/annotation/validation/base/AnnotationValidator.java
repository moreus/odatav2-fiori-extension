package com.sap.odatav2.annotation.extensions.annotation.validation.base;

import org.apache.olingo.odata2.annotation.processor.api.extension.exception.ODataValidationFailedException;
import org.apache.olingo.odata2.api.commons.ODataHttpMethod;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.uri.expression.ExceptionVisitExpression;

public interface AnnotationValidator {
    void operationValidation(ValidatorParameters var1) throws ODataValidationFailedException, ExceptionVisitExpression, ODataApplicationException, EdmException;

    boolean requestMethodIsRelevantForFieldValidation(ODataHttpMethod var1);
}
