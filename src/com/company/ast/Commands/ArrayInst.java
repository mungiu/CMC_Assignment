package com.company.ast.Commands;

import com.company.ast.Command;
import com.company.ast.ExpressionList;
import com.company.ast.Terminals.Identifier;

public class ArrayInst extends Command {
    public Identifier name;
    public ExpressionList value;

    public ArrayInst(Identifier name, ExpressionList value) {
        this.name = name;
        this.value = value;
    }
}
