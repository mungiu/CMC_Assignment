package com.company.ast.Commands;

import com.company.ast.CommandList;
import com.company.ast.Command;
import com.company.VisitorsPattern.Visitor;

public class IfExec extends Command {
    public Expression condition;
    public CommandList body;

    public IfExec(Expression condition, CommandList body) {
        this.condition = condition;
        this.body = body;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitIfExec(this, arg);
    }
}
