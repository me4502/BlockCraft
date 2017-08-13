package com.me4502.blockcraft.mixin;

import com.me4502.blockcraft.BlockCraft;
import com.me4502.blockcraft.blockchain.BlockChain;
import com.me4502.blockcraft.blockchain.BlockData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public abstract class MixinChunk {

    @Shadow
    @Final
    private World world;

    @Inject(method = "setBlockState", at = @At("HEAD"))
    public void onBlockStateChange(BlockPos pos, IBlockState state, CallbackInfoReturnable cir) {
        BlockChain blockChain = BlockCraft.instance.blockChains.get(world.getWorldInfo().getWorldName() + "," + world.provider.getDimension());
        blockChain.addBlock(blockChain.generateNextBlock(new BlockData(pos, state)));
    }
}
