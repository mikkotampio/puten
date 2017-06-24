package fi.purkka.puten.runtime;

/** Represents a missing value. */
public class Nothing implements Value {
	
	public final static Nothing INSTANCE = new Nothing();
	
	private Nothing() {}
	
	@Override
	public String string() { return "Nothing"; }
}