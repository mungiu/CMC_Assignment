package com.company.ast.Commands;

import com.company.ast.Command;
import com.company.ast.Terminals.Character;
import com.company.ast.Terminals.Identifier;
import com.company.VisitorsPattern.Visitor;

public class CharInst extends Command {
    public Identifier name;
    public Character value;

    public CharInst(Identifier name, Character value) {
        this.name = name;
        this.value = value;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitCharInst(this, arg);
    }
}
