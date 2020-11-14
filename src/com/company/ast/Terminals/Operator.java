package com.company.ast.Terminals;

import com.company.VisitorsPattern.Visitor;

public class Operator extends Terminal {
    public Operator(String spelling) {
        this.spelling = spelling;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitOperator(this, arg);
    }
}
