package com.sap.odatav2.annotation.extensions.annotation.util;

import org.apache.olingo.odata2.api.edm.provider.FunctionImport;

import java.lang.annotation.Annotation;
import java.util.ResourceBundle;

public class FunctionImportAnnotationCreator extends AnnotationCreator{
    private final ResourceBundle resourceBundle;

    public FunctionImportAnnotationCreator(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public void addFunctionImportAnnotations(FunctionImport functionImport, Annotation[] annotations) {

    }
}
