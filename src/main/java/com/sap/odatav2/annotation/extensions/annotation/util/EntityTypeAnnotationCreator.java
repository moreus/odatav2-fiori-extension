package com.sap.odatav2.annotation.extensions.annotation.util;

import com.sap.odatav2.annotation.extensions.annotation.ModelEntityType;
import com.sap.odatav2.annotation.extensions.annotation.value.AnnotationPropertyNames;
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.EntityType;

import java.lang.annotation.Annotation;
import java.util.*;

import static com.sap.odatav2.annotation.extensions.annotation.util.AnnotationUtils.createSapDataAnnotationAttribute;


public class EntityTypeAnnotationCreator extends AnnotationCreator {
    private final ResourceBundle resourceBundle;

    public EntityTypeAnnotationCreator(ResourceBundle bundle) {
        this.resourceBundle = bundle;
    }

    /**
     * Add annotation to an EntitySet based on Annotation of the Edm Entity defined.
     *
     * @param et          The entity type
     * @param annotations The annotation array
     */
    public void addEntityTypeAnnotations(EntityType et, Annotation[] annotations) {
        List<AnnotationAttribute> attributes = new ArrayList<>();

        Arrays.stream(annotations).forEach(annotation -> {
            if (annotation instanceof ModelEntityType) {
                createSAPEntitySetProperty(attributes, (ModelEntityType) annotation);
            }


        });

        Optional.ofNullable(et.getAnnotationAttributes())
            .map((List<AnnotationAttribute> annotationAttributeList) -> {
                annotationAttributeList.addAll(attributes);
                return annotationAttributeList;
            }).orElseGet(() -> {
            et.setAnnotationAttributes(attributes);
            return attributes;
        });
    }
    private void createSAPEntitySetProperty(List<AnnotationAttribute> attributes, ModelEntityType entityTypeAnnotation) {
        createContentVersionProperty(attributes, entityTypeAnnotation);
        createLabelProperty(attributes, entityTypeAnnotation);
    }
    private void createLabelProperty(List<AnnotationAttribute> attributes, ModelEntityType property) {
        addLableProperty(this.resourceBundle, attributes, property.label());
    }

    private void createContentVersionProperty(List<AnnotationAttribute> attributes, ModelEntityType entityTypeAnnotation) {
        int contentVersion = entityTypeAnnotation.contentVersion();
        if (contentVersion != 0) {
            attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.ContentVersion.getPropertyName(), String.valueOf(contentVersion)));
        }
    }

}
