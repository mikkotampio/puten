package fi.purkka.puten.runtime;

import java.util.List;

import fi.purkka.puten.parser.Body;

public interface Value {
	
	/** Calls this value in some context and with optional arguments
	 * and body. */
	@SuppressWarnings("unused")
	public default Value call(List<Value> args, Body body, Context context) {
		throw new EvaluationException("Not callable: " + string());
	}
	
	/** Evaluates this value in the given context. */
	@SuppressWarnings("unused")
	public default Value evaluate(Context context) {
		return this;
	}
	
	/** Returns a string representation of this value. */
	public default String string() {
		return getClass().getSimpleName();
	}
	
	/** Returns a double representation of this value. */
	public default double number() {
		throw new EvaluationException("Not numeric: " + string());
	}
}