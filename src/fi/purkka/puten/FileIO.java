package fi.purkka.puten;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import fi.purkka.puten.lexer.Lexer;
import fi.purkka.puten.parser.Body;
import fi.purkka.puten.parser.Parser;

/** General file IO utilities. */
public class FileIO {
	
	/** Reads the file with the given path. */
	public static String read(String file) {
		try {
			return Files.readAllLines(Paths.get(file), Charset.forName("utf8"))
					.stream()
					.collect(Collectors.joining("\n"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** Writes content to the given file. */
	public static void write(String file, String content) {
		try {
			Files.write(Paths.get(file), content.getBytes(Charset.forName("utf8")));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** Loads libraries from the given location. */
	public static Body loadLibraries(String stdLocation, String target) {
		return Parser.parse(Lexer.process(getLibraries(stdLocation, target)));
	}
	
	private static String getLibraries(String stdLocation, String target) {
		return read(stdLocation + "/std.pte") +
				(target == null ? "" : read(stdLocation + "/" + target + ".pte"));
	}
	
	public static List<String> filesMatching(String regex) {
		Pattern pattern = Pattern.compile(regex);
		try {
			return Files.list(Paths.get(""))
					.map(Path::toString)
					.filter(f -> pattern.matcher(f).matches())
					.sorted()
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}