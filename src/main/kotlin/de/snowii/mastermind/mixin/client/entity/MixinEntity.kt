package de.snowii.mastermind.mixin.client.entity

import com.llamalad7.mixinextras.injector.ModifyReturnValue
import de.snowii.mastermind.util.RotationUtils
import net.minecraft.entity.Entity
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.util.math.MathHelper
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.ModifyArg
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Entity::class)
class MixinEntity {

    @Inject(
        method = ["changeLookDirection()V"],
        at = [At(
            "TAIL"
        )],
    )
    fun changeLookDirection(cursorDeltaX: Double, cursorDeltaY: Double, ci: CallbackInfo) {
        val f = cursorDeltaY.toFloat() * 0.15f
        val g = cursorDeltaX.toFloat() * 0.15f
        RotationUtils.camera_pitch += f
        RotationUtils.camera_yaw += g
        RotationUtils.camera_pitch = MathHelper.clamp(RotationUtils.camera_pitch, -90.0f, 90.0f)
        RotationUtils.prev_camera_pitch += f
        RotationUtils.prev_camera_yaw += g
        RotationUtils.prev_camera_pitch = MathHelper.clamp(RotationUtils.prev_camera_pitch, -90.0f, 90.0f)
    }

    @Inject(
        method = ["resetPosition()V"],
        at = [At(
            "TAIL"
        )],
    )
    fun resetPosition(ci: CallbackInfo) {
        RotationUtils.prev_camera_yaw = RotationUtils.camera_yaw
        RotationUtils.prev_camera_pitch = RotationUtils.camera_yaw
    }



    @Inject(
        method = ["onSpawnPacket()V"],
        at = [At(
            "TAIL"
        )],
    )
    fun onSpawnPacket(packet: EntitySpawnS2CPacket, ci: CallbackInfo) {
        RotationUtils.camera_yaw = packet.yaw
        RotationUtils.camera_pitch = packet.pitch
    }

    @Inject(
        method = ["refreshPositionAndAngles(DDDFF)V"],
        at = [At(
            "TAIL"
        )],
    )
    fun refreshPositionAndAngles(x: Double, y: Double, z: Double, yaw: Float, pitch: Float, ci: CallbackInfo) {
        RotationUtils.camera_yaw = yaw
        RotationUtils.camera_pitch = pitch
    }

    @Inject(
        method = ["baseTick()V"],
        at = [At(
            "TAIL"
        )],
    )
    fun baseTick(ci: CallbackInfo) {
        RotationUtils.prev_camera_yaw = RotationUtils.camera_yaw
        RotationUtils.prev_camera_pitch = RotationUtils.camera_yaw
    }

}