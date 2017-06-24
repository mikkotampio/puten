package fi.purkka.puten.parser;

import java.util.List;

/** A call of some command with some arguments and a body. */
public class CommandCall implements Node {
	
	private final String name;
	private final List<Node> args;
	private final Body body;
	
	CommandCall(String name, List<Node> args, Body body) {
		this.name = name;
		this.args = args;
		this.body = body;
	}

	@Override
	public String evaluate() {
		throw new AssertionError("NYI");
	}
	
	@Override
	public String toString() {
		return "COMMAND(" + name + ", " + args + ", " + body + ")";
	}
}