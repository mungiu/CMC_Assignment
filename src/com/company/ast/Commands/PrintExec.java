package com.company.ast.Commands;

import com.company.ast.Command;
import com.company.VisitorsPattern.Visitor;

public class PrintExec extends Command {
    public Expression exp;

    public PrintExec(Expression exp) {
        this.exp = exp;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitPrintExec(this, arg);
    }
}
