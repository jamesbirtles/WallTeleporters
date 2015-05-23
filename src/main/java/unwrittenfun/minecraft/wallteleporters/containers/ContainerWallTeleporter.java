package unwrittenfun.minecraft.wallteleporters.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.wallteleporters.Config;
import unwrittenfun.minecraft.wallteleporters.containers.slots.SlotValid;
import unwrittenfun.minecraft.wallteleporters.helpers.CompareStacks;
import unwrittenfun.minecraft.wallteleporters.recipes.TeleporterFuelRegistry;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterBase;

public class ContainerWallTeleporter extends Container {
  public TileWallTeleporterBase teleporter;

  public ContainerWallTeleporter(InventoryPlayer playerInventory, TileWallTeleporterBase teleporterBase) {
    teleporter = teleporterBase;

    for (int x = 0; x < 9; x++) {
      addSlotToContainer(new Slot(playerInventory, x, 8 + 18 * x, 148));
    }

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 9; x++) {
        addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + 18 * x, 90 + 18 * y));
      }
    }

    addSlotToContainer(new SlotValid(teleporterBase, 0, 152, 8));
    if (!Config.disableFuel) addSlotToContainer(new SlotValid(teleporterBase, 1, 8, 68));
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {
    return true;
  }

  @Override
  public void addCraftingToCrafters(ICrafting crafter) {
    crafter.sendProgressBarUpdate(this, 0, teleporter.getWTNetwork().fuel);
    crafter.sendProgressBarUpdate(this, 1, teleporter.getWTNetwork().maskLocked ? 1 : 0);
    crafter.sendProgressBarUpdate(this, 2, teleporter.getWTNetwork().useRotation ? 1 : 0);
    super.addCraftingToCrafters(crafter);
  }

  @Override
  public void updateProgressBar(int type, int value) {
    switch (type) {
      case 0: // Fuel
        teleporter.getWTNetwork().fuel = value;
        break;
      case 1: // Mask Lock
        teleporter.getWTNetwork().maskLocked = value == 1;
        break;
      case 2: // Use rotation
        teleporter.getWTNetwork().useRotation = value == 1;
        break;
    }
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int i) {
    Slot slot = getSlot(i);
    if (slot != null && slot.getHasStack()) {
      ItemStack stack = slot.getStack();
      ItemStack result = stack.copy();
      if (i >= 36) {
        if (!mergeItemStack(stack, 0, 36, false)) return null;
      } else {
        if (teleporter.isItemValidForSlot(0, stack)) {
          if (!mergeItemStack(stack, 36, 37, false)) return null;
        } else if (!Config.disableFuel && teleporter.isItemValidForSlot(1, stack)) {
          if (!mergeItemStack(stack, 37, 38, false)) return null;
        } else {
          return null;
        }
      }
      if (stack.stackSize == 0) slot.putStack(null);
      else slot.onSlotChanged();
      slot.onPickupFromSlot(player, stack);
      return result;
    }
    return null;
  }
}
