package fi.purkka.puten;

import static fi.purkka.puten.lexer.Lexer.process;
import static fi.purkka.puten.lexer.Token.*;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import fi.purkka.puten.lexer.Token;

public class TestLexer {
	
	private static List<Token> list(Token...tokens) {
		return Arrays.asList(tokens);
	}
	
	@Test
	public void testLexingNormalCharacters() {
		assertEquals(process("a:bbb{}\\c"), list(
				string("a"),
				COLON,
				string("bbb"),
				OPENING_BRACE,
				CLOSING_BRACE,
				BACKSLASH,
				string("c")));
	}
	
	@Test
	public void testEscapeSequences() {
		assertEquals(process("\\:\\\\\\{\\}a"), list(
				string("\\:\\\\\\{\\}a")));
	}
	
	@Test
	public void testLexingWhitespace() {
		assertEquals(process("a\\#a \nbb\t\tc"), list(
				string("a\\#a"),
				whitespace(" \n"),
				string("bb"),
				whitespace("\t\t"),
				string("c")));
	}
	

	@Test
	public void testEscapingBug() {
		assertEquals(process("\\}}"), list(
				string("\\}"),
				CLOSING_BRACE));
	}
	
	@Test
	public void testLexingEscapedString() {
		assertEquals(process("#kissa\\#{}:#  #aa#  #b#"), list(
				string("#kissa\\#{}:#"),
				whitespace("  "),
				string("#aa#"),
				whitespace("  "),
				string("#b#")));
	}
}