package org.valkyrienskies.vs_rnd.block

import net.fabricmc.loader.impl.lib.sat4j.core.Vec
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import org.joml.Vector3d
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
        println("IN POCKET")

        val offsets = arrayListOf(Vector3i(1,0,0),Vector3i(-1,0,0),Vector3i(0,1,0),Vector3i(0,-1,0),Vector3i(0,0,1),Vector3i(0,0,-1))

        val walls = mutableListOf<Vector3i>()
        pocket.pocket.forEach { (apos, vertex) ->
            val wpos = shipThisIsIn.transform.shipToWorld.transformPosition(Vector3d(apos.x().toDouble(),apos.y().toDouble(),apos.z().toDouble()))
            println(wpos)
            
            offsets.forEach { offset ->
                val opos = Vector3i(0,0,0)
                apos.add(offset,opos)

                if (pocket.pocket.get(opos)==null) walls.add(opos)
            }
        }
        walls.forEach { p ->
            //println(level.getBlockState(BlockPos(p.x,p.y,p.z)).block.name)
            //println(shipThisIsIn.transform.shipToWorld.transformPosition(Vector3d(p.x.toDouble(),p.y.toDouble(),p.z.toDouble())))
        }

        return InteractionResult.SUCCESS
    }
}