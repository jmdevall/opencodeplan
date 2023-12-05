package jmdevall.opencodeplan.adapter.out.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

public class SourceFolderTest {

	@Test
	public void giveIfTheSourceFolderContainsFileOrNot() throws IOException {
		File tempFolder = Files.createTempDirectory("testSourceFolder").toFile();
		new File(tempFolder,"holamundo.txt").createNewFile();
		
		SourceFolder sourceFolder=new SourceFolder(tempFolder,FiltersFactory.allFiles(),true);
		
		assertTrue(sourceFolder.contains("holamundo.txt"));
		assertFalse(sourceFolder.contains("esteno.txt"));
	}
}
