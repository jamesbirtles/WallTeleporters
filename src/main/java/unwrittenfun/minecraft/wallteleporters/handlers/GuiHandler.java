package unwrittenfun.minecraft.wallteleporters.handlers;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import unwrittenfun.minecraft.wallteleporters.client.gui.GuiWallTeleporter;
import unwrittenfun.minecraft.wallteleporters.containers.ContainerWallTeleporter;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterBase;

public class GuiHandler implements IGuiHandler {
  @Override
  public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    switch (id) {
      case 0: // Wall Teleporter
        if (tileEntity instanceof TileWallTeleporterBase) {
          return new ContainerWallTeleporter(player.inventory, (TileWallTeleporterBase) tileEntity);
        }
        break;
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    switch (id) {
      case 0: // Wall Teleporter
        if (tileEntity instanceof TileWallTeleporterBase) {
          return new GuiWallTeleporter(player.inventory, (TileWallTeleporterBase) tileEntity);
        }
        break;
    }
    return null;
  }
}
