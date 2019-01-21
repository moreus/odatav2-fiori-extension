package com.sap.odatav2.annotation.extensions.annotation.validation;

import com.sap.cloud.commons.collection.CollectionUtils;
import com.sap.odatav2.annotation.extensions.annotation.util.ValidatorUtils;
import com.sap.odatav2.annotation.extensions.annotation.validation.base.ValidatorParameters;
import com.sap.odatav2.annotation.extensions.annotation.value.AnnotationPropertyNames;
import com.sap.odatav2.annotation.extensions.annotation.validation.base.AnnotationValidator;
import org.apache.olingo.odata2.annotation.processor.api.extension.exception.ODataValidationFailedException;
import org.apache.olingo.odata2.api.commons.ODataHttpMethod;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.uri.expression.ExceptionVisitExpression;

public class CreatableValidator implements AnnotationValidator {
	public CreatableValidator() {
	}

	public void operationValidation(ValidatorParameters params)
			throws ODataValidationFailedException, ExceptionVisitExpression, ODataApplicationException, EdmException {
		if (this.requestMethodIsRelevantForFieldValidation(params.getODataHttpMethod())
				&& CollectionUtils.allNonNull(new Object[] { params.getEdmEntitySet(), params.getRequestParams() })) {
			ValidatorUtils.checkEntityPropertyAnnotations(params.getEdmEntitySet(), params.getRequestParams(),
					AnnotationPropertyNames.Creatable);
		}

	}

	public boolean requestMethodIsRelevantForFieldValidation(ODataHttpMethod odataHttpMethod) {
		return odataHttpMethod == ODataHttpMethod.POST;
	}
}
