package com.company.ast;

import com.company.ast.Commands.Expression;
import com.company.VisitorsPattern.Visitor;

import java.util.Vector;

public class ExpressionList extends AST {
    public Vector<Expression> expressionList = new Vector<>();

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitExpresionList(this, arg);
    }
}
