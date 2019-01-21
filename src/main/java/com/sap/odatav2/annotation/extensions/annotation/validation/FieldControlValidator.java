package com.sap.odatav2.annotation.extensions.annotation.validation;

import com.sap.cloud.commons.collection.CollectionUtils;
import com.sap.cloud.commons.lambda.ThrowingFunction;
import com.sap.odatav2.annotation.extensions.annotation.validation.base.ValidatorParameters;
import com.sap.odatav2.annotation.extensions.annotation.util.ValidatorUtils;
import com.sap.odatav2.annotation.extensions.annotation.validation.base.AnnotationValidator;
import com.sap.odatav2.annotation.extensions.annotation.value.AnnotationPropertyNames;
import com.sap.odatav2.annotation.extensions.annotation.value.FieldControlOptions;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.olingo.odata2.annotation.processor.api.extension.exception.ODataValidationFailedException;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.commons.ODataHttpMethod;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataHttpException;
import org.apache.olingo.odata2.api.uri.expression.ExceptionVisitExpression;

public class FieldControlValidator implements AnnotationValidator {
	public FieldControlValidator() {
	}

	@Override
	public void operationValidation(ValidatorParameters params)
			throws ODataValidationFailedException, ExceptionVisitExpression, ODataApplicationException, EdmException {
		if (this.requestMethodIsRelevantForFieldValidation(params.getODataHttpMethod()) && CollectionUtils.allNonNull(
				new Object[] { params.getEdmEntitySet(), params.getRequestParams(), params.getEntityValues() })) {
			this.checkFieldControlAnnotation(params.getEdmEntitySet(), params.getRequestParams(),
					params.getEntityValues());
		}

	}

	@Override
	public boolean requestMethodIsRelevantForFieldValidation(ODataHttpMethod odataHttpMethod) {
		return odataHttpMethod == ODataHttpMethod.MERGE || odataHttpMethod == ODataHttpMethod.PATCH
				|| odataHttpMethod == ODataHttpMethod.PUT;
	}

	/**
	 * Check the field control annotation.
	 * 
	 * @param edmEntitySet
	 *            The edm entity set
	 * @param payloadMap
	 *            The payload
	 * @param entityValues
	 *            The entity properties values
	 * @throws EdmException
	 *             Throws exception if an edm error occurs
	 * @throws ODataValidationFailedException
	 *             Throws exception if the http operation validation fails
	 */
	private void checkFieldControlAnnotation(EdmEntitySet edmEntitySet, Map<String, Object> payloadMap,
			Map<String, Object> entityValues) throws EdmException, ODataValidationFailedException { // NOSONAR
		EdmEntityType entity = edmEntitySet.getEntityType();
		Optional.ofNullable(payloadMap.keySet())
				.ifPresent(mapEntries -> Optional.ofNullable(new ArrayList<String>(mapEntries))
						.ifPresent(proptyNames -> proptyNames.stream().forEach(propName -> {
							try {
								EdmProperty edmProperty = (EdmProperty) entity.getProperty(propName);
								checkRequiredNullableAnnotationFromPayload(edmProperty, payloadMap, entityValues);
								removeUpdatableAnnotationFromPayload(edmProperty, payloadMap, entityValues);
								removeVisibleAnnotationFromPayload(edmProperty, payloadMap, entityValues);
							} catch (EdmException | ODataValidationFailedException e) {
								ThrowingFunction.throwManagedException(e);
							}
						})));
	}

	private void checkRequiredNullableAnnotationFromPayload(EdmProperty edmProperty, Map<String, Object> payloadMap,
			Map<String, Object> entityValues) throws EdmException, ODataValidationFailedException {
		Object value = ValidatorUtils.getFieldValue(edmProperty.getName(), payloadMap);
		boolean isNullable = edmProperty.getFacets().isNullable().booleanValue();
		FieldControlOptions fieldControlValue = this.getUxFcFieldValue(edmProperty, entityValues);
		if (value == null && isNullable && fieldControlValue == FieldControlOptions.REQUIRED) {
			ThrowingFunction.throwManagedException(new ODataValidationFailedException(
					ODataHttpException.COMMON
							.addContent(new Object[] { "Validation failed for property: " + edmProperty.getName() }),
					HttpStatusCodes.METHOD_NOT_ALLOWED));
		}

	}

	private void removeVisibleAnnotationFromPayload(EdmProperty edmProperty, Map<String, Object> payloadMap,
			Map<String, Object> entityValues) throws EdmException {
		boolean isVisible = ValidatorUtils.getAnnotationValueForModelProperty(edmProperty,
				AnnotationPropertyNames.Visible);
		FieldControlOptions fieldControlValue = this.getUxFcFieldValue(edmProperty, entityValues);
		if (this.isVisibleRemoveFromPayload(isVisible).test(fieldControlValue)) {
			ValidatorUtils.removefromPayload(edmProperty.getName(), payloadMap);
		}

	}

	private Predicate<FieldControlOptions> isVisibleRemoveFromPayload(boolean isVisible) {
		return (fieldControlValue) -> {
			return !isVisible
					&& (fieldControlValue == FieldControlOptions.READONLY
							|| fieldControlValue == FieldControlOptions.OPTIONAL
							|| fieldControlValue == FieldControlOptions.REQUIRED)
					|| isVisible && fieldControlValue == FieldControlOptions.HIDDEN;
		};
	}

	private void removeUpdatableAnnotationFromPayload(EdmProperty edmProperty, Map<String, Object> payloadMap,
			Map<String, Object> entityValues) throws EdmException {
		boolean isUpdatable = ValidatorUtils.getAnnotationValueForModelProperty(edmProperty,
				AnnotationPropertyNames.Updatable);
		FieldControlOptions fieldControlValue = this.getUxFcFieldValue(edmProperty, entityValues);
		if (this.isUpdatableRemoveFromPayload(isUpdatable).test(fieldControlValue)) {
			ValidatorUtils.removefromPayload(edmProperty.getName(), payloadMap);
		}

	}

	private Predicate<FieldControlOptions> isUpdatableRemoveFromPayload(boolean isUpdatable) {
		return (fieldControlValue) -> {
			return !isUpdatable && (fieldControlValue == FieldControlOptions.OPTIONAL
					|| fieldControlValue == FieldControlOptions.REQUIRED);
		};
	}

	private FieldControlOptions getUxFcFieldValue(EdmProperty edmProperty, Map<String, Object> entityValues)
			throws EdmException {
		String uxFcFieldName = ValidatorUtils.getUxFcFieldName(edmProperty);
		return Optional.ofNullable(ValidatorUtils.getValueofUxFcField(uxFcFieldName, entityValues))
				.map((valueofUxFcField) -> {
					return FieldControlOptions.getFieldControlValue(valueofUxFcField.byteValue());
				}).orElseGet(() -> {
					return FieldControlOptions.OPTIONAL;
				});
	}
}