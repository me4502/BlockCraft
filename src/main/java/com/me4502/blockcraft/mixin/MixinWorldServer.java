package com.me4502.blockcraft.mixin;

import com.me4502.blockcraft.BlockCraft;
import com.me4502.blockcraft.blockchain.BlockChain;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldServer.class)
public abstract class MixinWorldServer extends World implements IThreadListener {

    int dimensionId;

    protected MixinWorldServer(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
        super(saveHandlerIn, info, providerIn, profilerIn, client);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onConstruct(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId, Profiler profilerIn,
            CallbackInfo callbackInfo) {
        this.dimensionId = dimensionId;
        BlockChain chain = new BlockChain(worldInfo.getWorldName() + "," + dimensionId);
        chain.load();
        BlockCraft.instance.blockChains.put(worldInfo.getWorldName() + "," + dimensionId, chain);
    }

    @Inject(method = "saveLevel", at = @At("HEAD"))
    public void onSave(CallbackInfo callbackInfo) {
        BlockCraft.instance.blockChains.remove(worldInfo.getWorldName() + "," + dimensionId).save();
    }
}
