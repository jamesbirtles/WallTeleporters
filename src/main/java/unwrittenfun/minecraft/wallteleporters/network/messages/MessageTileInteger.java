package unwrittenfun.minecraft.wallteleporters.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

public class MessageTileInteger extends MessageTileCoords
{
	public int value;
	
	public static MessageTileInteger messageFrom(World worldObj, int xCoord, int yCoord, int zCoord, int id, int value)
	{
		MessageTileInteger message = new MessageTileInteger();
		message.worldId = worldObj.provider.dimensionId;
		message.x = xCoord;
		message.y = yCoord;
		message.z = zCoord;
		message.id = (byte)id;
		message.value = value;
		return message;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		super.fromBytes(buf);
		value = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		super.toBytes(buf);
		buf.writeInt(value);
	}
}