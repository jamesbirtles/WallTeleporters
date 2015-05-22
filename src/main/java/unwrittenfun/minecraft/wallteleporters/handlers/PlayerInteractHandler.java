package unwrittenfun.minecraft.wallteleporters.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import unwrittenfun.minecraft.commonfun.helpers.InventoryHelpers;
import unwrittenfun.minecraft.commonfun.helpers.ItemHelpers;
import unwrittenfun.minecraft.wallteleporters.helpers.CompareStacks;
import unwrittenfun.minecraft.wallteleporters.items.ItemRegister;

public class PlayerInteractHandler {
  @SubscribeEvent
  public void onInteract(PlayerInteractEvent event) {
    if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && !event.entityPlayer.capabilities.isCreativeMode && event.entityPlayer.getHeldItem() != null && CompareStacks.enderPearl.isItemEqual(event.entityPlayer.getHeldItem())) {
      Block block = event.world.getBlock(event.x, event.y, event.z);
      if (block == Blocks.bedrock) {
        ItemStack result = new ItemStack(ItemRegister.enderEssence, 9);
        int leftover = InventoryHelpers.insertStackIntoSlotsInRange(event.entityPlayer.inventory, result, 0, 36);
        if (leftover > 0) {
          ItemStack drop = result.copy();
          drop.stackSize = leftover;
          ItemHelpers.dropItemStack(drop, event.world, event.x, event.y + 1, event.z);
        }
        event.entityPlayer.inventory.decrStackSize(event.entityPlayer.inventory.currentItem, 1);
        event.world.playSoundAtEntity(event.entity, "random.explode", 1, 1);
      }
    }
  }
}
