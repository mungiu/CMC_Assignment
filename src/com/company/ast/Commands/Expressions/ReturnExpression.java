package com.company.ast.Commands.Expressions;

import com.company.ast.Commands.Expression;
import com.company.VisitorsPattern.Visitor;

public class ReturnExpression extends Expression {
    public Expression expression;

    public ReturnExpression(Expression expression) {
        this.expression = expression;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitReturnExpression(this, arg);
    }
}
