package com.company;

import com.company.ast.Command;
import com.company.ast.CommandList;
import com.company.ast.Commands.*;
import com.company.ast.ExpressionList;
import com.company.ast.Expressions.*;
import com.company.ast.Program;
import com.company.ast.Terminals.Character;
import com.company.ast.Terminals.Identifier;
import com.company.ast.Terminals.Numbers;
import com.company.ast.Terminals.Operator;

import static com.company.TokenKind.*;

public class Parser {
    private Scanner scan;
    private Token currentTerminal;

    public Parser(Scanner scan) {
        this.scan = scan;

        currentTerminal = scan.scan();
    }

    public Program parseProgram() {
        CommandList temp = parseCommandList();
        if (currentTerminal.kind != EOT)
            System.out.println("Tokens found after end of program");

        return new Program(temp);
    }

    private CommandList parseCommandList() {
        CommandList temp = new CommandList();

        temp.commandList.add(parseCommand());
        while (currentTerminal.kind == SEMICOLON) {
            accept(SEMICOLON);
            temp.commandList.add(parseCommand());
        }
        return temp;
    }

    private Command parseCommand() {
        switch (currentTerminal.kind) {
            case INT:
                accept(INT);
                Identifier intName = parseIdentifier();
                accept(LEFT_SQUARE_BRACKET);
                Expression intValue = parseExpressionComponent();
                accept(RIGHT_SQUARE_BRACKET);
                return new IntInst(intName, intValue);
            case CHAR:
                accept(CHAR);
                Identifier charName = parseIdentifier();
                accept(LEFT_SQUARE_BRACKET);
                Character charValue = parseCharacter();
                accept(RIGHT_SQUARE_BRACKET);
                return new CharInst(charName, charValue);
            case ARRAY:
                accept(ARRAY);
                Identifier arrName = parseIdentifier();
                accept(LEFT_SQUARE_BRACKET);
                ExpressionList arrValue = parseExpressionList();
                accept(RIGHT_SQUARE_BRACKET);
                return new ArrayInst(arrName, arrValue);
            case FUNC:
                accept(FUNC);
                Identifier funcName = parseIdentifier();
                accept(LEFT_PARANTHESIS);
                ExpressionList args;
                if (currentTerminal.kind == NUMBERS
                        || currentTerminal.kind == OPERATOR
                        || currentTerminal.kind == IDENTIFIER
                        || currentTerminal.kind == LEFT_PARANTHESIS
                        || currentTerminal.kind == APOSTROPHE) {
                    args = parseExpressionList();
                } else
                    args = new ExpressionList();
                accept(RIGHT_PARANTHESIS);
                accept(LEFTBRACKET);
                CommandList funcBody = parseCommandList();
                // BY DEFAULT THE RIGHT BRAKET or the SQUARE WILL BE CAUGHT at the end of the Command
                return new FunctionInst(funcName, args, funcBody);
            case RETURN:
                accept(RETURN);
                Expression returnExpression = parseExpressionComponent();
                return new ReturnExpression(returnExpression);
            case WHILE:
                accept(WHILE);
                Expression whileCondition = parseExpressionComponent();
                accept(LEFTBRACKET);
                CommandList whileBody = parseCommandList();
                accept(RIGHTBRACKET);
                return new WhileExec(whileCondition, whileBody);
            case IF:
                accept(IF);
                Expression ifCondition = parseExpressionComponent();
                accept(LEFTBRACKET);
                CommandList ifBody = parseCommandList();
                accept(RIGHTBRACKET);
                return new IfExec(ifCondition, ifBody);
            case IDENTIFIER:
                Expression simpleExpression = parseExpressionComponent();
                return simpleExpression;
            case RIGHTBRACKET:
                accept(RIGHTBRACKET);
                return null;
            case RIGHT_SQUARE_BRACKET:
                accept(RIGHT_SQUARE_BRACKET);
                return null;
            case EOT:
                return null;
            default:
                System.out.println("Error in command");
                return null;
        }
    }

