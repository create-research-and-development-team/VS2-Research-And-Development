package org.valkyrienskies.vs_rnd.networking

import dev.architectury.networking.NetworkManager
import net.minecraft.client.Minecraft
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.FriendlyByteBuf
import org.joml.Vector3d
import java.util.function.Supplier

class TestBlockPacket() {



    var lpos: ArrayList<Vector3d> = ArrayList<Vector3d>()

    constructor(list:ArrayList<Vector3d>) : this() {
        this.lpos = list
    }

    constructor(buf: FriendlyByteBuf) : this() {
        val size = buf.readInt()
        for (i in 1..size) {
            lpos.add(Vector3d(buf.readDouble(),buf.readDouble(),buf.readDouble()))
        }

    }

    fun encode(buf: FriendlyByteBuf) {
        buf.writeInt(lpos.size)

        lpos.forEach { v -> buf.writeDouble(v.x); buf.writeDouble(v.y); buf.writeDouble(v.z) }
    }

    fun apply(contextSupplier: Supplier<NetworkManager.PacketContext?>) {

        lpos.forEach { v->
            Minecraft.getInstance().level?.addParticle(ParticleTypes.DRAGON_BREATH,v.x+0.5,v.y+0.5,v.z+0.5,0.0,0.0,0.0)

        }

    }
}
