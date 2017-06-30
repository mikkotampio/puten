package fi.purkka.puten.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Describes some context in which commands are evaluated. */
public class Context {
	
	private final static Context GLOBAL = newGlobal();
	static {
		GLOBAL.map.putAll(Natives.INSTANCE);
	}
	
	private final Context upper;
	private final Map<String, Value> map = new HashMap<>();
	private final boolean mutable;
	private final List<Runnable> afters = new ArrayList<>();
	
	private Context(Context upper, boolean mutable) {
		this.upper = upper;
		this.mutable = mutable;
	}
	
	public boolean hasLocalValue(String var) {
		return map.containsKey(var);
	}
	
	public Value get(String var) {
		if(map.containsKey(var)) return map.get(var);
		if(upper != null) return upper.get(var);
		
		return Nothing.INSTANCE;
	}
	
	public void localSet(String var, Value val) {
		if(mutable) {
			map.put(var, val);
			return;
		} else if(upper != null && upper.mutable) {
			upper.localSet(var, val);
			return;
		}
		
		throw new EvaluationException("Cannot set in this scope");
	}
	
	public void globalSet(String var, Value val) {
		if(upper != null && upper.mutable) {
			upper.globalSet(var, val);
			return;
		} else if(mutable) {
			map.put(var, val);
			return;
		}
		
		throw new EvaluationException("Cannot set in this scope");
	}
	
	public void define(String var, Value val) {
		if(upper != null) {
			upper.define(var, val);
		} else {
			if(map.containsKey(var)) {
				throw new EvaluationException("Cannot redefine " + var);
			}
			map.put(var, val);
		}
	}
	
	public void addAfterHook(Runnable r) {
		if(upper != null) {
			upper.addAfterHook(r);
		} else {
			afters.add(r);
		}
	}
	
	public List<Runnable> afterHooks() {
		return upper == null ? afters : upper.afterHooks();
	}
	
	@Override
	public String toString() {
		return (upper == null ? "" : upper + " > ") +
				"[" + (mutable ? "mutable" : "immutable") + map + "]";
	}
	
	private static Context newGlobal() {
		return new Context(null, false);
	}
	
	public static Context mutable() {
		return new Context(GLOBAL, true);
	}
	
	public static Context commandCallContext(Context upper) {
		return new Context(upper, true);
	}
}