package emc.marketplace.gui;

import org.lwjgl.input.Keyboard;

import emc.marketplace.main.Main;
import emc.marketplace.modinstaller.Mod;
import emc.marketplace.modinstaller.Mod.ModData;
import me.deftware.client.framework.Wrappers.IMinecraft;
import me.deftware.client.framework.Wrappers.Objects.IGuiButton;
import me.deftware.client.framework.Wrappers.Objects.IGuiScreen;
import me.deftware.client.framework.Wrappers.Objects.IGuiSlot;
import me.deftware.client.framework.Wrappers.Render.IFontRenderer;

/**
 * The gui for browsing and installing mods
 * 
 * @author Deftware
 *
 */
public class ModList extends IGuiScreen {

	private List list;
	private Main main;

	public ModList(Main main) {
		this.main = main;
	}

	@Override
	protected void onInitGui() {
		list = new List(this);
		list.registerScrollbars(7, 8);
		list.clickElement(-1, false, 0, 0);
		this.clearButtons();
		this.addButton(
				new IGuiButton(0, getIGuiScreenWidth() / 2 - 100, getIGuiScreenHeight() - 28, 98, 20, "Install"));
		this.addButton(
				new IGuiButton(1, getIGuiScreenWidth() / 2 + 2, getIGuiScreenHeight() - 28, 98, 20, "Details..."));
	}

	@Override
	protected void onDraw(int mouseX, int mouseY, float partialTicks) {
		this.drawIDefaultBackground();
		list.doDraw(mouseX, mouseY, partialTicks);
		IFontRenderer.drawCenteredString("EMC Marketplace", getIGuiScreenWidth() / 2, 8, 16777215);
		IFontRenderer.drawCenteredString("Mods available: " + main.getModData().size(), getIGuiScreenWidth() / 2, 20,
				16777215);
	}

	@Override
	protected void onMouseInput() {
		list.onMouseInput();
	}

	@Override
	protected void onUpdate() {
		Mod selected = null;
		if (!main.getModData().isEmpty() && list.getSelectedSlot() != -1) {
			selected = main.getModData().get(list.getSelectedSlot());
		}
		if (selected != null) {
			getIButtonList().get(0).setText(selected.isInstalled() ? "Uninstall"
					: selected.getData(ModData.PRICE).equals("Free") ? "Install" : "Buy");
			getIButtonList().get(0).setEnabled(true);
			getIButtonList().get(1).setEnabled(true);
		} else {
			getIButtonList().get(0).setEnabled(false);
			getIButtonList().get(1).setEnabled(false);
		}
	}

	@Override
	protected void onActionPerformed(int buttonID, boolean enabled) {
		if (!enabled) {
			return;
		}
		// We can assume the selected is not null, since you cannot press any of the
		// buttons if no mod is selected
		Mod selected = main.getModData().get(list.getSelectedSlot());
		if (buttonID == 0) {
			// Install/Buy | Uninstall
			if (selected.isInstalled()) {
				selected.uninstall();
			} else {
				selected.install();
			}
		} else if (buttonID == 1) {
			// More info
			IMinecraft.setGuiScreen(new ModInfo(selected));
		}
	}

	@Override
	protected void onKeyTyped(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			IMinecraft.setGuiScreen(null);
		}
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

	private class List extends IGuiSlot {

		public List(IGuiScreen parent) {
			super(parent.getIGuiScreenWidth(), parent.getIGuiScreenHeight(), 36, parent.getIGuiScreenHeight() - 56, 30);
		}

		@Override
		protected int getISize() {
			return main.getModData().size();
		}

		@Override
		protected void drawISlot(int id, int x, int y) {
			// TODO
		}

	}

}
