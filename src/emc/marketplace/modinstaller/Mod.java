package emc.marketplace.modinstaller;

import lombok.Getter;
import lombok.Setter;

/**
 * The mod class which contains all info about a mod
 * 
 * @author Deftware
 *
 */
public class Mod {

	@Getter
	String Name, Owner, Description, Version;

	@Getter
	int Price;

	@Getter
	@Setter
	private boolean installed = false;

	public void install(InstallCallback cb) {
		if (installed) {
			return;
		}

	}

	public void uninstall() {
		if (!installed) {
			return;
		}

	}

	@FunctionalInterface
	public static interface InstallCallback {

		public void callback();

	}

}
