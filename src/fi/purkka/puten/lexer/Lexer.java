package fi.purkka.puten.lexer;

import static fi.purkka.puten.lexer.Token.*;

import java.util.ArrayList;
import java.util.List;

/** Transforms a string into a list of {@link Token} instances. */
public class Lexer {
	
	private final String string;
	private int index = 0;
	
	private Lexer(String string) {
		this.string = string;
	}
	
	private List<Token> toTokens() {
		List<Token> tokens = new ArrayList<>();
		while(true) {
			Token next = next();
			if(next == null) break;
			tokens.add(next);
		}
		return tokens;
	}
	
	private Token next() {
		if(isNextSpecialToken()) {
			Token next = nextSpecialToken();
			index += next.content.length();
			return next;
		}
		
		int start = index;
		while(index < string.length() && !isNextSpecialToken()) {
			nextChar();
		}
		
		String substr = string.substring(start, index);
		if(substr.isEmpty()) return null;
		return string(substr);
	}
	
	private boolean isNextSpecialToken() {
		if(isNext('\\')) {
			if(isNextEscapeSequence()) {
				return false;
			}
			return true;
		}
		
		if(isNext(':')) return true;
		if(isNext('{')) return true;
		if(isNext('}')) return true;
		
		return false;
	}
	
	private boolean isNext(char arg) {
		if(index >= string.length()) return false;
		return string.charAt(index) == arg;
	}
	
	private boolean isNextEscapeSequence() {
		return isNext("\\:") || isNext("\\{") || isNext("\\}") || isNext("\\\\");
	}
	
	private boolean isNext(String arg) {
		if(index + arg.length() >= string.length()) return false;
		return string.startsWith(arg, index);
	}

	private Token nextSpecialToken() {
		if(isNext(':')) return COLON;
		if(isNext('{')) return OPENING_BRACE;
		if(isNext('}')) return CLOSING_BRACE;
		if(isNext('\\')) return BACKSLASH;
		throw new AssertionError("was " + string.charAt(index));
	}
	
	private void nextChar() {
		if(isNext('\\')) {
			if(isNextEscapeSequence()) {
				index+=2;
			}
		}
		index++;
	}
	
	public static List<Token> process(String string) {
		return new Lexer(string).toTokens();
	}
}