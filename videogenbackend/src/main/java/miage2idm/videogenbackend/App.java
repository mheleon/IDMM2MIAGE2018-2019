package miage2idm.videogenbackend;

import static spark.Spark.*;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import vgts.*;

/**
 * 
 * IDM M2 MIAGE 2018-2019 | Projet - VideoGenToolSuite Back-end
 * 
 * @author HERNANDEZ Maykol, ELMAIZI Sohaib
 * @version 1.1
 * @since 2018-11-20
 * 
 */

public class App {

	public static void main(String[] args) {

		final String VIDEOGENFILE = "file.videogen";
		Gson gson = new Gson();

		// Configure Spark
		port(8080);
		String projectDir = System.getProperty("user.dir");
		String staticDir = "/src/main/resources/public";
		staticFiles.externalLocation(projectDir + staticDir);

		// Call this before mapping thy routes
		App.apply();

		// status-test route
		get("/status", (req, res) -> "Everything is ok Mako");

		get("/api/v1/VideoGenList", (req, res) -> {
			assert(VideoGenToolSuite.checkVideoGenSpecifications(VIDEOGENFILE));
			res.type("application/json");
			return VideoGenToolSuite.videoGen2List(VIDEOGENFILE);
		}, gson::toJson);

		// Random mode
		post("/api/v1/random", (req, res) -> {
			assert(VideoGenToolSuite.checkVideoGenSpecifications(VIDEOGENFILE));
			res.type("application/json");
			if (!VideoGenToolSuite.checkVideoGenSpecifications(VIDEOGENFILE))
				return "{\"message\":\"Custom 500 handling\"}";
			VideoGenToolSuite.ParCompilation(VIDEOGENFILE, "src/main/resources/public/video/output/output.mp4", false);
			if (FfmpegSystem.convertToGif("src/main/resources/public/video/output/output.mp4",
					"src/main/resources/public/gif/output.gif"))
				return "{\"message\":\"successful\", \"video\":\"video/output/output.mp4\", \"gif\":\"video/gif/output.gif\"}";
			
			return "{\"message\":\"error\"}";
		});

		// Configurator mode
		post("/api/v1/conf", (req, res) -> {
			assert(VideoGenToolSuite.checkVideoGenSpecifications(VIDEOGENFILE));
			String dataPost = req.body().replace("\"", "").replace("[", "").replace("]", "");
			List<String> videoList = new ArrayList<String>(Arrays.asList(dataPost.split(",")));

			res.type("application/json");

			if (VideoGenToolSuite.concatenationCustomized(videoList, "src/main/resources/public/video/",
					"src/main/resources/public/video/output/output.mp4"))
				if (FfmpegSystem.convertToGif("src/main/resources/public/video/output/output.mp4",
						"src/main/resources/public/gif/output.gif"))
					return "{\"message\":\"successful\", \"video\":\"video/output/output.mp4\", \"gif\":\"video/gif/output.gif\"}";
			return "{\"message\":\"error\"}";
		});

		// Create gif
		get("/api/v1/createGif", (req, res) -> {
			FfmpegSystem.convertToGif("src/main/resources/public/video/output/output.mp4",
					"src/main/resources/public/gif/output.gif");
			return "{\"message\":\"successful\", \"location\":\"gif/output.gif\"}";
		});

		// Get input video
		get("/readVideo/:video", (req, res) -> {

			return "<video width=\"400\" controls>\n" + "  <source src=\"/video/" + req.params(":video")
					+ "\" type=\"video/mp4\">\n" + "  Your browser does not support HTML5 video.\n" + "</video>";
		});

		// Get output video
		get("/readVideo/output/:video", (req, res) -> {

			return "<video width=\"400\" controls>\n" + "  <source src=\"/video/output/" + req.params(":video")
					+ "\" type=\"video/mp4\">\n" + "  Your browser does not support HTML5 video.\n" + "</video>";
		});

		// redirect to default
		redirect.get("/", "/api/v1/VideoGenList");

		// Using Route - Not found (code 404) handling
		notFound((req, res) -> {
			res.type("application/json");
			return "{\"message\":\"Not found 404\"}";
		});
	}

	
	private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();

	/**
	 * Method to solve CORS
	 */
	static {
		corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
		corsHeaders.put("Access-Control-Allow-Origin", "*");
		corsHeaders.put("Access-Control-Allow-Headers",
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
		corsHeaders.put("Access-Control-Allow-Credentials", "true");
	}

	/**
	 * Method to solve CORS
	 */
	public final static void apply() {
		Filter filter = new Filter() {
			@Override
			public void handle(Request request, Response response) throws Exception {
				corsHeaders.forEach((key, value) -> {
					response.header(key, value);
				});
			}
		};
		after(filter);
	}
}
