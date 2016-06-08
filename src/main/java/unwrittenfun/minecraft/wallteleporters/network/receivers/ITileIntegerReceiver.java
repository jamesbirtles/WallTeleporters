package unwrittenfun.minecraft.wallteleporters.network.receivers;

public interface ITileIntegerReceiver
{
	void receiveIntegerMessage(byte id, int value);
}