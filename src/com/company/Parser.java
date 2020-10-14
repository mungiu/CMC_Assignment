package com.company;

import static com.company.TokenKind.*;

public class Parser {
    private Scanner scan;
    private Token currentTerminal;

    public Parser(Scanner scan) {
        this.scan = scan;

        currentTerminal = scan.scan();
    }

    public void parseProgram() {
        parseCommandList();

        if (currentTerminal.kind != EOT)
            System.out.println("Tokens found after end of program");
    }

    private void parseCommandList() {
        parseCommand();
        while (currentTerminal.kind == SEMICOLON) {
            accept(SEMICOLON);
            parseCommand();
        }
    }

    private void parseCommand() {
        // while source document is not yet finished
        while (currentTerminal.kind != EOT) {
            switch (currentTerminal.kind) {
                case INT:
                    accept(INT);
                    accept(IDENTIFIER);
                    accept(LEFT_SQUARE_BRACKET);
                    parseExpressionComponent();
                    accept(RIGHT_SQUARE_BRACKET);
                    break;
                case CHAR:
                    accept(CHAR);
                    accept(IDENTIFIER);
                    accept(LEFT_SQUARE_BRACKET);
                    parseCharacter();
                    accept(RIGHT_SQUARE_BRACKET);
                    break;
                case ARRAY:
                    accept(ARRAY);
                    accept(IDENTIFIER);
                    accept(LEFT_SQUARE_BRACKET);
                    parseExpressionList();
                    accept(RIGHT_SQUARE_BRACKET);
                    break;
                case FUNC:
                    accept(FUNC);
                    parseExpressionComponent();
                    accept(LEFT_SQUARE_BRACKET);
                    parseCommandList();
                    accept(RIGHT_SQUARE_BRACKET);
                    break;
                case RETURN:
                    accept(RETURN);
                    parseExpression();
                    break;
                case WHILE:
                    accept(WHILE);
                    parseExpression();
                    accept(LEFT_SQUARE_BRACKET);
                    parseCommandList();
                    accept(RIGHT_SQUARE_BRACKET);
                    break;
                case IF:
                    accept(IF);
                    parseExpression();
                    accept(LEFT_SQUARE_BRACKET);
                    parseCommandList();
                    accept(RIGHT_SQUARE_BRACKET);
                default:
                    parseExpressionComponent();
            }

            // the ; at the end of each command
            accept(SEMICOLON);
        }
    }

    private void parseExpressionList() {
        parseExpression();
        while (currentTerminal.kind == COMMA) {
            accept(COMMA);
            parseExpression();
        }
    }

    private void parseExpression() {
        parseExpressionComponent();

        while (currentTerminal.kind == OPERATOR) {
            accept(OPERATOR);
            parseExpressionComponent();
        }
    }

    private void parseExpressionComponent() {
        switch (currentTerminal.kind) {
            case NUMBERS:
                accept(NUMBERS);
                break;
            case OPERATOR:
                accept(OPERATOR);
                parseExpressionComponent();
                break;
            case IDENTIFIER:
                accept(IDENTIFIER);
                if (currentTerminal.kind == LEFT_PARANTHESIS) {
                    accept(LEFT_PARANTHESIS);
                    if (currentTerminal.kind == NUMBERS
                            || currentTerminal.kind == OPERATOR
                            || currentTerminal.kind == IDENTIFIER
                            || currentTerminal.kind == LEFT_PARANTHESIS
                            || currentTerminal.kind == APOSTROPHE) {
                        parseExpressionList();
                    }
                    accept(RIGHT_PARANTHESIS);
                }

                break;
            case APOSTROPHE:
                parseCharacter();
            default:
                System.out.println("Error in ExpressionComponent");
                break;
        }
    }

    private void parseCharacter() {
        accept(APOSTROPHE);
        accept(IDENTIFIER);
        accept(APOSTROPHE);
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
}
