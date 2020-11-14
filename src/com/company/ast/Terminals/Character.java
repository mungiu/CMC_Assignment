package com.company.ast.Terminals;

import com.company.VisitorsPattern.Visitor;

public class Character extends Terminal {
    public Character(char aChar) {
        this.aChar = aChar;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitCharacter(this, arg);
    }
}