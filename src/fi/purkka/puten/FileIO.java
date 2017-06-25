package fi.purkka.puten;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

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
}