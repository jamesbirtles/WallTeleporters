package unwrittenfun.minecraft.wallteleporters.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.wallteleporters.items.ItemRegister;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterBase;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterWall;

public class BlockRegister {
  public static final String WT_BASE_KEY = "wallTeleporterBase";
  public static final String WT_WALL_KEY = "wallTeleporterWall";

  public static BlockWallTeleporterBase wallTeleporterBase;
  public static BlockWallTeleporterWall wallTeleporterWall;

  public static void registerBlocks() {
    wallTeleporterBase = new BlockWallTeleporterBase(WT_BASE_KEY);
    wallTeleporterWall = new BlockWallTeleporterWall(WT_WALL_KEY);

    GameRegistry.registerBlock(wallTeleporterBase, WT_BASE_KEY);
    GameRegistry.registerBlock(wallTeleporterWall, WT_WALL_KEY);

    GameRegistry.registerTileEntity(TileWallTeleporterBase.class, "TE" + WT_BASE_KEY);
    GameRegistry.registerTileEntity(TileWallTeleporterWall.class, "TE" + WT_WALL_KEY);
  }

  public static void addRecipes() {
    GameRegistry.addRecipe(new ItemStack(wallTeleporterBase), "iei", "ece", "iei", 'i', Items.iron_ingot, 'e', ItemRegister.enderEssence, 'c', ItemRegister.teleporterCore);
    GameRegistry.addRecipe(new ItemStack(wallTeleporterWall, 4), "ses", "eie", "ses", 'i', Items.iron_ingot, 'e', ItemRegister.enderEssence, 's', Blocks.stone);
  }
}
