package org.valkyrienskies.vs_rnd

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.util.datafix.fixes.References
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.vs_rnd.block.TestBlockEntity


object VSRndBlockEntities {
    private val BLOCKENTITIES = DeferredRegister.create(VSRndMod.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY)

    var TEST_BLOCK_ENTITY = VSRndBlocks.TEST_BLOCK makePair ::TestBlockEntity byName "test_block_entity"

    private infix fun <T: BlockEntity, TT: Block> RegistrySupplier<TT>.makePair(blockEntity: (BlockPos, BlockState) -> T) = Pair(this, { bp: BlockPos, bs: BlockState -> blockEntity(bp, bs)})
    private infix fun <T: BlockEntity, TT: Block> Pair<RegistrySupplier<TT>, (BlockPos, BlockState) -> T>.byName(name: String): RegistrySupplier<BlockEntityType<T>> =
        BLOCKENTITIES.register(name) {
            val type = Util.fetchChoiceType(References.BLOCK_ENTITY, name)

            BlockEntityType.Builder.of(
                this.second,
                this.first.get()
            ).build(type)
        }

    fun register() {
        BLOCKENTITIES.register()
    }
}