    private ExpressionList parseExpressionList() {
        ExpressionList temp = new ExpressionList();
        temp.expressionList.add(parseExpressionComponent());

        while (currentTerminal.kind == COMMA) {
            accept(COMMA);
            temp.expressionList.add(parseExpressionComponent());
        }

        return temp;
    }

//    private void parseExpression() {
//        parseExpressionComponent();
//
//        if (currentTerminal.kind == OPERATOR) {
//            accept(OPERATOR);
//            parseExpressionComponent();
//        }
//    }

    private Expression parseExpressionComponent() {
        switch (currentTerminal.kind) {
            case NUMBERS:
                Numbers temp = parseNumbersExpression();
                return new NumbersExpression(temp);
            case OPERATOR:
                Operator operator = parseOperator();
                Expression operand = parseExpressionComponent();
                return new UnaryExpression(operator, operand);
            case IDENTIFIER:
                Identifier name = parseIdentifier();
                if (currentTerminal.kind == LEFT_PARANTHESIS) {
                    accept(LEFT_PARANTHESIS);
                    ExpressionList args;
                    if (currentTerminal.kind == NUMBERS
                            || currentTerminal.kind == OPERATOR
                            || currentTerminal.kind == IDENTIFIER
                            || currentTerminal.kind == LEFT_PARANTHESIS
                            || currentTerminal.kind == APOSTROPHE) {
                        args = parseExpressionList();
                    } else
                        args = new ExpressionList();

                    accept(RIGHT_PARANTHESIS);
                    return new CallExpression(name, args);
                } else
                    return new IdentifierExpression(name);

            default:
                System.out.println("Error in ExpressionComponent");
                return null;
        }
    }

    private Identifier parseIdentifier() {
        if (currentTerminal.kind == IDENTIFIER) {
            Identifier temp = new Identifier(currentTerminal.spelling);
            currentTerminal = scan.scan();

            return temp;
        } else {
            System.out.println("Identifier expected");

            return new Identifier("???");
        }
    }

    private Operator parseOperator() {
        if (currentTerminal.kind == OPERATOR) {
            Operator temp = new Operator(currentTerminal.spelling);
            currentTerminal = scan.scan();

            return temp;
        } else {
            System.out.println("Operator expected");

            return new Operator("???");
        }
    }

    private Numbers parseNumbersExpression() {
        if (currentTerminal.kind == NUMBERS) {
            Numbers temp = new Numbers(currentTerminal.spelling);
            currentTerminal = scan.scan();

            return temp;
        } else {
            System.out.println("Integer literal expected");

            return new Numbers("???");
        }
    }

    private Character parseCharacter() {
        if (currentTerminal.kind == APOSTROPHE) {
            accept(APOSTROPHE);
            if (currentTerminal.spelling.length() != 1) {
                System.out.println("Char expected");
                return null;
            }

            Character temp = new Character(currentTerminal.spelling.charAt(0));
            acceptMultiple(IDENTIFIER, NUMBERS, OPERATOR);
            accept(APOSTROPHE);

            return temp;
        } else {
            accept(APOSTROPHE);
            System.out.println("Identifier expected");
            accept(APOSTROPHE);
            return new Character('?');
        }
    }

    /**
     * Checks if the current token is as expected and writes the next scanned token into currentTerminal
     *
     * @param expected the currentToken
     */
    private void accept(TokenKind expected) {
        if (currentTerminal.kind == expected)
            currentTerminal = scan.scan();
        else
            System.out.println("Expected token of kind " + expected);
    }

    private void acceptMultiple(TokenKind... expected) {
        for (TokenKind tk : expected) {
            if (currentTerminal.kind == tk) {
                currentTerminal = scan.scan();
                return;
            }
        }

        System.out.println("Expected token of kind " + expected);
    }
}
