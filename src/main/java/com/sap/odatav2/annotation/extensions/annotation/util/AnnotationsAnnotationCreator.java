package com.sap.odatav2.annotation.extensions.annotation.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.sap.odatav2.annotation.extensions.annotation.ModelProperty;
import org.apache.olingo.odata2.api.edm.provider.AnnotationElement;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;

public class AnnotationsAnnotationCreator {
	public AnnotationsAnnotationCreator() {
	}

	public void addAnnotationsAnnotationElement(EdmProvider edmProvider, Annotation[] fieldAnnotations) throws ODataException {
		List<AnnotationElement> annotationElements = new ArrayList<>();
		Optional<Schema> optionalSchema = edmProvider
				.getSchemas()
				.stream()
				.findFirst()
				.map(schema -> {
			return schema;
		});
		
		Arrays.stream(fieldAnnotations).forEach((fieldAnnotation) -> {
			if (fieldAnnotation instanceof ModelProperty.ValueList) {
				List<AnnotationAttribute> childAttributes = new ArrayList<AnnotationAttribute>();
				childAttributes.add(new AnnotationAttribute()
				        .setName("Target")
				        .setText("Target_Service"));
				AnnotationElement annotationElement = new AnnotationElement()
						.setName("Annotations")
						.setNamespace("http://docs.oasis-open.org/odata/ns/edmx");
				annotationElement.setAttributes(childAttributes);
				annotationElements.add(annotationElement);
			}
		});
		optionalSchema.get().getEntityContainers().get(0).setAnnotationElements(annotationElements);
	}
}
