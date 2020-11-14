package com.company.ast;

import com.company.VisitorsPattern.Visitor;

public abstract class AST {
    public abstract Object visit(Visitor visitor, Object arg);
}
