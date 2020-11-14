package com.company.ast.Commands.Expressions;

import com.company.ast.Commands.Expression;
import com.company.ast.Terminals.Operator;
import com.company.VisitorsPattern.Visitor;

public class BinaryExpression extends Expression {
    public Operator operator;
    public Expression operand1;
    public Expression operand2;

    public BinaryExpression(Operator operator, Expression operand1, Expression operand2 )
    {
        this.operator = operator;
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitBinaryExpression(this, arg);
    }
}
