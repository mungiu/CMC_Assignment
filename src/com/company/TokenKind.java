/*
 * 23.08.2019 Original version
 */


package com.company;


public enum TokenKind
{
	// INVESTIGATED BY JEPPE
	// Calls empty constructor of TokenKind
	IDENTIFIER,
	NUMBERS,
	OPERATOR,

	// INVESTIGATED BY ANDREI
	// Calls TokenKind constructor that takes a string (NOTE THIS IS HARDCODED INSTANTIATION)
	//Variable types
	CHAR("Char"),
	INT("Int"),
	ARRAY("Array"),
	FUNC( "Func" ),
	//-----
	IF( "if" ),
	RETURN( "return" ),
	SAY( "say" ),
	THEN( "then" ),
	VAR( "var" ),
	WHILE( "&" ),
	
	COMMA( "," ),
	SEMICOLON( ";" ),
	LEFTPARAN( "(" ),
	RIGHTPARAN( ")" ),
	APOSTROPHE("\'"),
	LEFTBRACKET("["),
	RIGHTBRACKET("]"),
	
	EOT,
	
	ERROR;
	
	// spelling is set when the ENUM constructor is called ABOVE
	private String spelling = null;
	
	
	private TokenKind()
	{
	}
	
	
	private TokenKind( String spelling )
	{
		this.spelling = spelling;
	}
	
	
	public String getSpelling()
	{
		return spelling;
	}
}


