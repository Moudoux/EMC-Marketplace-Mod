package emc.marketplace.gui;


import emc.marketplace.main.Main;
import emc.marketplace.modinstaller.API;
import emc.marketplace.modinstaller.Mod;
import me.deftware.client.framework.wrappers.IMinecraft;
import me.deftware.client.framework.wrappers.gui.IGuiButton;
import me.deftware.client.framework.wrappers.gui.IGuiScreen;
import me.deftware.client.framework.wrappers.gui.IGuiSlot;
import me.deftware.client.framework.wrappers.item.IItemStack;
import me.deftware.client.framework.wrappers.render.*;
import org.lwjgl.input.Keyboard;

/**
 * The gui for browsing and installing API.getMods()
 * 
 * @author Deftware
 *
 */
public class ModList extends IGuiScreen {

	public static final String frontend = "https://emc.sky-net.me/";

	private boolean overrideText = false;
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
		this.addButton(new IGuiButton(0, getIGuiScreenWidth() / 2 - 100, getIGuiScreenHeight() - 28, 200, 20, "Back"));
		this.addButton(
				new IGuiButton(1, getIGuiScreenWidth() / 2 - 100, getIGuiScreenHeight() - 51, 98, 20, "Install"));
		this.addButton(
				new IGuiButton(2, getIGuiScreenWidth() / 2 + 2, getIGuiScreenHeight() - 51, 98, 20, "Details..."));
		getIButtonList().get(1).setEnabled(false);
		getIButtonList().get(2).setEnabled(false);
	}

	@Override
	protected void onDraw(int mouseX, int mouseY, float partialTicks) {
		this.drawIDefaultBackground();
		list.doDraw(mouseX, mouseY, partialTicks);
		IFontRenderer.drawCenteredString("EMC Marketplace", getIGuiScreenWidth() / 2, 8, 16777215);
		IFontRenderer.drawCenteredString("Mods available: " + (API.getMods() != null ? API.getMods().length : "0"),
				getIGuiScreenWidth() / 2, 20, 16777215);
		if (API.getMods() == null) {
			IFontRenderer.drawCenteredString("Loading mods... ", getIGuiScreenWidth() / 2, 45, 16777215);
		}
		IFontRenderer.drawString("Session active: " + (Main.isSessionActive() ? "�aYes" : "�cNo"), 2, 2, 0xFFFFFF);
	}

	@Override
	protected void onMouseInput() {
		list.onMouseInput();
	}

	@Override
	protected void onUpdate() {
		if (API.getMods() == null) {
			return;
		}
		Mod selected = null;
		if (!(API.getMods().length == 0) && list.getSelectedSlot() != -1) {
			selected = API.getMods()[list.getSelectedSlot()];
		}
		if (selected != null) {
			if (!overrideText) {
				getIButtonList().get(1).setText(selected.isInstalled() ? "Uninstall"
						: (selected.getPrice() == 0 || selected.isHasPaid()) ? "Install" : "Buy");
			}
			getIButtonList().get(1).setEnabled(true);
			getIButtonList().get(2).setEnabled(true);
		} else {
			getIButtonList().get(1).setEnabled(false);
			getIButtonList().get(2).setEnabled(false);
		}
	}

	@Override
	protected void onActionPerformed(int buttonID, boolean enabled) {
		if (!enabled) {
			return;
		}
		if (buttonID == 0) {
			IMinecraft.setGuiScreen(null);
		} else if (API.getMods() != null) {
			// We can assume the selected is not null, since you cannot press any of the
			// buttons if no mod is selected
			Mod selected = API.getMods()[list.getSelectedSlot()];
			if (buttonID == 1) {
				// Install/Buy | Uninstall
				if (selected.isInstalled()) {
					selected.uninstall();
				} else {
					if (selected.getPrice() == 0 || selected.isHasPaid()) {
						overrideText = true;
						getIButtonList().get(1).setText("Installing mod...");
						selected.install(() -> {
							overrideText = false;
							getIButtonList().get(1).setText(selected.isInstalled() ? "Uninstall"
									: (selected.getPrice() == 0 || selected.isHasPaid()) ? "Install" : "Buy");
						});
					} else {
						IGuiScreen.openLink(frontend + "product/" + selected.getName());
					}
				}
			} else if (buttonID == 2) {
				// More info
				IMinecraft.setGuiScreen(new ModInfo(selected, main));
			}
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
			return API.getMods() != null ? API.getMods().length : 0;
		}

		@Override
		protected void drawISlot(int id, int x, int y) {
			Mod mod = API.getMods()[id];

			IGlStateManager.enableRescaleNormal();
			IGlStateManager.enableBlend();
			IRenderHelper.enableGUIStandardItemLighting();
			IGlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

			IItemStack stack = new IItemStack(mod.isInstalled() ? "emerald" : "diamond");

			try {
				IRenderItem.renderItemAndEffectIntoGUI(stack, x + 4, y + 4);
				IRenderItem.renderItemOverlays(stack, x + 4, y + 4);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			IRenderHelper.disableStandardItemLighting();
			IGlStateManager.disableRescaleNormal();
			IGlStateManager.disableBlend();
			IGL11.disableLightning();

			String price = mod.getPrice() == 0 ? "Free" : "$" + mod.getPrice() + " USD";
			IFontRenderer.drawString("Name: " + mod.getName(), x + 31, y + 3, 10526880);
			IFontRenderer.drawString("Price: " + price + " | By: " + mod.getOwner(), x + 31, y + 15, 10526880);
		}

	}

}
