package vgts;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

import video_gen_xtend.VideoGenHelper;

/**
 * 
 * IDM M2 MIAGE 2018-2019 (VideoGenToolSuite) - Class to manage VideoGen App
 * 
 * @author HERNANDEZ Maykol, ELMAIZI Sohaib
 * @version 1.1
 * @since 2018-11-02
 * 
 */

public class VideoGenToolSuite {

	/* ================================ TP2 ================================ */

	/**
	 * TP2, Question 1. Transformation par interpretation
	 * 
	 * @param videoGenFile
	 * @param fileNameOutputM3ua
	 * @param runVLC
	 */
	public static void ParInterpretation(String videoGenFile, String fileNameOutputM3ua, boolean runVLC) {
		StringBuffer dataToFile = new StringBuffer("#EXTM3U \n");
		assert(VideoGenToolSuite.checkVideoGenSpecifications(videoGenFile));

		// loading
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(videoGenFile));
		assertNotNull(videoGen);
		// System.out.println("nb de vidéos" + videoGen.getMedias().size());

		// Model Management (Analysis and create)
		for (Media videoseq : videoGen.getMedias()) {
			if (videoseq instanceof MandatoryVideoSeq) {
				MandatoryVideoSeq desc = (MandatoryVideoSeq) videoseq;
				// System.out.println("add mandatory " + desc.getDescription().getVideoid());
				dataToFile.append(desc.getDescription().getLocation() + "\r\n");

			} else if (videoseq instanceof OptionalVideoSeq) {
				OptionalVideoSeq desc = (OptionalVideoSeq) videoseq;
				if (getRandomOptional(desc.getDescription().getProbability())) {
					// System.out.println("add optional " + desc.getDescription().getVideoid());
					dataToFile.append("file " + "\'" + desc.getDescription().getLocation() + "\'\r\n");
				}
			} else {
				AlternativeVideoSeq altvid = (AlternativeVideoSeq) videoseq;

				List<VideoDescription> videosList = altvid.getVideodescs();
				List<Integer> probs = new ArrayList<>();

				for (VideoDescription videoAlt : videosList)
					probs.add(videoAlt.getProbability());
				int r = getRandomAlternative(probs);

				// System.out.println("add alternative " + videosList.get(r).getVideoid());
				dataToFile.append(videosList.get(r).getLocation() + "\r\n");
			}
		}

		// save file m3u
		writeFile(fileNameOutputM3ua, dataToFile.toString());

