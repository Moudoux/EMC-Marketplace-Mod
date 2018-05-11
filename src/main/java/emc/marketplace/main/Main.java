package emc.marketplace.main;

import java.util.List;

import emc.marketplace.gui.ModList;
import emc.marketplace.modinstaller.API;
import emc.marketplace.modinstaller.Mod;
import lombok.Getter;
import me.deftware.client.framework.apis.marketplace.MarketplaceAPI;
import me.deftware.client.framework.event.Event;
import me.deftware.client.framework.event.events.EventActionPerformed;
import me.deftware.client.framework.event.events.EventGuiScreenDraw;
import me.deftware.client.framework.main.EMCMod;
import me.deftware.client.framework.wrappers.IMinecraft;
import me.deftware.client.framework.wrappers.gui.IGuiButton;

public class Main extends EMCMod {

	@Getter
	private static boolean sessionActive = false;

	@Override
	public void initialize() {
		API.init();
	}

	@Override
	public EMCModInfo getModInfo() {
		return new EMCModInfo("EMC-Marketplace", "4.0.0");
	}

	@Override
	public void onEvent(Event event) {
		if ((event instanceof EventGuiScreenDraw)) {
			drawButton((EventGuiScreenDraw) event);
		} else if ((event instanceof EventActionPerformed)) {
			drawButton((EventActionPerformed) event);
		}
	}

	@Override
	public void callMethod(String method, String caller) {
		if (method.equals("openGUI()")) {
			IMinecraft.setGuiScreen(new ModList(this));
		}
	}

	@Override
	public void onMarketplaceAuth(boolean success) {
		sessionActive = success;
		if (success) {
			if (API.getMods() != null) {
				List<String> paidModNames = MarketplaceAPI.getLicensedModNames();
				for (Mod mod : API.getMods()) {
					mod.setPaid(paidModNames.contains(mod.getName()));
				}
			}
		}
	}

	private void drawButton(EventGuiScreenDraw event) {
		if ((event instanceof EventActionPerformed)) {
			if (((EventActionPerformed) event).getId() == 25) {
				IMinecraft.setGuiScreen(new ModList(this));
			}
		} else if ((event.instanceOf(EventGuiScreenDraw.CommonScreenTypes.GuiIngameMenu))
				&& (event.getIButtonList().isEmpty() || event.getIButtonList().size() == 1)) {
			event.addButton(
					new IGuiButton(25, event.getWidth() / 2 - 100, event.getHeight() / 4 + 128, "Addons Marketplace"));
		}
	}

}
