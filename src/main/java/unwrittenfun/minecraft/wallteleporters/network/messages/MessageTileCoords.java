package unwrittenfun.minecraft.wallteleporters.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageTileCoords implements IMessage {
  public byte id;
  public int worldId, x, y, z;

  @Override
  public void fromBytes(ByteBuf buf) {
    worldId = buf.readInt();
    x = buf.readInt();
    y = buf.readInt();
    z = buf.readInt();
    id = buf.readByte();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(worldId);
    buf.writeInt(x);
    buf.writeInt(y);
    buf.writeInt(z);
    buf.writeByte(id);
  }
}