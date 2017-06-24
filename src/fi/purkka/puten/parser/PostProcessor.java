package fi.purkka.puten.parser;

import java.util.regex.Pattern;

/** Responsible for correcting whitespace in evaluated
 * Puten code. */
public class PostProcessor {
	
	private final static Pattern NEWLINE_FIXER = Pattern.compile("\\n\\n\\n+");
	private final static Pattern SPACE_FIXER = Pattern.compile("\\ \\ +");
	
	public static String process(String str) {
		str = NEWLINE_FIXER.matcher(str).replaceAll("\n\n");
		str = SPACE_FIXER.matcher(str).replaceAll(" ");
		return str.trim();
	}
}