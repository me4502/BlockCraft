package com.me4502.blockcraft;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class BlockCraftAccessTransformer extends AccessTransformer {

    public BlockCraftAccessTransformer() throws IOException {
        super("blockcraft_at.cfg");
    }
}
