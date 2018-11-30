package vgts;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * IDM M2 MIAGE 2018-2019 (VideoGenToolSuite) - Class to manage VideoGen analysis
 * 
 * @author HERNANDEZ Maykol, ELMAIZI Sohaib
 * @version 1.1
 * @since 2018-11-20
 * 
 */

public class VideoAnalysisTools {
	
	/* ================================ TP3 ================================ */

	static StringBuffer dataToFile = new StringBuffer();
	static int idCount = 1;

	/**
	 * Q1, Q2 - Taille des variantes de vidéo, La bonne taille
	 * 
	 * @param videoGenFile
	 * @return true if successful
	 */
	public static boolean analyseVideoGen(String videoGenFile) {

		List<String> gen = new ArrayList<String>();
		List<String> idVideo = new ArrayList<String>();
		List<String> locVideo = new ArrayList<String>();
		dataToFile.delete(0, dataToFile.length());
		dataToFile.append("id,");

		// loading
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(videoGenFile));
		assertNotNull(videoGen);
		if (videoGen.getMedias().size() == 0)
			return false;

		// Model Management (Analysis and counting)
		for (Media videoseq : videoGen.getMedias()) {
			if (videoseq instanceof MandatoryVideoSeq) {
				gen.add("m");
				MandatoryVideoSeq desc = (MandatoryVideoSeq) videoseq;
				idVideo.add(desc.getDescription().getVideoid());
				locVideo.add(desc.getDescription().getLocation());
				dataToFile.append(desc.getDescription().getVideoid() + ",");

			} else if (videoseq instanceof OptionalVideoSeq) {
				gen.add("o");
				OptionalVideoSeq desc = (OptionalVideoSeq) videoseq;
				idVideo.add(desc.getDescription().getVideoid());
				locVideo.add(desc.getDescription().getLocation());
				dataToFile.append(desc.getDescription().getVideoid() + ",");

			} else if (videoseq instanceof AlternativeVideoSeq) {
				AlternativeVideoSeq altvid = (AlternativeVideoSeq) videoseq;
				List<VideoDescription> videosList = altvid.getVideodescs();
				for (VideoDescription videoAlt : videosList) {
					gen.add("a");
					idVideo.add(videoAlt.getVideoid());
					locVideo.add(videoAlt.getLocation());
					dataToFile.append(videoAlt.getVideoid() + ",");
				}
			}
		}

		dataToFile.append("size,realSize\r\n");

		createFileCSV(gen, idVideo, locVideo, 0, "");
		idCount = 1;

		VideoGenToolSuite.writeFile("csv.csv", dataToFile.toString());

		return true;
	}

	/**
	 * Create CSV file
	 * 
	 * @param gen
	 * @param idVideo
	 * @param locVideo
	 * @param rect
	 * @param csv
	 * @return CSV file content
	 */
	private static String createFileCSV(List<String> gen, List<String> idVideo, List<String> locVideo, int rect,
			String csv) {
		String comma = ",";
		if (rect >= gen.size()) {
			long size = 0;
			String[] parts = csv.split(",");
			StringBuffer myList = new StringBuffer();
			
			for (int i = 0; i < idVideo.size(); i++)
				if (parts[i].equals("true")) {
					size += getFileSize(locVideo.get(i));
					myList.append("file '" + locVideo.get(i) + "'\r\n");
				}
			// TODO get realsize
			// System.out.println(myList.toString());
			VideoGenToolSuite.writeFile("VideoList.txt", myList.toString());
			// TODO verificar donde colocar los archivos
			FfmpegSystem.videoConcatenation("VideoList.txt", "ressources/videoRealSize.mp4");
			long realSize = getFileSize("ressources/videoRealSize.mp4");
			
			dataToFile.append(idCount++ + "," + csv + "," + size + "," + realSize + "\r\n");
			return csv;
		} else if (rect >= gen.size() - 1) {
			comma = "";
		}
		// System.out.println(rect);
		switch (gen.get(rect)) {
		case "m":
			// System.out.println(csv + idVideo.get(rect) + "=true ");
			createFileCSV(gen, idVideo, locVideo, rect + 1, csv + "true" + comma);
			break;
		case "o":
			// System.out.println(csv + idVideo.get(rect) + "=true ");
			createFileCSV(gen, idVideo, locVideo, rect + 1, csv + "true" + comma);

			// System.out.println(csv + idVideo.get(rect) + "=false ");
			createFileCSV(gen, idVideo, locVideo, rect + 1, csv + "false" + comma);
			break;
		case "a":
			// System.out.println("+");
			int count = rect;
			while ((count + 1) < gen.size() && gen.get(count + 1) == "a")
				count++;
			count = count + 1 - rect;
			// System.out.println("count=" + (count + 1 - rect));
			String[] altArray = new String[count];
			Arrays.fill(altArray, "false");
			// System.out.println(Arrays.toString(altArray));
			for (int i = 0; i < count; i++) {
				altArray[i] = "true";
				String csvAlt = csv;
				for (int j = 0; j < altArray.length; j++) {
					if (rect + j >= gen.size() - 1) {
						System.out.println(rect + j);
						comma = "";
					}
					csvAlt = csvAlt + altArray[j] + comma;
					comma = ",";
				}
				Arrays.fill(altArray, "false");
				// System.out.println(csvAlt);
				createFileCSV(gen, idVideo, locVideo, rect + count, csvAlt);

			}
			break;
		}
		return csv;
	}

	/**
	 * Get file size
	 * 
	 * @param fileName
	 * @return file size
	 */
	protected static long getFileSize(String fileName) {
		File file = new File(fileName);
		return file.length();
	}

}
