package unwrittenfun.minecraft.wallteleporters.client.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import unwrittenfun.minecraft.wallteleporters.client.gui.GuiWallTeleporter;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterBase;

public class GuiButtonArrow extends GuiButton {
  protected static final int WIDTH = 18;
  protected static final int HEIGHT = 14;
  protected TileWallTeleporterBase teleporter;

  public GuiButtonArrow(TileWallTeleporterBase teleporter, int buttonId, int x, int y) {
    super(buttonId, x, y, WIDTH, HEIGHT, "<-");
    this.teleporter = teleporter;
  }

  @Override
  public void drawButton(Minecraft minecraft, int mX, int mY) {
    minecraft.renderEngine.bindTexture(GuiWallTeleporter.texture);
    boolean hovering = mX >= xPosition && mX < xPosition + WIDTH && mY >= yPosition && mY < yPosition + HEIGHT;
    drawTexturedModalRect(xPosition, yPosition, 176 + (teleporter.getStackInSlot(0) == null ? WIDTH : 0), 54 + (hovering ? HEIGHT : 0), WIDTH, HEIGHT);
  }
}