		// Execute VLC
		if (runVLC) {
			try {
				String[] commande = { "vlc", fileNameOutputM3ua };
				Process p = Runtime.getRuntime().exec(commande);
				p.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Return random boolean with probability, if prob = 0 prob -> 50%
	 * 
	 * @return random boolean
	 */
	private static boolean getRandomOptional(int probability) {
		assert (probability >= 0 && probability <= 100);
		if (probability == 0) {
			Random random = new Random();
			return random.nextBoolean();
		}
		Random random = new Random();
		int r = random.nextInt(100);
		if (r <= probability)
			return true;
		return false;
	};

	/**
	 * Return integer
	 * 
	 * @param nbVideos, number of random videos
	 * @return video number (random)
	 */
	private static int getRandomAlternativeVideo(int nbVideos) {
		Random random = new Random();
		return random.nextInt(nbVideos);
	}

	/**
	 * Return random index of the array (with probability)
	 * 
	 * @param probs
	 * @return index of the array (with probability)
	 */
	private static int getRandomAlternative(List<Integer> probs) {
		List<Integer> probsSum = new ArrayList<Integer>();
		int count = 0;
		for (int i = 0; i < probs.size(); i++) {
			probsSum.add(probs.get(i) + count);
			count = probsSum.get(i);
		}
		assert (count <= 100);

		if (count == 0)
			return getRandomAlternativeVideo(probs.size() - 1);

		Random random = new Random();
		int r = random.nextInt(count);

		for (int i = 0; i < probs.size(); i++)
			if (r <= probsSum.get(i))
				return i;
		return 0;
	}

	/**
	 * Write files (Java 1.7 and so on...), internally uses OutputStream
	 * 
	 * @param filename
	 * @param data
	 */
	protected static void writeFile(String filename, String data) {
		try {
			Files.write(Paths.get(filename), data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * TP2, Question 2. Transformation par compilation
	 * 
	 * @param videoGenFile
	 * @param outputVideo
	 * @param runVLC
	 */
	public static void ParCompilation(String videoGenFile, String outputVideo, boolean runVLC) {
		assert(VideoGenToolSuite.checkVideoGenSpecifications(videoGenFile));
		String videoList = "VideoList.txt";
		// String outputVideo = "ressources/videos/outputVideo.mp4";
		StringBuffer dataToFile = new StringBuffer();

		// loading
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(videoGenFile));
		assertNotNull(videoGen);

		// Model Management (Analysis and create )
		for (Media videoseq : videoGen.getMedias()) {
			if (videoseq instanceof MandatoryVideoSeq) {
				MandatoryVideoSeq desc = (MandatoryVideoSeq) videoseq;
				// System.out.println("add mandatory " + desc.getDescription().getVideoid());
				dataToFile.append("file " + "\'" + desc.getDescription().getLocation() + "\'\r\n");

			} else if (videoseq instanceof OptionalVideoSeq) {
				OptionalVideoSeq desc = (OptionalVideoSeq) videoseq;
				if (getRandomOptional(desc.getDescription().getProbability())) {
					// System.out.println("add optional " + desc.getDescription().getVideoid());
					dataToFile.append("file " + "\'" + desc.getDescription().getLocation() + "\'\r\n");
				}

			} else {
				AlternativeVideoSeq altvid = (AlternativeVideoSeq) videoseq;

				List<VideoDescription> videosList = altvid.getVideodescs();
				List<Integer> probs = new ArrayList<>();

				for (VideoDescription videoAlt : videosList)
					probs.add(videoAlt.getProbability());
				int r = getRandomAlternative(probs);

				// System.out.println("add alternative " + videosList.get(r).getVideoid());
				dataToFile.append("file " + "\'" + videosList.get(r).getLocation() + "\'\r\n");
			}
		}

		// save file txt
		writeFile(videoList, dataToFile.toString());

		// Video concatenation
		if (FfmpegSystem.videoConcatenation(videoList, outputVideo)) {
			// System.out.println("video concatenated");
			// Execute VLC
			if (runVLC)
				runVLC(outputVideo);
		} else {
			System.out.println("Error concatenating videos");
		}

	}

	/**
	 * VLC execution
	 * 
	 * @param videoName
	 */
	private static void runVLC(String videoName) {
		try {
			String[] commande = { "vlc", videoName };
			System.out.println("Running VLC");
			Process p = Runtime.getRuntime().exec(commande);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TP2, Question 3. Transformation de la variante qui a la plus longue durée
	 * 
	 * @param videoGenFile
	 * @param outputVideoName
	 * @param runVLC
	 */
	public static void varianteLaPlusLongueDuree(String videoGenFile, String outputVideoName, boolean runVLC) {
		String videoList = "VideoList.txt";
		// String outputVideo = "ressources/videos/outputVideo.mp4";
		StringBuffer dataToFile = new StringBuffer();

		// loading
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(videoGenFile));
		assertNotNull(videoGen);

		// Model Management (Analysis and create )
		for (Media videoseq : videoGen.getMedias()) {
			if (videoseq instanceof MandatoryVideoSeq) {
				MandatoryVideoSeq desc = (MandatoryVideoSeq) videoseq;
				// System.out.println("add mandatory " + desc.getDescription().getVideoid());
				dataToFile.append("file " + "\'" + desc.getDescription().getLocation() + "\'\r\n");

			} else if (videoseq instanceof OptionalVideoSeq) {
				OptionalVideoSeq desc = (OptionalVideoSeq) videoseq;
				// System.out.println("add optional " + desc.getDescription().getVideoid());
				dataToFile.append("file " + "\'" + desc.getDescription().getLocation() + "\'\r\n");

			} else {
				AlternativeVideoSeq altvid = (AlternativeVideoSeq) videoseq;
				Double maxDuration = 0.0;

				List<VideoDescription> videosList = altvid.getVideodescs();

				int indexVideoAltControl = 0;
				int indexVideoAlt = 0;
				for (VideoDescription videoAlt : videosList) {

					String location = videoAlt.getLocation();

					try {
						// Check video duration
						String[] commande = { "ffprobe", "-v", "error", "-show_entries", "format=duration", "-of",
								"default=noprint_wrappers=1:nokey=1", location };

						Process p = Runtime.getRuntime().exec(commande);
						p.waitFor();
						// Get value returned by command
						BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						double dur = Double.parseDouble(stdInput.readLine());
						// while ((s = stdInput.readLine()) != null) {
						// System.out.println("Calculating video duration: " + dur);
						if (dur > maxDuration) {
							indexVideoAlt = indexVideoAltControl;
							maxDuration = dur;
						}
						indexVideoAltControl++;
						// }
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
				// System.out.println(indexVideoAlt);
				// System.out.println("add alternative " + videosList.get(indexVideoAlt).getVideoid());
				dataToFile.append("file " + "\'" + videosList.get(indexVideoAlt).getLocation() + "\'\r\n");
			}
		}

		// save video list in a file
		writeFile(videoList, dataToFile.toString());

		// Video concatenation
		if (FfmpegSystem.videoConcatenation(videoList, outputVideoName)) {
			// System.out.println("video concatenated");
			// Execute VLC
			if (runVLC)
				runVLC(outputVideoName);
		} else {
			System.out.println("Error concatenating videos");
		}
	}

	/**
	 * TP2, Question 4. Creation des vignettes
	 * 
	 * @param videoGenFile
	 * @param dirOutput
	 */
	public static void generateurDeVignettes(String videoGenFile, String dirOutput) {
		assert(VideoGenToolSuite.checkVideoGenSpecifications(videoGenFile));
		Boolean fileExists = new File(videoGenFile).isFile();
		assertTrue("File not found: " + videoGenFile, fileExists);
		
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(videoGenFile));
		assertNotNull(videoGen);

		// Model Management (Analysis and images creation)
		for (Media videoseq : videoGen.getMedias()) {
			if (videoseq instanceof MandatoryVideoSeq) {
				MandatoryVideoSeq desc = (MandatoryVideoSeq) videoseq;
				if (!FfmpegSystem.imageCreation(desc.getDescription().getVideoid(), desc.getDescription().getLocation(),
						dirOutput))
					System.err.println("Error creating image of the video");

			} else if (videoseq instanceof OptionalVideoSeq) {
				OptionalVideoSeq desc = (OptionalVideoSeq) videoseq;
				if (!FfmpegSystem.imageCreation(desc.getDescription().getVideoid(), desc.getDescription().getLocation(),
						dirOutput))
					System.err.println("Error creating image of the video");

			} else {
				AlternativeVideoSeq altvid = (AlternativeVideoSeq) videoseq;
				List<VideoDescription> videosList = altvid.getVideodescs();

				for (VideoDescription videoAlt : videosList) {
					if (!FfmpegSystem.imageCreation(videoAlt.getVideoid(), videoAlt.getLocation(), dirOutput))
						System.err.println("Error creating image of the video");
				}
			}
		}
	}

	/**
	 * Get videos' location
	 * 
	 * @param videoGenFile
	 * @return
	 */
	public static ArrayList<ArrayList<String>> getImagesList(String videoGenFile) {
		ArrayList<ArrayList<String>> seqImages = new ArrayList<>();

		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(videoGenFile));
		assertNotNull(videoGen);

		// Model Management (Analysis and list creation)
		for (Media videoseq : videoGen.getMedias()) {
			if (videoseq instanceof MandatoryVideoSeq) {
				MandatoryVideoSeq desc = (MandatoryVideoSeq) videoseq;
				ArrayList<String> vid = new ArrayList<>();
				vid.add("mandatory");
				vid.add(desc.getDescription().getVideoid());
				seqImages.add(vid);

			} else if (videoseq instanceof OptionalVideoSeq) {
				OptionalVideoSeq desc = (OptionalVideoSeq) videoseq;
				ArrayList<String> vid = new ArrayList<>();
				vid.add("optional");
				vid.add(desc.getDescription().getVideoid());
				seqImages.add(vid);

			} else {
				AlternativeVideoSeq altvid = (AlternativeVideoSeq) videoseq;
				List<VideoDescription> videosList = altvid.getVideodescs();
				ArrayList<String> vid = new ArrayList<>();
				vid.add("alternative");
				for (VideoDescription videoAlt : videosList) {
					vid.add(videoAlt.getVideoid());
				}
				seqImages.add(vid);
			}
		}
		// System.out.println(seqImages);
		return seqImages;

	}

	/**
	 * Convert VideoGen parameter to list
	 * 
	 * @param videoGenFile
	 * @return list
	 */
	public static List<Object> videoGen2List(String videoGenFile) {
		List<Object> list = new ArrayList<>();

		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(videoGenFile));
		assertNotNull(videoGen);

		// Model Management (Analysis and list creation)
		for (Media videoseq : videoGen.getMedias()) {
			if (videoseq instanceof MandatoryVideoSeq) {
				MandatoryVideoSeq desc = (MandatoryVideoSeq) videoseq;
				Map<String, Object> map = new HashMap<>();
				map.put("type", "mandatory");
				map.put("id", desc.getDescription().getVideoid());
				map.put("video", desc.getDescription().getLocation());
				map.put("image", "img/" + desc.getDescription().getVideoid() + ".png");
				map.put("gif", "gif/" + desc.getDescription().getVideoid() + ".gif");
				map.put("description", desc.getDescription().getDescription());
				map.put("duration", desc.getDescription().getDuration());
				map.put("probability", desc.getDescription().getProbability());
				map.put("text", desc.getDescription().getText());
				map.put("filter", desc.getDescription().getFilter());
				list.add(map);

			} else if (videoseq instanceof OptionalVideoSeq) {
				OptionalVideoSeq desc = (OptionalVideoSeq) videoseq;
				Map<String, Object> map = new HashMap<>();
				map.put("type", "optional");
				map.put("id", desc.getDescription().getVideoid());
				map.put("video", desc.getDescription().getLocation());
				map.put("image", "img/" + desc.getDescription().getVideoid() + ".png");
				map.put("gif", "gif/" + desc.getDescription().getVideoid() + ".gif");
				map.put("description", desc.getDescription().getDescription());
				map.put("duration", desc.getDescription().getDuration());
				map.put("probability", desc.getDescription().getProbability());
				map.put("text", desc.getDescription().getText());
				map.put("filter", desc.getDescription().getFilter());
				list.add(map);

			} else {
				AlternativeVideoSeq altvid = (AlternativeVideoSeq) videoseq;
				Map<String, Object> map = new HashMap<>();
				// map.put("type", "alternative");

				List<VideoDescription> videosList = altvid.getVideodescs();
				int idx = 0;
				for (VideoDescription videoAlt : videosList) {
					Map<String, Object> mapA = new HashMap<>();
					mapA.put("type", "alternative");
					mapA.put("id", videoAlt.getVideoid());
					mapA.put("video", videoAlt.getLocation());
					mapA.put("image", "img/" + videoAlt.getVideoid() + ".png");
					mapA.put("gif", "gif/" + videoAlt.getVideoid() + ".gif");
					mapA.put("description", videoAlt.getDescription());
					mapA.put("duration", videoAlt.getDuration());
					mapA.put("probability", videoAlt.getProbability());
					mapA.put("text", videoAlt.getText());
					mapA.put("filter", videoAlt.getFilter());

					map.put(String.valueOf(idx), mapA);
					idx++;
				}
				list.add(map);
			}
		}
		return list;

	}

	/**
	 * Video concatenation customized
	 * 
	 * @param videoList
	 * @param outputVideo
	 * @return true if successful
	 */
	public static boolean concatenationCustomized(List<String> videoList, String dirVideos, String outputVideo) {
		String fileList = "VideoList.txt";
		StringBuffer dataToFile = new StringBuffer();
		for (String video : videoList) {
			dataToFile.append("file \'" + dirVideos + video + ".mp4\'\r\n");
		}
		// System.out.println(dataToFile);

		writeFile(fileList, dataToFile.toString());

		// Video concatenation
		if (FfmpegSystem.videoConcatenation(fileList, outputVideo)) {
			// System.out.println("video concatenated");
			return true;
		} else {
			// System.out.println("Error concatenating video");
			return false;
		}
	}

	/* ================================ TP3 ================================ */

	/* ================================ TP4 ================================ */

	/**
	 * TP4, Q1. Nombre de variantes de modèle VideoGen
	 * 
	 * @param videoGenFile
	 * @return
	 */
	public static int nbVariantes(String videoGenFile) {
		int nbVariantes = 0;

		// loading
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(videoGenFile));
		assertNotNull(videoGen);
		if (videoGen.getMedias().size() > 0)
			nbVariantes = 1;

		// Model Management (Analysis and counting)
		for (Media videoseq : videoGen.getMedias()) {
			if (videoseq instanceof OptionalVideoSeq) {
				nbVariantes = nbVariantes * 2;

			} else if (videoseq instanceof AlternativeVideoSeq) {
				AlternativeVideoSeq altvid = (AlternativeVideoSeq) videoseq;

				List<VideoDescription> videosList = altvid.getVideodescs();
				nbVariantes = nbVariantes * videosList.size();
			}
		}
		return nbVariantes;
	}

	/**
	 * TP4, 2. Vérification des spécifications VideoGen
	 * 
	 * @param videoGenFile
	 * @return true if there are no error in a VideoGen specification
	 */
	public static boolean checkVideoGenSpecifications(String videoGenFile) {

		List<String> idSeq = new ArrayList<String>();

		// loading
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(videoGenFile));
		assertNotNull(videoGen);
		if (videoGen.getMedias().size() == 0)
			return false;

		// Model Management (Analysis and counting)
		for (Media videoseq : videoGen.getMedias()) {
			if (videoseq instanceof MandatoryVideoSeq) {
				MandatoryVideoSeq desc = (MandatoryVideoSeq) videoseq;

				// Check if file exists
				Boolean fileExists = new File(desc.getDescription().getLocation()).isFile();
				assertTrue("File not found: " + desc.getDescription().getLocation(), fileExists);
				if (!fileExists)
					return false;

				idSeq.add(desc.getDescription().getVideoid());

			} else if (videoseq instanceof OptionalVideoSeq) {
				OptionalVideoSeq desc = (OptionalVideoSeq) videoseq;

				// Check if file exists
				Boolean fileExists = new File(desc.getDescription().getLocation()).isFile();
				assertTrue("File not found: " + desc.getDescription().getLocation(), fileExists);
				if (!fileExists)
					return false;

				idSeq.add(desc.getDescription().getVideoid());

				// Check probability
				assertTrue("Probability > 100", desc.getDescription().getProbability() <= 100);
				if (desc.getDescription().getProbability() > 100)
					return false;

			} else if (videoseq instanceof AlternativeVideoSeq) {
				AlternativeVideoSeq altvid = (AlternativeVideoSeq) videoseq;
				List<VideoDescription> videosList = altvid.getVideodescs();
				int probability = 0;

				for (VideoDescription videoAlt : videosList) {

					// Check if file exists
					Boolean fileExists = new File(videoAlt.getLocation()).isFile();
					assertTrue("File not found: " + videoAlt.getLocation(), fileExists);
					if (!fileExists)
						return false;

					idSeq.add(videoAlt.getVideoid());
					probability += videoAlt.getProbability();

				}
				assertTrue("Probability > 100", probability <= 100);
				if (probability > 100)
					return false;
			}
		}

		// Check if duplicates
		assertFalse("There are duplcates", findDuplicates(idSeq));
		if (findDuplicates(idSeq))
			return false;

		return true;
	}

	/**
	 * Find duplicates
	 * 
	 * @param listContainingDuplicates
	 * @return true if duplicates
	 */
	public static boolean findDuplicates(List<String> listContainingDuplicates) {
		final Set<String> set = new HashSet<String>();
		for (String yourInt : listContainingDuplicates)
			if (!set.add(yourInt))
				return true;
		return false;
	}

}
