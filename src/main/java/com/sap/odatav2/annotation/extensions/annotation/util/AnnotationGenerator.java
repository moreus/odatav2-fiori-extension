package com.sap.odatav2.annotation.extensions.annotation.util;

import com.sap.cloud.commons.lambda.ThrowingFunction;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.FunctionImport;
import org.apache.olingo.odata2.api.exception.ODataException;

public class AnnotationGenerator {
    private EntitySetAnnotationCreator entitySetAnnotationCreator;
    private EntityPropertyAnnotationCreator entityPropertyAnnotationCreator;
    private EntityTypeAnnotationCreator entityTypeAnnotationCreator;
    private FunctionImportAnnotationCreator functionImportAnnotationCreator;
    private AnnotationsAnnotationCreator annotationsAnnotationCreator;

    public AnnotationGenerator(EntitySetAnnotationCreator entitySetAnnotationCreator,
                               EntityTypeAnnotationCreator entityTypeAnnotationCreator,
                               EntityPropertyAnnotationCreator entityPropertyAnnotationCreator) {
        this.entitySetAnnotationCreator = entitySetAnnotationCreator;
        this.entityTypeAnnotationCreator = entityTypeAnnotationCreator;
        this.entityPropertyAnnotationCreator = entityPropertyAnnotationCreator;
    }

    public AnnotationGenerator(FunctionImportAnnotationCreator functionImportAnnotationCreator) {
        this.functionImportAnnotationCreator = functionImportAnnotationCreator;
    }

    public AnnotationGenerator(AnnotationsAnnotationCreator annotationsAnnotationCreator) {
        this.annotationsAnnotationCreator = annotationsAnnotationCreator;
    }

    public void generateFunctionImportAnnotations(EdmProvider edmProvider, List<FunctionImport> functionImports,
                                                  Collection<Class<?>> annotatedClasses){

    }

    public void generateAnnotations(EdmProvider edmProvider, List<EntitySet> entitySets, List<EntityType> entityTypes,
                                    Collection<Class<?>> annotatedClasses) {

        annotatedClasses.stream().filter(Objects::nonNull).forEach((aAnnotatedClass) -> {
            entitySets.stream().filter(
                    (entitySet) -> this.entitySetMapsToAnnotatedClass(edmProvider, aAnnotatedClass, entitySet)
                ).findFirst()
                .ifPresent((entitySet) -> {
                try {
                    this.addAnnotationToField(edmProvider, aAnnotatedClass, entitySet);
                } catch (ODataException exception) {
                    ThrowingFunction.throwManagedException(new ODataException(exception));
                }
            });
            entityTypes.stream().filter(
                    (entityType) -> this.entityTypeMapsToAnnotatedClass(edmProvider, aAnnotatedClass, entityType)
                ).findFirst().ifPresent((entityType) -> {
                try {
                    this.addAnnotationToField(edmProvider, aAnnotatedClass, entityType);
                } catch (ODataException exception) {
                    ThrowingFunction.throwManagedException(new ODataException(exception));
                }
            });
        });
    }

    public void addAnnotationToField(EdmProvider edmProvider, Class<?> annotatedClass, EntitySet entitySet)
        throws ODataException {
        EntityType et = Optional.ofNullable(edmProvider.getEntityType(entitySet.getEntityType()))
            .orElseThrow(AnnotationUtils::throwNullPointerException);
        Optional.ofNullable(annotatedClass.getAnnotations()).ifPresent((annotations) -> {
            this.entitySetAnnotationCreator.addEntitySetAnnotations(entitySet, annotations);
        });
        Optional.ofNullable(annotatedClass.getDeclaredFields()).ifPresent((fields) -> {
            Arrays.stream(fields).forEach((field) -> {
                Optional.ofNullable(field.getAnnotations()).ifPresent((annotations) -> {
                    this.entityPropertyAnnotationCreator.addPropertyAnnotations(et, field, annotations);
                });
            });
        });
    }

    public void addAnnotationToField(EdmProvider edmProvider, Class<?> annotatedClass, FunctionImport functionImport)
        throws ODataException {
    }

    public void addAnnotationToField(EdmProvider edmProvider, Class<?> annotatedClass, EntityType entityType)
        throws ODataException {
        Optional.ofNullable(annotatedClass.getAnnotations()).ifPresent((annotations) -> {
            this.entityTypeAnnotationCreator.addEntityTypeAnnotations(entityType, annotations);
        });
    }
    public boolean entityTypeMapsToAnnotatedClass(EdmProvider edmProvider, Class<?> annotatedClass,
                                                  FunctionImport functionImport) {
        return true;
    }

    public boolean entityTypeMapsToAnnotatedClass(EdmProvider edmProvider, Class<?> annotatedClass,
                                                  EntityType entityType) {
        boolean mapsEntitySetToAnnotatedClass = false;
        String annotationClassName = annotatedClass.getSimpleName();
        String entityTypeName = entityType.getName();
        Predicate<String> predicateAnnotationClass = this.isAnnotatedClassMapEntitySet(entityTypeName);
        if (predicateAnnotationClass.test(annotationClassName)) {
            mapsEntitySetToAnnotatedClass = true;
        }
        return mapsEntitySetToAnnotatedClass;
    }

    public boolean entitySetMapsToAnnotatedClass(EdmProvider edmProvider, Class<?> annotatedClass,
                                                 EntitySet entitySet) {
        boolean mapsEntitySetToAnnotatedClass = false;

        try {
            EntityType entityType = Optional.ofNullable(edmProvider.getEntityType(entitySet.getEntityType()))
                .orElseThrow(AnnotationUtils::throwNullPointerException);
            String annotationClassName = annotatedClass.getSimpleName();
            String entityTypeName = entityType.getName();
            Predicate<String> predicateAnnotationClass = this.isAnnotatedClassMapEntitySet(entityTypeName);
            if (predicateAnnotationClass.test(annotationClassName)) {
                mapsEntitySetToAnnotatedClass = true;
            }
        } catch (ODataException var9) {
            ThrowingFunction.throwManagedException(var9);
        }

        return mapsEntitySetToAnnotatedClass;
    }

    public Predicate<String> isAnnotatedClassMapEntitySet(String entityTypeName) {
        return (annotationClassName) -> {
            return annotationClassName.equals(entityTypeName);
        };
    }
}
