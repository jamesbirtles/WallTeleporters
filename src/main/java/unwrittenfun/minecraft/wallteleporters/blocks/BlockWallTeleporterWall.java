package unwrittenfun.minecraft.wallteleporters.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.wallteleporters.Config;
import unwrittenfun.minecraft.wallteleporters.ModInfo;
import unwrittenfun.minecraft.wallteleporters.WallTeleporters;
import unwrittenfun.minecraft.wallteleporters.helpers.CompareStacks;
import unwrittenfun.minecraft.wallteleporters.multiblock.WallTeleporterNetwork;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporter;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterBase;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterWall;

public class BlockWallTeleporterWall extends BlockContainer {
  public IIcon[] icons;

  public BlockWallTeleporterWall(String key) {
    super(Material.iron);
    setCreativeTab(WallTeleporters.creativeTab);
    setBlockName(key);
    setBlockTextureName(ModInfo.RESOURCE_LOCATION + ":" + key);
    setHardness(2F);
  }

  @Override
  public void registerBlockIcons(IIconRegister register) {
    icons = new IIcon[16];

    icons[0] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/0"); // None
    icons[1] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/1"); // Left
    icons[2] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/2"); // Right
    icons[3] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/3"); // Left + Right
    icons[4] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/4"); // Top
    icons[5] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/5"); // Top + Left
    icons[6] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/6"); // Top + Right
    icons[7] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/7"); // Top + Left + Right
    icons[8] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/8"); // Bottom
    icons[9] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/9"); // Bottom + Left
    icons[10] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/10"); // Bottom + Right
    icons[11] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/11"); // Bottom + Left + Right
    icons[12] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/12"); // Top + Bottom
    icons[13] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/13"); // Top + Bottom + Left
    icons[14] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/14"); // Top + Bottom + Right
    icons[15] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterWall/15"); // Top + Bottom + Left + Right

    blockIcon = icons[0];
  }

  @Override
  public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (!(tileEntity instanceof TileWallTeleporter)) return blockIcon;

    TileWallTeleporter teleporter = (TileWallTeleporter) tileEntity;
    ItemStack mask = teleporter.mask;
    if (mask.isItemEqual(CompareStacks.wallTeleporter)) {
      int connectedSides = 0; // Left 1, Right 2, Up 4, Down 8

      ForgeDirection direction = ForgeDirection.getOrientation(side);
      if (direction.offsetX == 0) {
        for (int dx = -1; dx <= 1; dx += 2) {
          connectedSides = connectedSides | BlockWallTeleporterBase.connectedSidesForDelta(side, world, x, y, z, dx, 0, 0);
        }
      }

      if (direction.offsetY == 0) {
        for (int dy = -1; dy <= 1; dy += 2) {
          connectedSides = connectedSides | BlockWallTeleporterBase.connectedSidesForDelta(side, world, x, y, z, 0, dy, 0);
        }
      }

      if (direction.offsetZ == 0) {
        for (int dz = -1; dz <= 1; dz += 2) {
          connectedSides = connectedSides | BlockWallTeleporterBase.connectedSidesForDelta(side, world, x, y, z, 0, 0, dz);
        }
      }

      return icons[connectedSides];

    } else {
      Block block = Block.getBlockFromItem(mask.getItem());
      return block.getIcon(side, mask.getItemDamage());
    }
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  @Override
  public boolean renderAsNormalBlock() {
    return false;
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (!(tileEntity instanceof TileWallTeleporter)) return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    TileWallTeleporterWall teleporterWall = (TileWallTeleporterWall) tileEntity;
    if (teleporterWall.hasWTNetwork() && teleporterWall.getWTNetwork().hasDestination() && (Config.disableFuel || teleporterWall.getWTNetwork().fuel > 0))
      return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
    return super.getCollisionBoundingBoxFromPool(world, x, y, z);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int idk) {
    return new TileWallTeleporterWall();
  }

  @Override
  public boolean canPlaceBlockAt(World world, int x, int y, int z) {
    WallTeleporterNetwork networkTemp = null;
    for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
      TileEntity tileEntity = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
      if (tileEntity instanceof TileWallTeleporter) {
        TileWallTeleporter teleporter = (TileWallTeleporter) tileEntity;
        if (teleporter.hasWTNetwork()) {
          if (networkTemp == null) {
            networkTemp = teleporter.getWTNetwork();
          } else if (networkTemp != teleporter.getWTNetwork()) {
            return false;
          }
        }
      }
    }
    return super.canPlaceBlockAt(world, x, y, z);
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
    TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof TileWallTeleporter) {
      TileWallTeleporter teleporter = (TileWallTeleporter) tileEntity;
      if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBlock) {
        if (teleporter.hasWTNetwork() && !teleporter.getWTNetwork().maskLocked && !(teleporter.mask.isItemEqual(CompareStacks.wallTeleporter) && player.getHeldItem().isItemEqual(CompareStacks.wallTeleporterBase)) && !teleporter.mask.isItemEqual(player.getHeldItem())) {
          teleporter.setMask(player.getHeldItem().copy());
          return true;
        }
      }

      if (!player.isSneaking() && (player.getHeldItem() == null || (!player.getHeldItem().isItemEqual(CompareStacks.wallTeleporter)) && teleporter.network != null && teleporter.network.base != null)) {
        TileWallTeleporterBase base = teleporter.network.base;
        FMLNetworkHandler.openGui(player, WallTeleporters.instance, 0, world, base.xCoord, base.yCoord, base.zCoord);
        return true;
      }
    }

    return false;
  }

  @Override
  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    if (!world.isRemote) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if (!(tileEntity instanceof TileWallTeleporter)) return;
      TileWallTeleporterWall teleporterWall = (TileWallTeleporterWall) tileEntity;
      if (teleporterWall.hasWTNetwork()) teleporterWall.getWTNetwork().entityCollided(entity);
    }
    super.onEntityCollidedWithBlock(world, x, y, z, entity);
  }

  @Override
  public int getRenderBlockPass() {
    return 1;
  }

  @Override
  public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
    Block adjBlock = world.getBlock(x, y, z);
    return !(adjBlock == BlockRegister.wallTeleporterBase || adjBlock == BlockRegister.wallTeleporterWall);
  }
}
