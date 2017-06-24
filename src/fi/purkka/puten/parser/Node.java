package fi.purkka.puten.parser;

/** Describes a node within the Puten AST. */
public interface Node {
	
	/** Evaluates this node, returning a string that represents
	 * the result. */
	public String evaluate();
}