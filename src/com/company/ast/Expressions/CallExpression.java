package com.company.ast.Expressions;

import com.company.ast.Commands.Expression;
import com.company.ast.ExpressionList;
import com.company.ast.Terminals.Identifier;

public class CallExpression extends Expression {
    public Identifier name;
    public ExpressionList args;


    public CallExpression( Identifier name, ExpressionList args )
    {
        this.name = name;
        this.args = args;
    }
}
