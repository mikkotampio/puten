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
		
		put("exec", new Command(c -> {
			try {
				String cmd = c.get("content").string();
				new ProcessBuilder(cmd.split("\\s"))
						.inheritIO()
						.start();
			} catch (Exception e) {
				e.printStackTrace();
				return new StrValue(e.getMessage());
			}
			return Void.INSTANCE;
		}));
		
		put("if", new Command(c -> {
			String val1 = c.get("val1").string();
			String val2 = c.get("val2").string();
			
			if(val1.equals(val2)) {
				return c.get("content").evaluate(c);
			}
			
			return Void.INSTANCE;
		}, "val1", "val2"));
	}
}