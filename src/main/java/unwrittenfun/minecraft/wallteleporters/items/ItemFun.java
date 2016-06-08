package unwrittenfun.minecraft.wallteleporters.items;

import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;

public class ItemFun extends Item {
  public String key;

  public ItemFun(String key, String resourceLocation) {
    this.key = key;
    setUnlocalizedName(key);
    setTextureName(resourceLocation + ":" + key);
  }

  public static String[] getLinesFromLang(String address) {
    return StatCollector.translateToLocal(address).split("\\\\n");
  }
}
