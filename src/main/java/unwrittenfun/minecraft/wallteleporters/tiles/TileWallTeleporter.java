package unwrittenfun.minecraft.wallteleporters.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import unwrittenfun.minecraft.wallteleporters.blocks.BlockRegister;
import unwrittenfun.minecraft.wallteleporters.helpers.CompareStacks;
import unwrittenfun.minecraft.wallteleporters.multiblock.WallTeleporterNetwork;

/**
 * Author: James Birtles
 */
public abstract class TileWallTeleporter extends TileEntity {
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

    if (worldObj != null) {
      if (!worldObj.isRemote) {
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
      } else {
        worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
      }
    }
  }

  protected abstract void setDefaultMask();

  public abstract void connectToWallsAround();

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    writeCustomNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    readCustomNBT(compound);
  }

  @Override
  public void invalidate() {
    super.invalidate();
    if (hasWTNetwork()) getWTNetwork().refreshNetwork();
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound compound = new NBTTagCompound();
    writeCustomNBT(compound);
    return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, compound);
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    readCustomNBT(pkt.func_148857_g());
  }

  private void writeCustomNBT(NBTTagCompound compound) {
    NBTTagCompound maskCompound = new NBTTagCompound();
    mask.writeToNBT(maskCompound);
    compound.setTag("Mask", maskCompound);
  }

  private void readCustomNBT(NBTTagCompound compound) {
    if (compound.hasKey("Mask")) setMask(ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Mask")));
  }
}
