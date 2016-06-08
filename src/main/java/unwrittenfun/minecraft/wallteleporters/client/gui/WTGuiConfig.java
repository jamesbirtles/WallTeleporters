package unwrittenfun.minecraft.wallteleporters.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import unwrittenfun.minecraft.wallteleporters.Config;
import unwrittenfun.minecraft.wallteleporters.ModInfo;
import cpw.mods.fml.client.config.GuiConfig;

public class WTGuiConfig extends GuiConfig
{
	@SuppressWarnings({"unchecked", "rawtypes"})
	public WTGuiConfig(GuiScreen parentScreen)
	{
		super(parentScreen, new ConfigElement(Config.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), ModInfo.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(Config.configuration.toString()));
	}
}
