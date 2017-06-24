package fi.purkka.puten.runtime;

import java.util.Arrays;
import java.util.List;

import fi.purkka.puten.parser.Body;

/** Describes a command that may be called to yield a result. */
public class Command implements Value {
	
	private final List<String> args;
	private final CommandFunc func;
	private boolean varargs = false;
	
	public Command(CommandFunc func, String...args) {
		this.args = Arrays.asList(args);
		this.func = func;
	}
	
	public List<String> args() { return args; }
	
	@Override
	public Value call(List<Value> args, Body body, Context upContext) {
		Context context = Context.commandCallContext(upContext);
		
		if(!varargs) {
			if(this.args.size() != args.size()) {
				throw new EvaluationException("Incorrect number of args: "
						+ this.args.size() + " required, " + args.size() + "given");
			}
			
			for(int i = 0; i < args.size(); i++) {
				context.localSet(this.args.get(i), args.get(i));
			}
		} else {
			for(int i = 0; i < args.size(); i++) {
				context.localSet(""+(i+1), args.get(i));
			}
		}
		
		context.localSet("content", body.evaluate(upContext));
		
		return func.callImpl(context);
	}
	
	public static Command varargs(CommandFunc func) {
		Command cmd = new Command(func);
		cmd.varargs = true;
		return cmd;
	}
	
	@FunctionalInterface
	public static interface CommandFunc {
		
		public Value callImpl(Context context);
	}
}