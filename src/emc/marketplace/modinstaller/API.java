package emc.marketplace.modinstaller;

import com.google.gson.Gson;

import lombok.Getter;

/**
 * Handles communication between this plugin and the backend
 * 
 * @author Deftware
 *
 */
public class API {

	private static final String endpoint = "https://emc-api.sky-net.me/";

	@Getter
	public static Mod[] mods = null;

	public static void init() {
		new Thread(() -> mods = fetchMods()).start();
	}

	/**
	 * All endpoints
	 *
	 */
	public static enum Types {

		/**
		 * Returns a list of all mods
		 */
		ListProducts("products");

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
	public static String fetchEndpoint(Types type, Object[] args) throws Exception {
		return Web.get(args != null ? String.format(endpoint + type.url, args) : endpoint + type.url);
	}

	/**
	 * Returns an array of all mods
	 * 
	 * @return
	 */
	public static Mod[] fetchMods() {
		try {
			Mod[] mods = new Gson().fromJson(fetchEndpoint(Types.ListProducts, null), Mod[].class);
			for (Mod mod : mods) {
				mod.init();
			}
			return mods;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Mod[0];
	}

}
