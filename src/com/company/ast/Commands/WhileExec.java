package com.company.ast.Commands;

import com.company.ast.Command;
import com.company.ast.CommandList;

public class WhileExec extends Command {
    public Expression condition;
    public CommandList body;

    public WhileExec(Expression condition, CommandList body) {
        this.condition = condition;
        this.body = body;
    }
}
