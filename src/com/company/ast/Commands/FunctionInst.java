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
    public Command returnCommand;

    public Address adr;

    public FunctionInst(Identifier name, ExpressionList params, CommandList body, Command returnCommand) {
        this.name = name;
        this.params = params;
        this.body = body;
        this.returnCommand = returnCommand;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitFunctionInst(this, arg);
    }
}
