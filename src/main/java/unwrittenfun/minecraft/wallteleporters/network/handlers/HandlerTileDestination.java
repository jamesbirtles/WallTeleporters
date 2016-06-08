package unwrittenfun.minecraft.wallteleporters.network.handlers;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import unwrittenfun.minecraft.wallteleporters.WallTeleporters;
import unwrittenfun.minecraft.wallteleporters.multiblock.WallTeleporterNetwork;
import unwrittenfun.minecraft.wallteleporters.network.messages.MessageTileDestination;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterBase;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandlerTileDestination implements IMessageHandler<MessageTileDestination,IMessage>
{
	@Override
	public IMessage onMessage(MessageTileDestination message, MessageContext ctx)
	{
		World world = WallTeleporters.proxy.getWorldForId(message.worldId, ctx.side);
		if(world != null)
		{
			TileEntity tileEntity = world.getTileEntity(message.x, message.y, message.z);
			if(tileEntity instanceof TileWallTeleporterBase)
			{
				WallTeleporterNetwork network = ((TileWallTeleporterBase)tileEntity).getWTNetwork();
				if(message.destinationData[1] == -1)
				{
					network.destinationName = null;
					network.destinationWorldId = 0;
					network.destinationData = null;
				} else
				{
					network.destinationName = message.destinationName;
					network.destinationWorldId = message.worldId;
					network.destinationData = message.destinationData;
				}
			}
		}
		return null;
	}
}
