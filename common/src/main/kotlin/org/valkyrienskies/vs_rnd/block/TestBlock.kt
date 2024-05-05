package org.valkyrienskies.vs_rnd.block

import dev.architectury.networking.NetworkManager
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import org.joml.Vector3d
import org.joml.Vector3i
import org.valkyrienskies.core.api.ships.LoadedServerShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.datastructures.ShipConnDataAttachment
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.core.impl.hooks.VSEvents
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.vs_rnd.VSRndPackets
import org.valkyrienskies.vs_rnd.networking.TestBlockPacket


class TestBlock(properties: Properties) : BaseEntityBlock(properties) {




    override fun use(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {
        val be = level.getBlockEntity(pos) as TestBlockEntity? ?: return InteractionResult.FAIL
        return be.use(state,level,pos,player,hand,hit)

    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return TestBlockEntity(pos, state)
    }
}