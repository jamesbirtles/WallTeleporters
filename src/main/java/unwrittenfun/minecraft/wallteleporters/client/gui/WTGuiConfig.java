package unwrittenfun.minecraft.wallteleporters.client.gui;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import unwrittenfun.minecraft.wallteleporters.Config;
import unwrittenfun.minecraft.wallteleporters.ModInfo;

public class WTGuiConfig extends GuiConfig {
  @SuppressWarnings("unchecked")
  public WTGuiConfig(GuiScreen parentScreen) {
    super(parentScreen, new ConfigElement(Config.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), ModInfo.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(Config.configuration.toString()));
  }
}
