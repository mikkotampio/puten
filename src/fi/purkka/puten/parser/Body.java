package fi.purkka.puten.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

	@Override
	public String evaluate() {
		StringBuilder sb = new StringBuilder();
		for(Node node : nodes) {
			sb.append(node.evaluate());
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return "BODY(" + nodes.stream()
				.map(Object::toString)
				.collect(Collectors.joining(", ")) + ")";
	}
}