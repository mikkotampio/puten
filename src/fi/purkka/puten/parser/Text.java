package fi.purkka.puten.parser;

import fi.purkka.puten.lexer.Token;
import fi.purkka.puten.runtime.Context;
import fi.purkka.puten.runtime.StrValue;
import fi.purkka.puten.runtime.Value;

/** Represents a single {@link Token} interpreted as plaintext. */
public class Text implements Node {
	
	private final String content;
	
	public Text(Token token) {
		this.content = token.content;
	}

	@Override
	public Value evaluate(Context context) {
		return new StrValue(content);
	}
	
	@Override
	public String toString() {
		return "TEXT(" + content + ")";
	}
}