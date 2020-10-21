package com.company.ast.Commands;

import com.company.ast.Command;
import com.company.ast.CommandList;

public class FunctionInst extends Command {
    public Expression signature;
    public CommandList body;

    public FunctionInst(Expression signature, CommandList body) {
        this.signature = signature;
        this.body = body;
    }
}
