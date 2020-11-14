package com.company.ast.Commands.Expressions;

import com.company.ast.Commands.Expression;
import com.company.ast.Terminals.Numbers;
import com.company.VisitorsPattern.Visitor;

public class NumbersExpression extends Expression {
    public Numbers numbers;

    public NumbersExpression(Numbers numbers) {
        this.numbers = numbers;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitNumbersExpression(this, arg);
    }
}
