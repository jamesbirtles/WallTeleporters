package unwrittenfun.minecraft.wallteleporters.helpers;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.lang.reflect.Field;

public class TeleportationHelper {
  public static void teleportEntityTo(Entity entity, int worldId, double x, double y, double z, float yaw, float pitch) {
    if (!entity.worldObj.isRemote && !entity.isDead) {
      if (entity.dimension == worldId) {
        entity.setLocationAndAngles(x, y, z, yaw, pitch);
      } else {
        WorldServer destinationWorld = MinecraftServer.getServer().worldServerForDimension(worldId);

        Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), destinationWorld);
        NBTTagCompound entityData = new NBTTagCompound();
        entity.writeToNBT(entityData);
        newEntity.readFromNBT(entityData);
        newEntity.setLocationAndAngles(x, y, z, yaw, pitch);
        newEntity.dimension = worldId;
        newEntity.forceSpawn = true;
        destinationWorld.spawnEntityInWorld(newEntity);

        if (entity instanceof EntityMinecartContainer) {
          try {
            Field field = EntityMinecartContainer.class.getDeclaredField("dropContentsWhenDead");
            field.setAccessible(true);
            field.set(entity, false);
          } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
          }
        }

        entity.setDead();
        entity.worldObj.removeEntity(entity);
      }
    }
  }

  public static void teleportPlayerTo(EntityPlayerMP player, int worldId, double x, double y, double z, float yaw, float pitch) {
    if (worldId != player.worldObj.provider.dimensionId) {
      int oldDimensionId = player.dimension;
      WorldServer oldWorld = MinecraftServer.getServer().worldServerForDimension(player.dimension);
      player.dimension = worldId;
      WorldServer newWorld = MinecraftServer.getServer().worldServerForDimension(player.dimension);

      player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, newWorld.difficultySetting, newWorld.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
      oldWorld.removePlayerEntityDangerously(player);
      player.isDead = false;

      oldWorld.theProfiler.startSection("placing");
      if (player.isEntityAlive()) {
        player.setLocationAndAngles(x, y, z, yaw, pitch);
        newWorld.spawnEntityInWorld(player);
        newWorld.updateEntityWithOptionalForce(player, false);
      }
      oldWorld.theProfiler.endSection();
      player.setWorld(newWorld);

      MinecraftServer.getServer().getConfigurationManager().func_72375_a(player, oldWorld);
      player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
      player.theItemInWorldManager.setWorld(newWorld);
      MinecraftServer.getServer().getConfigurationManager().updateTimeAndWeatherForPlayer(player, newWorld);
      MinecraftServer.getServer().getConfigurationManager().syncPlayerInventory(player);

      for (Object potionEffect : player.getActivePotionEffects()) {
        player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), (PotionEffect) potionEffect));
      }
      FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDimensionId, worldId);
    } else {
      player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
    }
  }
}
