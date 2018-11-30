package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

import vgts.FfmpegSystem;
import vgts.VideoAnalysisTools;
import vgts.VideoGenToolSuite;
import video_gen_xtend.VideoGenHelper;

/**
 * 
 * IDM M2 MIAGE 2018-2019 (VideoGenToolSuite) - Tests TP4
 * 
 * @author HERNANDEZ Maykol, ELMAIZI Sohaib
 * @version 1.1
 * @since 2018-11-20
 * 
 */

public class VideoGenTestTp4 {

	@Test
	public void testNbVariantes1() {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
		assertNotNull(videoGen);
		assertEquals(4, VideoGenToolSuite.nbVariantes("testFiles/test1.videogen"));
	}

	@Test
	public void testNbVariantes2() {
		VideoGeneratorModel videoGen = new VideoGenHelper()
				.loadVideoGenerator(URI.createURI("testFiles/test2.videogen"));
		assertNotNull(videoGen);
		assertEquals(6, VideoGenToolSuite.nbVariantes("testFiles/test2.videogen"));
	}

	@Test
	public void testNbVariantes3() {
		VideoGeneratorModel videoGen = new VideoGenHelper()
				.loadVideoGenerator(URI.createURI("testFiles/test3.videogen"));
		assertNotNull(videoGen);
		assertEquals(1, VideoGenToolSuite.nbVariantes("testFiles/test3.videogen"));
	}

	@Test
	public void testNbVariantes4() {
		VideoGeneratorModel videoGen = new VideoGenHelper()
				.loadVideoGenerator(URI.createURI("testFiles/test4.videogen"));
		assertNotNull(videoGen);
		assertEquals(2, VideoGenToolSuite.nbVariantes("testFiles/test4.videogen"));
	}

	@Test
	public void testNbVariantes5() {
		VideoGeneratorModel videoGen = new VideoGenHelper()
				.loadVideoGenerator(URI.createURI("testFiles/test5.videogen"));
		assertNotNull(videoGen);
		assertEquals(3, VideoGenToolSuite.nbVariantes("testFiles/test5.videogen"));
	}

	@Test
	public void testNbVariantes6() {
		VideoGeneratorModel videoGen = new VideoGenHelper()
				.loadVideoGenerator(URI.createURI("testFiles/test6.videogen"));
		assertNotNull(videoGen);
		assertEquals(6, VideoGenToolSuite.nbVariantes("testFiles/test6.videogen"));
	}

	@Test
	public void testNbVariantes7() {
		VideoGeneratorModel videoGen = new VideoGenHelper()
				.loadVideoGenerator(URI.createURI("testFiles/test7.videogen"));
		assertNotNull(videoGen);
		assertEquals(120, VideoGenToolSuite.nbVariantes("testFiles/test7.videogen"));
	}

	@Test
	public void testNbVariantes8() {
		VideoGeneratorModel videoGen = new VideoGenHelper()
				.loadVideoGenerator(URI.createURI("testFiles/test8.videogen"));
		assertNotNull(videoGen);
		assertEquals(16, VideoGenToolSuite.nbVariantes("testFiles/test8.videogen"));
	}

	@Test
	public void testNbVariantes9() {
		VideoGeneratorModel videoGen = new VideoGenHelper()
				.loadVideoGenerator(URI.createURI("testFiles/test9.videogen"));
		assertNotNull(videoGen);
		assertEquals(600, VideoGenToolSuite.nbVariantes("testFiles/test9.videogen"));
	}

	@Test
	public void testNbVariantes10() {
		VideoGeneratorModel videoGen = new VideoGenHelper()
				.loadVideoGenerator(URI.createURI("testFiles/test10.videogen"));
		assertNotNull(videoGen);
		assertEquals(48, VideoGenToolSuite.nbVariantes("testFiles/test10.videogen"));
	}

	@Test
	public void testCSV() throws IOException {
		
		String videoGenFile = "example1.videogen";
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(videoGenFile));
		assertNotNull(videoGen);
		
