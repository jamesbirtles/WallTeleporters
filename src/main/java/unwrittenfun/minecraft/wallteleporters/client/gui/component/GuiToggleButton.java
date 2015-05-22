package unwrittenfun.minecraft.wallteleporters.client.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import unwrittenfun.minecraft.wallteleporters.client.gui.GuiWallTeleporter;

import java.util.List;

public class GuiToggleButton extends GuiButton {
  public int type;
  public boolean state = false;
  public List<String> lines;

  public GuiToggleButton(int type, int id, int x, int y, boolean state) {
    super(id, x, y, "");
    this.type = type;
    this.width = 18;
    this.height = 18;
    this.state = state;
  }

  @Override
  public void drawButton(Minecraft minecraft, int x, int y) {
    minecraft.renderEngine.bindTexture(GuiWallTeleporter.texture);

    if (x >= this.xPosition && x < this.xPosition + 18 && y >= this.yPosition && y < this.yPosition + 18) {
      drawTexturedModalRect(this.xPosition, this.yPosition, 212 + 18, 54, 18, 18);
    } else {
      drawTexturedModalRect(this.xPosition, this.yPosition, 212, 54, 18, 18);
    }

    if (state) {
      drawTexturedModalRect(this.xPosition, this.yPosition, 176, 82 + 18 * type, 18, 18);
    } else {
      drawTexturedModalRect(this.xPosition, this.yPosition, 194, 82 + 18 * type, 18, 18);
    }
  }
}
