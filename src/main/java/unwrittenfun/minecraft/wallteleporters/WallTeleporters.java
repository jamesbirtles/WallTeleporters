package unwrittenfun.minecraft.wallteleporters;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;
import unwrittenfun.minecraft.wallteleporters.blocks.BlockRegister;
import unwrittenfun.minecraft.wallteleporters.handlers.GuiHandler;
import unwrittenfun.minecraft.wallteleporters.handlers.PlayerInteractHandler;
import unwrittenfun.minecraft.wallteleporters.helpers.CompareStacks;
import unwrittenfun.minecraft.wallteleporters.items.ItemRegister;
import unwrittenfun.minecraft.wallteleporters.network.NetworkRegister;
import unwrittenfun.minecraft.wallteleporters.recipes.TeleporterFuelRegistry;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = "required-after:Forge;required-after:commonfun")
public class WallTeleporters {
  @Mod.Instance
  public static WallTeleporters instance;

  public static NetworkRegister networkRegister;

  public static CreativeTabs creativeTab = new CreativeTabs("wallTeleporters") {
    @Override
    public Item getTabIconItem() {
      return ItemRegister.gpsChip;
    }
  };

  public static Logger log;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    Config.initConfig(new Configuration(event.getSuggestedConfigurationFile()));

    log = event.getModLog();

    BlockRegister.registerBlocks();
    ItemRegister.registerItems();

    BlockRegister.addRecipes();
    ItemRegister.addRecipes();

    networkRegister = new NetworkRegister();

    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

    MinecraftForge.EVENT_BUS.register(new PlayerInteractHandler());
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    CompareStacks.assignStacks();
    TeleporterFuelRegistry.registerDefaults();
  }
}
