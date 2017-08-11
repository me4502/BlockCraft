package com.me4502.blockcraft.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Chunk.class)
public abstract class MixinChunk {

    @Inject(method = "setBlockState", at = @At("HEAD"))
    public void onBlockStateChange(BlockPos pos, IBlockState state) {

    }
}
