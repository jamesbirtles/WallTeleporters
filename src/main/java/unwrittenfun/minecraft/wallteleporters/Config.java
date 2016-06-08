package unwrittenfun.minecraft.wallteleporters;

import java.io.File;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config
{
	public static boolean disableFuel = false;
	public static boolean disableDimHop = false;
	public static Configuration configuration;
	
	public static void init(File file)
	{
		configuration = new Configuration(file);
		readConfig();
	}
	
	public static void readConfig()
	{
		disableFuel = configuration.getBoolean("disableFuel", Configuration.CATEGORY_GENERAL, false, "Set to true if you would like to disable the need for fuel on the teleporters");
		disableDimHop = configuration.getBoolean("disableDimHop", Configuration.CATEGORY_GENERAL, false, "Disables interdimensional teleportation");
		
		if(configuration.hasChanged())
		{
			configuration.save();
		}
	}
	
	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event)
	{
		if(event.modID.equalsIgnoreCase(ModInfo.MOD_ID))
		{
			readConfig();
		}
	}
}
