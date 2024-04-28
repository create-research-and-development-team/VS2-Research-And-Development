package org.valkyrienskies.vs_rnd.block

import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import org.joml.Vector3i
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.datastructures.ShipConnDataAttachment
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.core.impl.datastructures.ShipConnDataAttachmentImpl
import org.valkyrienskies.mod.common.getShipManagingPos


class TestBlock(properties: Properties) : Block(properties) {

    override fun use(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {


        if (level.isClientSide) return InteractionResult.SUCCESS
        val shipThisIsIn = level.getShipManagingPos(pos) as ServerShip
        println(shipThisIsIn)
        val pocket = shipThisIsIn.getAttachment<ShipConnDataAttachmentImpl>()?.getAirPocketIdFromPoint(Vector3i(pos.x,pos.y,pos.z))
        println(shipThisIsIn.getAttachment<ShipConnDataAttachmentImpl>())
        println(pocket)

        return InteractionResult.SUCCESS
    }
}