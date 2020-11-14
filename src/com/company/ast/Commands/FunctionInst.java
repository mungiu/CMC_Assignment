package com.company.ast.Commands;

import com.company.ast.Command;
import com.company.ast.CommandList;
import com.company.ast.ExpressionList;
import com.company.ast.Terminals.Identifier;
import com.company.VisitorsPattern.Visitor;

public class FunctionInst extends Command {
    public Identifier name;
    public ExpressionList args;
    public CommandList body;

    public FunctionInst(Identifier name, ExpressionList args, CommandList body) {
        this.name = name;
        this.args = args;
        this.body = body;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitFunctionInst(this, arg);
    }
}
