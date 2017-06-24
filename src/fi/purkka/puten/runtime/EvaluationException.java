package fi.purkka.puten.runtime;

/** Indicates that something went wrong while evaluating a
 * command call. */
public class EvaluationException extends RuntimeException {
	
	private static final long serialVersionUID = -4791430331170412218L;
	
	private final String msg;
	
	EvaluationException(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String getMessage() { return msg; }
}