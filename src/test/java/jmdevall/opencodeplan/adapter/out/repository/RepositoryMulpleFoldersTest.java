package jmdevall.opencodeplan.adapter.out.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.adapter.out.javaparser.util.TestingUtil;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.application.port.out.repository.SourceFolder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepositoryMulpleFoldersTest {

	@Test
	public void canSaveContentOfFile() throws Exception{
		File srcRoot = Files.createTempDirectory("openCodePlanTestFileRespository").toFile();
		String tmpdir = srcRoot.getAbsolutePath();
		log.debug("temp folder="+tmpdir);
		
		RepositoryMulpleFolders sut=RepositoryMulpleFolders.newFromSingleSourceRoot(srcRoot);
		
		String filepath = "/foo/bar/Foo.java";
		
		sut.save(filepath, "content1");
		
		String readcontent = new String(Files.readAllBytes(Paths.get(tmpdir+filepath)));
		assertEquals("content1",readcontent);
		
		sut.save(filepath, "content2");
		String other = new String(Files.readAllBytes(Paths.get(tmpdir+filepath)));
		assertEquals("content2",other);
	}
	
	@Test
	public void multiplesFolders() throws Exception{
		File srcRoot1 = Files.createTempDirectory("openCodePlanTestFileRespository").toFile();
		String tmpdir1 = srcRoot1.getAbsolutePath();
		log.debug("temp folder 1="+tmpdir1);

		File srcRoot2 = Files.createTempDirectory("openCodePlanTestFileRespository").toFile();
		String tmpdir2 = srcRoot2.getAbsolutePath();
		log.debug("temp folder 2="+tmpdir2);
		
		

		SourceFolder sf1=new SourceFolder(srcRoot1,FiltersFactory.defaultJavaExtensionFilter(),false);
		SourceFolder sf2=new SourceFolder(srcRoot2,FiltersFactory.defaultJavaExtensionFilter(),true);
		
		RepositorySingleFolder onlySf2=new RepositorySingleFolder(sf2);
		
		onlySf2.save("/test.java", "untest");
		
		RepositoryMulpleFolders sut=RepositoryMulpleFolders.newRepositoryMultipleFolders(
				Arrays.asList(sf1,sf2));

		String source=sut.getCuSource().getSource("/test.java");
		
		assertNotNull(source);
		assertEquals("untest",source);
	}
	
	TestingUtil testUtil=new TestingUtil();
	
	@Test
	public void canReadSourceCode() {
		File root=testUtil.getSrcTestFile("/testbench");
		CuSource sut=RepositoryMulpleFolders.newFromSingleSourceRoot(root).getCuSource();
		
		String sourceCode=sut.getSource(sut.getPaths().iterator().next());
		assertNotNull(sourceCode);
	}
	
}
