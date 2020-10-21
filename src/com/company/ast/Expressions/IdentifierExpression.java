package com.company.ast.Expressions;

import com.company.ast.Commands.Expression;
import com.company.ast.Terminals.Identifier;

public class IdentifierExpression extends Expression {
    public Identifier identifier;

    public IdentifierExpression( Identifier identifier )
    {
        this.identifier = identifier;
    }
}
