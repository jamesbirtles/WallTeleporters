package unwrittenfun.minecraft.wallteleporters;

import net.minecraftforge.common.config.Configuration;

public class Config {
  public static boolean disableFuel = false;

  public static void initConfig(Configuration config) {
    config.load();

    disableFuel = config.getBoolean("disableFuel", "general", false, "Set to true if you would like to disable the need for fuel on the teleporters");

    config.save();
  }
}
