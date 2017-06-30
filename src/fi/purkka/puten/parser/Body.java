package fi.purkka.puten.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fi.purkka.puten.runtime.Context;
import fi.purkka.puten.runtime.StrValue;
import fi.purkka.puten.runtime.Value;

/** Contains zero or more other nodes and evaluates itself by combining
 * the results of evaluating its children. */
public class Body implements Node {
	
	/** A body without any inner nodes. */
	public static final Body EMPTY = new Body(Collections.EMPTY_LIST);
	
	private final List<Node> nodes;
	
	Body() {
		nodes = new ArrayList<>();
	}
	
	Body(List<Node> nodes) {
		this.nodes = nodes;
	}
	
	public void append(Node node) {
		nodes.add(node);
	}
	
	public Node first() {
		return nodes.get(0);
	}
	
	public Body join(Body other) {
		List<Node> nodes = new ArrayList<>(this.nodes);
		nodes.addAll(other.nodes);
		return new Body(nodes);
	}

	@Override
	public Value evaluate(Context context) {
		return new BodyValue(context);
	}
	
	private class BodyValue implements Value {
		
		private Context defaultContext;
		
		BodyValue(Context defaultContext) {
			this.defaultContext = defaultContext;
		}
		
		@Override
		public Value evaluate(Context context) {
			StringBuilder sb = new StringBuilder();
			for(Node node : nodes) {
				sb.append(node.evaluate(context).string());
			}
			return new StrValue(sb.toString());
		}
		
		@Override
		public String string() {
			return evaluate(defaultContext).string();
		}
	}
	
	@Override
	public String toString() {
		return "BODY(" + nodes.stream()
				.map(Object::toString)
				.collect(Collectors.joining(", ")) + ")";
	}
}