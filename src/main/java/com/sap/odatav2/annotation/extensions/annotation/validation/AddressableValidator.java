package com.sap.odatav2.annotation.extensions.annotation.validation;

import com.sap.cloud.commons.collection.CollectionUtils;
import com.sap.cloud.commons.lambda.ThrowingFunction;
import com.sap.odatav2.annotation.extensions.annotation.util.ValidatorUtils;
import com.sap.odatav2.annotation.extensions.annotation.validation.base.ValidatorParameters;
import com.sap.odatav2.annotation.extensions.annotation.validation.base.AnnotationValidator;
import com.sap.odatav2.annotation.extensions.annotation.value.AnnotationPropertyNames;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.apache.olingo.odata2.annotation.processor.api.extension.exception.ODataValidationFailedException;
import org.apache.olingo.odata2.api.commons.ODataHttpMethod;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.uri.PathSegment;
import org.apache.olingo.odata2.api.uri.expression.ExceptionVisitExpression;

public class AddressableValidator implements AnnotationValidator {
    public AddressableValidator() {
    }

    private void checkAddressableAnnotation(EdmEntitySet edmEntitySet, List<PathSegment> pathSegmentList) {
        Optional.ofNullable(pathSegmentList).ifPresent((pathSegments) -> {
            if(!pathSegmentList.isEmpty()) {
                try {
                    String entitySetName = edmEntitySet.getName();
                    String path = pathSegmentList.get(0).getPath();
                    Predicate<String> predicateEntityName = this.isEntityName(entitySetName);
                    if(predicateEntityName.test(path)) {
                        ValidatorUtils.checkAnnotationEntitySet(edmEntitySet, AnnotationPropertyNames.Addressable.getPropertyName());
                    }
                } catch (ODataValidationFailedException | EdmException var7) {
                    ThrowingFunction.throwManagedException(var7);
                }
            }

        });
    }

    private Predicate<String> isEntityName(String entitySetName) {
        return (p) -> {
            return p.startsWith(entitySetName + "(") || p.equals(entitySetName);
        };
    }

    @Override
	public void operationValidation(ValidatorParameters params) throws ODataValidationFailedException, ExceptionVisitExpression, ODataApplicationException, EdmException {
        if(this.requestMethodIsRelevantForFieldValidation(params.getODataHttpMethod()) && CollectionUtils.allNonNull(new Object[]{params.getEdmEntitySet(), params.getPathSegmentList()})) {
            this.checkAddressableAnnotation(params.getEdmEntitySet(), params.getPathSegmentList());
        }

    }

    @Override
	public boolean requestMethodIsRelevantForFieldValidation(ODataHttpMethod odataHttpMethod) {
        return odataHttpMethod == ODataHttpMethod.PUT || odataHttpMethod == ODataHttpMethod.POST || odataHttpMethod == ODataHttpMethod.GET || odataHttpMethod == ODataHttpMethod.PATCH || odataHttpMethod == ODataHttpMethod.MERGE || odataHttpMethod == ODataHttpMethod.DELETE;
    }
}
