package fi.purkka.puten;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fi.purkka.puten.lexer.Lexer;
import fi.purkka.puten.parser.Parser;
import fi.purkka.puten.parser.PostProcessor;
import fi.purkka.puten.runtime.Context;
import fi.purkka.puten.runtime.EvaluationException;
import fi.purkka.puten.runtime.StrValue;

public class TestRuntime {
	
	static {
		 FileIO.loadLibraries("std", "test").evaluate(Context.mutable()).string();
	}
	
	private static String eval(String code) {
		Context context = Context.mutable();
		context.globalSet("target", new StrValue("test"));
		return PostProcessor.process(
				Parser.parse(Lexer.process(code))
				.evaluate(context).string());
	}
	
	@Test
	public void testLibraryLoading() {
		assertEquals(eval("\\puten_testing{}"), "true");
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
	public void testTargets() {
		eval("\\targets:test{}");
	}
	
	@Test
	public void testFixingNewlines() {
		assertEquals(eval("a\n\n\n\n\n\n\n\nb"), "a\n\nb");
	}
	
	@Test
	public void testFixingSpaces() {
		assertEquals(eval("a                b"), "a b");
	}
	
	@Test
	public void testIf() {
		assertEquals(eval("\\set:n:1{} \\if:{\\n}:1{kissa}"), "kissa");
	}
	
	@Test
	public void testReplace() {
		assertEquals(eval("\\replace:äää:ÄÄÄ{} äää"), "ÄÄÄ");
	}
	
	@Test
	public void testEscaping() {
		assertEquals(eval("\\\\\\{\\}\\:"), "\\{}:");
	}
	
	@Test
	public void testMatchingFiles() {
		assertEquals(eval("\\filesmatching{*.gradle}"),
				"[.gradle, build.gradle, settings.gradle]");
	}
	
	@Test
	public void testTrim() {
		assertEquals(eval("a \\trim{   kissa    } b"), "a kissa b");
	}
	
	@Test
	public void testEscapeCodes() {
		assertEquals(eval("#kissat\\novat\\nhauskoja#"), "kissat\novat\nhauskoja");
		assertEquals(eval("#\\n#"), "");
		assertEquals(eval("#\\\\n#"), "\\n");
	}
	
	@Test
	public void testEscapeCodesInCommandCalls() {
		assertEquals(eval("\\set:#a#:6{} \\a"), "6");
	}
}