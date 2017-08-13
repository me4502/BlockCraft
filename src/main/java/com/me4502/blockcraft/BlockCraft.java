package com.me4502.blockcraft;

import com.google.common.eventbus.Subscribe;
import com.me4502.blockcraft.blockchain.BlockChain;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.Map;

@Mod(modid = BlockCraft.MOD_ID, name = BlockCraft.MOD_NAME, version = BlockCraft.MOD_VERSION)
public class BlockCraft {

    public static final String MOD_ID = "blockcraft";
    public static final String MOD_NAME = "BlockCraft";
    public static final String MOD_VERSION = "1.0";

    public static BlockCraft instance;

    public Map<String, BlockChain> blockChains = new HashMap<>();

    @Subscribe
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }
}