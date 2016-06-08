package unwrittenfun.minecraft.wallteleporters.network.handlers;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import unwrittenfun.minecraft.wallteleporters.WallTeleporters;
import unwrittenfun.minecraft.wallteleporters.network.messages.MessageTileInteger;
import unwrittenfun.minecraft.wallteleporters.network.receivers.ITileIntegerReceiver;

public class HandlerTileInteger implements IMessageHandler<MessageTileInteger, IMessage> {
  @Override
  public IMessage onMessage(MessageTileInteger message, MessageContext ctx) {
    World world = WallTeleporters.proxy.getWorldForId(message.worldId, ctx.side);
    if (world != null) {
      TileEntity tileEntity = world.getTileEntity(message.x, message.y, message.z);
      if (tileEntity instanceof ITileIntegerReceiver) {
        ((ITileIntegerReceiver) tileEntity).receiveIntegerMessage(message.id, message.value);
      }
    }
    return null;
  }
}