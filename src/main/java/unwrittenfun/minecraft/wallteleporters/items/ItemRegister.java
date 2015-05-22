package unwrittenfun.minecraft.wallteleporters.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemRegister {
  public static final String GPS_CHIP_KEY = "gpsChip";
  public static final String ENDER_ESSENCE_KEY = "enderEssence";
  public static final String TELEPORTER_CORE = "teleporterCore";

  public static Item gpsChip, enderEssence, teleporterCore;

  public static void registerItems() {
    gpsChip = new ItemGPSChip(GPS_CHIP_KEY);
    enderEssence = new ItemEnderEssence(ENDER_ESSENCE_KEY);
    teleporterCore = new ItemTeleporterCore(TELEPORTER_CORE);

    GameRegistry.registerItem(gpsChip, GPS_CHIP_KEY);
    GameRegistry.registerItem(enderEssence, ENDER_ESSENCE_KEY);
    GameRegistry.registerItem(teleporterCore, TELEPORTER_CORE);
  }

  public static void addRecipes() {
    GameRegistry.addRecipe(new ItemStack(teleporterCore), " e ", "ede", " e ", 'e', enderEssence, 'd', Items.diamond);
  }
}
