package emc.marketplace.gui;

import emc.marketplace.modinstaller.Mod;
import lombok.Getter;
import me.deftware.client.framework.Wrappers.Objects.IGuiScreen;

/**
 * Gui for showing more info about a mod
 * 
 * @author Deftware
 *
 */
public class ModInfo extends IGuiScreen {

	@Getter
	private Mod mod;

	public ModInfo(Mod mod) {
		this.mod = mod;
	}

	@Override
	protected void onInitGui() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDraw(int mouseX, int mouseY, float partialTicks) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActionPerformed(int buttonID, boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onKeyTyped(char typedChar, int keyCode) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onGuiResize(int w, int h) {
		// TODO Auto-generated method stub

	}

}
