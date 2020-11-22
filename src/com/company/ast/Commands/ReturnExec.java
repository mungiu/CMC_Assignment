package com.company.ast.Commands;

import com.company.ast.Command;
import com.company.VisitorsPattern.Visitor;

public class ReturnExec extends Command {
    public Expression expression;

    public ReturnExec(Expression expression) {
        this.expression = expression;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitReturnExec(this, arg);
    }
}
