package unwrittenfun.minecraft.wallteleporters.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.commonfun.helpers.InventoryHelpers;
import unwrittenfun.minecraft.commonfun.network.messages.MessageTileRequest;
import unwrittenfun.minecraft.commonfun.network.receivers.ITileIntegerReceiver;
import unwrittenfun.minecraft.wallteleporters.WallTeleporters;
import unwrittenfun.minecraft.wallteleporters.blocks.BlockRegister;
import unwrittenfun.minecraft.wallteleporters.helpers.CompareStacks;
import unwrittenfun.minecraft.wallteleporters.multiblock.WallTeleporterNetwork;
import unwrittenfun.minecraft.wallteleporters.recipes.TeleporterFuelRegistry;

/**
 * Author: James Birtles
 */
public class TileWallTeleporterBase extends TileWallTeleporter implements IInventory, ISidedInventory, ITileIntegerReceiver {

  public ItemStack[] items = new ItemStack[2];

  public TileWallTeleporterBase() {
    network = new WallTeleporterNetwork(this);
  }

  @Override
  public void updateEntity() {
    super.updateEntity();

    if (getWTNetwork().cooldown > 0) {
      getWTNetwork().cooldown--;
    }
  }

  @Override
  protected void onLoaded() {
    super.onLoaded();
    if (worldObj.isRemote) { // If on client, request info.
      WallTeleporters.networkRegister.wrapper.sendToServer(MessageTileRequest.messageFrom(worldObj, xCoord, yCoord, zCoord, 1));
      WallTeleporters.networkRegister.wrapper.sendToServer(MessageTileRequest.messageFrom(worldObj, xCoord, yCoord, zCoord, 2));
    }
  }

  @Override
  public boolean hasWTNetwork() {
    return true;
  }

  @Override
  public void connectToWallsAround() {
    for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
      TileEntity tileEntity = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
      if (tileEntity instanceof TileWallTeleporter) {
        TileWallTeleporter teleporter = (TileWallTeleporter) tileEntity;
        if (!teleporter.isInvalid()) {
          teleporter.connectToWallsAround();
        }
      }
    }
  }

  @Override
  public void receiveIntegerMessage(byte id, int value) {
    switch (id) {
      case 0: // Button packets
        getWTNetwork().handleButton(value);
        break;
      case 1: // Fuel
        getWTNetwork().fuel = value;
        break;
      case 2: // Mask locked
        getWTNetwork().setMaskLocked(value == 1);
        break;
      case 3: // Use rotation
        getWTNetwork().setUseRotation(value == 1);
        break;
    }
  }

  @Override
  public void receiveRequestMessage(byte id, EntityPlayerMP player) {
    switch (id) {
      case 1: // Destination Data
        getWTNetwork().requestDestinationData(player);
        break;
      case 2: // Fuel
        getWTNetwork().requestFuel(player);
        break;
      default:
        super.receiveRequestMessage(id, player);
    }
  }

  @Override
  public int getSizeInventory() {
    return items.length;
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    return slot < getSizeInventory() ? items[slot] : null;
  }

  @Override
  public ItemStack decrStackSize(int slot, int amount) {
    ItemStack stack = getStackInSlot(slot);
    if (stack != null) {
      if (stack.stackSize <= amount) {
        setInventorySlotContents(slot, null);
      } else {
        stack = stack.splitStack(amount);
        onInventoryChanged();
      }
    }
    return stack;
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int slot) {
    ItemStack stack = getStackInSlot(slot);
    setInventorySlotContents(slot, null);
    return stack;
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack stack) {
    items[slot] = stack;
    onInventoryChanged();
  }

  @Override
  public String getInventoryName() {
    return StatCollector.translateToLocal("wallteleporters.wallTeleporterBase.name");
  }

  @Override
  public boolean hasCustomInventoryName() {
    return false;
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player) {
    return true;
  }

  @Override
  public void openInventory() {
  }

  @Override
  public void closeInventory() {
  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack stack) {
    switch (slot) {
      case 0:
        return stack.isItemEqual(CompareStacks.gpsChipLinked);
      case 1:
        return TeleporterFuelRegistry.getFuelAmount(stack) > 0;
      default:
        return false;
    }
  }

  public void onInventoryChanged() {
    ItemStack fuelStack = getStackInSlot(1);
    if (fuelStack != null) {
      int fuelAmount = TeleporterFuelRegistry.getFuelAmount(fuelStack);
      if (fuelAmount > 0 && getWTNetwork().fuel + fuelAmount <= WallTeleporterNetwork.MAX_TRIPS) {
        getWTNetwork().addFuel(fuelAmount);
        fuelStack.stackSize--;
        if (fuelStack.stackSize < 1) setInventorySlotContents(1, null);
        onInventoryChanged();
      }
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    network.writeToNBT(compound);
    InventoryHelpers.writeInventoryToNBT(compound, this);
    super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    network.readFromNBT(compound);
    InventoryHelpers.readInventoryFromNBT(compound, this);
    super.readFromNBT(compound);
  }

  @Override
  protected void setDefaultMask() {
    this.mask = new ItemStack(BlockRegister.wallTeleporterBase);
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
    return new int[]{0, 1};
  }

  @Override
  public boolean canInsertItem(int slot, ItemStack stack, int side) {
    return isItemValidForSlot(slot, stack);
  }

  @Override
  public boolean canExtractItem(int slot, ItemStack stack, int side) {
    return slot != 1;
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound compound = new NBTTagCompound();
    compound.setBoolean("MaskLocked", getWTNetwork().maskLocked);
    return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, compound);
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    NBTTagCompound compound = pkt.func_148857_g();
    getWTNetwork().maskLocked = compound.getBoolean("MaskLocked");
  }
}
