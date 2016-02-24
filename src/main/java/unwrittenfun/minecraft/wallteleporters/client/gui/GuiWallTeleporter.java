package unwrittenfun.minecraft.wallteleporters.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import unwrittenfun.minecraft.wallteleporters.Config;
import unwrittenfun.minecraft.wallteleporters.ModInfo;
import unwrittenfun.minecraft.wallteleporters.WallTeleporters;
import unwrittenfun.minecraft.wallteleporters.client.gui.component.GuiButtonArrow;
import unwrittenfun.minecraft.wallteleporters.client.gui.component.GuiToggleButton;
import unwrittenfun.minecraft.wallteleporters.containers.ContainerWallTeleporter;
import unwrittenfun.minecraft.wallteleporters.helpers.TooltipHelper;
import unwrittenfun.minecraft.wallteleporters.multiblock.WallTeleporterNetwork;
import unwrittenfun.minecraft.wallteleporters.network.messages.MessageTileInteger;
import unwrittenfun.minecraft.wallteleporters.recipes.TeleporterFuelRegistry;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterBase;

import java.util.ArrayList;
import java.util.List;

public class GuiWallTeleporter extends GuiFunContainer {
  public static ResourceLocation texture = new ResourceLocation(ModInfo.RESOURCE_LOCATION, "textures/gui/wallTeleporter.png");
  private static List<String> maskTooltip = new ArrayList<>();
  private static List<String> rotationTooltip = new ArrayList<>();

  static {
    maskTooltip.add("Lock the ability");
    maskTooltip.add("to mask the teleporter");

    rotationTooltip.add("If the teleporter");
    rotationTooltip.add("should respect rotation");
  }

  private final ContainerWallTeleporter container;
  public TileWallTeleporterBase teleporter;

  public GuiWallTeleporter(ContainerWallTeleporter container) {
    super(container);

    this.container = container;
    this.teleporter = container.teleporter;
    xSize = 176;
    ySize = 172;
  }

