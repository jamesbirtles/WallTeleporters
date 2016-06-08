package unwrittenfun.minecraft.wallteleporters.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import unwrittenfun.minecraft.wallteleporters.blocks.BlockRegister;

public class TileWallTeleporterWall extends TileWallTeleporter implements IInventory, ISidedInventory
{
	@Override
	public void connectToWallsAround()
	{
		for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tileEntity = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
			if(tileEntity instanceof TileWallTeleporter)
			{
				TileWallTeleporter teleporter = (TileWallTeleporter)tileEntity;
				if(!teleporter.isInvalid() && teleporter.hasWTNetwork() && teleporter.getWTNetwork() != getWTNetwork())
				{
					teleporter.getWTNetwork().add(this);
				}
			}
		}
		
		if(hasWTNetwork())
		{
			for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
			{
				TileEntity tileEntity = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
				if(tileEntity instanceof TileWallTeleporter)
				{
					TileWallTeleporter teleporter = (TileWallTeleporter)tileEntity;
					if(!teleporter.isInvalid() && !teleporter.hasWTNetwork())
					{
						teleporter.connectToWallsAround();
					}
				}
			}
		}
	}
	
	@Override
	protected void setDefaultMask()
	{
		mask = new ItemStack(BlockRegister.wallTeleporterWall);
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		if(network != null)
		{
			return network.base.getAccessibleSlotsFromSide(side);
		}
		return new int[0];
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side)
	{
		if(network != null)
		{
			return network.base.canInsertItem(slot, stack, side);
		}
		return false;
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side)
	{
		if(network != null)
		{
			return network.base.canExtractItem(slot, stack, side);
		}
		return false;
	}
	
	@Override
	public int getSizeInventory()
	{
		if(network != null)
		{
			return network.base.getSizeInventory();
		}
		return 0;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if(network != null)
		{
			return network.base.getStackInSlot(slot);
		}
		return null;
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amout)
	{
		if(network != null)
		{
			return network.base.decrStackSize(slot, amout);
		}
		return null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if(network != null)
		{
			return network.base.getStackInSlot(slot);
		}
		return null;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		if(network != null)
		{
			network.base.setInventorySlotContents(slot, stack);
		}
	}
	
	@Override
	public String getInventoryName()
	{
		if(network != null)
		{
			return network.base.getInventoryName();
		}
		return null;
	}
	
	@Override
	public boolean hasCustomInventoryName()
	{
		if(network != null)
		{
			return network.base.hasCustomInventoryName();
		}
		return false;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		if(network != null)
		{
			return network.base.getInventoryStackLimit();
		}
		return 0;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if(network != null)
		{
			return network.base.isUseableByPlayer(player);
		}
		return false;
	}
	
	@Override
	public void openInventory()
	{
		if(network != null)
		{
			network.base.openInventory();
		}
	}
	
	@Override
	public void closeInventory()
	{
		if(network != null)
		{
			network.base.closeInventory();
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		if(network != null)
		{
			return network.base.isItemValidForSlot(slot, stack);
		}
		return false;
	}
}
