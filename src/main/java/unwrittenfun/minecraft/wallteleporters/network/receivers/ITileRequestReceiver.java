package unwrittenfun.minecraft.wallteleporters.network.receivers;

import net.minecraft.entity.player.EntityPlayerMP;

public interface ITileRequestReceiver
{
	void receiveRequestMessage(byte id, EntityPlayerMP player);
}