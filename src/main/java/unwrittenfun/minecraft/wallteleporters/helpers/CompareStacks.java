package unwrittenfun.minecraft.wallteleporters.helpers;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.wallteleporters.blocks.BlockRegister;
import unwrittenfun.minecraft.wallteleporters.items.ItemRegister;

public class CompareStacks {
  public static ItemStack gpsChipLinked, wallTeleporter, wallTeleporterBase, enderPearl, enderEssence;

  public static void assignStacks() {
    gpsChipLinked = new ItemStack(ItemRegister.gpsChip, 1, 1);
    wallTeleporter = new ItemStack(BlockRegister.wallTeleporterWall);
    wallTeleporterBase = new ItemStack(BlockRegister.wallTeleporterBase);
    enderPearl = new ItemStack(Items.ender_pearl);
    enderEssence = new ItemStack(ItemRegister.enderEssence);
  }
}
