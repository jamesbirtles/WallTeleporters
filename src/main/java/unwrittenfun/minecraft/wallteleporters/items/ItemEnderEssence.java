package unwrittenfun.minecraft.wallteleporters.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.wallteleporters.ModInfo;
import unwrittenfun.minecraft.wallteleporters.WallTeleporters;

import java.util.Collections;
import java.util.List;

public class ItemEnderEssence extends ItemFun {
  public ItemEnderEssence(String key) {
    super(key, ModInfo.RESOURCE_LOCATION);
    setCreativeTab(WallTeleporters.creativeTab);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean bool) {
    Collections.addAll(lines, getLinesFromLang("wallteleporters.text.enderEssence"));
  }
}
