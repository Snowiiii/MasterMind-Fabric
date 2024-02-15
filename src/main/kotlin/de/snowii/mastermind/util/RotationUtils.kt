package de.snowii.mastermind.util

import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import kotlin.math.atan2
import kotlin.math.hypot

object RotationUtils {
    private val mc = MinecraftClient.getInstance()

    var camera_yaw: Float = 0.0F
    var camera_pitch: Float = 0.0F
    var prev_camera_yaw: Float = 0.0F
    var prev_camera_pitch: Float = 0.0F

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

    fun getRotationsToEntity(target: Entity, speed: Float): FloatArray {
        val player = mc.player!!
        val d: Double = target.x - player.x
        val e: Double = target.y - player.eyeY
        val f: Double = target.z - player.z
        val d3 = hypot(d, f)
        val yaw = MathHelper.wrapDegrees((MathHelper.atan2(f, d) * 57.2957763671875).toFloat() - 90.0f)
        val pitch = MathHelper.wrapDegrees((-(MathHelper.atan2(e, d3) * 57.2957763671875)).toFloat())
        return floatArrayOf(yaw, pitch)
    }

    fun getRotationsToBlock(pos: BlockPos, speed: Float): FloatArray {
        val player = mc.player!!
        val d0 = pos.x - player.x
        val d1 = pos.y - player.eyeY
        val d2 = pos.z - player.z
        val d3 = hypot(d0, d2)
        val f = MathHelper.wrapDegrees(atan2(d2, d0)).toFloat() - 90f
        val f1 = -MathHelper.wrapDegrees(atan2(d1, d3)).toFloat()
        return floatArrayOf(updateRotation(player.yaw, f, speed), updateRotation(player.pitch, f1, speed))
    }

    fun limitAngleChange(current: Float, intended: Float): Float {
        val currentWrapped = MathHelper.wrapDegrees(current)
        val intendedWrapped = MathHelper.wrapDegrees(intended)
        val change = MathHelper.wrapDegrees(intendedWrapped - currentWrapped)
        return current + change
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