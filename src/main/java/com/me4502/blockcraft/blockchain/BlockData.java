package com.me4502.blockcraft.blockchain;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class BlockData {
    public BlockPos blockPos;
    public IBlockState blockState;

    public BlockData(BlockPos blockPos, IBlockState blockState) {
        this.blockPos = blockPos;
        this.blockState = blockState;
    }
}
