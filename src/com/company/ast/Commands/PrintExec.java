package com.company.ast.Commands;

import com.company.ast.Command;

public class PrintExec extends Command {
    public Expression exp;

    public PrintExec(Expression exp )
    {
        this.exp = exp;
    }
}
