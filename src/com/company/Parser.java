package com.company;

import static com.company.TokenKind.*;

public class Parser {
    private Scanner scan;


    private Token currentTerminal;


    public Parser(Scanner scan) {
        this.scan = scan;

        currentTerminal = scan.scan();
    }

    // 1st layer
    public void parseProgram() {
        parseCommandList();

        if (currentTerminal.kind != EOT)
            System.out.println("Tokens found after end of program");
    }


    private void parseCommandList() {
        // while source document is not yet finished
        while (currentTerminal.kind != EOT) {
            switch (currentTerminal.kind) {
                case INT:
                    accept(INT);
                    accept(IDENTIFIER);
                    accept(LEFT_SQUARE_BRACKET);
                    accept(NUMBERS);
                    accept(RIGHT_SQUARE_BRACKET);
                    break;
                case CHAR:
                    accept(CHAR);
                    accept(IDENTIFIER);
                    accept(LEFT_SQUARE_BRACKET);
                    accept(NUMBERS);
                    accept(RIGHT_SQUARE_BRACKET);
                    break;
                case ARRAY:
                    accept(ARRAY);
                    accept(IDENTIFIER);
                    accept(LEFT_SQUARE_BRACKET);
                    accept(NUMBERS);
                    accept(RIGHT_SQUARE_BRACKET);
                    break;
                case WHILE:
                    accept(WHILE);
                    parseExpressionList();
                    accept(LEFT_SQUARE_BRACKET);
                    parseCommandList();
                    accept(RIGHT_SQUARE_BRACKET);
                    break;
                case IF:
                    parseExpressionList();
                    accept(LEFT_SQUARE_BRACKET);
                    parseCommandList();
                    accept(RIGHT_SQUARE_BRACKET);
                case IDENTIFIER:            // CASE METHOD
                    accept(IDENTIFIER);
                    accept(LEFT_PARANTHESIS);
                    parseVariableList();
                    accept(RIGHT_PARANTHESIS);
                    switch (currentTerminal.kind) {
                        case SEMICOLON:             // CASE METHOD CALL
                            break;
                        case LEFT_SQUARE_BRACKET:   // CASE METHOD DECLARATION
                            accept(LEFT_SQUARE_BRACKET);
                            parseCommandList();
                            accept(RIGHT_SQUARE_BRACKET);
                            break;
                    }

            }

            // the ; at the end of each command
            accept(SEMICOLON);
        }
    }


    private void parseDeclarations() {
        while (currentTerminal.kind == VAR ||
                currentTerminal.kind == FUNC)
            parseOneDeclaration();
    }


    private void parseOneDeclaration() {
        switch (currentTerminal.kind) {
            case VAR:
                accept(VAR);
                accept(IDENTIFIER);
                accept(SEMICOLON);
                break;

            case FUNC:
                accept(FUNC);
                accept(IDENTIFIER);
                accept(LEFT_PARANTHESIS);

                if (currentTerminal.kind == IDENTIFIER)
                    parseIdList();

                accept(RIGHT_PARANTHESIS);
                parseCommandList();
                accept(RETURN);
                parseExpression();
                accept(SEMICOLON);
                break;

            default:
                System.out.println("var or func expected");
                break;
        }
    }

    private void parseIdList() {
        accept(IDENTIFIER);

        while (currentTerminal.kind == COMMA) {
            accept(COMMA);
            accept(IDENTIFIER);
        }
    }

    private void parseOneStatement() {
        switch (currentTerminal.kind) {
            case IDENTIFIER:
            case NUMBERS:
            case OPERATOR:
            case LEFT_SQUARE_BRACKET:
                parseExpression();
                accept(RIGH_SQUARE_BRACKET);
                break;

            case IF:
                accept(IF);
                parseExpression();
                accept(LEFT_SQUARE_BRACKET);
                parseStatements();

                if (currentTerminal.kind == IF) {
                    accept(IF);
                    parseStatements();
                }

                accept(RIGHT_SQUARE_BRACKET);
//				accept( SEMICOLON );
                break;

            case WHILE:
                accept(WHILE);
                parseExpression();
                parseStatements();
//				accept( SEMICOLON );
                break;

            case SAY:
                accept(SAY);
                parseExpression();
                accept(SEMICOLON);
                break;

            default:
                System.out.println("Error in statement");
                break;
        }
    }

    private void parseStatements() {
        while (currentTerminal.kind == IDENTIFIER ||
                currentTerminal.kind == OPERATOR ||
                currentTerminal.kind == NUMBERS ||
                currentTerminal.kind == LEFT_SQUARE_BRACKET ||
                currentTerminal.kind == IF ||
                currentTerminal.kind == WHILE ||
                currentTerminal.kind == SAY)
            parseOneStatement();
    }

    private void parsePrimary() {
        switch (currentTerminal.kind) {
            case IDENTIFIER:
                accept(IDENTIFIER);

                if (currentTerminal.kind == LEFT_SQUARE_BRACKET) {
                    accept(LEFT_SQUARE_BRACKET);

                    if (currentTerminal.kind == IDENTIFIER ||
                            currentTerminal.kind == NUMBERS ||
                            currentTerminal.kind == OPERATOR)
                        parseExpressionList();


                    accept(RIGHT_SQUARE_BRACKET);
                }
                break;

            case NUMBERS:
                accept(NUMBERS);
                break;

            case OPERATOR:
                accept(OPERATOR);
                parsePrimary();
                break;

            case LEFT_SQUARE_BRACKET:
                accept(LEFT_SQUARE_BRACKET);
                parseExpression();
                accept(RIGHT_SQUARE_BRACKET);
                break;

            default:
                System.out.println("Error in primary");
                break;
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
        parsePrimary();
        while (currentTerminal.kind == OPERATOR) {
            accept(OPERATOR);
            parsePrimary();
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
}
