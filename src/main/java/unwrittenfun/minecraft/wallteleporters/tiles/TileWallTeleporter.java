package unwrittenfun.minecraft.wallteleporters.tiles;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import unwrittenfun.minecraft.commonfun.network.messages.MessageTileInteger;
import unwrittenfun.minecraft.commonfun.network.messages.MessageTileRequest;
import unwrittenfun.minecraft.commonfun.network.messages.MessageTileStack;
import unwrittenfun.minecraft.commonfun.network.receivers.ITileIntegerReceiver;
import unwrittenfun.minecraft.commonfun.network.receivers.ITileRequestReceiver;
import unwrittenfun.minecraft.commonfun.network.receivers.ITileStackReceiver;
import unwrittenfun.minecraft.wallteleporters.WallTeleporters;
import unwrittenfun.minecraft.wallteleporters.blocks.BlockRegister;
import unwrittenfun.minecraft.wallteleporters.helpers.CompareStacks;
import unwrittenfun.minecraft.wallteleporters.multiblock.WallTeleporterNetwork;

/**
 * Author: James Birtles
 */
public abstract class TileWallTeleporter extends TileEntity implements ITileStackReceiver, ITileRequestReceiver {
  public WallTeleporterNetwork network;
  public boolean loaded = false;
  public ItemStack mask = new ItemStack(BlockRegister.wallTeleporterWall);

  @Override
  public void updateEntity() {
    if (hasWorldObj() && !loaded) {
      loaded = true;
      onLoaded();
    }
  }

  protected void onLoaded() {
    connectToWallsAround();
    if (worldObj.isRemote) {
      WallTeleporters.networkRegister.wrapper.sendToServer(MessageTileRequest.messageFrom(worldObj, xCoord, yCoord, zCoord, 0));
    }
  }

  public boolean hasWTNetwork() {
    return network != null;
  }

  public WallTeleporterNetwork getWTNetwork() {
    return network;
  }

  public void setWTNetwork(WallTeleporterNetwork wtNetwork) {
    network = wtNetwork;
  }

  public void setMask(ItemStack mask) {
    if (mask.isItemEqual(CompareStacks.wallTeleporterBase) || mask.isItemEqual(CompareStacks.wallTeleporter)) {
      setDefaultMask();
    } else {
      this.mask = mask;
    }

    if (!worldObj.isRemote) {
      WallTeleporters.networkRegister.wrapper.sendToDimension(getMaskStackMessage(), worldObj.provider.dimensionId);
    }
  }

  protected abstract void setDefaultMask();

  private MessageTileStack getMaskStackMessage() {
    return MessageTileStack.messageFrom(worldObj, xCoord, yCoord, zCoord, 0, mask);
  }

  @Override
  public void receiveStackMessage(byte id, ItemStack stack) {
    switch (id) {
      case 0:
        setMask(stack);
        if (worldObj.isRemote) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        break;
    }
  }

  public abstract void connectToWallsAround();

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    NBTTagCompound maskCompound = new NBTTagCompound();
    mask.writeToNBT(maskCompound);
    compound.setTag("Mask", maskCompound);

    super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    if (compound.hasKey("Mask")) mask = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Mask"));
    super.readFromNBT(compound);
  }

  @Override
  public void receiveRequestMessage(byte id, EntityPlayerMP player) {
    switch (id) {
      case 0:
        WallTeleporters.networkRegister.wrapper.sendTo(getMaskStackMessage(), player);
        break;
    }
  }

  @Override
  public void invalidate() {
    super.invalidate();
    if (hasWTNetwork()) getWTNetwork().refreshNetwork();
  }
}
