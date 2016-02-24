package unwrittenfun.minecraft.wallteleporters;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class CommonProxy {
  public World getWorldForId(int worldId, Side side) {
    return MinecraftServer.getServer().worldServerForDimension(worldId);
  }
}