package com.sap.odatav2.annotation.extensions.annotation.validation.base;

import java.util.List;
import java.util.Map;
import org.apache.olingo.odata2.api.commons.ODataHttpMethod;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.uri.PathSegment;
import org.apache.olingo.odata2.api.uri.expression.FilterExpression;
import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;

public class ValidatorParameters {
    private ODataHttpMethod odataHttpMethod;
    private EdmEntitySet edmEntitySet;
    private FilterExpression filterExpression;
    private OrderByExpression orderByExpression;
    private List<PathSegment> pathSegmentList;
    private Map<String, Object> requestParams;
    private Map<String, Object> entityValues;

    public ValidatorParameters() {
    }

    public ODataHttpMethod getODataHttpMethod() {
        return this.odataHttpMethod;
    }

    public void setOdataHttpMethod(ODataHttpMethod odataHttpMethod) {
        this.odataHttpMethod = odataHttpMethod;
    }

    public EdmEntitySet getEdmEntitySet() {
        return this.edmEntitySet;
    }

    public void setEdmEntitySet(EdmEntitySet edmEntitySet) {
        this.edmEntitySet = edmEntitySet;
    }

    public FilterExpression getFilterExpression() {
        return this.filterExpression;
    }

    public void setFilterExpression(FilterExpression filterExpression) {
        this.filterExpression = filterExpression;
    }

    public OrderByExpression getOrderByExpression() {
        return this.orderByExpression;
    }

    public void setOrderByExpression(OrderByExpression orderByExpression) {
        this.orderByExpression = orderByExpression;
    }

    public List<PathSegment> getPathSegmentList() {
        return this.pathSegmentList;
    }

    public void setPathSegmentList(List<PathSegment> pathSegmentList) {
        this.pathSegmentList = pathSegmentList;
    }

    public Map<String, Object> getRequestParams() {
        return this.requestParams;
    }

    public void setRequestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
    }

    public Map<String, Object> getEntityValues() {
        return this.entityValues;
    }

    public void setEntityValues(Map<String, Object> entityValues) {
        this.entityValues = entityValues;
    }
}

