package fi.purkka.puten.runtime;

/** Represents a value that will be evaluated to the empty string. */
public class Void implements Value {
	
	public final static Void INSTANCE = new Void();
	
	private Void() {}
	
	@Override
	public String string() { return ""; }
}