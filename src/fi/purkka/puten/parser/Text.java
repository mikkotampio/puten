package fi.purkka.puten.parser;

import fi.purkka.puten.lexer.Token;

/** Represents a single {@link Token} interpreted as plaintext. */
public class Text implements Node {
	
	private final String content;
	
	public Text(Token token) {
		this.content = token.content;
	}

	@Override
	public String evaluate() {
		return content;
	}
	
	@Override
	public String toString() {
		return "TEXT(" + content + ")";
	}
}