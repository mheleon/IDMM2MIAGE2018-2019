package vgts;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

/**
 * 
 * IDM M2 MIAGE 2018-2019 (VideoGenToolSuite) - Class to manage all ffmpeg system calls
 * 
 * @author HERNANDEZ Maykol, ELMAIZI Sohaib
 * @version 1.1
 * @since 2018-11-20
 * 
 */

public class FfmpegSystem {

	/* ================================ TP2 ================================ */

	/**
	 * Concatenation of videos with ffmpeg
	 * 
	 * @param videoList
	 * @param outputVideo
	 * @return true if successful
	 */
	protected static boolean videoConcatenation(String videoList, String outputVideo) {
		try {
			String[] commande = { "ffmpeg", "-y", "-f", "concat", "-i", videoList, "-c", "copy", outputVideo };
			Process p = Runtime.getRuntime().exec(commande);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		}
		return true;
	}

	/**
	 * Image creation of the video with ffmpeg
	 * 
	 * @param id            of the video
	 * @param videoFileName
	 * @param dirOutput
	 * @return true if successful
	 */
	protected static boolean imageCreation(String id, String videoFileName, String dirOutput) {
		try {
			assertTrue("File not found: " + videoFileName, new File(videoFileName).isFile());
			assertTrue("Folder not found: " + dirOutput, new File(dirOutput).isDirectory());
			String[] commande = { "ffmpeg", "-y", "-i", videoFileName, "-r", "1", "-t", "00:00:01", "-ss", "00:00:02",
					"-f", "image2", dirOutput + id + ".png" };
			Process p = Runtime.getRuntime().exec(commande);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		}
		// System.out.println("Image created from " + id + " " + videoFileName);
		return true;
	}

	/* ================================ TP3 ================================ */

	/**
	 * Gif creation from video
	 * 
	 * @param inputVideo
	 * @param outputGif
	 * @return true if successful
	 */
	public static boolean convertToGif(String inputVideo, String outputGif) {

		// String start = "1";
		// String duration = "2";

		try {
			assertTrue("File not found: " + inputVideo, new File(inputVideo).isFile());
			// String[] commande = { "ffmpeg", "-y", "-i", inputVideo, "-ss", start, "-t", duration, outputGif };
			 String[] commande = { "ffmpeg", "-y", "-i", inputVideo, outputGif };
			Process p = Runtime.getRuntime().exec(commande);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println(e);
			return false;
		}
		return true;
	}

	/* ================================ TP4 ================================ */

	/**
	 * Traitements (filtres) sur les vidéos
	 * 
	 * @param inputVideoFile
	 * @param outputVideoFile
	 * @param filter (FlipFilterVertical | FlipFilterHorizontal | NegateFilter | BlackWhiteFilter)
	 * @return true if successful
	 */
	public static boolean filterEffectVideo(String inputVideoFile, String outputVideoFile, String filter) {
		assertTrue("File not found: " + inputVideoFile, new File(inputVideoFile).isFile());

		String[] commande;
		switch (filter) {
		case "FlipFilterVertical":
			String[] ffv = { "ffmpeg", "-y", "-i", inputVideoFile, "-vf", "vflip", outputVideoFile };
			commande = ffv;
			break;
		case "FlipFilterHorizontal":
			String[] ffh = { "ffmpeg", "-y", "-i", inputVideoFile, "-vf", "hflip", outputVideoFile };
			commande = ffh;
			break;
		case "NegateFilter":
			String[] nf = { "ffmpeg", "-y", "-i", inputVideoFile, "-vf", "negate", outputVideoFile };
			commande = nf;
			break;
		case "BlackWhiteFilter":
			String[] bw = { "ffmpeg", "-y", "-i", inputVideoFile, "-vf", "hue=s=0", "-c:a", "copy", outputVideoFile };
			commande = bw;
			break;
		default:
			assertTrue("Request not expected: " + filter,
					filter == "FlipFilter" || filter == "NegateFilter" || filter == "BlackWhiteFilter");
			return false;
		}

		try {
			Process p = Runtime.getRuntime().exec(commande);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Add test into video
	 * 
	 * @param inputVideoFile
	 * @param outputVideoFile
	 * @param content
	 * @param position (TOP | BOTTOM | CENTER)
	 * @param color
	 * @param size integer
	 * @return true if successful
	 */
	public static boolean textIntoVideo(String inputVideoFile, String outputVideoFile, String content, String position,
			String color, int size) {
		String pos = "";
		switch (position) {
		case "TOP":
			pos = "x=(w-text_w)/2: y=10";
			break;
		case "BOTTOM":
			pos = "x=(w-text_w)/2: y=(h-text_h)-10";
			break;
		case "CENTER":
			pos = "x=(w-text_w)/2: y=(h-text_h)/2";
			break;
		default:
			assertTrue("Request not expected: " + position,
					position == "TOP" || position == "BOTTOM" || position == "CENTER");
			return false;
		}

		assertTrue("File not found: " + inputVideoFile, new File(inputVideoFile).isFile());

		String[] commande = { "ffmpeg", "-i", inputVideoFile, "-vf",
				"drawtext=\"fontfile=/path/to/font.ttf: text=\'" + content + "\': fontcolor=" + color + ": fontsize="
						+ size + ": box=0: boxcolor=black@0.5: boxborderw=5: " + pos,
				"-codec:a", "copy", outputVideoFile };

		try {
			Process p = Runtime.getRuntime().exec(commande);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
