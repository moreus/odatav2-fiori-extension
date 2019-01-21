package com.sap.odatav2.annotation.extensions.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sap.odatav2.annotation.extensions.annotation.property.Label;
import com.sap.odatav2.annotation.extensions.annotation.value.FilterRestriction;
import com.sap.odatav2.annotation.extensions.annotation.value.DisplayFormatOptions;
import com.sap.odatav2.annotation.extensions.annotation.value.SemanticsOptions;
import com.sap.odatav2.annotation.extensions.annotation.value.ValueListOptions;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ModelProperty {
    /**
     * Values for this property can be chosen by client when creating an instance. “False” if value
     * is always set by the server, e.g. document number from number range.
     * @return creatable attribute value
     */
    boolean creatable() default true;

    /**
     * Values of this property can be changed. Must be “false” if it is “false” at entity set level.
     * If updatability can change per entity or based on the entities' state, do not use this static
     * annotation and use sap:field­control instead.
     * @return updatable attribute value
     */
    boolean updatable() default true;

    /**
     * Can be used in $filter system query option.
     * @return filterable attribute value
     */
    boolean filterable() default true;

    /**
     * Can be used in $orderby system query option.
     * @return sortable attribute value
     */
    boolean sortable() default true;

    /**
     * Can be assign initial null value.
     * @return nullable attribute value
     */
    boolean nullable() default true;

    /**
     * Support unicode value.
     * @return unicode attribute value
     */
    boolean unicode() default true;

    /**
     * Values of this property are typically visible to end users. If visibility can change per
     * entity or based on the entities' state, do not use this static annotation and use
     * sap:field­control instead.
     * @return
     */
    boolean visible() default true;

    /**
     * A short, human­readable text suitable for labels and captions in UIs
     * support i18n with provider key value.
     * @return
     */
    Label label() default @Label(text = "");

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Semantics {
        SemanticsOptions value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Unicode {
        boolean value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface FieldControl {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface DisplayFormat {
        DisplayFormatOptions value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Filterable {
        boolean value();
        FilterRestriction restrict();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Unit {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface ValueList {
        ValueListOptions value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Hierarchy {
        /**
         * A non­key property holding node IDs for a hierarchy structure of values of some other property includes
         * this attribute. The value of this attribute is always the name of another property in the same type.
         * It points to the property for whose values the hierarchy is defined. An OData request with a $filter
         * condition testing equality of a property holding hierarchy node IDs with a specific node ID selects all
         * entities in the sub­hierarchy with the root node identified by the given node ID.
         * @return
         */
        String nodeFor() default "";

        /**
         * A property holding the external key of a hierarchy node includes this attribute. The external key is
         * a human­readable identification of a node. The value of this attribute is always the name of another
         * property in the same type. It points to the related property holding the hierarchy node ID.
         * @return
         */
        String nodeExternalKeyFor() default "";

        /**
         * A property holding level numbers for a hierarchy structure of values of some other property includes
         * this attribute. The value of this attribute is always the name of another property in the same type.
         * It points to the related property holding the hierarchy node ID. A property holding level numbers has
         * an integer data type. The root node of the hierarchy is at level 0.
         * @return
         */
        String levelFor() default "";

        /**
         * A property holding parent node IDs for a hierarchy structure of values of some other property includes
         * this attribute. The value of this attribute is always the name of another property in the same type.
         * It points to the related property holding the hierarchy node ID. For the root node of the hierarchy
         * the parent node ID is null.
         * @return
         */
        String parentNodeFor() default "";

        /**
         * A navigation property for accessing the parent entity of a node. It offers an alternative method for
         * accessing the parent node ID, if the entity type does not have a dedicated property for that.
         * @return
         */
        String parentNavigationFor() default "";

        /**
         * A property holding the drill state of a hierarchy node includes this attribute. The drill state is
         * indicated by one of the following values: collapsed, expanded, leaf. The value of this attribute is
         * always the name of another property in the same type. It points to the related property holding the
         * hierarchy node ID. For an expanded node, its children are included in the result collection. For a
         * collapsed node, the children are included in the entity set, but they are not part of the result
         * collection. Retrieving them requires a relaxed filter expression or a separate request filtering on
         * the parent node ID with the ID of the collapsed node. A leaf does not have any child in the entity set.
         * @return
         */
        String drillStateFor() default "";

        /**
         * A property holding the descendant count for a hierarchy node includes this attribute. The descendant
         * count of a node is the number of its descendants in the hierarchy structure of the result considering
         * only those nodes matching any specified $filter and $search. The value of this attribute is always the
         * name of another property in the same type. It points to the related property holding the hierarchy node ID.
         * A property holding descendant counts has an integer data type.
         * @return
         */
        String nodeDescendantCountFor() default "";

        /**
         * A property holding the preorder rank for a hierarchy node includes this attribute. The preorder rank
         * of a node expresses its position in the sequence of nodes created from preorder traversal of the
         * hierarchy structure after evaluating the $filter expression in the request excluding any conditions
         * on key properties. The value of this attribute is always the name of another property in the same type.
         * It points to the related property holding the hierarchy node ID. A property holding preorder ranks has
         * an integer data type. The first node in preorder traversal has rank 0.
         * @return
         */
        String preOrderRankFor() default "";

        /**
         * A property holding the sibling rank for a hierarchy node includes this attribute. The sibling rank of
         * a node is the index of the node in the sequence of all nodes with the same parent created by preorder
         * traversal of the hierarchy structure after evaluating the $filter expression in the request excluding
         * any conditions on key properties. The value of this attribute is always the name of another property
         * in the same type. It points to the related property holding the hierarchy node ID. A property holding
         * sibling positions has an integer data type. The first sibling is at position 0.
         * @return
         */
        String siblingRankFor() default "";
    }
}
