package emc.marketplace.modinstaller;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;

/**
 * The mod class which contains all info about a mod
 * 
 * @author Deftware
 *
 */
public class Mod {

	private JsonObject data;

	@Getter
	@Setter
	private boolean installed = false;

	public Mod(JsonObject data) {
		this.data = data;
	}

	public String getData(ModData type) {
		return data.get(type.name().toLowerCase()).getAsString();
	}

	public JsonObject toJson() {
		return data;
	}

	public void install() {
		if (installed) {
			return;
		}
		if (getData(ModData.PRICE).equals("Free")) {
			// Install

		} else {
			// License check/Buy

		}
	}

	public void uninstall() {
		if (!installed) {
			return;
		}
		// TODO
	}

	public static enum ModData {
		NAME, AUTHOR, DESCRIPTION, VERSION, ID, PRICE
	}

}
