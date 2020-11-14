package com.company.ast.Commands.Expressions;

import com.company.ast.Commands.Expression;
import com.company.ast.Commands.FunctionInst;
import com.company.ast.ExpressionList;
import com.company.ast.Terminals.Identifier;
import com.company.VisitorsPattern.Visitor;

public class CallExpression extends Expression {
    public Identifier name;
    public ExpressionList args;
    public FunctionInst declaration;

    public CallExpression(Identifier name, ExpressionList args) {
        this.name = name;
        this.args = args;
    }

    public Object visit(Visitor visitor, Object arg) {
        return visitor.visitCallExpression(this, arg);
    }
}
