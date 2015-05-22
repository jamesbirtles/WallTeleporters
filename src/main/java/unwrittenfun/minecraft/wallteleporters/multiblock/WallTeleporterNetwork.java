package unwrittenfun.minecraft.wallteleporters.multiblock;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import unwrittenfun.minecraft.commonfun.network.messages.MessageTileInteger;
import unwrittenfun.minecraft.wallteleporters.WallTeleporters;
import unwrittenfun.minecraft.wallteleporters.helpers.CompareStacks;
import unwrittenfun.minecraft.wallteleporters.helpers.TeleportationHelper;
import unwrittenfun.minecraft.wallteleporters.network.messages.MessageTileDestination;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporter;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterBase;

import java.util.ArrayList;

/**
 * Author: James Birtles
 */
public class WallTeleporterNetwork {
  public static final int MAX_TRIPS = 18;

  public ArrayList<TileWallTeleporter> walls = new ArrayList<>();
  public TileWallTeleporterBase base;
  public String destinationName;
  public int destinationWorldId;
  public float[] destinationData;
  public int fuel = 0;
  public int cooldown = 0;
  public boolean maskLocked = false;
  public boolean useRotation = false;

  public WallTeleporterNetwork(TileWallTeleporterBase base) {
    this.base = base;
  }

  public void add(TileWallTeleporter wallTeleporterBlock) {
    wallTeleporterBlock.setWTNetwork(this);
    if (!walls.contains(wallTeleporterBlock)) {
      walls.add(wallTeleporterBlock);
    }
  }

  public void refreshNetwork() {
    base.setWTNetwork(null);
    if (!base.isInvalid()) {
      WallTeleporterNetwork network = new WallTeleporterNetwork(base);
      network.destinationData = destinationData;
      network.destinationName = destinationName;
      network.destinationWorldId = destinationWorldId;
      network.fuel = fuel;
      base.setWTNetwork(network);
    }

    for (TileWallTeleporter wall : walls) {
      wall.setWTNetwork(null);
    }

    for (TileWallTeleporter wall : walls) {
      if (!wall.isInvalid()) {
        wall.connectToWallsAround();
      }
    }
  }

  public boolean hasDestination() {
    return destinationData != null;
  }

  public void entityCollided(Entity entity) {
    if (hasDestination() && fuel > 0 && cooldown == 0) {
      entity.worldObj.playSoundAtEntity(entity, "wallteleporters:woosh", 1, 1.5f);
      if (entity instanceof EntityPlayerMP) {
        TeleportationHelper.teleportPlayerTo((EntityPlayerMP) entity, destinationWorldId, destinationData[0], destinationData[1], destinationData[2], useRotation ? destinationData[3] : entity.rotationYaw, entity.rotationPitch);
      } else {
        TeleportationHelper.teleportEntityTo(entity, destinationWorldId, destinationData[0], destinationData[1], destinationData[2], useRotation ? destinationData[3] : entity.rotationYaw, entity.rotationPitch);
      }

      setFuel(fuel - 1);
      base.onInventoryChanged();
      cooldown = 10;
      entity.worldObj.playSoundAtEntity(entity, "wallteleporters:woosh", 1, 1.5f);
    }
  }

  public void handleButton(int id) {
    switch (id) {
      case 0:
        setDestinationFromChip();
        break;
      case 1:
        setMaskLocked(!maskLocked);
        break;
      case 2:
        setUseRotation(!useRotation);
        break;
    }
  }

  private void setDestinationFromChip() {
    if (!base.getWorldObj().isRemote) {
      ItemStack chip = base.getStackInSlot(0);
      if (chip == null) {
        destinationName = null;
        destinationData = null;
        destinationWorldId = 0;
      } else if (chip.isItemEqual(CompareStacks.gpsChipLinked)) {
        NBTTagCompound locationData = chip.getTagCompound().getCompoundTag("LocationData");
        destinationName = locationData.getString("worldName");
        destinationWorldId = locationData.getInteger("worldId");
        destinationData = new float[4];
        destinationData[0] = locationData.getFloat("locationX");
        destinationData[1] = locationData.getFloat("locationY");
        destinationData[2] = locationData.getFloat("locationZ");
        destinationData[3] = locationData.getFloat("rotationYaw");
      }
      WallTeleporters.networkRegister.wrapper.sendToDimension(getDestinationPacket(), base.getWorldObj().provider.dimensionId);
    }
  }

  public void requestDestinationData(EntityPlayerMP player) {
    WallTeleporters.networkRegister.wrapper.sendTo(getDestinationPacket(), player);
  }

  public MessageTileDestination getDestinationPacket() {
    return MessageTileDestination.messageFrom(base.getWorldObj(), base.xCoord, base.yCoord, base.zCoord, destinationName, destinationWorldId, destinationData);
  }

  public void requestFuel(EntityPlayerMP player) {
    WallTeleporters.networkRegister.wrapper.sendTo(getFuelPacket(), player);
  }

  public MessageTileInteger getFuelPacket() {
    return MessageTileInteger.messageFrom(base.getWorldObj(), base.xCoord, base.yCoord, base.zCoord, 1, fuel);
  }

  public void writeToNBT(NBTTagCompound compound) {
    if (destinationData != null) {
      compound.setString("destinationName", destinationName);
      compound.setInteger("destinationWorldId", destinationWorldId);

      compound.setFloat("destinationData0", destinationData[0]);
      compound.setFloat("destinationData1", destinationData[1]);
      compound.setFloat("destinationData2", destinationData[2]);
      compound.setFloat("destinationData3", destinationData[3]);
    }

    compound.setInteger("fuel", fuel);
    compound.setBoolean("maskLocked", maskLocked);
  }

  public void readFromNBT(NBTTagCompound compound) {
    if (compound.hasKey("destinationData0")) {
      destinationName = compound.getString("destinationName");
      destinationWorldId = compound.getInteger("destinationWorldId");
      destinationData = new float[4];
      destinationData[0] = compound.getFloat("destinationData0");
      destinationData[1] = compound.getFloat("destinationData1");
      destinationData[2] = compound.getFloat("destinationData2");
      destinationData[3] = compound.getFloat("destinationData3");
    }
    fuel = compound.getInteger("fuel");
    maskLocked = compound.getBoolean("maskLocked");
    useRotation = compound.getBoolean("useRotation");
  }

  public void setFuel(int fuel) {
    this.fuel = fuel;
    if (base.hasWorldObj() && !base.getWorldObj().isRemote) {
      WallTeleporters.networkRegister.wrapper.sendToDimension(getFuelPacket(), base.getWorldObj().provider.dimensionId);
    }
  }

  public void addFuel(int fuelAmount) {
    setFuel(Math.min(MAX_TRIPS, fuel + fuelAmount));
  }


  public void setMaskLocked(boolean locked) {
    this.maskLocked = locked;

    if (!base.getWorldObj().isRemote) {
      WallTeleporters.networkRegister.wrapper.sendToDimension(MessageTileInteger.messageFrom(base.getWorldObj(), base.xCoord, base.yCoord, base.zCoord, 2, maskLocked ? 1 : 0), base.getWorldObj().provider.dimensionId);
    }
  }

  public void setUseRotation(boolean rotation) {
    this.useRotation = rotation;

    if (!base.getWorldObj().isRemote) {
      WallTeleporters.networkRegister.wrapper.sendToDimension(MessageTileInteger.messageFrom(base.getWorldObj(), base.xCoord, base.yCoord, base.zCoord, 3, useRotation ? 1 : 0), base.getWorldObj().provider.dimensionId);
    }
  }
}
