package com.company.ast.Terminals;

import com.company.VisitorsPattern.Visitor;

public class Numbers extends Terminal {
    public Numbers(String spelling) {
        this.spelling = spelling;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitNumbers(this, arg);
    }
}
