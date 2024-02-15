package de.snowii.mastermind.mixin.client.render

import de.snowii.mastermind.util.RotationUtils
import net.minecraft.client.render.GameRenderer
import net.minecraft.util.math.RotationAxis
import org.joml.Quaternionf
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.ModifyArg

@Mixin(GameRenderer::class)
class MixinGameRenderer {

    @ModifyArg(
        method = ["renderWorld"],
        at = At(
            value = "INVOKE",
            target = "net.minecraft.client.util.math.MatrixStack.multiply(Lorg/joml/Quaternionf;)V",
            ordinal = 0
        ),
        index = 0
    )
    private fun modifyPitch(originalMatrix: Quaternionf): Quaternionf {
        return RotationAxis.POSITIVE_X.rotationDegrees(RotationUtils.camera_pitch)
    }

    @ModifyArg(
        method = ["renderWorld"],
        at = At(
            value = "INVOKE",
            target = "net.minecraft.client.util.math.MatrixStack.multiply(Lorg/joml/Quaternionf;)V",
            ordinal = 1
        ),
        index = 0
    )
    private fun modifyYaw(originalMatrix: Quaternionf): Quaternionf {
        return RotationAxis.POSITIVE_Y.rotationDegrees(RotationUtils.camera_yaw + 180.0F)
    }
}