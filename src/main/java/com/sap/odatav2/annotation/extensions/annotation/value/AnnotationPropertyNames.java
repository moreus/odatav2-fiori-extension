package com.sap.odatav2.annotation.extensions.annotation.value;

public enum AnnotationPropertyNames {
	HierarchyNodeFor("hierarchy-node-for"),
	HierarchyNodeExternalKeyFor("hierarchy-node-external-key-for"),
	HierarchyLevelFor("hierarchy-level-for"),
	HierarchyParentNodeFor("hierarchy-parent-node-for"),
	HierarchyDrillStateFor("hierarchy-drill-state-for"),
	HierarchyParentNavigationFor("hierarchy-parent-navigation-for"),
	HierarchyNodeDescendantCountFor("hierarchy-node-descendant-count-for"),
	HierarchyPreorderRankFor("hierarchy-preorder-rank-for"),
	HierarchySiblingRankFor("hierarchy-sibling-rank-for"),
    ValueList("value-list"),
    FilterRestriction("filter-restriction"),
    Unit("unit"),
    Unicode("unicode"),
    Filterable("filterable"),
    Updatable("updatable"),
    Addressable("addressable"),
    Sortable("sortable"),
    Nullable("nullable"),
    Searchable("searchable"),
    Creatable("createable"),
    Pageable("pageable"),
    Deletable("deletable"),
    Subscribable("subscribable"),
    RequireFilter("requireFilter"),
    ContentVersion("content-version"),
    Visible("visible"),
    DisplayFormat("display-format"),
    FieldControl("field-control"),
    Semantics("semantics"),
	Label("label"),
	Topable("topable"),
    Countable("countable"),
    ChangeTracking("changeTracking"),
    DeletablePath("deletablePath"),
    UpdatablePath("updatablePath");

    private final String propertyName;

    private AnnotationPropertyNames(String name) {
        this.propertyName = "sap:" + name;
    }

    public String getPropertyName() {
        return this.propertyName;
    }
}
