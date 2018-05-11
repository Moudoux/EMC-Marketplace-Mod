package emc.marketplace.modinstaller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;

import me.deftware.client.framework.apis.marketplace.MarketplaceAPI;
import me.deftware.client.framework.apis.marketplace.MarketplaceResponse;
import me.deftware.client.framework.main.Bootstrap;
import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.wrappers.IMinecraft;
import org.apache.commons.io.FileUtils;

import lombok.Getter;

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

	private EMCMod mod = null;

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
				if (!Bootstrap.getMods().containsKey(Name) && mod != null) {
					Bootstrap.getMods().put(Name, mod);
					mod = null;
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
					Bootstrap.loadMod(modFile);
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
			if (Bootstrap.getMods().containsKey(Name)) {
				mod = Bootstrap.getMods().get(Name);
				Bootstrap.getMods().remove(Name);
			}
		}).start();
	}

	@FunctionalInterface
	public interface InstallCallback {

		void callback();

	}

	public void setPaid(boolean flag) {
		this.hasPaid = flag;
	}

}
