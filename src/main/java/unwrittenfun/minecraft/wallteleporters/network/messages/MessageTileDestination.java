package unwrittenfun.minecraft.wallteleporters.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;

public class MessageTileDestination extends MessageTileCoords
{
	public String destinationName;
	public int destinationWorldId;
	public float[] destinationData;
	
	public static MessageTileDestination messageFrom(World worldObj, int xCoord, int yCoord, int zCoord, String destName, int destWorldId, float[] destData)
	{
		MessageTileDestination message = new MessageTileDestination();
		message.worldId = worldObj.provider.dimensionId;
		message.x = xCoord;
		message.y = yCoord;
		message.z = zCoord;
		message.id = 0;
		message.destinationName = destName;
		message.destinationWorldId = destWorldId;
		message.destinationData = destData;
		return message;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		super.fromBytes(buf);
		destinationName = ByteBufUtils.readUTF8String(buf);
		destinationWorldId = buf.readInt();
		destinationData = new float[4];
		destinationData[0] = buf.readFloat();
		destinationData[1] = buf.readFloat();
		destinationData[2] = buf.readFloat();
		destinationData[3] = buf.readFloat();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		super.toBytes(buf);
		if(destinationName == null)
		{
			destinationName = "";
		}
		ByteBufUtils.writeUTF8String(buf, destinationName);
		buf.writeInt(destinationWorldId);
		
		if(destinationData == null)
		{
			destinationData = new float[4];
			destinationData[0] = -1;
			destinationData[1] = -1;
			destinationData[2] = -1;
			destinationData[3] = -1;
		}
		buf.writeFloat(destinationData[0]);
		buf.writeFloat(destinationData[1]);
		buf.writeFloat(destinationData[2]);
		buf.writeFloat(destinationData[3]);
	}
}
