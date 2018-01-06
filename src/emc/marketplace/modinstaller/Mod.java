package emc.marketplace.modinstaller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

import lombok.Getter;
import me.deftware.client.framework.Client.EMCClient;
import me.deftware.client.framework.Main.FrameworkLoader;
import me.deftware.client.framework.Marketplace.MarketplaceAPI;
import me.deftware.client.framework.Marketplace.MarketplaceResponse;
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
	private File modFile = null, deleted = null;

	private EMCClient client = null;

	public void init() {
		try {
			modFile = new File(IMinecraft.getMinecraftFile().getParentFile() + File.separator + "mods" + File.separator
					+ Name + ".jar");
			deleted = new File(IMinecraft.getMinecraftFile().getParentFile() + File.separator + "mods" + File.separator
					+ Name + ".jar.delete");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public boolean isInstalled() {
		return modFile == null || deleted == null ? false : modFile.exists() && !deleted.exists();
	}

	public void install(InstallCallback cb) {
		new Thread(() -> {
			if (deleted.exists() && modFile.exists()) {
				deleted.delete();
				if (!FrameworkLoader.getClients().containsKey(Name) && client != null) {
					FrameworkLoader.getClients().put(Name, client);
					client = null;
				}
				cb.callback();
				return;
			}
			if (isInstalled()) {
				return;
			}
			try {
				MarketplaceResponse data = MarketplaceAPI.downloadMod(Name);
				if (data.success) {
					byte[] bytes = Base64.getDecoder().decode(data.data);
					FileUtils.writeByteArrayToFile(modFile, bytes);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (modFile.exists()) {
				try {
					FrameworkLoader.loadClient(modFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			cb.callback();
		}).start();
	}

	public void uninstall() {
		new Thread(() -> {
			if (!isInstalled()) {
				return;
			}
			try {
				FileUtils.writeStringToFile(deleted, "Delete mod", "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (FrameworkLoader.getClients().containsKey(Name)) {
				client = FrameworkLoader.getClients().get(Name);
				FrameworkLoader.getClients().remove(Name);
			}
		}).start();
	}

	@FunctionalInterface
	public static interface InstallCallback {

		public void callback();

	}

	public void setPaid(boolean flag) {
		this.hasPaid = flag;
	}

}
