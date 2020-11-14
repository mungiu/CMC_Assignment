package com.company.ast;

import com.company.VisitorsPattern.Visitor;

import java.util.Vector;

public class CommandList extends AST {
    public Vector<Command> commandList = new Vector<>();

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitCommandList(this, arg);
    }
}
