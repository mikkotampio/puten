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
			return nextSpecialToken();
		}
		
		if(isNextWhitespace()) {
			return nextWhitespace();
		}
		
		int start = index;
		while(index < string.length() && isNextValidStringChar()) {
			nextChar();
		}
		
		String substr = string.substring(start, index);
		if(substr.isEmpty()) return null;
		return string(substr);
	}
	
	private boolean isNextValidStringChar() {
		return !isNextWhitespace() && !isNextSpecialToken();
	}
	
	private boolean isNextWhitespace() {
		return isNext(' ') || isNext('\t') || isNext('\n');
	}
	
	private Token nextWhitespace() {
		int start = index;
		while(index < string.length() && isNextWhitespace()) {
			index++;
		}
		String substr = string.substring(start, index);
		if(substr.isEmpty()) return null;
		return whitespace(substr);
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
		char chr = string.charAt(index);
		index++;
		if(chr == ':') return COLON;
		if(chr == '{') return OPENING_BRACE;
		if(chr == '}') return CLOSING_BRACE;
		if(chr == '\\') return BACKSLASH;
		throw new AssertionError("was " + chr);
	}
	
	private void nextChar() {
		if(isNext('\\')) {
			if(isNextEscapeSequence()) {
				index+=2;
			}
		}
		index++;
	}
	
	/** Turns its argument into a list of tokens. Should not
	 * throw any exceptions. */
	public static List<Token> process(String string) {
		return new Lexer(string).toTokens();
	}
}