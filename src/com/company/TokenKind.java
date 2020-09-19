/*
 * 23.08.2019 Original version
 */


package com.company;


public enum TokenKind
{
	// INCESTIGATED BY JEPPE
	// empty onstructor of TokenKind
	IDENTIFIER,
	INTEGERLITERAL,
	OPERATOR,

	// INVESTIGATED BY ANDREI
	// TokenKind constructor that takes a string (NOTE THIS IS HARDCODED INSTANTIATION)
	DECLARE( "declare" ),
	DO( "do" ),
	ELSE( "else" ),
	FI( "fi" ),
	FUNC( "func" ),
	IF( "if" ),
	OD( "od" ),
	RETURN( "return" ),
	SAY( "say" ),
	THEN( "then" ),
	VAR( "var" ),
	WHILE( "while" ),
	
	COMMA( "," ),
	SEMICOLON( ";" ),
	LEFTPARAN( "(" ),
	RIGHTPARAN( ")" ),
	
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