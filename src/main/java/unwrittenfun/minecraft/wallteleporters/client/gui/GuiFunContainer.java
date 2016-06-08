package unwrittenfun.minecraft.wallteleporters.client.gui;

import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;

public abstract class GuiFunContainer extends GuiContainer
{
	public GuiFunContainer(Container container)
	{
		super(container);
	}
	
	@Override
	public void drawScreen(int mx, int my, float f)
	{
		super.drawScreen(mx, my, f);
		
		drawTooltips(mx - guiLeft, my - guiTop, mx, my);
	}
	
	public void drawTooltips(int guiMX, int guiMY, int renderMX, int renderMY)
	{
		
	}
	
	public void drawTooltip(List<String> lines, int x, int y)
	{
		drawHoveringText(lines, x, y, fontRendererObj);
		RenderHelper.enableGUIStandardItemLighting();
	}
}