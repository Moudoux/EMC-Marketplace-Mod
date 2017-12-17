package emc.marketplace.modinstaller;

import com.google.gson.Gson;

/**
 * Handles communication between this plugin and the backend
 * 
 * @author Deftware
 *
 */
public class API {

	private static final String endpoint = "https://emc-api.sky-net.me/";

	/**
	 * All endpoints
	 *
	 */
	public static enum Types {

		ListProducts("listproducts");

		String url;

		Types(String url) {
			this.url = url;
		}

	}

	/**
	 * Fetches a given endpoint and returns the data as a string
	 * 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static String fetchEndpoint(Types type) throws Exception {
		String url = endpoint + type.url;
		return Web.get(url);
	}

	/**
	 * Returns an array of all mods
	 * 
	 * @return
	 */
	public static Mod[] fetchMods() {
		try {
			return new Gson().fromJson(fetchEndpoint(Types.ListProducts), Mod[].class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Mod[0];
	}

}
