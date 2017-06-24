package fi.purkka.puten.parser;

import fi.purkka.puten.lexer.Token.TokenType;

/** Indicates that something went wrong while parsing. */
public class ParseException extends RuntimeException {

	private static final long serialVersionUID = 3962318535893920062L;
	
	private final String expected, got;
	
	ParseException(TokenType expected, TokenType got) {
		this.expected = expected.toString();
		this.got = got.toString();
	}
	
	ParseException(TokenType expected, String got) {
		this.expected = expected.toString();
		this.got = got;
	}
	
	@Override
	public String getMessage() {
		return "Expected " + expected + ", got " + got; 
	}
}