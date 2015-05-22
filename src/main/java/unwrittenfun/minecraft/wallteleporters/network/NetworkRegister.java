package unwrittenfun.minecraft.wallteleporters.network;

import unwrittenfun.minecraft.commonfun.network.CommonNetworkRegister;
import unwrittenfun.minecraft.wallteleporters.ModInfo;
import unwrittenfun.minecraft.wallteleporters.network.handlers.HandlerTileDestination;
import unwrittenfun.minecraft.wallteleporters.network.messages.MessageTileDestination;

public class NetworkRegister extends CommonNetworkRegister {
  public NetworkRegister() {
    super(ModInfo.MOD_ID);

    registerMessageClient(HandlerTileDestination.class, MessageTileDestination.class);
//    wrapper.registerMessage(HandlerTileDestination.class, MessageTileDestination.class, 2, Side.CLIENT);
  }
}
