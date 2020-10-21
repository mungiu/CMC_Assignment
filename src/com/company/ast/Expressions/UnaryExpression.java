package com.company.ast.Expressions;

import com.company.ast.Commands.Expression;
import com.company.ast.Terminals.Operator;

public class UnaryExpression extends Expression{
    public Operator operator;
    public Expression operand;


    public UnaryExpression( Operator operator, Expression operand )
    {
        this.operator = operator;
        this.operand = operand;
    }
}
