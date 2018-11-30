package test;
import static org.junit.Assert.*;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;


import video_gen_xtend.VideoGenHelper;

public class VideoGenTestJava1 {
	
	@Test
	public void testInJava1() {
		
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
		assertNotNull(videoGen);		
		System.out.println(videoGen.getInformation().getAuthorName());		
		assertEquals(4, videoGen.getMedias().size());	
		
	}

	

}
