package com.company.ast.Commands;

import com.company.ast.Command;
import com.company.ast.Terminals.Identifier;

public class IntInst extends Command {
    public Identifier name;
    public Expression value;

    public IntInst(Identifier name, Expression value) {
        this.name = name;
        this.value = value;
    }
}
