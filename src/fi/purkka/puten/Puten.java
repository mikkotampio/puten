package fi.purkka.puten;

import static fi.purkka.jarpa.JarpaArg.string;
import fi.purkka.jarpa.JarpaArg;
import fi.purkka.jarpa.JarpaArgs;
import fi.purkka.jarpa.JarpaException;
import fi.purkka.jarpa.JarpaParser;
import fi.purkka.puten.lexer.Lexer;
import fi.purkka.puten.parser.Body;
import fi.purkka.puten.parser.Parser;
import fi.purkka.puten.parser.PostProcessor;
import fi.purkka.puten.runtime.Context;
import fi.purkka.puten.runtime.StrValue;

public class Puten {
	
	public static void main(String[] args) {
		
		try(JarpaArgs jargs = JarpaParser.parsing(args)
				.equalsSeparated().parse()) {
			
			if(jargs.get(JarpaArg.flag("--help").alias("-h"))) {
				System.out.println("Usage: puten --target|-t=TARGET --output|-o=OUTPUT [--std|-s=STD_LOCATION] FILE");
				return;
			}
			
			String target = jargs.get(string("--target").alias("-t"));
			String file = jargs.get(string(""));
			String outputFile = jargs.get(string("--output").alias("-o"));
			String std = jargs.get(string("--std").alias("-s")
					.optional()).orElse(System.getProperty("home.directory")+"/.puten");
			
			System.out.println("std " + std);
			
			Context context = Context.mutable();
			context.globalSet("target", new StrValue(target));
			
			Body body = Parser.parse(Lexer.process(FileIO.read(file)));
			String output = PostProcessor.process(body.evaluate(context).string());
			
			FileIO.write(outputFile, output);
		} catch(JarpaException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}