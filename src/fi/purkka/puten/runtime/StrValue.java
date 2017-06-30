package fi.purkka.puten.runtime;

/** Represents a string. */
public class StrValue implements Value {
	
	private final String string;
	
	public StrValue(String string) {
		this.string = string;
	}
	
	@Override
	public String string() {
		return string;
	}
	
	@Override
	public String toString() {
		return string;
	}
	
	@Override
	public double number() {
		return Double.parseDouble(string);
	}
}