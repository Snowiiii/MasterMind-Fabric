package de.snowii.mastermind.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import org.joml.Matrix3x2fStack

// Ye this adds just more spaghetti code but i think its better than but this into the RenderUtil class
class SmoothAnimator {
    var lastPercent = 0f
    var percent = 0f
    var percent2 = 0f
    var lastPercent2 = 1f
    fun reset() {
        percent = 1.10f
        lastPercent = 1f
        percent2 = 1.33f
    }

    fun runGLAnim(matrixStack: Matrix3x2fStack) {
        percent = RenderUtil.smoothTrans(percent.toDouble(), lastPercent.toDouble())
        percent2 = RenderUtil.smoothTrans(percent2.toDouble(), lastPercent2.toDouble())
        runGLAnimRaw(matrixStack)
    }

    private fun runGLAnimRaw(matrixStack: Matrix3x2fStack) {
        val window = MinecraftClient.getInstance().window
        if (percent > 0.98) {
            matrixStack.translate(
                (window.scaledWidth / 2).toFloat(),
                (window.scaledHeight / 2).toFloat(),
            )
            matrixStack.scale(percent, percent)
            matrixStack.translate(
                (-window.scaledWidth / 2).toFloat(),
                (-window.scaledHeight / 2).toFloat(),
            )
        } else if (percent2 <= 1) {
            matrixStack.translate(
                (window.scaledWidth / 2).toFloat(),
                (window.scaledHeight / 2).toFloat(),
            )
            matrixStack.scale(percent2, percent2)
            matrixStack.translate(
                (-window.scaledWidth / 2).toFloat(),
                (-window.scaledHeight / 2).toFloat(),
            )
        }
    }
}