package unwrittenfun.minecraft.wallteleporters.network.messages;

import net.minecraft.world.World;

public class MessageTileRequest extends MessageTileCoords {
  public static MessageTileRequest messageFrom(World worldObj, int xCoord, int yCoord, int zCoord, int id) {
    MessageTileRequest message = new MessageTileRequest();
    message.worldId = worldObj.provider.dimensionId;
    message.x = xCoord;
    message.y = yCoord;
    message.z = zCoord;
    message.id = (byte) id;
    return message;
  }
}