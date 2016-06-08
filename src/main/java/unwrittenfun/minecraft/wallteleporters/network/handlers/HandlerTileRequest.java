package unwrittenfun.minecraft.wallteleporters.network.handlers;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import unwrittenfun.minecraft.wallteleporters.WallTeleporters;
import unwrittenfun.minecraft.wallteleporters.network.messages.MessageTileRequest;
import unwrittenfun.minecraft.wallteleporters.network.receivers.ITileRequestReceiver;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandlerTileRequest implements IMessageHandler<MessageTileRequest,IMessage>
{
	@Override
	public IMessage onMessage(MessageTileRequest message, MessageContext ctx)
	{
		World world = WallTeleporters.proxy.getWorldForId(message.worldId, ctx.side);
		if(world != null)
		{
			TileEntity tileEntity = world.getTileEntity(message.x, message.y, message.z);
			if(tileEntity instanceof ITileRequestReceiver)
			{
				((ITileRequestReceiver)tileEntity).receiveRequestMessage(message.id, ctx.getServerHandler().playerEntity);
			}
		}
		return null;
	}
}