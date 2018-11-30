package test;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.Assert;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;
import video_gen_xtend.VideoGenHelper;

@SuppressWarnings("all")
public class VideoGenTest1 {
  @Test
  public void testLoadModel() {
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
    Assert.assertNotNull(videoGen);
    InputOutput.<String>println(videoGen.getInformation().getAuthorName());
    Assert.assertEquals(4, videoGen.getMedias().size());
  }
}
