package unwrittenfun.minecraft.wallteleporters.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import unwrittenfun.minecraft.wallteleporters.items.ItemRegister;

import java.util.ArrayList;
import java.util.List;

public class TeleporterFuelRegistry {
  public static List<ItemStack> fuels = new ArrayList<>();
  protected static List<Integer> fuelAmounts = new ArrayList<>();

  public static void registerFuel(ItemStack fuel, int amount) {
    fuels.add(fuel.copy());
    fuelAmounts.add(amount);
  }

  public static int getFuelAmount(ItemStack fuel) {
    for (int index = 0; index < fuels.size(); index++) {
      ItemStack compFuel = fuels.get(index);
      if (compFuel.isItemEqual(fuel)) {
        return fuelAmounts.get(index);
      }
    }

    return 0;
  }

  public static void registerDefaults() {
    registerFuel(new ItemStack(Items.ender_pearl), 18);
    registerFuel(new ItemStack(ItemRegister.enderEssence), 2);
  }
}
