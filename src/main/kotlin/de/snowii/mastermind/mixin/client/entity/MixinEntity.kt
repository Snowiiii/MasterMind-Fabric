package de.snowii.mastermind.mixin.client.entity

import de.snowii.mastermind.util.RotationUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.util.math.Vec3d
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect


@Mixin(Entity::class)
abstract class MixinEntity {

    @Redirect(
        method = ["updateVelocity"],
        at = At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;movementInputToVelocity"),
    )
    fun updateVelocity(movementInput: Vec3d, speed: Float, yaw: Float): Vec3d {
        var yaw = yaw
        if (RotationUtils.move_camera) {
            yaw = RotationUtils.savedYaw
        }
        return Entity.movementInputToVelocity(movementInput, speed, yaw)
    }


}