  @Override
  public void initGui() {
    super.initGui();
    initButtons();
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mx, int my) {
    int xOff = Config.disableFuel ? 8 : 28;

    if (teleporter.getWTNetwork().hasDestination()) {
      float[] destinationData = teleporter.getWTNetwork().destinationData;
      String worldName = teleporter.getWTNetwork().destinationName;
      String coords = "(" + (int) destinationData[0] + ", " + (int) destinationData[1] + ", " + (int) destinationData[2] + ")";
      String yaw = StatCollector.translateToLocal("wallteleporters.info.yaw") + ": " + (int) destinationData[3];
      int locationYOffset = 20;
      fontRendererObj.drawString(worldName, 74 + (xOff / 2) - fontRendererObj.getStringWidth(worldName) / 2, locationYOffset, 0x404040);
      fontRendererObj.drawString(coords, 74 + (xOff / 2) - fontRendererObj.getStringWidth(coords) / 2, locationYOffset + 12, 0x404040);
      fontRendererObj.drawString(yaw, 74 + (xOff / 2) - fontRendererObj.getStringWidth(yaw) / 2, locationYOffset + 24, 0x404040);
    }


    if (!Config.disableFuel) {
      GL11.glColor4f(1, 1, 1, 1);
      Minecraft.getMinecraft().renderEngine.bindTexture(texture);
      int fuel = teleporter.getWTNetwork().fuel;
      drawTexturedModalRect(8, 8 + (54 - fuel * 3), xSize + 16, 0, 16, fuel * 3);
      drawTexturedModalRect(8, 8, xSize, 0, 16, 54);
    }


    fontRendererObj.drawString(teleporter.getInventoryName(), xOff, 68, 0x373737);
    if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak)) {
      fontRendererObj.drawString(StatCollector.translateToLocal("wallteleporters.gui.sneakHelp"), xOff, 78, 0x6a6a6a);
    } else {
      fontRendererObj.drawString(StatCollector.translateToLocal("wallteleporters.gui.sneakForHelp"), xOff, 78, 0x6a6a6a);
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
    GL11.glColor4f(1, 1, 1, 1);
    Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

    if (!Config.disableFuel) {
      drawTexturedModalRect(guiLeft + 7, guiTop + 7, 0, ySize, 18, 78);
    }
  }

  @SuppressWarnings("unchecked")
  private void initButtons() {
    buttonList.clear();

    buttonList.add(new GuiButtonArrow(teleporter, 0, guiLeft + 151, guiTop + 29));
    buttonList.add(new GuiToggleButton(0, 1, guiLeft + 151, guiTop + 46, teleporter.getWTNetwork().maskLocked));
    buttonList.add(new GuiToggleButton(1, 2, guiLeft + 151, guiTop + 45 + 22, teleporter.getWTNetwork().useRotation));
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    if (button instanceof GuiToggleButton) {
      ((GuiToggleButton) button).state = !((GuiToggleButton) button).state;
    }
    WallTeleporters.networkRegister.wrapper.sendToServer(MessageTileInteger.messageFrom(teleporter.getWorldObj(), teleporter.xCoord, teleporter.yCoord, teleporter.zCoord, 0, button.id));
  }

  @Override
  public void drawTooltips(int guiMX, int guiMY, int renderMX, int renderMY) {
    if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak)) {
      if (guiMX >= 151 && guiMX < 169 && guiMY >= 29 && guiMY < 43) {
        if (teleporter.getStackInSlot(0) == null) {
          drawTooltip(TooltipHelper.listForUnloc("wallteleporters.gui.clearDestination"), renderMX, renderMY);
        } else {
          drawTooltip(TooltipHelper.listForUnloc("wallteleporters.gui.copyDestination"), renderMX, renderMY);
        }
      }

      if (guiMX >= 151 && guiMX < 169) {
        if (guiMY >= 46 && guiMY < 64) {
          drawTooltip(maskTooltip, renderMX, renderMY);
        } else if (guiMY >= 67 && guiMY < 85) {
          drawTooltip(rotationTooltip, renderMX, renderMY);
        }
      }

      if (teleporter.getStackInSlot(0) == null && guiMX >= 152 && guiMX < 170 && guiMY >= 8 && guiMY < 26) {
        drawTooltip(TooltipHelper.listForUnloc("wallteleporters.gui.gpsChip"), renderMX, renderMY);
      }
    }

    if (!Config.disableFuel) {
      if (guiMX > 8 && guiMX < 24 && guiMY > 8 && guiMY < 62) {
        ArrayList<String> lines = new ArrayList<String>();
        lines.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("wallteleporters.gui.wt.trips") + ": " + teleporter.getWTNetwork().fuel + "/" + WallTeleporterNetwork.MAX_TRIPS);
        if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak)) {
          for (ItemStack fuelStack : TeleporterFuelRegistry.fuels) {
            lines.add(fuelStack.getDisplayName() + EnumChatFormatting.DARK_PURPLE + " -> " + TeleporterFuelRegistry.getFuelAmount(fuelStack));
          }
        }
        drawTooltip(lines, renderMX, renderMY);
      }

      if (teleporter.getStackInSlot(1) == null && guiMX >= 7 && guiMX < 25 && guiMY >= 67 && guiMY < 85) {
        ArrayList<String> lines = new ArrayList<String>();
        if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak)) {
          lines.add(StatCollector.translateToLocal("wallteleporters.gui.insert") + " ");
          for (ItemStack fuelStack : TeleporterFuelRegistry.fuels) {
            lines.add("- " + fuelStack.getDisplayName() + EnumChatFormatting.DARK_PURPLE + " " + StatCollector.translateToLocal("wallteleporters.gui.for") + " " + TeleporterFuelRegistry.getFuelAmount(fuelStack) + " " + StatCollector.translateToLocal("wallteleporters.gui.wt.trips"));
          }
        } else {
          lines.add(StatCollector.translateToLocal("wallteleporters.gui.insertFuel"));
        }
        drawTooltip(lines, renderMX, renderMY);
      }
    }
  }
}
