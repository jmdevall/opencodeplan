package jmdevall.opencodeplan.adapter.out.file;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

	public static String readFile(String path) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, StandardCharsets.UTF_8);
		} catch (Exception error) {
			System.err.println(error);
			return "";
		}

	}

}
