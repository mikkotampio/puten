package fi.purkka.puten.parser;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Responsible for correcting whitespace in evaluated
 * Puten code. */
public class PostProcessor {
	
	private final static Pattern NEWLINE_FIXER = Pattern.compile("\\n\\n\\n+");
	private final static Pattern SPACE_FIXER = Pattern.compile("\\ \\ +");
	
	private final static Deque<Function<String, String>> STACK = new ArrayDeque<>();
	
	public static String process(String str) {
		str = NEWLINE_FIXER.matcher(str).replaceAll("\n\n");
		str = SPACE_FIXER.matcher(str).replaceAll(" ");
		str = Arrays.stream(str.split("\n")).map(String::trim).collect(Collectors.joining("\n"));
		str = str.replace("\\\\", "\\");
		str = str.replace("\\:", ":");
		str = str.replace("\\{", "{");
		str = str.replace("\\}", "}");
		str = str.trim();
		
		Deque<Function<String, String>> stack = new ArrayDeque<>(STACK);
		
		while(!stack.isEmpty()) {
			Function<String, String> func = stack.pop();
			str = func.apply(str);
		}
		return str;
	}
	
	public static void registerTransformation(Function<String, String> trans) {
		STACK.push(trans);
	}
}