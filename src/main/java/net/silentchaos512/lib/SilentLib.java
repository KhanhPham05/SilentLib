package net.silentchaos512.lib;

import com.google.common.collect.Maps;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.silentchaos512.lib.client.key.internal.InternalKeyTracker;
import net.silentchaos512.lib.event.SilentLibClientEvents;
import net.silentchaos512.lib.event.SilentLibCommonEvents;
import net.silentchaos512.lib.gui.GuiHandlerLibF;
import net.silentchaos512.lib.network.NetworkHandlerSL;
import net.silentchaos512.lib.network.internal.MessageChangeGamemode;
import net.silentchaos512.lib.network.internal.MessageLeftClick;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.LogHelper;

import java.util.Locale;
import java.util.Map;

@Mod(modid = SilentLib.MOD_ID, name = SilentLib.MOD_NAME, version = SilentLib.VERSION, dependencies = SilentLib.DEPENDENCIES)
public final class SilentLib {

  public static final String MOD_ID = "silentlib";
  public static final String MOD_NAME = "Silent Lib";
  public static final String VERSION = "2.3.8";
  public static final int BUILD_NUM = 0;
  public static final String DEPENDENCIES = "required-after:forge@[14.23.3.2669,);";

  public static NetworkHandlerSL network;
  public static LogHelper logHelper = new LogHelper(MOD_NAME, BUILD_NUM);

  @Instance(MOD_ID)
  public static SilentLib instance;

  private final Map<String, LocalizationHelper> locHelpers = Maps.newHashMap();

  public LocalizationHelper getLocalizationHelperForMod(String modId) {

    return locHelpers.get(modId.toLowerCase(Locale.ROOT));
  }

  public void registerLocalizationHelperForMod(String modId, LocalizationHelper loc) {

    locHelpers.put(modId.toLowerCase(Locale.ROOT), loc);
  }

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {

    network = new NetworkHandlerSL(MOD_ID);
    network.register(MessageLeftClick.class, Side.SERVER);
    network.register(MessageChangeGamemode.class, Side.SERVER);
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerLibF());

    MinecraftForge.EVENT_BUS.register(new SilentLibCommonEvents());
    if (event.getSide() == Side.CLIENT) {
      MinecraftForge.EVENT_BUS.register(new SilentLibClientEvents());
      MinecraftForge.EVENT_BUS.register(InternalKeyTracker.INSTANCE);
    }
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {

  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {

    // TODO: Add config
//    DataDump.dumpEntityList();
//    DataDump.dumpEnchantments();
//    DataDump.dumpPotionEffects();
//    if ("SL_VERSION".equals(VERSION))
//      DataDump.dumpRecipes();
  }

  @Deprecated
  public static int getMCVersion() {

    return 12;
  }

  public int getBuildNumber() {

    return BUILD_NUM;
  }
}
