package fi.purkka.puten.parser;

import java.util.regex.Pattern;

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
		return new StrValue(unescape(content));
	}
	
	private final static Pattern N_PATTERN = Pattern.compile("([^\\\\])\\\\n");
	private final static Pattern T_PATTERN = Pattern.compile("([^\\\\])\\\\t");
	
	private static String unescape(String str) {
		if(str.length() >= 2 && str.startsWith("#") && str.endsWith("#") && !str.endsWith("\\#")) {
			str = N_PATTERN.matcher(str).replaceAll("$1\n");
			str = T_PATTERN.matcher(str).replaceAll("$1\t");
			str = str.replace("\\\\n", "\\n");
			str = str.replace("\\\\t", "\\t");
			str = str.substring(1, str.length()-1);
		}
		
		return str;
	}
	
	@Override
	public String toString() {
		return "TEXT(" + content + ")";
	}
}