package com.sap.odatav2.annotation.extensions.annotation.util;

import com.sap.odatav2.annotation.extensions.annotation.ModelProperty;
import com.sap.odatav2.annotation.extensions.annotation.value.DisplayFormatOptions;
import com.sap.odatav2.annotation.extensions.annotation.value.FilterRestriction;
import com.sap.odatav2.annotation.extensions.annotation.value.SemanticsOptions;
import com.sap.odatav2.annotation.extensions.annotation.value.ValueListOptions;
import com.sap.odatav2.annotation.extensions.annotation.value.AnnotationPropertyNames;
import org.apache.olingo.odata2.api.edm.provider.AnnotationAttribute;
import org.apache.olingo.odata2.api.edm.provider.EntityType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

import static com.sap.odatav2.annotation.extensions.annotation.util.AnnotationUtils.createAnnotationProperty;
import static com.sap.odatav2.annotation.extensions.annotation.util.AnnotationUtils.createSapDataAnnotationAttribute;

public class EntityPropertyAnnotationCreator extends AnnotationCreator {
	private final ResourceBundle resourceBundle;
	public EntityPropertyAnnotationCreator(ResourceBundle bundle) {
		this.resourceBundle = bundle;
	}

	public void addPropertyAnnotations(EntityType et, Field field, Annotation[] annotations) {
		
		List<AnnotationAttribute> attributes = new ArrayList<>();
		Arrays.stream(annotations).forEach((annotation) -> {
			if( annotation instanceof ModelProperty) {
				createSAPBasicPropertyAttributes(attributes, (ModelProperty) annotation);
			}
			if (annotation instanceof ModelProperty.ValueList) {
				ValueListOptions optionx = ((ModelProperty.ValueList) annotation).value();
				attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.ValueList.getPropertyName(), optionx.getValue()));
			}
			if (annotation instanceof ModelProperty.Unit) {
				String value = ((ModelProperty.Unit) annotation).value();
				attributes.add(AnnotationUtils.createSapDataAnnotationAttribute(AnnotationPropertyNames.Unit.getPropertyName(), value));
			}
			if (annotation instanceof ModelProperty.Filterable) {
				//boolean valuex = ((Filterable) annotation).value();
				//attributes.add(createSapDataAnnotationAttribute(Filterable.getPropertyName(), String.valueOf(valuex)));
				FilterRestriction restriction = ((ModelProperty.Filterable) annotation).restrict();
				if (null != restriction){
					attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.FilterRestriction.getPropertyName(), restriction.getValue()));
				}
			}
			if (annotation instanceof ModelProperty.Unicode) {
				createUnicodeProperty(attributes, (ModelProperty.Unicode) annotation);
			}

			if (annotation instanceof ModelProperty.DisplayFormat) {
				createDisplayFormatProperty(attributes, (ModelProperty.DisplayFormat) annotation);
			}
			if (annotation instanceof ModelProperty.FieldControl) {
				createFieldControlProperty(attributes, (ModelProperty.FieldControl) annotation);
			}

			if (annotation instanceof ModelProperty.Semantics) {
				SemanticsOptions option = ((ModelProperty.Semantics) annotation).value();
				attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.Semantics.getPropertyName(), option.getValue()));
			}

			if (annotation instanceof ModelProperty.Hierarchy){
				createHierarchyAttributes(attributes, (ModelProperty.Hierarchy) annotation);
			}

		});
		createAnnotationProperty(et, field, attributes);
	}

	private void createHierarchyAttributes(List<AnnotationAttribute> attributes, ModelProperty.Hierarchy annotation) {
		createHierarchyNodeForProperty(attributes, annotation);
		createHierarchyParentNavigationForProperty(attributes,  annotation);
		createHierarchyDrillStateForProperty(attributes,  annotation);
		createHierarchySiblingRankForProperty(attributes,  annotation);
		createHierarchyNodeDescendantCountForProperty(attributes,  annotation);
		createHierarchyParentNodeForProperty(attributes,  annotation);
		createHierarchyNodeExternalKeyForProperty(attributes,  annotation);
		createHierarchyPreorderRankForProperty(attributes,  annotation);
		createHierarchyLevelForProperty(attributes,  annotation);
	}

	private void createHierarchyNodeForProperty(List<AnnotationAttribute> attributes, ModelProperty.Hierarchy annotation) {
		addHierarchyProperty(attributes, annotation.nodeFor(), AnnotationPropertyNames.HierarchyNodeFor);
	}

	private void createHierarchyParentNavigationForProperty(List<AnnotationAttribute> attributes, ModelProperty.Hierarchy annotation) {
		addHierarchyProperty(attributes, annotation.parentNavigationFor(), AnnotationPropertyNames.HierarchyParentNavigationFor);
	}

	private void createHierarchyDrillStateForProperty(List<AnnotationAttribute> attributes, ModelProperty.Hierarchy annotation) {
		addHierarchyProperty(attributes, annotation.drillStateFor(), AnnotationPropertyNames.HierarchyDrillStateFor);
	}

	private void createHierarchySiblingRankForProperty(List<AnnotationAttribute> attributes, ModelProperty.Hierarchy annotation) {
		addHierarchyProperty(attributes, annotation.siblingRankFor(), AnnotationPropertyNames.HierarchySiblingRankFor);
	}

	private void createHierarchyNodeDescendantCountForProperty(List<AnnotationAttribute> attributes, ModelProperty.Hierarchy annotation) {
		addHierarchyProperty(attributes, annotation.nodeDescendantCountFor(), AnnotationPropertyNames.HierarchyNodeDescendantCountFor);
	}

	private void createHierarchyParentNodeForProperty(List<AnnotationAttribute> attributes, ModelProperty.Hierarchy annotation) {
		addHierarchyProperty(attributes, annotation.parentNodeFor(), AnnotationPropertyNames.HierarchyParentNodeFor);
	}

	private void createHierarchyNodeExternalKeyForProperty(List<AnnotationAttribute> attributes, ModelProperty.Hierarchy annotation) {
		addHierarchyProperty(attributes, annotation.nodeExternalKeyFor(), AnnotationPropertyNames.HierarchyNodeExternalKeyFor);
	}

	private void createHierarchyPreorderRankForProperty(List<AnnotationAttribute> attributes, ModelProperty.Hierarchy annotation) {
		addHierarchyProperty(attributes, annotation.preOrderRankFor(), AnnotationPropertyNames.HierarchyPreorderRankFor);
	}

	private void createHierarchyLevelForProperty(List<AnnotationAttribute> attributes, ModelProperty.Hierarchy annotation) {
		addHierarchyProperty(attributes, annotation.levelFor(), AnnotationPropertyNames.HierarchyLevelFor);
	}

	private void createFieldControlProperty(List<AnnotationAttribute> attributes, ModelProperty.FieldControl annotation) {
		String value = annotation.value();
		attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.FieldControl.getPropertyName(), value));
	}

	private void createDisplayFormatProperty(List<AnnotationAttribute> attributes, ModelProperty.DisplayFormat annotation) {
		DisplayFormatOptions optionxx = annotation.value();
		attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.DisplayFormat.getPropertyName(), optionxx.getValue()));
	}
	private void createUnicodeProperty(List<AnnotationAttribute> attributes, ModelProperty annotation) {
		boolean valuex = annotation.unicode();
		attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.Unicode.getPropertyName(), String.valueOf(valuex)));
	}

	private void createUnicodeProperty(List<AnnotationAttribute> attributes, ModelProperty.Unicode annotation) {
		boolean valuex = annotation.value();
		attributes.add(createSapDataAnnotationAttribute(AnnotationPropertyNames.Unicode.getPropertyName(), String.valueOf(valuex)));
	}

	private void createSAPBasicPropertyAttributes(List<AnnotationAttribute> attributes, ModelProperty property) {
		createCreatableProperty(attributes, property);
		createUpdatableProperty(attributes, property);
		createFilterableProperty(attributes, property);
		createNullableProperty(attributes, property);
		createSortableProperty(attributes, property);
		createUnicodeProperty(attributes, property);
		createLabelProperty(attributes, property);
		createVisibleProperty(attributes, property);
	}
	private void createVisibleProperty(List<AnnotationAttribute> attributes, ModelProperty property) {
		addDefautLogicTrueProperty(attributes, property.visible(), AnnotationPropertyNames.Visible);
	}
	private void createCreatableProperty(List<AnnotationAttribute> attributes, ModelProperty property) {
		addDefautLogicTrueProperty(attributes, property.creatable(), AnnotationPropertyNames.Creatable);
	}

	private void createUpdatableProperty(List<AnnotationAttribute> attributes, ModelProperty property) {
		addDefautLogicTrueProperty(attributes, property.updatable(), AnnotationPropertyNames.Updatable);
	}

	private void createSortableProperty(List<AnnotationAttribute> attributes, ModelProperty property) {
		addDefautLogicTrueProperty(attributes, property.sortable(), AnnotationPropertyNames.Sortable);
	}

	private void createNullableProperty(List<AnnotationAttribute> attributes, ModelProperty property) {
		addDefautLogicTrueProperty(attributes, property.nullable(), AnnotationPropertyNames.Nullable);
	}

	private void createFilterableProperty(List<AnnotationAttribute> attributes, ModelProperty property) {
		addDefautLogicTrueProperty(attributes, property.filterable(), AnnotationPropertyNames.Filterable);
	}

	private void createLabelProperty(List<AnnotationAttribute> attributes, ModelProperty property) {
		addLableProperty(this.resourceBundle, attributes, property.label());
	}
}
