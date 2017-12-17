package emc.marketplace.modinstaller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import me.deftware.client.framework.MC_OAuth.StaticOAuth;

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

		/**
		 * Returns a list of all mods
		 */
		ListProducts("listproducts"),

		/**
		 * Get's a jar file
		 */
		GetProduct("getproduct?product=%s&token=%s"),

		/**
		 * Returns a list of all plugins a given user has bought
		 */
		CheckProducts("checkproducts?token=%s");

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
			StaticOAuth.getToken((token) -> {
				try {
					JsonObject json = new Gson()
							.fromJson(API.fetchEndpoint(Types.CheckProducts, new String[] { token }), JsonObject.class);
					if (json.get("success").getAsBoolean()) {
						JsonArray arr = json.get("data").getAsJsonArray();
						for (Mod mod : mods) {
							mod.setPaid(arr.contains(new JsonPrimitive(mod.Name)));
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			return mods;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Mod[0];
	}

}
