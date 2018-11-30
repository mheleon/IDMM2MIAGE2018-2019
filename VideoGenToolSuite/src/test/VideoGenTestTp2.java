package test;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

import vgts.VideoGenToolSuite;
import video_gen_xtend.VideoGenHelper;

public class VideoGenTestTp2 {

	@Test
	public void testRandomConcatenation() {
		String fileVideoGen = "example1.videogen";
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(fileVideoGen));
		assertNotNull(videoGen);
		VideoGenToolSuite.ParCompilation(fileVideoGen, "ressources/videos/testRandomConcatenation.mp4", false);

	}

	@Test
	public void testConcatenationCustomized() {
		String fileVideoGen = "example1.videogen";
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(fileVideoGen));
		assertNotNull(videoGen);
		List<String> list = new ArrayList<String>();
		list.add("v1");
		list.add("v2");
		list.add("v31");

		assert (VideoGenToolSuite.concatenationCustomized(list, "ressources/videos/",
				"ressources/videos/testConcatenationCustomized.mp4"));

	}

	@Test
	public void testcheckVideoGenSpecifications() throws IOException {
		String fileVideoGen = "example1.videogen";
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(fileVideoGen));
		assertNotNull(videoGen);
		assert (VideoGenToolSuite.checkVideoGenSpecifications(fileVideoGen));

	}

}
