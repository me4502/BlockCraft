package com.me4502.blockcraft.blockchain;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class BlockData {
    private String worldName;
    private BlockPos blockPos;
    private IBlockState blockState;

    public BlockData(String worldName, BlockPos blockPos, IBlockState blockState) {
        this.worldName = worldName;
        this.blockPos = blockPos;
        this.blockState = blockState;
    }
}
