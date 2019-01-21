package com.sap.odatav2.annotation.extensions.annotation.util;

import com.sap.cloud.commons.lambda.ThrowingFunction;
import com.sap.odatav2.annotation.extensions.annotation.validation.expression.VisitFilterOrderByExpression;
import com.sap.odatav2.annotation.extensions.annotation.value.AnnotationPropertyNames;
import com.sap.odatav2.annotation.extensions.annotation.value.FieldControlOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.olingo.odata2.annotation.processor.api.extension.exception.ODataValidationFailedException;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.EdmAnnotationAttribute;
import org.apache.olingo.odata2.api.edm.EdmAnnotations;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataHttpException;
import org.apache.olingo.odata2.api.uri.expression.CommonExpression;
import org.apache.olingo.odata2.api.uri.expression.ExceptionVisitExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidatorUtils {
    private static final String ANNOTATION_VALUE_FALSE = "false";
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorUtils.class);

    private ValidatorUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Removes fields from the payload.
     * @param name The field name
     * @param payloadMap The payload
     */
    public static void removefromPayload(String name, Map<String, Object> payloadMap) {
      Iterator<Map.Entry<String,Object>> iterator = payloadMap.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, Object> entry = iterator.next();
        if (entry.getKey().equals(name)) {
          iterator.remove();
          LOGGER.warn("Entry:" + entry.getKey() + " was removed from payload");
        }
      }
    }

    /**
     * Retrieves the value of the specific annotation for a specific property.
     * @param edmProperty The entity property
     * @param annotationName The annotation name
     * @return boolean The value of the annotation [true or false]
     * @throws EdmException Throws exception if edm error occurs
     */
    public static boolean getAnnotationValueForModelProperty(
        org.apache.olingo.odata2.api.edm.EdmProperty edmProperty,
        AnnotationPropertyNames annotationName) throws EdmException {

      List<EdmAnnotationAttribute> edmAnnotationAttributes =
          Optional.ofNullable(edmProperty.getAnnotations())
          .filter(Objects::nonNull)
          .map(EdmAnnotations::getAnnotationAttributes)
          .orElseGet(Collections::emptyList);

      return edmAnnotationAttributes.stream()
          .filter(edmAnnotationAttribute ->
              annotationName.getPropertyName().equals(edmAnnotationAttribute.getName()))
          .map(edmAnnotationAttribute ->
              Boolean.valueOf(edmAnnotationAttribute.getText())).findFirst()
          .orElseGet(() -> true);
    }

    /**
     * Get the value of the specific field in the payload.
     * @param edmPropertyName The property name
     * @param payloadMap The payload
     * @return The value of the field
     */
    public static Object getFieldValue(String edmPropertyName, Map<String, Object> payloadMap) {
      Object fieldValue = null;
      Iterator<Map.Entry<String,Object>> iterator = payloadMap.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, Object> entry = iterator.next();
        if (entry.getKey().equals(edmPropertyName)) {
          fieldValue = entry.getValue();
          break;
        }
      }
      return fieldValue;
    }

    /**
     * Check the Entity Set Annotation value for the specific http operation.
     * if value is false the operation will not be execute
     * @param edmEntitySet The Edm Entity Set
     * @param annotationName The annotation name
     * @throws EdmException Throws exception if edm error occurs
     * @throws ODataValidationFailedException Throws exception if odata validation error occurs
     */

    public static void checkAnnotationEntitySet(EdmEntitySet edmEntitySet, String annotationName)
        throws EdmException, ODataValidationFailedException { // NOSONAR
      Optional.ofNullable(edmEntitySet.getAnnotations()).ifPresent(edmAnnotations ->
          Optional.ofNullable(edmAnnotations.getAnnotationAttributes())
              .ifPresent(edmAnnotationAttributes -> edmAnnotationAttributes.stream()
                  .filter(edmAnnotationAttribute ->
                      edmAnnotationAttribute.getName().equalsIgnoreCase(annotationName)
            ).forEach(edmAnnotationAttribute ->
              Optional.ofNullable(edmAnnotationAttribute.getText()).ifPresent(annoValue -> {
                if (edmAnnotationAttribute.getText().equals(ANNOTATION_VALUE_FALSE)) {
                  ThrowingFunction.throwManagedException(
                      new ODataValidationFailedException(
                          ODataHttpException.COMMON
                              .addContent("Validation failed for: " + annotationName),
                          HttpStatusCodes.METHOD_NOT_ALLOWED)
                  );
                }
              })
            )
          )
      );
    }

    /**
     * Get the property name from a {@link CommonExpression} Filterable or OrderByExpression.
     * @param commonExpression The expression
     * @return The list of property names
     * @throws ExceptionVisitExpression Throws an exception if the visit expression fails
     * @throws ODataApplicationException Throws exception if odata error occurs
     */
    public static List<String> getCommonProperties(CommonExpression commonExpression)
        throws ExceptionVisitExpression, ODataApplicationException { // NOSONAR
      List<String> commonProperties = new ArrayList<>();
      Optional.ofNullable(commonExpression).ifPresent(commonExp -> {
        VisitFilterOrderByExpression visitFilterOrderByExpression =
            new VisitFilterOrderByExpression();
        try {
          commonExp.accept(visitFilterOrderByExpression);
        } catch (ExceptionVisitExpression | ODataApplicationException e) {
          ThrowingFunction.throwManagedException(e);
        }
        commonProperties.addAll(visitFilterOrderByExpression.getAttributeNames());

      });
      return commonProperties;
    }
    
    /**
     * Check the annotation value if is false remove field from the payload.
     * @param edmEntitySet The Edm Entity Set
     * @param payloadMap The payload
     * @param annoName The annotation name
     * @throws EdmException Throws an edm exception if edm error occurs
     */
    public static void checkEntityPropertyAnnotations(
        EdmEntitySet edmEntitySet,
        Map<String, Object> payloadMap,
        AnnotationPropertyNames annoName) throws EdmException {

      EdmEntityType entity = edmEntitySet.getEntityType();
      Optional.ofNullable(payloadMap.keySet()).ifPresent(mapEntries ->
          Optional.ofNullable(new ArrayList<String>(mapEntries)).ifPresent(propNames ->
            propNames.stream().forEach(propName -> {
              try {
                EdmProperty edmProperty = (EdmProperty) entity.getProperty(propName);
                Optional.ofNullable(edmProperty.getAnnotations()).ifPresent(edmAnnotations ->
                    Optional.ofNullable(edmAnnotations.getAnnotationAttributes())
                        .ifPresent(edmAnnotationAttributes ->
                      edmAnnotationAttributes.stream().forEach(edmAnnotationAttribute -> {
                        String name = edmAnnotationAttribute.getName();
                        if (annoName.getPropertyName().equals(name)
                            && edmAnnotationAttribute.getText().equals(ANNOTATION_VALUE_FALSE)) {
                          ValidatorUtils.removefromPayload(propName, payloadMap);
                        }
                      })
                    )
                );
              } catch (EdmException e) {
                ThrowingFunction.throwManagedException(e);
              }
            })
          )
      );
    }

    /**
     * Check if field names contained in a filterable/sortable expression is annotated.
     * if annotations are false the http operation will be cancelled
     * @param edmEntitySet The edm entity set
     * @param expressionFieldNames The field names in the sortable/filterable expression
     * @param annotationName The annotation property name
     * @throws EdmException Throws an edm exception if edm error occurs
     * @throws ODataValidationFailedException Throws exception if odata validation error occurs
     */
    public static void checkFilterableSortableProps(
        EdmEntitySet edmEntitySet,
        List<String> expressionFieldNames,
        AnnotationPropertyNames annotationName)
        throws EdmException, ODataValidationFailedException { // NOSONAR
      EdmEntityType entity = edmEntitySet.getEntityType();
      Optional.ofNullable(expressionFieldNames).ifPresent(propertyNames ->
          propertyNames.stream().forEach(propertyName -> {
            try {
              EdmProperty edmProperty = (EdmProperty) entity.getProperty(propertyName);
              checkAnnotationValueIsFalse(annotationName, edmProperty);
            } catch (EdmException | ODataValidationFailedException e) {
              ThrowingFunction.throwManagedException(e);
            }
          })
      );
    }

    /**
     * Checks if the annotation value is false, in this case throws an exception.
     * @param annotationName The annotation name
     * @param edmProperty The edm property object
     * @throws EdmException Throws edm exception if an edm error occurs
     */
    private static void checkAnnotationValueIsFalse(
        AnnotationPropertyNames annotationName,
        EdmProperty edmProperty) throws EdmException,ODataValidationFailedException { // NOSONAR
      Optional.ofNullable(edmProperty.getAnnotations()).ifPresent(edmAnnotations ->
          Optional.ofNullable(edmAnnotations.getAnnotationAttributes())
            .ifPresent(edmAnnotationAttributes -> {
                  try {
                    checkFilterableSortableAnnoValueFalse(
                        annotationName,
                        edmAnnotationAttributes);
                  } catch (ODataValidationFailedException e) {
                    ThrowingFunction.throwManagedException(e);
                  }
                }
            )
      );
    }

    /**
     * Check value of the annotation
     * if value false the http operation will b not be executed.
     * @param annotationName The annotation name
     * @param edmAnnotationAttributes The list of annotations for the specific field
     * @throws ODataValidationFailedException Throws exception if odata validation error occurs
     */
    private static void checkFilterableSortableAnnoValueFalse(
        AnnotationPropertyNames annotationName,
        List<EdmAnnotationAttribute> edmAnnotationAttributes)
        throws ODataValidationFailedException { // NOSONAR
      edmAnnotationAttributes.stream().forEach(edmAnnotationAttribute -> {
        String name = edmAnnotationAttribute.getName();
        if (annotationName.getPropertyName().equals(name)) {
          String value = edmAnnotationAttribute.getText();
          if (value.equals(ANNOTATION_VALUE_FALSE)) {
            ThrowingFunction.throwManagedException(
                new ODataValidationFailedException(
                    ODataHttpException.COMMON
                        .addContent("Validation failed for: " + annotationName),
                    HttpStatusCodes.METHOD_NOT_ALLOWED)
            );
          }
        }
      });
    }

    /**
     * Retrieves the UxFc Field name.
     * @param edmProperty The edm entity property
     * @return The uxfc name
     * @throws EdmException Throws an edm exception if edm error occurs
     */
    public static String getUxFcFieldName(
        org.apache.olingo.odata2.api.edm.EdmProperty edmProperty) throws EdmException {

      List<EdmAnnotationAttribute> edmAnnotationAttributes =
          Optional.ofNullable(edmProperty.getAnnotations())
              .filter(Objects::nonNull)
              .map(EdmAnnotations::getAnnotationAttributes)
              .orElseGet(Collections::emptyList);

      return edmAnnotationAttributes.stream()
          .filter(edmAnnotationAttribute ->
              AnnotationPropertyNames.FieldControl
                  .getPropertyName().equals(edmAnnotationAttribute.getName()))
          .map(EdmAnnotationAttribute::getText)
          .findFirst().orElseGet(() -> "");
    }

    /**
     * Retreives the UxFc field value.
     * @param uxFcFieldName The uxfc field name
     * @param payloadMap The payload
     * @return Byte The UxFc Field value
     * @throws EdmException Throws an edm exception if edm error occurs
     */
    public static Byte getValueofUxFcField(
        String uxFcFieldName,
        Map<String, Object> payloadMap) {

      final Set<String> propertyNameList = Optional.ofNullable(payloadMap)
          .map(Map::keySet)
          .orElseGet(HashSet::new);

      return propertyNameList.stream().filter(propertyName -> propertyName.equals(uxFcFieldName))
          .map(edmPropertyName -> Optional.ofNullable(getFieldValue(uxFcFieldName,payloadMap))
              .map(fieldValue -> (byte)fieldValue)
              .orElseGet(() -> (byte)FieldControlOptions.OPTIONAL.getValue())).findFirst()
          .orElseGet(() -> (byte)FieldControlOptions.OPTIONAL.getValue());
    }
}