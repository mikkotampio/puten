package fi.purkka.puten.parser;

import java.util.List;
import java.util.stream.Collectors;

import fi.purkka.puten.runtime.Context;
import fi.purkka.puten.runtime.Value;

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
	public Value evaluate(Context context) {
		return context.get(name).call(args.stream()
				.map(a -> a.evaluate(context))
				.collect(Collectors.toList()), body, context);
	}
	
	@Override
	public String toString() {
		return "COMMAND(" + name + ", " + args + ", " + body + ")";
	}
}