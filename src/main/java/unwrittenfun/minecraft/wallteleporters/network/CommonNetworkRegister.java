package unwrittenfun.minecraft.wallteleporters.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import unwrittenfun.minecraft.wallteleporters.network.handlers.HandlerTileInteger;
import unwrittenfun.minecraft.wallteleporters.network.handlers.HandlerTileRequest;
import unwrittenfun.minecraft.wallteleporters.network.messages.MessageTileInteger;
import unwrittenfun.minecraft.wallteleporters.network.messages.MessageTileRequest;

public class CommonNetworkRegister {
  public SimpleNetworkWrapper wrapper;
  public String modid;
  protected int descriminator = 0;

  public CommonNetworkRegister(String modid) {
    this.modid = modid;
    this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(modid);

    registerMessageCommon(HandlerTileInteger.class, MessageTileInteger.class);
//    registerMessageCommon(HandlerTileStack.class, MessageTileStack.class);
    registerMessageServer(HandlerTileRequest.class, MessageTileRequest.class);
  }

  protected <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> messageType, Side side) {
    wrapper.registerMessage(messageHandler, messageType, descriminator, side);
    descriminator++;
  }

  protected <REQ extends IMessage, REPLY extends IMessage> void registerMessageClient(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> messageType) {
    registerMessage(messageHandler, messageType, Side.CLIENT);
  }

  protected <REQ extends IMessage, REPLY extends IMessage> void registerMessageServer(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> messageType) {
    registerMessage(messageHandler, messageType, Side.SERVER);
  }

  protected <REQ extends IMessage, REPLY extends IMessage> void registerMessageCommon(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> messageType) {
    registerMessageClient(messageHandler, messageType);
    registerMessageServer(messageHandler, messageType);
  }
}