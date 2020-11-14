package com.company.ast.Commands.Expressions;

import com.company.ast.Commands.Expression;
import com.company.ast.Terminals.Identifier;
import com.company.VisitorsPattern.Visitor;

public class IdentifierExpression extends Expression {
    public Identifier identifier;

    public IdentifierExpression(Identifier identifier) {
        this.identifier = identifier;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitIdentifierExpression(this, arg);
    }
}
