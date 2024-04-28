package org.valkyrienskies.vs_rnd.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import org.joml.Vector3i
import org.valkyrienskies.core.api.ships.LoadedServerShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.datastructures.ShipConnDataAttachment
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.shipObjectWorld


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
        val unloaded = level.getShipManagingPos(pos) ?: return InteractionResult.SUCCESS
        val shipThisIsIn = level.shipObjectWorld.loadedShips.getById(unloaded.id) as LoadedServerShip
        val pocket = shipThisIsIn.getAttachment<ShipConnDataAttachment>()?.getAirPocketFromPoint(Vector3i(pos.x,pos.y+1,pos.z)) ?: return InteractionResult.SUCCESS
        pocket.pocket.forEach { t, u ->
            print(t)
            print(" ")
            print(u)
            println()
        }


        return InteractionResult.SUCCESS
    }
}