package emc.marketplace.gui;

import emc.marketplace.main.Main;
import emc.marketplace.modinstaller.Mod;
import lombok.Getter;
import me.deftware.client.framework.Wrappers.IMinecraft;
import me.deftware.client.framework.Wrappers.Objects.IGuiButton;
import me.deftware.client.framework.Wrappers.Objects.IGuiScreen;
import me.deftware.client.framework.Wrappers.Render.IFontRenderer;

/**
 * Gui for showing more info about a mod
 * 
 * @author Deftware
 *
 */
public class ModInfo extends IGuiScreen {

	@Getter
	private Mod mod;
	private Main main;

	public ModInfo(Mod mod, Main main) {
		this.mod = mod;
		this.main = main;
	}

	@Override
	protected void onInitGui() {
		this.clearButtons();
		this.addButton(new IGuiButton(0, getIGuiScreenWidth() / 2 - 100, getIGuiScreenHeight() - 28, 98, 20,
				mod.isInstalled() ? "Uninstall" : mod.getPrice() == 0 ? "Install" : "Buy"));
		this.addButton(new IGuiButton(1, getIGuiScreenWidth() / 2 + 2, getIGuiScreenHeight() - 28, 98, 20, "Back"));
	}

	@Override
	protected void onDraw(int mouseX, int mouseY, float partialTicks) {
		this.drawIDefaultBackground();
		IFontRenderer.drawCenteredString(mod.getName(), getIGuiScreenWidth() / 2, 8, 16777215);
		IFontRenderer.drawCenteredString("Developed by: " + mod.getOwner(), getIGuiScreenWidth() / 2, 20, 16777215);
	}

	@Override
	protected void onUpdate() {

	}

	@Override
	protected void onActionPerformed(int buttonID, boolean enabled) {
		if (!enabled) {
			return;
		}
		if (buttonID == 0) {
			if (mod.isInstalled()) {
				mod.uninstall();
			} else {
				if (mod.getPrice() == 0) {
					getIButtonList().get(0).setText("Installing mod...");
					mod.install(() -> {
						getIButtonList().get(0).setText("Uninstall");
					});
				} else {
					IGuiScreen.openLink(ModList.frontend + "mod/" + mod.getName());
				}
			}
		} else if (buttonID == 1) {
			IMinecraft.setGuiScreen(new ModList(main));
		}
	}

	@Override
	protected void onKeyTyped(char typedChar, int keyCode) {

	}

	@Override
	protected void onMouseReleased(int mouseX, int mouseY, int mouseButton) {

	}

	@Override
	protected void onMouseClicked(int mouseX, int mouseY, int mouseButton) {

	}

	@Override
	protected void onGuiResize(int w, int h) {

	}

}
