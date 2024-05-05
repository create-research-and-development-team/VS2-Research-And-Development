package org.valkyrienskies.vs_rnd.block

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
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
import org.joml.Vector3ic
import org.valkyrienskies.core.api.datastructures.dynconn.BlockPosVertex
import org.valkyrienskies.core.api.ships.LoadedServerShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.datastructures.ShipConnDataAttachment
import org.valkyrienskies.core.api.ships.getAttachment
import org.valkyrienskies.core.impl.hooks.VSEvents
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.vs_rnd.VSRndBlockEntities
import org.valkyrienskies.vs_rnd.VSRndPackets
import org.valkyrienskies.vs_rnd.networking.TestBlockPacket

class TestBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(VSRndBlockEntities.TEST_BLOCK_ENTITY.get(), pos, blockState) {

    var player: Player? = null
    val offsets = arrayListOf(
        Vector3i(1,0,0),
        Vector3i(-1,0,0),
        Vector3i(0,1,0),
        Vector3i(0,-1,0),
        Vector3i(0,0,1),
        Vector3i(0,0,-1)
    )

    var airPocket: HashMap<Vector3ic,BlockPosVertex> = HashMap()

    init {
        load(CompoundTag())
        VSEvents.airPocketModifyEvent.on {
                (shipId, airPocketId, removed), handler ->
                if (!level!!.isClientSide) { // block entity level can apparently be client sided?
                    val slevel = level as ServerLevel
                    val ship = slevel.shipObjectWorld.loadedShips.getById(shipId) as LoadedServerShip?
                    if (ship != null && ship.id == shipId) {
                        val ap = ship.getAttachment<ShipConnDataAttachment>()?.getAirPocket(airPocketId)
                        print("AP: ")
                        print(ap == null)
                        print(" R: ")
                        print(removed)
                        println()
                        println()
                        if (ap != null) {


                            var mine = false
                            ap.pocket.forEach { (v, _) ->
                                if (v == Vector3i(pos.x, pos.y + 1, pos.z)) {
                                    mine = true

                                }
                            }
                            if (mine) {
                                airPocket = ap.pocket

                                val innards = arrayListOf<Vector3d>()

                                airPocket.forEach { (apos, _) ->
                                    val wpos = ship.transform.shipToWorld.transformPosition(Vector3d(apos.x().toDouble(),apos.y().toDouble(),apos.z().toDouble()))
                                    println(wpos)

                                    offsets.forEach { offset ->
                                        val opos = Vector3i(0,0,0)
                                        apos.add(offset,opos)


                                    }

                                    innards.add(ship.transform.shipToWorld.transformPosition(Vector3d(apos.x().toDouble(),apos.y().toDouble(),apos.z().toDouble())))
                                }

                                if (player!=null) VSRndPackets.CHANNEL.sendToPlayer<TestBlockPacket>(player as ServerPlayer, TestBlockPacket(innards))
                            }

                        }
                    }


                }
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
        this.player = player
        if (level.isClientSide) return InteractionResult.FAIL

        val shipId = level.getShipManagingPos(pos)?.id ?: return InteractionResult.FAIL
        val ship = level.shipObjectWorld.loadedShips.getById(shipId) as ServerShip?
        if (ship == null || airPocket.size == 0) return InteractionResult.FAIL
        val shipThisIsIn = level.shipObjectWorld.loadedShips.getById(ship.id) as LoadedServerShip? ?: return InteractionResult.FAIL





        val walls = arrayListOf<Vector3i>()
        val innards = arrayListOf<Vector3d>()

        airPocket.forEach { (apos, _) ->
            val wpos = shipThisIsIn.transform.shipToWorld.transformPosition(Vector3d(apos.x().toDouble(),apos.y().toDouble(),apos.z().toDouble()))
            println(wpos)

            offsets.forEach { offset ->
                val opos = Vector3i(0,0,0)
                apos.add(offset,opos)

                if (airPocket[opos] ==null) walls.add(opos)
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