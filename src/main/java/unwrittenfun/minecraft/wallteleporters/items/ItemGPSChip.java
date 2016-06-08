package unwrittenfun.minecraft.wallteleporters.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import unwrittenfun.minecraft.wallteleporters.ModInfo;
import unwrittenfun.minecraft.wallteleporters.WallTeleporters;

import java.util.List;

public class ItemGPSChip extends ItemFun {
  public IIcon linkedIcon;

  public ItemGPSChip(String key) {
    super(key, ModInfo.RESOURCE_LOCATION);

    setCreativeTab(WallTeleporters.creativeTab);
    setMaxStackSize(1);
  }

  @Override
  public IIcon getIconFromDamage(int meta) {
    return meta == 1 ? linkedIcon : itemIcon;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    if (!world.isRemote) {
      if (stack.getItemDamage() == 0) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());

        NBTTagCompound locationData = new NBTTagCompound();
        locationData.setString("worldName", world.provider.getDimensionName());
        locationData.setInteger("worldId", world.provider.dimensionId);
        locationData.setFloat("locationX", (float) player.posX);
        locationData.setFloat("locationY", (float) player.posY);
        locationData.setFloat("locationZ", (float) player.posZ);
        locationData.setFloat("rotationYaw", player.rotationYaw);
        stack.getTagCompound().setTag("LocationData", locationData);

        stack.setItemDamage(1);
      } else if (stack.getItemDamage() == 1) {
        stack.getTagCompound().removeTag("LocationData");
        stack.setItemDamage(0);
      }
    }

    return stack;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {
    return "item." + key + (stack.getItemDamage() == 1 ? "Linked" : "");
  }

  @SuppressWarnings("unchecked")
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list,
                             boolean bool) {
    super.addInformation(stack, player, list, bool);

    validateStack(stack);
    if (stack.getItemDamage() == 1) {
      NBTTagCompound locationData = stack.getTagCompound().getCompoundTag("LocationData");
      list.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("wallteleporters.info.rightClickUnlink"));
      list.add(StatCollector.translateToLocal("wallteleporters.info.world") + ": " + EnumChatFormatting.WHITE + locationData.getString("worldName"));
      list.add(StatCollector.translateToLocal("wallteleporters.info.coordXSpaced") + ": " + EnumChatFormatting.WHITE + (int) locationData.getFloat("locationX"));
      list.add(StatCollector.translateToLocal("wallteleporters.info.coordYSpaced") + ": " + EnumChatFormatting.WHITE + (int) locationData.getFloat("locationY"));
      list.add(StatCollector.translateToLocal("wallteleporters.info.coordZSpaced") + ": " + EnumChatFormatting.WHITE + (int) locationData.getFloat("locationZ"));
      list.add(StatCollector.translateToLocal("wallteleporters.info.yawSpaced") + ": " + EnumChatFormatting.WHITE + (int) locationData.getFloat("rotationYaw"));
    } else {
      list.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("wallteleporters.info.rightClickLink"));
    }
  }

  @Override
  public void registerIcons(IIconRegister iconRegister) {
    super.registerIcons(iconRegister);
    linkedIcon = iconRegister.registerIcon(getIconString() + "Linked");
  }

  private void validateStack(ItemStack stack) {
    if (stack.getItemDamage() == 1) {
      if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());

      if (!stack.getTagCompound().hasKey("LocationData")) {
        NBTTagCompound locationData = new NBTTagCompound();
        locationData.setString("worldName", "Overworld");
        locationData.setInteger("worldId", 0);
        locationData.setFloat("locationX", 0F);
        locationData.setFloat("locationY", 0F);
        locationData.setFloat("locationZ", 0F);
        locationData.setFloat("rotationYaw", 0F);
        stack.getTagCompound().setTag("LocationData", locationData);
      }
    }
  }
}