		File file = new File("csv.csv");
		file.delete();
		
		assert(VideoAnalysisTools.analyseVideoGen(videoGenFile));
		
		BufferedReader reader = new BufferedReader(new FileReader("csv.csv"));
		int lines = 0;
		while (reader.readLine() != null) lines++;
		reader.close();
		
		assertEquals(lines, VideoGenToolSuite.nbVariantes(videoGenFile) + 1);
	}

	@Test
	public void testNbVignettes() {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
		assertNotNull(videoGen);

		File file = new File("testFiles/testVignettes");
		String[] entries = file.list();
		for (String s : entries) {
			File currentFile = new File(file.getPath(), s);
			currentFile.delete();
		}
		file.delete();
		assert (file.mkdirs());

		VideoGenToolSuite.generateurDeVignettes("example1.videogen", "testFiles/testVignettes/");
		entries = file.list();
		int nbImages = 0;
		for (String s : entries) {
			File currentFile = new File(file.getPath(), s);
			currentFile.delete();
			nbImages++;
		}
		assertEquals(4, videoGen.getMedias().size());
		assertEquals(5, nbImages);
	}

	@Test
	public void testCheckVideoGenSpecifications() {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
		assertNotNull(videoGen);
		assertTrue(VideoGenToolSuite.checkVideoGenSpecifications("example1.videogen"));
	}

	@Test
	public void testFilterEffectVideo() {
		File file = new File("testFiles/testVideo/testFilterEffectVideoFF.mp4");
		file.delete();
		file = new File("testFiles/testVideo/testFilterEffectVideoNF.mp4");
		file.delete();
		file = new File("testFiles/testVideo/testFilterEffectVideoBWF.mp4");
		file.delete();

		assertTrue("Error filter FlipFilterVertical", FfmpegSystem.filterEffectVideo("testFiles/testVideo/snus-nu.mp4",
				"testFiles/testVideo/testFilterEffectVideoFFV.mp4", "FlipFilterVertical"));
		assertTrue("File not found: FlipFilterVertical test",
				new File("testFiles/testVideo/testFilterEffectVideoFFV.mp4").isFile());
		
		assertTrue("Error filter FlipFilterHorizontal", FfmpegSystem.filterEffectVideo("testFiles/testVideo/snus-nu.mp4",
				"testFiles/testVideo/testFilterEffectVideoFFH.mp4", "FlipFilterHorizontal"));
		assertTrue("File not found: FlipFilterHorizontal test",
				new File("testFiles/testVideo/testFilterEffectVideoFFH.mp4").isFile());

		assertTrue("Error filter NegateFilter", FfmpegSystem.filterEffectVideo("testFiles/testVideo/snus-nu.mp4",
				"testFiles/testVideo/testFilterEffectVideoNF.mp4", "NegateFilter"));
		assertTrue("File not found: FlipFilter test",
				new File("testFiles/testVideo/testFilterEffectVideoNF.mp4").isFile());

		assertTrue("Error filter BlackWhiteFilter", FfmpegSystem.filterEffectVideo("testFiles/testVideo/snus-nu.mp4",
				"testFiles/testVideo/testFilterEffectVideoBWF.mp4", "BlackWhiteFilter"));
		assertTrue("File not found: FlipFilter test",
				new File("testFiles/testVideo/testFilterEffectVideoBWF.mp4").isFile());
	}

	@Test
	public void testTextIntoVideo() {
		File file = new File("testFiles/testVideo/testTextIntoVideo.mp4");
		file.delete();

		assertTrue(FfmpegSystem.textIntoVideo("testFiles/testVideo/snus-nu.mp4",
				"testFiles/testVideo/testTextIntoVideo.mp4", "This is a test text", "CENTER", "red", 20));
		assertTrue("File not found: testTextIntoVideo.mp4",
				new File("testFiles/testVideo/testTextIntoVideo.mp4").isFile());
	}

}
