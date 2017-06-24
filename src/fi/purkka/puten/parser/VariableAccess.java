package fi.purkka.puten.parser;

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
	public String evaluate() {
		throw new AssertionError("NYI");
	}
}