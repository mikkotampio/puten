package fi.purkka.puten.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Natives {
	
	public static Map<String, Value> INSTANCE = new HashMap<>();
	
	private static void put(String key, Value val) {
		INSTANCE.put(key, val);
	}
	
	static {
		put("set", new Command(c -> {
			c.globalSet(c.get("var").string(), c.get("val"));
			return Void.INSTANCE;
		}, "var", "val"));
		
		put("define", Command.varargs(c -> {
			List<String> args = new ArrayList<>();
			if(!c.hasLocalValue("1")) {
				throw new EvaluationException("Name required for define");
			}
			String name = c.get("1").string();
			
			int i = 2;
			while(c.hasLocalValue(""+i)) {
				args.add(c.get(""+i).string());
				i++;
			}
			
			c.define(name, new Command(context -> {
				return c.get("content").evaluate(context);
			}, args.toArray(new String[args.size()])));
			
			return Void.INSTANCE;
		}));
	}
}