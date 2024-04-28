package org.valkyrienskies.vs_rnd

import dev.architectury.networking.NetworkChannel
import net.minecraft.resources.ResourceLocation
import org.valkyrienskies.vs_rnd.networking.TestBlockPacket

object  VSRndPackets {

    val CHANNEL: NetworkChannel = NetworkChannel.create(ResourceLocation(VSRndMod.MOD_ID, "networking_channel"))


   val TEST_BLOCK_PACKET = CHANNEL.register(TestBlockPacket::class.java,TestBlockPacket::encode,::TestBlockPacket,TestBlockPacket::apply)



}