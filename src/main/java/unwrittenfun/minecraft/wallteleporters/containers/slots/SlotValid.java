package unwrittenfun.minecraft.wallteleporters.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterBase;

public class SlotValid extends Slot {
  public SlotValid(IInventory inventory, int slot, int x, int y) {
    super(inventory, slot, x, y);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
  }
}
