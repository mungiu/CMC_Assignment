package com.company.ast.Commands.Expressions;

import com.company.ast.Commands.Expression;
import com.company.ast.Terminals.Operator;
import com.company.VisitorsPattern.Visitor;

public class UnaryExpression extends Expression {
    public Operator operator;
    public Expression operand;


    public UnaryExpression(Operator operator, Expression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitUnaryExpression(this, arg);
    }
}
