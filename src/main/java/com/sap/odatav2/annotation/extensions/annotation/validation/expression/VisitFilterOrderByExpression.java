package com.sap.odatav2.annotation.extensions.annotation.validation.expression;

import java.util.ArrayList;
import java.util.List;
import org.apache.olingo.odata2.api.edm.EdmLiteral;
import org.apache.olingo.odata2.api.edm.EdmTyped;
import org.apache.olingo.odata2.api.uri.expression.BinaryExpression;
import org.apache.olingo.odata2.api.uri.expression.BinaryOperator;
import org.apache.olingo.odata2.api.uri.expression.ExpressionVisitor;
import org.apache.olingo.odata2.api.uri.expression.FilterExpression;
import org.apache.olingo.odata2.api.uri.expression.LiteralExpression;
import org.apache.olingo.odata2.api.uri.expression.MemberExpression;
import org.apache.olingo.odata2.api.uri.expression.MethodExpression;
import org.apache.olingo.odata2.api.uri.expression.MethodOperator;
import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;
import org.apache.olingo.odata2.api.uri.expression.OrderExpression;
import org.apache.olingo.odata2.api.uri.expression.PropertyExpression;
import org.apache.olingo.odata2.api.uri.expression.SortOrder;
import org.apache.olingo.odata2.api.uri.expression.UnaryExpression;
import org.apache.olingo.odata2.api.uri.expression.UnaryOperator;

public class VisitFilterOrderByExpression implements ExpressionVisitor {
    private List<String> attributeNames = new ArrayList<>();

    public VisitFilterOrderByExpression() {
    }

    public Object visitFilterExpression(FilterExpression filterExpression, String s, Object o) {
        return null;
    }

    public Object visitBinary(BinaryExpression binaryExpression, BinaryOperator binaryOperator, Object leftSide, Object rightSide) {
        return null;
    }

    public List<String> getAttributeNames() {
        return this.attributeNames;
    }

    public Object visitOrderByExpression(OrderByExpression orderByExpression, String s, List<Object> list) {
        return null;
    }

    public Object visitOrder(OrderExpression orderExpression, Object o, SortOrder sortOrder) {
        return null;
    }

    public Object visitLiteral(LiteralExpression literalExpression, EdmLiteral edmLiteral) {
        return null;
    }

    public Object visitMethod(MethodExpression methodExpression, MethodOperator methodOperator, List<Object> list) {
        return null;
    }

    public Object visitMember(MemberExpression memberExpression, Object o, Object o1) {
        return null;
    }

    public Object visitProperty(PropertyExpression propertyExpression, String s, EdmTyped edmTyped) {
        this.attributeNames.add(propertyExpression.getPropertyName());
        return propertyExpression.getPropertyName();
    }

    public Object visitUnary(UnaryExpression unaryExpression, UnaryOperator unaryOperator, Object o) {
        return null;
    }
}
