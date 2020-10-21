package com.company.ast.Expressions;

import com.company.ast.Commands.Expression;

public class ReturnExpression extends Expression {
    public Expression expression;

    public ReturnExpression(Expression expression) {
        this.expression = expression;
    }
}
