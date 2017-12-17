package emc.marketplace.modinstaller;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import emc.marketplace.modinstaller.API.Types;
import lombok.Getter;
import me.deftware.client.framework.MC_OAuth.StaticOAuth;
import me.deftware.client.framework.Wrappers.IMinecraft;

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
	private boolean hasPaid = false;

	@Getter
	private File modFile = null;

	public void init() {
		try {
			modFile = new File(IMinecraft.getMinecraftFile().getParentFile() + File.separator + "mods" + File.separator
					+ Name + ".jar");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public boolean isInstalled() {
		return modFile == null ? false : modFile.exists();
	}

	public void install(InstallCallback cb) {
		StaticOAuth.getToken((token) -> {
			if (isInstalled()) {
				return;
			}
			// Base64 encoded jar bytes
			try {
				String data = API.fetchEndpoint(Types.GetProduct, new String[] { Name, token });
				JsonObject json = new Gson().fromJson(data, JsonObject.class);
				if (json.get("success").getAsBoolean()) {
					data = json.get("data").getAsString();
					byte[] bytes = Base64.getDecoder().decode(data);
					FileUtils.writeByteArrayToFile(modFile, bytes);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			cb.callback();
		});
	}

	public void uninstall() {
		System.out.println("Uni");
		new Thread(() -> {
			System.out.println(isInstalled());
			if (!isInstalled()) {
				return;
			}
			// TODO: Tell EMC to unload this mod
			modFile.delete();
		}).start();
	}

	@FunctionalInterface
	public static interface InstallCallback {

		public void callback();

	}

	public void checkPaid(String token) {
		try {
			hasPaid = API.fetchEndpoint(Types.CheckProduct, new String[] { Name, token }).contains("true");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
