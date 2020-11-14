package com.company.ast;

import com.company.VisitorsPattern.Visitor;

public class Program extends AST {
    public CommandList commandList;

    public Program(CommandList commandList) {
        this.commandList = commandList;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitProgram(this, arg);
    }
}
