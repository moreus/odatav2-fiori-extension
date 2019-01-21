package com.sap.odatav2.annotation.extensions;

import com.sap.cloud.commons.lambda.ThrowingFunction;
import com.sap.odatav2.annotation.extensions.annotation.util.*;
import com.sap.odatav2.annotation.extensions.annotation.validation.*;
import com.sap.odatav2.annotation.extensions.annotation.validation.base.AnnotationValidator;
import com.sap.odatav2.annotation.extensions.annotation.validation.base.ODataOperationValidators;
import com.sap.odatav2.annotation.extensions.annotation.validation.base.ValidatorParameters;
import org.apache.olingo.odata2.annotation.processor.api.extension.ODataAnnotationEdmExtension;
import org.apache.olingo.odata2.annotation.processor.api.extension.exception.ODataValidationFailedException;
import org.apache.olingo.odata2.api.commons.ODataHttpMethod;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.FunctionImport;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.PathSegment;
import org.apache.olingo.odata2.api.uri.expression.ExceptionVisitExpression;
import org.apache.olingo.odata2.api.uri.expression.FilterExpression;
import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;

import java.util.*;

public class ODataAnnotationEdmExtensionImpl implements ODataAnnotationEdmExtension {
	private AnnotationGenerator annotationGenerator;
	private EntityPropertyAnnotationCreator entityPropertyAnnotationCreator;
	private EntityTypeAnnotationCreator entityTypeAnnotationCreator;
	private EntitySetAnnotationCreator entitySetAnnotationCreator;
	private ODataOperationValidators odataOperationValidators;
	private ResourceBundle resourceBundle;

	public ODataAnnotationEdmExtensionImpl(ODataContext context) {
		List<Locale> locales = context.getAcceptableLanguages();
		if (locales.size() > 0) {
			resourceBundle = ResourceBundle.getBundle("i18n.message", locales.get(0));
		}
		this.entitySetAnnotationCreator = new EntitySetAnnotationCreator(resourceBundle);
		this.entityPropertyAnnotationCreator = new EntityPropertyAnnotationCreator(resourceBundle);
		this.entityTypeAnnotationCreator = new EntityTypeAnnotationCreator(resourceBundle);
		this.annotationGenerator = new AnnotationGenerator(
				this.entitySetAnnotationCreator,
				this.entityTypeAnnotationCreator,
				this.entityPropertyAnnotationCreator);
		this.odataOperationValidators = new ODataOperationValidators(this.getAnnotationValidators());
	}

	@Override
	public void extendEdmSchema(EdmProvider edmProvider, Collection<Class<?>> annotatedClasses) throws ODataException {
		List<EntitySet> entitySets = AnnotationUtils.getEntitySets(edmProvider);
		List<EntityType> entityTypes = AnnotationUtils.getEntityTypes(edmProvider);
		List<FunctionImport> functionImports = AnnotationUtils.getFunctionImports(edmProvider);
		if (!entitySets.isEmpty() && !annotatedClasses.isEmpty()) {
			this.annotationGenerator.generateAnnotations(edmProvider, entitySets, entityTypes, annotatedClasses);
		}
	}

	@Override
	public Collection<Class<?>> getFunctionImportImplementations() {
		return new ArrayList<>();
	}

	@Override
	public void validateOperation(ODataHttpMethod odataHttpMethod, EdmEntitySet edmEntitySet,
			FilterExpression filterExpression, OrderByExpression orderByExpression, List<PathSegment> pathSegmentList,
			Map<String, Object> payloadMap, Map<String, Object> entityValues) throws ODataException {
		Optional.ofNullable(this.odataOperationValidators.getValidatorList()).ifPresent((annotationValidators) -> {
			annotationValidators.forEach((annotationValidator) -> {
				ValidatorParameters params = new ValidatorParameters();
				params.setOdataHttpMethod(odataHttpMethod);
				params.setEdmEntitySet(edmEntitySet);
				setValidatorParameters(params, annotationValidator, filterExpression, orderByExpression,
						pathSegmentList, payloadMap, entityValues);

				try {
					annotationValidator.operationValidation(params);
				} catch (ODataValidationFailedException | EdmException | ODataApplicationException
						| ExceptionVisitExpression var11) {
					ThrowingFunction.throwManagedException(var11);
				}

			});
		});
	}

	private void setValidatorParameters(ValidatorParameters params, AnnotationValidator annotationValidator,
			FilterExpression filterExpression, OrderByExpression orderByExpression, List<PathSegment> pathSegmentList,
			Map<String, Object> payloadMap, Map<String, Object> entityValues) {
		if (annotationValidator instanceof AddressableValidator) {
			params.setPathSegmentList(pathSegmentList);
		} else if (!(annotationValidator instanceof CreatableValidator)
				&& !(annotationValidator instanceof UpdatableValidator)
				&& !(annotationValidator instanceof VisibleValidator)) {
			if (annotationValidator instanceof FieldControlValidator) {
				params.setRequestParams(payloadMap);
				params.setEntityValues(entityValues);
			} else if (annotationValidator instanceof FilterableValidator) {
				params.setFilterExpression(filterExpression);
			} else if (annotationValidator instanceof SortableValidator) {
				params.setOrderByExpression(orderByExpression);
			}
		} else {
			params.setRequestParams(payloadMap);
		}

	}

	private List<AnnotationValidator> getAnnotationValidators() {
		List<AnnotationValidator> annotationGeneratorList = new ArrayList<>();
		annotationGeneratorList.add(new AddressableValidator());
		annotationGeneratorList.add(new CreatableValidator());
		annotationGeneratorList.add(new FieldControlValidator());
		annotationGeneratorList.add(new FilterableValidator());
		annotationGeneratorList.add(new SortableValidator());
		annotationGeneratorList.add(new UpdatableValidator());
		annotationGeneratorList.add(new VisibleValidator());
		return annotationGeneratorList;
	}
}
