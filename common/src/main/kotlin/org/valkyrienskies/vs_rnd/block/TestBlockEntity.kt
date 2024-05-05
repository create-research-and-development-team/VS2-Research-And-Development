package org.valkyrienskies.vs_rnd.block

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import org.joml.Vector3d
import org.joml.Vector3i
import org.valkyrienskies.core.api.ships.LoadedServerShip
import org.valkyrienskies.core.api.ships.datastructures.ShipConnDataAttachment
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.core.impl.hooks.VSEvents
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.vs_rnd.VSRndBlockEntities
import org.valkyrienskies.vs_rnd.VSRndPackets
import org.valkyrienskies.vs_rnd.networking.TestBlockPacket

class TestBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(VSRndBlockEntities.TEST_BLOCK_ENTITY.get(), pos, blockState) {


    val shipIdThisIsIn: Long? = null
    val airPocketIdThisIn: Int? = null

    init {
        load(CompoundTag())
        VSEvents.airPocketModifyEvent.on {
                (shipId, airPocketId, removed), _ ->
                println(airPocketId)



        }
    }


    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)

    }

    override fun load(tag: CompoundTag) {
        super.load(tag)

    }


    fun use(
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

        val offsets = arrayListOf(
            Vector3i(1,0,0),
            Vector3i(-1,0,0),
            Vector3i(0,1,0),
            Vector3i(0,-1,0),
            Vector3i(0,0,1),
            Vector3i(0,0,-1)
        )

        val walls = mutableListOf<Vector3i>()
        val innards = arrayListOf<Vector3d>()

        pocket.pocket.forEach { (apos, vertex) ->
            val wpos = shipThisIsIn.transform.shipToWorld.transformPosition(Vector3d(apos.x().toDouble(),apos.y().toDouble(),apos.z().toDouble()))
            println(wpos)

            offsets.forEach { offset ->
                val opos = Vector3i(0,0,0)
                apos.add(offset,opos)

                if (pocket.pocket[opos] ==null) walls.add(opos)
            }

            innards.add(shipThisIsIn.transform.shipToWorld.transformPosition(Vector3d(apos.x().toDouble(),apos.y().toDouble(),apos.z().toDouble())))
        }
        walls.forEach { p ->
            //println(level.getBlockState(BlockPos(p.x,p.y,p.z)).block.name)
            //println(shipThisIsIn.transform.shipToWorld.transformPosition(Vector3d(p.x.toDouble(),p.y.toDouble(),p.z.toDouble())))
        }
        VSRndPackets.CHANNEL.sendToPlayer<TestBlockPacket>(player as ServerPlayer, TestBlockPacket(innards))

        return InteractionResult.SUCCESS
    }
}