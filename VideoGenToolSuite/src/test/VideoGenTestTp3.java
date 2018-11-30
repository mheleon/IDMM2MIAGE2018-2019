package test;

import static org.junit.Assert.*;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

import vgts.FfmpegSystem;
import vgts.VideoAnalysisTools;
import video_gen_xtend.VideoGenHelper;

/**
 * 
 * IDM M2 MIAGE 2018-2019 (VideoGenToolSuite) - Tests TP3
 * 
 * @author HERNANDEZ Maykol, ELMAIZI Sohaib
 * @version 1.1
 * @since 2018-11-20
 * 
 */

public class VideoGenTestTp3 {

	@Test
	public void testAnalyseVideoGen() throws IOException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
		assertNotNull(videoGen);

		File file = new File("ressources/videoRealSize.mp4");
		file.delete();

		assertTrue(VideoAnalysisTools.analyseVideoGen("example1.videogen"));
		assertTrue("File not found: testTextIntoVideo.mp4", new File("csv.csv").isFile());

		File file1 = new File("testFiles/testCsvFiles/csvTestFile.csv");
		File file2 = new File("csv.csv");
		assertTrue(FileUtils.contentEquals(file1, file2));
	}

	@Test
	public void testConvertToGif() throws IOException {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
		assertNotNull(videoGen);

		File file = new File("testFiles/testGif/testGifOutput.gif");
		file.delete();

		assertTrue(FfmpegSystem.convertToGif("testFiles/testVideo/snus-nu.mp4", "testFiles/testGif/testGifOutput.gif"));

		assertTrue("File not found: testTextIntoVideo.mp4", new File("testFiles/testGif/testGifOutput.gif").isFile());

		File fileTest = new File("testFiles/testGif/GifTestFile.gif");
		File fileCreated = new File("testFiles/testGif/testGifOutput.gif");
		assertTrue(FileUtils.contentEquals(fileTest, fileCreated));
	}

}
