package test

import org.junit.Test
import org.eclipse.emf.common.util.URI

import static org.junit.Assert.*

import video_gen_xtend.VideoGenHelper;

class VideoGenTest1 {


	@Test
	def void testLoadModel() {
		
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"))
		assertNotNull(videoGen)
		println(videoGen.information.authorName)
		assertEquals(4, videoGen.medias.size);
					
	}
}
