package com.company.ast.Commands;

import com.company.ast.Command;
import com.company.ast.ExpressionList;
import com.company.ast.Terminals.Identifier;
import com.company.VisitorsPattern.Visitor;

public class ArrayInst extends Command {
    public Identifier name;
    public ExpressionList value;

    public ArrayInst(Identifier name, ExpressionList value) {
        this.name = name;
        this.value = value;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitArrayInst(this, arg);
    }
}
