package unwrittenfun.minecraft.wallteleporters.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import unwrittenfun.minecraft.wallteleporters.CommonProxy;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
	@Override
	public World getWorldForId(int worldId, Side side)
	{
		if(side.isClient())
		{
			if(Minecraft.getMinecraft().theWorld.provider.dimensionId == worldId)
			{
				return Minecraft.getMinecraft().theWorld;
			}
		} else
		{
			return super.getWorldForId(worldId, side);
		}
		
		return null;
	}
}