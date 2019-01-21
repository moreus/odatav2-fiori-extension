package com.sap.odatav2.annotation.extensions.annotation.validation;

import com.sap.cloud.commons.collection.CollectionUtils;
import com.sap.cloud.commons.lambda.ThrowingFunction;
import com.sap.odatav2.annotation.extensions.annotation.util.ValidatorUtils;
import com.sap.odatav2.annotation.extensions.annotation.validation.base.ValidatorParameters;
import com.sap.odatav2.annotation.extensions.annotation.validation.base.AnnotationValidator;
import com.sap.odatav2.annotation.extensions.annotation.value.AnnotationPropertyNames;
import java.util.Optional;

import org.apache.olingo.odata2.annotation.processor.api.extension.exception.ODataValidationFailedException;
import org.apache.olingo.odata2.api.commons.ODataHttpMethod;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.uri.expression.ExceptionVisitExpression;

public class SortableValidator implements AnnotationValidator {
    public SortableValidator() {
    }

    @Override
	public void operationValidation(ValidatorParameters params) throws ODataValidationFailedException, ExceptionVisitExpression, ODataApplicationException, EdmException {
        if(this.requestMethodIsRelevantForFieldValidation(params.getODataHttpMethod()) && CollectionUtils.allNonNull(new Object[]{params.getEdmEntitySet(), params.getOrderByExpression()})) {
            Optional.ofNullable(ValidatorUtils.getCommonProperties(params.getOrderByExpression())).ifPresent((sortableFieldNames) -> {
                try {
                    ValidatorUtils.checkFilterableSortableProps(params.getEdmEntitySet(), sortableFieldNames, AnnotationPropertyNames.Sortable);
                } catch (ODataValidationFailedException | EdmException var3) {
                    ThrowingFunction.throwManagedException(var3);
                }

            });
        }

    }

    @Override
	public boolean requestMethodIsRelevantForFieldValidation(ODataHttpMethod odataHttpMethod) {
        return odataHttpMethod == ODataHttpMethod.GET;
    }
}
