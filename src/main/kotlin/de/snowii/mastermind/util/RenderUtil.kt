package de.snowii.mastermind.util

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.Util
import org.joml.Matrix4f
import kotlin.math.cos
import kotlin.math.sin


object RenderUtil {

    fun renderTargetESPCircle(
        context: WorldRenderContext,
        entity: Entity,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float,
        lines: Int,
        width: Float,
        speed: Float
    ) {
        val lineSpacing = 0.03
        val CENTER = (entity.width / 2).toDouble()
        val bRadius = 1.5 / entity.width
        val boxHeight = entity.height.toDouble()
        val lineSmoothness = 0.2
        val currentY = sin(Util.getEpochTimeMs().toDouble() / (150 - speed)) / 1.4 - 0.8
        val aa = entity.boundingBox
        val camera = context.camera().pos
        val vertexConsumer: VertexConsumer =
            context.consumers()!!.getBuffer(RenderLayer.getDebugLineStrip(width.toDouble()))
        val xx = aa.maxX - camera.x
        val yy = aa.maxY - camera.y
        val zz = aa.maxZ - camera.z
        val matrices: MatrixStack = context.matrixStack()

        var currentLineY: Double

        RenderSystem.depthMask(false)
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableDepthTest()
        RenderSystem.enableBlend()

        val matrix4f: Matrix4f = matrices.peek().positionMatrix
        for (line in 0 until lines) {
            matrices.push()
            matrices.translate(-camera.x, -camera.y, -camera.z);
            currentLineY = line * lineSpacing
            var i = -boxHeight
            while (i <= boxHeight + 1f) {
                vertexConsumer.vertex(
                    matrix4f,
                    (xx - CENTER + sin((i - lineSmoothness) * 2) / bRadius).toFloat(),
                    (yy + currentY + currentLineY).toFloat(),
                    (zz - CENTER + cos((i - lineSmoothness) * 2) / bRadius).toFloat()
                ).color(red, green, blue, alpha).next()
                vertexConsumer.vertex(
                    matrix4f,
                    (xx - CENTER + sin((i - lineSmoothness / 2) * 2) / bRadius).toFloat(),
                    (yy + currentY + currentLineY).toFloat(),
                    (zz - CENTER + cos((i - lineSmoothness / 2) * 2) / bRadius).toFloat()
                ).color(red, green, blue, alpha).next()
                vertexConsumer.vertex(
                    matrix4f,
                    (xx - CENTER + sin(i * 2) / bRadius).toFloat(), (yy + currentY + currentLineY).toFloat(),
                    (zz - CENTER + cos(i * 2) / bRadius).toFloat()
                ).color(red, green, blue, alpha).next()
                i += lineSmoothness
            }
            matrices.pop()
        }
        RenderSystem.depthMask(true)
        RenderSystem.disableBlend()
        RenderSystem.enableDepthTest()
    }

}