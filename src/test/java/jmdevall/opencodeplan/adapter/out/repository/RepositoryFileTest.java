package jmdevall.opencodeplan.adapter.out.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepositoryFileTest {

	@Test
	public void canSaveContentOfFile() throws Exception{
		File srcRoot = Files.createTempDirectory("openCodePlanTestFileRespository").toFile();
		String tmpdir = srcRoot.getAbsolutePath();
		log.debug("temp folder="+tmpdir);
		
		RepositoryFile sut=RepositoryFile.newRepositoryFile(srcRoot);
		
		String filepath = "/foo/bar/Foo.java";
		
		sut.save(filepath, "content1");
		
		String readcontent = new String(Files.readAllBytes(Paths.get(tmpdir+filepath)));
		assertEquals("content1",readcontent);
		
		sut.save(filepath, "content2");
		String other = new String(Files.readAllBytes(Paths.get(tmpdir+filepath)));
		assertEquals("content2",other);
	}
}
