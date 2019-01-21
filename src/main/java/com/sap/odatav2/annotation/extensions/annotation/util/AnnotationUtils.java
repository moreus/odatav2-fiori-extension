package com.sap.odatav2.annotation.extensions.annotation.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.olingo.odata2.api.edm.provider.*;
import org.apache.olingo.odata2.api.exception.ODataException;

/**
 * Utility class contains utility methods that can be (re)used for the
 * generation of entity and property annotations.
 */
public class AnnotationUtils {
	private static final String SAP_DATA_NAMESPACE = "http://www.sap.com/Protocols/SAPData";
	private static final String SAP_DATA_PREFIX = "sap";
	
	/**
	 * Private Constructor (dont call for utility classes).
	 */
	private AnnotationUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Create annotation attributes in SAPData Namespace with SAPData Prefix.
	 *
	 * @param key
	 *            The annotation name
	 * @param value
	 *            The annotation value
	 * @return The annotation Attribute {@link AnnotationAttribute}
	 */
	public static AnnotationAttribute createSapDataAnnotationAttribute(final String key, final String value) {
		return new AnnotationAttribute()
				.setNamespace(null)
				.setPrefix(null)
				.setName(key)
				.setText(value);
	}

	/**
	 * Retrieve property from field based on the name.
	 * 
	 * @param field
	 *            The field {@link Field}
	 * @param et
	 *            The entity type
	 * @return The correpsonding property {@link Property}
	 */
	public static Property getFieldProperty(final Field field, EntityType et) {
		return Optional.ofNullable(et.getProperties())
				.map(properties -> properties.stream()
				.filter(property -> field.getName().equalsIgnoreCase(property.getName())).findFirst().get()).orElse(null);
	}

	/**
	 * Retrieves all entity sets from the specified schema.
	 * 
	 * @param edmProvider
	 *            The edm provider {@link EdmProvider}
	 * @return The list of entity sets
	 * @throws ODataException
	 *             throws odata exception
	 */
	public static List<EntitySet> getEntitySets(EdmProvider edmProvider) throws ODataException {
		Optional<Schema> optionalSchema = edmProvider
				.getSchemas()
				.stream()
				.findFirst()
				.map(schema -> {
			addSapAnnotationSchema(schema);
			return schema;
		});

		Optional<EntityContainer> entityContainer = optionalSchema
				.map(schema -> Optional.ofNullable(schema.getEntityContainers()).map(entityContainers -> schema
						.getEntityContainers().stream().findFirst().orElseGet(EntityContainer::new))
						.orElseGet(EntityContainer::new));

		return entityContainer.filter(Objects::nonNull).map(EntityContainer::getEntitySets)
				.orElseGet(Collections::emptyList);
	}

	public static List<FunctionImport> getFunctionImports(EdmProvider edmProvider) throws ODataException {
		Optional<Schema> optionalSchema = edmProvider
			.getSchemas()
			.stream()
			.findFirst();

		Optional<EntityContainer> entityContainer = optionalSchema
			.map(schema -> Optional.ofNullable(schema.getEntityContainers()).map(entityContainers -> schema
				.getEntityContainers().stream().findFirst().orElseGet(EntityContainer::new))
				.orElseGet(EntityContainer::new));

		return entityContainer.filter(Objects::nonNull).map(EntityContainer::getFunctionImports)
			.orElseGet(Collections::emptyList);
	}

	public static List<EntityType> getEntityTypes(EdmProvider edmProvider) throws ODataException {
		Optional<Schema> optionalSchema = edmProvider
			.getSchemas()
			.stream()
			.findFirst();

		return optionalSchema.map(
			schema -> Optional.ofNullable(schema.getEntityTypes()).orElse(new ArrayList<>())
		).get();
	}

	/**
	 * Adds SAP namespace and version to schema.
	 * 
	 * @param schema
	 *            The schema {@link Schema}
	 */
	private static void addSapAnnotationSchema(Schema schema) {
		List<AnnotationAttribute> schemaAnnotationAttributes = new ArrayList<>();
		schemaAnnotationAttributes.add(new AnnotationAttribute()
				.setName("schema-version")
				.setText("1")
				.setPrefix(SAP_DATA_PREFIX)
				.setNamespace(SAP_DATA_NAMESPACE));
		
		Optional.ofNullable(schema.getAnnotationAttributes()).map(annotationAttributes -> {
			annotationAttributes.addAll(schemaAnnotationAttributes);
			return annotationAttributes;
		}).orElseGet(() -> {
			schema.setAnnotationAttributes(schemaAnnotationAttributes);
			return schema.getAnnotationAttributes();
		});
	}

	/**
	 * Get a new instance of {@link NullPointerException} exception.
	 * 
	 * @return NullPointerException The exception
	 */
	public static NullPointerException throwNullPointerException() {
		return new NullPointerException();
	}

	/**
	 * Attach annotation attributes to specific property.
	 * 
	 * @param et
	 *            EntityType The entity type
	 * @param field
	 *            Field The field
	 * @param annotationAttributes
	 *            The list of annotation attributes
	 */
	public static void createAnnotationProperty(EntityType et, Field field,
			List<AnnotationAttribute> annotationAttributes) {
		if (!annotationAttributes.isEmpty()) {
			Property property = getFieldProperty(field, et);
			if (property != null) {
				List<AnnotationAttribute> propertyAnnotationAttributes = property.getAnnotationAttributes();
				if (propertyAnnotationAttributes == null) {
					property.setAnnotationAttributes(annotationAttributes);
				} else {
					property.getAnnotationAttributes().addAll(annotationAttributes);
				}
			}
		}
	}
}