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
}