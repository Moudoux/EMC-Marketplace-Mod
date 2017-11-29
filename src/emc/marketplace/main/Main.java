package emc.marketplace.main;

import java.util.ArrayList;

import emc.marketplace.gui.ModList;
import emc.marketplace.modinstaller.API;
import emc.marketplace.modinstaller.Mod;
import lombok.Getter;
import me.deftware.client.framework.Client.EMCClient;
import me.deftware.client.framework.Event.Event;
import me.deftware.client.framework.Event.Events.EventActionPerformed;
import me.deftware.client.framework.Event.Events.EventGuiScreenDraw;
import me.deftware.client.framework.Wrappers.IMinecraft;
import me.deftware.client.framework.Wrappers.Objects.IGuiButton;

public class Main extends EMCClient {

	@Getter
	private ArrayList<Mod> modData = new ArrayList<Mod>();

	public void refreshMods() {
		modData.clear();
		new Thread(() -> modData = API.fetchMods()).start();
	}

	@Override
	public void initialize() {
		refreshMods();
	}

	@Override
	public EMCClientInfo getClientInfo() {
		return new EMCClientInfo("EMC-Marketplace", "1");
	}

	@Override
	public void onEvent(Event event) {
		if ((event instanceof EventGuiScreenDraw)) {
			drawButton((EventGuiScreenDraw) event);
		} else if ((event instanceof EventActionPerformed)) {
			drawButton((EventActionPerformed) event);
		}
	}

	private void drawButton(EventGuiScreenDraw event) {
		if ((event instanceof EventActionPerformed)) {
			if (((EventActionPerformed) event).getId() == 25) {
				IMinecraft.setGuiScreen(new ModList(this));
			}
		} else if ((event.instanceOf(EventGuiScreenDraw.CommonScreenTypes.GuiIngameMenu))
				&& (event.getIButtonList().isEmpty())) {
			event.addButton(
					new IGuiButton(25, event.getWidth() / 2 - 100, event.getHeight() / 4 + 125, "Addons Marketplace"));
		}
	}

}
