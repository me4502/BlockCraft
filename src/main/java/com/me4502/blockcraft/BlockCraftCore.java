package com.me4502.blockcraft;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import javax.annotation.Nullable;

@IFMLLoadingPlugin.Name(value = "BlockCraftCore")
public class BlockCraftCore implements IFMLLoadingPlugin {

    public BlockCraftCore() {
        MixinBootstrap.init();

        try {
            Mixins.addConfiguration("mixins.blockcraft.json");
        } catch(Throwable t) {
            t.printStackTrace();
            ClassLoader cl = this.getClass().getClassLoader();
            URL[] urls = ((URLClassLoader)cl).getURLs();
            for(URL url: urls){
                System.out.println(url.getFile());
            }
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return "com.me4502.blockcraft.BlockCraftAccessTransformer";
    }
}
