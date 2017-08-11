package com.me4502.blockcraft;

import com.google.common.eventbus.Subscribe;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BlockCraft.MOD_ID, name = BlockCraft.MOD_NAME, version = BlockCraft.MOD_VERSION)
public class BlockCraft {

    public static final String MOD_ID = "blockcraft";
    public static final String MOD_NAME = "BlockCraft";
    public static final String MOD_VERSION = "1.0";

    @Subscribe
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }
}