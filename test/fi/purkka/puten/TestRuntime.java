package fi.purkka.puten;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fi.purkka.puten.lexer.Lexer;
import fi.purkka.puten.parser.Parser;
import fi.purkka.puten.parser.PostProcessor;
import fi.purkka.puten.runtime.Context;
import fi.purkka.puten.runtime.EvaluationException;

public class TestRuntime {
	
	private static String eval(String code) {
		return PostProcessor.process(
				Parser.parse(
				Lexer.process(code))
				.evaluate(Context.mutable()).string());
	}
	
	@Test
	public void testUnknownVariable() {
		assertEquals(eval("\\unknownvar"), "Nothing");
	}
	
	@Test(expected=EvaluationException.class)
	public void testCallingUnknownVariable() {
		eval("\\unknowncommand{}");
	}
	
	@Test
	public void testIdentity() {
		assertEquals(eval("\\identity{kissa}"), "kissa");
	}
	
	@Test
	public void testSet() {
		assertEquals(eval("\\set:var:a{} \\var"), "a");
	}
	
	@Test
	public void testFixingNewlines() {
		assertEquals(eval("a\n\n\n\n\n\n\n\nb"), "a\n\nb");
	}
	
	@Test
	public void testFixingSpaces() {
		assertEquals(eval("a                b"), "a b");
	}
}