package com.company.ast.Expressions;

import com.company.ast.AST;
import com.company.ast.Commands.Expression;
import com.company.ast.Terminals.Numbers;

public class NumbersExpression extends Expression {
    public Numbers numbers;

    public NumbersExpression( Numbers numbers )
    {
        this.numbers = numbers;
    }
}
