package com.sap.odatav2.annotation.extensions.annotation.util;

import com.sap.odatav2.annotation.extensions.annotation.property.Label;
import com.sap.odatav2.annotation.extensions.annotation.ModelEntitySet;

import java.lang.annotation.Annotation;
import java.util.*;

import com.sap.odatav2.annotation.extensions.annotation.value.AnnotationPropertyNames;
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;

import static com.sap.odatav2.annotation.extensions.annotation.util.AnnotationUtils.createSapDataAnnotationAttribute;

/**
 * Adds annotations on entity set level.
 */
public class EntitySetAnnotationCreator extends AnnotationCreator {
    private final ResourceBundle resourceBundle;

    public EntitySetAnnotationCreator(ResourceBundle bundle) {
        this.resourceBundle = bundle;
    }

    /**
     * Add annotation to an EntitySet based on Annotation of the Edm Entity defined.
     *
     * @param es          The entity set
     * @param annotations The annotation array
     */
    public void addEntitySetAnnotations(EntitySet es, Annotation[] annotations) {
        List<AnnotationAttribute> attributes = new ArrayList<>();

        Arrays.stream(annotations).forEach(annotation -> {
            if (annotation instanceof ModelEntitySet) {
                createSAPEntitySetProperty(attributes, (ModelEntitySet) annotation);
            }
            if (annotation instanceof Label) {
                createEntitySetLabel(attributes, (Label) annotation);
            }
        });

        Optional.ofNullable(es.getAnnotationAttributes())
            .map(annotationAttributeList -> {
                annotationAttributeList.addAll(attributes);
                return annotationAttributeList;
            }).orElseGet(() -> {
            es.setAnnotationAttributes(attributes);
            return attributes;
        });
    }

    private void createEntitySetLabel(List<AnnotationAttribute> attributes, Label annotation) {
        addLableProperty(this.resourceBundle, attributes, annotation);
    }

    private void createSAPEntitySetProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        createCreatableProperty(attributes, entitySetAnnotation);
        createDeletableProperty(attributes, entitySetAnnotation);
        createAddressableProperty(attributes, entitySetAnnotation);
        createPageableProperty(attributes, entitySetAnnotation);
        createSearchableProperty(attributes, entitySetAnnotation);
        createRequireFilterProperty(attributes, entitySetAnnotation);
        createSubScribableProperty(attributes, entitySetAnnotation);
        createUpdatableProperty(attributes, entitySetAnnotation);
        createContentVersionProperty(attributes, entitySetAnnotation);
        createTopableProperty(attributes, entitySetAnnotation);
        createCountableProperty(attributes, entitySetAnnotation);
        createChangeTrackingProperty(attributes, entitySetAnnotation);
        createDeletablePathProperty(attributes, entitySetAnnotation);
        createUpdatablePathProperty(attributes, entitySetAnnotation);
    }
    private void createCreatableProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        addDefautLogicTrueProperty(attributes, entitySetAnnotation.creatable(), AnnotationPropertyNames.Creatable);
    }
    private void createUpdatableProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        addDefautLogicTrueProperty(attributes, entitySetAnnotation.updatable(), AnnotationPropertyNames.Updatable);
    }

    private void createContentVersionProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        int contentVersion = entitySetAnnotation.contentVersion();
        if (contentVersion != 0) {
            attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.ContentVersion.getPropertyName(), String.valueOf(contentVersion)));
        }
    }

    private void createSubScribableProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        addDefautLogicTrueProperty(attributes, entitySetAnnotation.subscribable(), AnnotationPropertyNames.Subscribable);
    }

    private void createRequireFilterProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        addDefautLogicFalseProperty(attributes, entitySetAnnotation.requireFilter(), AnnotationPropertyNames.RequireFilter);
    }

    private void createSearchableProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        addDefautLogicFalseProperty(attributes, entitySetAnnotation.searchable(), AnnotationPropertyNames.Searchable);
    }

    private void createPageableProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        addDefautLogicTrueProperty(attributes, entitySetAnnotation.pageable(), AnnotationPropertyNames.Pageable);
    }

    private void createAddressableProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        addDefautLogicTrueProperty(attributes, entitySetAnnotation.addressable(), AnnotationPropertyNames.Addressable);
    }

    private void createDeletableProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        addDefautLogicTrueProperty(attributes, entitySetAnnotation.deletable(), AnnotationPropertyNames.Deletable);
    }

    private void createTopableProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        addDefautLogicTrueProperty(attributes, entitySetAnnotation.topable(), AnnotationPropertyNames.Topable);
    }

    private void createCountableProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        addDefautLogicTrueProperty(attributes, entitySetAnnotation.countable(), AnnotationPropertyNames.Countable);
    }

    private void createChangeTrackingProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        addDefautLogicFalseProperty(attributes, entitySetAnnotation.changeTracking(), AnnotationPropertyNames.ChangeTracking);
    }

    private void createDeletablePathProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        String deletablePath = entitySetAnnotation.deletablePath();
        if (!"".equals(deletablePath)) {
            attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.DeletablePath.getPropertyName(), deletablePath));
        }
    }

    private void createUpdatablePathProperty(List<AnnotationAttribute> attributes, ModelEntitySet entitySetAnnotation) {
        String updatablePath = entitySetAnnotation.updatablePath();
        if (!"".equals(updatablePath)) {
            attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.UpdatablePath.getPropertyName(), updatablePath));
        }
    }
}
