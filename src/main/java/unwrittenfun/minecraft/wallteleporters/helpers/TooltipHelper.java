package unwrittenfun.minecraft.wallteleporters.helpers;

import net.minecraft.util.StatCollector;

import java.util.Arrays;
import java.util.List;

public class TooltipHelper {
  public static List<String> listFor(String string) {
    return Arrays.asList(string.split("\\\\n"));
  }

  public static List<String> listForUnloc(String string) {
    return listFor(StatCollector.translateToLocal(string));
  }
}