package unwrittenfun.minecraft.wallteleporters.helpers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class InventoryHelpers
{
	public static void dropInventory(World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(tileEntity instanceof IInventory)
		{
			IInventory inventory = (IInventory)tileEntity;
			for(int i = 0; i < inventory.getSizeInventory(); i++)
			{
				ItemStack stack = inventory.getStackInSlotOnClosing(i);
				if(stack != null)
				{
					dropItemStack(stack, world, x, y, z);
				}
			}
		}
	}
	
	public static void dropItemStack(ItemStack stack, World world, int x, int y, int z)
	{
		float spawnX = x + world.rand.nextFloat();
		float spawnY = y + world.rand.nextFloat();
		float spawnZ = z + world.rand.nextFloat();
		EntityItem droppedItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);
		droppedItem.motionX = (-0.5f + world.rand.nextFloat()) * 0.05F;
		droppedItem.motionY = (4f + world.rand.nextFloat()) * 0.05F;
		droppedItem.motionZ = (-0.5f + world.rand.nextFloat()) * 0.05F;
		world.spawnEntityInWorld(droppedItem);
	}
	
	public static int insertStackIntoSlotsInRange(IInventory inventory, ItemStack stack, int slotMin, int slotMax)
	{
		ItemStack item = stack.copy();
		for(int i = slotMin; i < slotMax; i++)
		{
			if(item.stackSize > 0)
			{
				ItemStack slotItem = inventory.getStackInSlot(i);
				if(slotItem == null || (slotItem.stackSize < slotItem.getMaxStackSize() && stack.isItemEqual(slotItem)))
				{
					if(slotItem == null)
					{
						inventory.setInventorySlotContents(i, item.copy());
						item.stackSize = 0;
					} else
					{
						if(slotItem.stackSize + item.stackSize > slotItem.getMaxStackSize())
						{
							item.stackSize -= slotItem.getMaxStackSize() - slotItem.stackSize;
							slotItem.stackSize = slotItem.getMaxStackSize();
						} else
						{
							slotItem.stackSize += item.stackSize;
							item.stackSize = 0;
						}
					}
				}
			}
		}
		
		return item.stackSize;
	}
	
	public static void writeInventoryToNBT(NBTTagCompound compound, IInventory inventory)
	{
		NBTTagList items = new NBTTagList();
		for(byte slot = 0; slot < inventory.getSizeInventory(); slot++)
		{
			ItemStack stack = inventory.getStackInSlot(slot);
			if(stack != null)
			{
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Slot", slot);
				stack.writeToNBT(item);
				items.appendTag(item);
			}
		}
		compound.setTag("InventoryItems", items);
	}
	
	public static void readInventoryFromNBT(NBTTagCompound compound, IInventory inventory)
	{
		NBTTagList items = compound.getTagList("InventoryItems", 10);
		for(int i = 0; i < items.tagCount(); i++)
		{
			NBTTagCompound item = items.getCompoundTagAt(i);
			int slot = item.getByte("Slot");
			inventory.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
		}
	}
	
}
