package com.company.ast.Commands;

import com.company.VisitorsPattern.Address;
import com.company.ast.Command;
import com.company.ast.CommandList;
import com.company.ast.ExpressionList;
import com.company.ast.Terminals.Identifier;
import com.company.VisitorsPattern.Visitor;

public class FunctionInst extends Command {
    public Identifier name;
    public ExpressionList params;
    public CommandList body;
    public Address adr;

    public FunctionInst(Identifier name, ExpressionList params, CommandList body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitFunctionInst(this, arg);
    }
}
