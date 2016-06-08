package unwrittenfun.minecraft.wallteleporters.client.gui.component;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import unwrittenfun.minecraft.wallteleporters.client.gui.GuiWallTeleporter;

public class GuiToggleButton extends GuiButton
{
	public int type;
	public boolean state = false;
	public List<String> lines;
	
	public GuiToggleButton(int type, int id, int x, int y, boolean state)
	{
		super(id, x, y, "");
		this.type = type;
		width = 18;
		height = 18;
		this.state = state;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int x, int y)
	{
		minecraft.renderEngine.bindTexture(GuiWallTeleporter.texture);
		
		if(x >= xPosition && x < xPosition + 18 && y >= yPosition && y < yPosition + 18)
		{
			drawTexturedModalRect(xPosition, yPosition, 212 + 18, 54, 18, 18);
		} else
		{
			drawTexturedModalRect(xPosition, yPosition, 212, 54, 18, 18);
		}
		
		if(state)
		{
			drawTexturedModalRect(xPosition, yPosition, 176, 82 + 18 * type, 18, 18);
		} else
		{
			drawTexturedModalRect(xPosition, yPosition, 194, 82 + 18 * type, 18, 18);
		}
	}
}
