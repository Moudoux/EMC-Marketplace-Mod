package emc.marketplace.modinstaller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Utils for http traffic
 * 
 * @author Deftware
 *
 */
public class Web {

	/**
	 * The user agent used for all the web traffic
	 */
	private static final String userAgent = "EMC-Marketplace";

	/**
	 * Sends a GET request to a given url
	 * 
	 * @param uri
	 * @return String
	 */
	public static String get(String uri) throws Exception {
		URL url = new URL(uri);
		Object connection = (uri.startsWith("https://") ? (HttpsURLConnection) url.openConnection()
				: (HttpURLConnection) url.openConnection());
		((URLConnection) connection).setConnectTimeout(8 * 1000);
		((URLConnection) connection).setRequestProperty("User-Agent", userAgent);
		((HttpURLConnection) connection).setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(((URLConnection) connection).getInputStream()));
		String text;
		String result = "";
		while ((text = in.readLine()) != null) {
			result = result + text;
		}
		in.close();
		return result;
	}

	/**
	 * Sends a POST request to a given url, with JSON data as payload
	 *
	 * @return String
	 */
	public static String post(String uri, HashMap<String, String> payload) throws Exception {
		URL url = new URL(uri);
		Object connection = (uri.startsWith("https://") ? (HttpsURLConnection) url.openConnection()
				: (HttpURLConnection) url.openConnection());
		((URLConnection) connection).setConnectTimeout(8 * 1000);
		((URLConnection) connection).setRequestProperty("User-Agent", userAgent);

		((URLConnection) connection).setDoInput(true);
		((URLConnection) connection).setDoOutput(true);
		((HttpURLConnection) connection).setRequestMethod("POST");
		((URLConnection) connection).setRequestProperty("Accept", "application/json");
		((URLConnection) connection).setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		OutputStreamWriter writer = new OutputStreamWriter(((URLConnection) connection).getOutputStream(), "UTF-8");
		writer.write(mapToJson(payload));
		writer.close();
		BufferedReader br = new BufferedReader(new InputStreamReader(((URLConnection) connection).getInputStream()));
		StringBuffer data = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			data.append(line);
		}
		br.close();
		((HttpURLConnection) connection).disconnect();
		return data.toString();
	}

	/**
	 * Converts a HashMap to a JSON string
	 * 
	 * @param map
	 * @return String
	 */
	private static String mapToJson(HashMap<String, String> map) {
		String json = "{";
		for (String key : map.keySet()) {
			json += "\"" + key + "\":\"" + map.get(key) + "\",";
		}
		return json.substring(0, json.length() - 1) + "}";
	}

}
