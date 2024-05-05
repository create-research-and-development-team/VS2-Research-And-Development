package org.valkyrienskies.vs_rnd

object VSRndMod {
    const val MOD_ID = "vs_rnd"

    @JvmStatic
    fun init() {
        VSRndBlocks.register()
        VSRndBlockEntities.register()
    }

    @JvmStatic
    fun initClient() {
    }
}
