package org.valkyrienskies.vs_rnd.forge

import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import org.valkyrienskies.vs_rnd.VSRndMod
import org.valkyrienskies.vs_rnd.VSRndMod.init
import org.valkyrienskies.vs_rnd.VSRndMod.initClient
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(VSRndMod.MOD_ID)
class VSRndModForge {
    init {
        MOD_BUS.addListener { event: FMLClientSetupEvent? ->
            clientSetup(
                event
            )
        }
        init()
    }

    private fun clientSetup(event: FMLClientSetupEvent?) {
        initClient()
    }

    companion object {
        fun getModBus(): IEventBus = MOD_BUS
    }
}
