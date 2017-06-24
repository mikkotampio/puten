package fi.purkka.puten.parser;

import fi.purkka.puten.runtime.Context;
import fi.purkka.puten.runtime.Value;

/** Describes a node within the Puten AST. */
public interface Node {
	
	/** Evaluates this node, returning a value that represents
	 * the result. */
	public Value evaluate(Context context);
}