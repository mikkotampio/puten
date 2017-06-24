package fi.purkka.puten;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fi.purkka.puten.lexer.Lexer;
import fi.purkka.puten.parser.ParseException;
import fi.purkka.puten.parser.Parser;

public class TestParser {
	
	private static String body(String code) {
		return Parser.parse(Lexer.process(code)).toString();
	}
	
	private static String node(String code) {
		return Parser.parse(Lexer.process(code)).first().toString();
	}
	
	private static String eval(String code) {
		return Parser.parse(Lexer.process(code)).evaluate();
	}
	
	@Test
	public void testParsingPlaintext() {
		assertEquals(eval("Kissat ovat hauskoja :)\n\nNiin ovat!"),
				"Kissat ovat hauskoja :)\n\nNiin ovat!");
	}
	
	@Test
	public void testParsingCommandCall() {
		assertEquals(node("\\cmd:a1:a2{body}"),
				"COMMAND(cmd, [TEXT(a1), TEXT(a2)], BODY(TEXT(body)))");
	}
	
	@Test
	public void testParsingVarAccess() {
		assertEquals(node("\\var"),
				"VAR(var)");
	}
	
	@Test
	public void testArgRecursiveCommandCall() {
		assertEquals(node("\\a:\\b{}{}"),
				"COMMAND(a, [COMMAND(b, [], BODY())], BODY())");
	}
	
	@Test
	public void testBodyRecursiveCommandCall() {
		assertEquals(node("\\a{\\b}"),
				"COMMAND(a, [], BODY(VAR(b)))");
	}
	
	@Test
	public void testCommandEscapeSyntax() {
		assertEquals(node("\\a:{\\b}{kissa}"),
				"COMMAND(a, [VAR(b)], BODY(TEXT(kissa)))");
	}
	
	@Test(expected=ParseException.class)
	public void testParseException() {
		eval("\\command{");
	}
	
	@Test
	public void testPreservingWhitespace() {
		assertEquals(body("par1\n\n\\var\n\npar2"),
				"BODY(TEXT(par1), TEXT(\n\n), VAR(var), TEXT(\n\n), TEXT(par2))");
	}
}