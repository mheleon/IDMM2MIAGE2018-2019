package miage2idm.videogenbackend;

import static spark.Spark.get;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public static void testBackEnd() {

		// JSON with ids
//		List<String> videoList = new ArrayList<String>();
//		videoList.add("v1");
//		videoList.add("v2");
//		videoList.add("v32");
//		videoList.add("v4");
//		System.out.println(videoList);
//		List<String> list = new Gson().fromJson(req.body(), List.class);

//    get("/api/v1/VideoGenListTest", (req, res) -> {
//		List<Object> list = new ArrayList<>();
//
//		Map<String, Object> map = new HashMap<>();
//		map.put("type", "mandatory");
//		map.put("id", "v1");
//		map.put("video", "video/Simsons1.mp4");
//		map.put("image", "img/Simsons1.png");
//		list.add(map);
//
//		Map<String, Object> map1 = new HashMap<>();
//		map1.put("type", "optionnel");
//		map1.put("id", "v2");
//		map1.put("localisation", "img/Simsons2.mp4");
//		list.add(map1);
//
//		Map<String, Object> map2 = new HashMap<>();
//		map2.put("type", "alternative");
//		Map<String, Object> map3 = new HashMap<>();
//		map3.put("type", "alternative");
//		map3.put("id", "v31");
//		map3.put("localisation", "img/Simsons3.mp4");
//		map2.put("0", map3);
//		Map<String, Object> map4 = new HashMap<>();
//		map4.put("type", "alternative");
//		map4.put("id", "v32");
//		map4.put("localisation", "img/Simsons4.mp4");
//		map4.put("1", map3);
//		list.add(map2);
//
//		res.type("application/json");
//		return list;
//	}, gson::toJson);

		assert (true);
	}
}
