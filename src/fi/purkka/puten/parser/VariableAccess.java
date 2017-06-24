package fi.purkka.puten.parser;

import fi.purkka.puten.runtime.Context;
import fi.purkka.puten.runtime.Value;

public class VariableAccess implements Node {
	
	private final String var;
	
	VariableAccess(String var) {
		this.var = var;
	}
	
	@Override
	public String toString() {
		return "VAR(" + var + ")";
	}

	@Override
	public Value evaluate(Context context) {
		return context.get(var);
	}
}