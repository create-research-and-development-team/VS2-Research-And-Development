package org.valkyrienskies.vs_rnd

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.Registry
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import org.valkyrienskies.vs_rnd.block.TestBlock


object  VSRndBlocks {
    private val BLOCKS = DeferredRegister.create(VSRndMod.MOD_ID, Registry.BLOCK_REGISTRY)

    @JvmField var TEST_BLOCK = BLOCKS.register("test_block") { TestBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f))}



    fun register() {
        BLOCKS.register()

    }

    fun registerItems(items: DeferredRegister<Item>) {
        BLOCKS.forEach {
            items.register(it.id) { BlockItem(it.get(), Item.Properties().tab(CreativeModeTab.TAB_MISC)) }
        }
    }


}