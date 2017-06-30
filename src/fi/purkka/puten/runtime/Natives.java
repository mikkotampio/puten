package fi.purkka.puten.runtime;

import java.util.*;
import java.util.stream.Collectors;

import fi.purkka.puten.FileIO;
import fi.purkka.puten.lexer.Lexer;
import fi.purkka.puten.parser.Parser;
import fi.purkka.puten.parser.PostProcessor;

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
		
		put("varargs", Special.VARARGS);
		
		put("define", Command.varargs(c -> {
			List<String> args = new ArrayList<>();
			if(!c.hasLocalValue("1")) {
				throw new EvaluationException("Name required for define");
			}
			String name = c.get("1").string();
			
			if(c.hasLocalValue("2") && c.get("2").equals(Special.VARARGS)) {
				c.define(name, Command.varargs(context -> {
					return c.get("content").evaluate(context);
				}));
			} else {
				int i = 2;
				while(c.hasLocalValue(""+i)) {
					args.add(c.get(""+i).string());
					i++;
				}
				
				c.define(name, new Command(context -> {
					return c.get("content").evaluate(context);
				}, args.toArray(new String[args.size()])));
			}
			
			return Void.INSTANCE;
		}));
		
		put("exec", new Command(c -> {
			try {
				String cmd = c.get("content").string();
				new ProcessBuilder(cmd.split("\\s"))
						.inheritIO()
						.start().waitFor();
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
		
		put("for", new Command(c -> {
			Iterator<Value> vals = c.get("list").evaluate(c).iterator();
			String item = c.get("item").string();
			StringBuilder sb = new StringBuilder();
			
			while(vals.hasNext()) {
				Value val = vals.next();
				Context context = Context.commandCallContext(c);
				context.localSet(item, val);
				sb.append(c.get("content").evaluate(context));
			}
			
			return new StrValue(sb.toString());
		}, "item", "list"));
		
		put("error", new Command(c -> {
			throw new EvaluationException(c.get("content").string());
		}));
		
		put("includeplain", new Command(c -> {
			return new StrValue(FileIO.read(c.get("content").string()));
		}));
		
		put("include", new Command(c -> {
			String content = FileIO.read(c.get("content").string());
			return Parser.parse(Lexer.process(content)).evaluate(c);
		}));
		
		put("replace", new Command(c -> {
			PostProcessor.registerTransformation(s -> {
				return s.replace(c.get("1").string(), c.get("2").string());
			});
			return Void.INSTANCE;
		}, "1", "2"));
		
		put("regexreplace", new Command(c -> {
			PostProcessor.registerTransformation(s -> {
				return s.replaceAll(c.get("1").string(), c.get("2").string());
			});
			return Void.INSTANCE;
		}, "1", "2"));
		
		put("filesmatching", new Command(c -> {
			String regex = Arrays.stream(c.get("content").string().split("\\*"))
					.map(s -> s.isEmpty() ? "" : "\\Q"+s+"\\E")
					.collect(Collectors.joining(".*"));
			
			if(regex.isEmpty()) { regex = ".*"; }
			
			return ListValue.of(FileIO.filesMatching(regex));
		}));
		
		put("trim", new Command(c -> {
			return new StrValue(c.get("content").evaluate(c).string().trim());
		}));
		
		put("after", new Command(c -> {
			Value content = c.get("content");
			c.addAfterHook(() -> {
				String result = content.evaluate(c).string().trim();
				if(!result.isEmpty()) {
					System.out.println(result);
				}
			});
			return Void.INSTANCE;
		}));
	}
}