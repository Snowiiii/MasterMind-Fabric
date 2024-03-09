package de.snowii.mastermind.util

import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

object RotationUtils {
    private val mc = MinecraftClient.getInstance()

    val gcd: Double
        get() {
            val f = mc.options.mouseSensitivity.value * 0.6F.toDouble() + 0.2F.toDouble()
            return f * f * f * 8.0 * 0.15F
        }

    var move_camera: Boolean = false

    var savedYaw: Float = 0.0F
    var savedPitch: Float = 0.0F

    fun rayTrace(yaw: Float, pitch: Float) {
        val player = mc.player!!
        val current_yaw = player.yaw
        val current_pitch = player.pitch
        player.yaw = yaw
        player.pitch = pitch
        mc.gameRenderer.updateTargetedEntity(1.0F)
        player.yaw = current_yaw
        player.pitch = current_pitch
    }

    fun getRotationsTo(vec: Vec3d): FloatArray {
        val player = mc.player!!
        val diffX = vec.x - player.x
        val diffY = vec.y - player.y
        val diffZ = vec.z - player.z

        return floatArrayOf(
            MathHelper.wrapDegrees(Math.toDegrees(atan2(diffZ, diffX)).toFloat() - 90f),
            MathHelper.wrapDegrees((-Math.toDegrees(atan2(diffY, sqrt(diffX * diffX + diffZ * diffZ)))).toFloat())
        )
    }

    fun fixedSensitivity(yaw: Float, pitch: Float): FloatArray {
        val player = mc.player!!

        val deltaYaw = yaw - player.lastYaw
        val deltaPitch = pitch - player.lastPitch

        // proper rounding
        val g1 = (deltaYaw / gcd).roundToInt() * gcd
        val g2 = (deltaPitch / gcd).roundToInt() * gcd

        return floatArrayOf(player.lastYaw + g1.toFloat(), player.lastPitch + g2.toFloat())
    }

    fun setRotation(yaw: Float, pitch: Float) {
        val player = mc.player!!
        player.pitch = pitch
        player.yaw = yaw
        player.headYaw = yaw
        player.pitch = MathHelper.clamp(player.pitch, -90.0f, 90.0f)
        player.prevPitch = pitch
        player.prevYaw = yaw
        player.prevPitch = MathHelper.clamp(player.prevPitch, -90.0f, 90.0f)
        if (player.vehicle != null) {
            player.vehicle!!.onPassengerLookAround(mc.player)
        }
    }

    fun updateRotation(pFrom: Float, pTo: Float, pMaxDelta: Float): Float {
        val f = MathHelper.lerpAngleDegrees(pMaxDelta, pFrom, pTo)
        val f1 = MathHelper.clamp(f, -pMaxDelta, pMaxDelta)
        return pFrom + f1
    }

}