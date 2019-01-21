package com.sap.odatav2.annotation.extensions.annotation.util;

import com.sap.odatav2.annotation.extensions.annotation.property.Label;
import com.sap.odatav2.annotation.extensions.annotation.value.AnnotationPropertyNames;
import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;

import java.util.List;
import java.util.ResourceBundle;

import static com.sap.odatav2.annotation.extensions.annotation.util.AnnotationUtils.createSapDataAnnotationAttribute;

public abstract class AnnotationCreator {

    private static final String DEFAULT_VALUE_FALSE = "false";
    private static final String DEFAULT_VALUE_TRUE = "true";

    protected void addLableProperty(ResourceBundle resourceBundle, List<AnnotationAttribute> attributes, Label label) {
        if (label != null) {
            String text = label.text();
            String i18nKey = label.key();
            if (!StringUtils.isEmpty(i18nKey)) {
                text = resourceBundle.getString(i18nKey);
            }
            attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.Label.getPropertyName(), text));
        }
    }

    protected void addDefautLogicTrueProperty(List<AnnotationAttribute> attributes, boolean defaultValue,
                                              AnnotationPropertyNames propertyName) {
        if (!defaultValue) {
            attributes.add(createSapDataAnnotationAttribute(propertyName.getPropertyName(), DEFAULT_VALUE_FALSE));
        } else {
            attributes.add(createSapDataAnnotationAttribute(propertyName.getPropertyName(), DEFAULT_VALUE_TRUE));
        }
    }

    protected void addDefautLogicFalseProperty(List<AnnotationAttribute> attributes, boolean defaultValue,
                                               AnnotationPropertyNames propertyName) {
        if (defaultValue) {
            attributes.add(createSapDataAnnotationAttribute(propertyName.getPropertyName(), DEFAULT_VALUE_TRUE));
        }
    }

    protected void addHierarchyProperty(List<AnnotationAttribute> attributes, String text, AnnotationPropertyNames propertyName) {
        if (!StringUtils.isEmpty(text)) {
            attributes.add(createSapDataAnnotationAttribute(propertyName.getPropertyName(), text));
        }
    }
}
