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
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.commonfun.helpers.InventoryHelpers;
import unwrittenfun.minecraft.wallteleporters.Config;
import unwrittenfun.minecraft.wallteleporters.ModInfo;
import unwrittenfun.minecraft.wallteleporters.WallTeleporters;
import unwrittenfun.minecraft.wallteleporters.helpers.CompareStacks;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporter;
import unwrittenfun.minecraft.wallteleporters.tiles.TileWallTeleporterBase;

public class BlockWallTeleporterBase extends BlockContainer {
  public IIcon[] icons;

  public BlockWallTeleporterBase(String key) {
    super(Material.iron);
    setCreativeTab(WallTeleporters.creativeTab);
    setBlockName(key);
    setBlockTextureName(ModInfo.RESOURCE_LOCATION + ":" + key);
    setHardness(2F);
  }

  public static int connectedSidesForDelta(int side, IBlockAccess world, int x, int y, int z, int dx, int dy, int dz) {
    int connectedSides = 0;
    TileEntity tileEntity = world.getTileEntity(x + dx, y + dy, z + dz);
    if (tileEntity instanceof TileWallTeleporter) {
      TileWallTeleporter wall = (TileWallTeleporter) tileEntity;
      if (wall.mask.isItemEqual(CompareStacks.wallTeleporter) || wall.mask.isItemEqual(CompareStacks.wallTeleporterBase)) {
        if ((dx == 1 && side == 2) || (dx == -1 && (side == 3 || side == 1 || side == 0))
            || (dz == 1 && side == 5) || (dz == -1 && side == 4)) {
          connectedSides = connectedSides | 1;
        } else if ((dx == -1 && side == 2) || (dx == 1 && (side == 3 || side == 1 || side == 0))
            || (dz == -1 && side == 5) || (dz == 1 && side == 4)) {
          connectedSides = connectedSides | 2;
        }

        if (dy == -1 || (dz == 1 && (side == 1 || side == 0))) {
          connectedSides = connectedSides | 8;
        } else if (dy == 1 || (dz == -1 && (side == 1 || side == 0))) {
          connectedSides = connectedSides | 4;
        }
      }
    }
    return connectedSides;
  }

  @Override
  public void registerBlockIcons(IIconRegister register) {
    icons = new IIcon[16];

    icons[0] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/0"); // None
    icons[1] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/1"); // Left
    icons[2] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/2"); // Right
    icons[3] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/3"); // Left + Right
    icons[4] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/4"); // Top
    icons[5] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/5"); // Top + Left
    icons[6] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/6"); // Top + Right
    icons[7] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/7"); // Top + Left + Right
    icons[8] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/8"); // Bottom
    icons[9] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/9"); // Bottom + Left
    icons[10] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/10"); // Bottom + Right
    icons[11] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/11"); // Bottom + Left + Right
    icons[12] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/12"); // Top + Bottom
    icons[13] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/13"); // Top + Bottom + Left
    icons[14] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/14"); // Top + Bottom + Right
    icons[15] = register.registerIcon(ModInfo.RESOURCE_LOCATION + ":teleporterBase/15"); // Top + Bottom + Left + Right

    blockIcon = icons[0];
  }

  @Override
  public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    TileWallTeleporter teleporter = (TileWallTeleporter) world.getTileEntity(x, y, z);
    ItemStack mask = teleporter.mask;
    if (mask.isItemEqual(CompareStacks.wallTeleporter) || mask.isItemEqual(CompareStacks.wallTeleporterBase)) {
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
    TileWallTeleporterBase teleporterBase = (TileWallTeleporterBase) world.getTileEntity(x, y, z);
    if (teleporterBase != null && teleporterBase.getWTNetwork().hasDestination() && (Config.disableFuel || teleporterBase.getWTNetwork().fuel > 0))
      return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
    return super.getCollisionBoundingBoxFromPool(world, x, y, z);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int idk) {
    return new TileWallTeleporterBase();
  }

  @Override
  public boolean canPlaceBlockAt(World world, int x, int y, int z) {
    for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
      TileEntity tileEntity = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
      if (tileEntity instanceof TileWallTeleporter) {
        TileWallTeleporter teleporter = (TileWallTeleporter) tileEntity;
        if (teleporter.hasWTNetwork()) {
          return false;
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
        if (!teleporter.getWTNetwork().maskLocked && !(teleporter.mask.isItemEqual(CompareStacks.wallTeleporterBase) && player.getHeldItem().isItemEqual(CompareStacks.wallTeleporter)) && !teleporter.mask.isItemEqual(player.getHeldItem())) {
          teleporter.setMask(player.getHeldItem().copy());
          return true;
        }
      }

      if (!player.isSneaking() && (player.getHeldItem() == null || !player.getHeldItem().isItemEqual(CompareStacks.wallTeleporter))) {
        FMLNetworkHandler.openGui(player, WallTeleporters.instance, 0, world, x, y, z);
        return true;
      }
    }

    return false;
  }

  @Override
  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
    if (!world.isRemote) {
      TileWallTeleporterBase teleporterBase = (TileWallTeleporterBase) world.getTileEntity(x, y, z);
      if (teleporterBase.hasWTNetwork()) teleporterBase.getWTNetwork().entityCollided(entity);
    }
    super.onEntityCollidedWithBlock(world, x, y, z, entity);
  }

  @Override
  public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
    if (!world.isRemote) {
      InventoryHelpers.dropInventory(world, x, y, z);
    }
    super.breakBlock(world, x, y, z, block, meta);
  }

  @Override
  public int getRenderBlockPass() {
    return 1;
  }

  @Override
  public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
    Block adjBlock = world.getBlock(x, y, z);
    return !(adjBlock == BlockRegister.wallTeleporterWall);
  }
}
