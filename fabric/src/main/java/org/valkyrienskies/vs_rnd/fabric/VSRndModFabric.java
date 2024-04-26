package org.valkyrienskies.vs_rnd.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import org.valkyrienskies.vs_rnd.VSRndMod;
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric;

public class VSRndModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // force VS2 to load before eureka
        new ValkyrienSkiesModFabric().onInitialize();

        VSRndMod.init();
    }

    @Environment(EnvType.CLIENT)
    public static class Client implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
            VSRndMod.initClient();
        }
    }
}
