package unwrittenfun.minecraft.wallteleporters;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy
{
	public World getWorldForId(int worldId, Side side)
	{
		return MinecraftServer.getServer().worldServerForDimension(worldId);
	}
}