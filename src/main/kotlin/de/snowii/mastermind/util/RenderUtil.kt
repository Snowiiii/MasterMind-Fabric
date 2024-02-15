package de.snowii.mastermind.util

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
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
        val camera = MinecraftClient.getInstance().gameRenderer.camera.pos.negate()
        val xx = aa.maxX - camera.x
        val yy = aa.maxY - camera.y
        val zz = aa.maxZ - camera.z

        RenderSystem.defaultBlendFunc()
        RenderSystem.disableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.lineWidth(width)
        RenderSystem.setShader { GameRenderer.getPositionColorProgram() }
        var currentLineY: Double
        val matrix4f: Matrix4f = context.matrixStack().peek().positionMatrix
        val tessellator = RenderSystem.renderThreadTesselator()
        val buffer = tessellator.buffer
        for (line in 0 until lines) {
            currentLineY = line * lineSpacing
            buffer.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION_COLOR)
            var i = -boxHeight
            while (i <= boxHeight + 1f) {
                buffer.vertex(
                    matrix4f,
                    (xx - CENTER + sin((i - lineSmoothness) * 2) / bRadius).toFloat(),
                    (yy + currentY + currentLineY).toFloat(),
                    (zz - CENTER + cos((i - lineSmoothness) * 2) / bRadius).toFloat()
                ).color(red, green, blue, alpha).next()
                buffer.vertex(
                    matrix4f,
                    (xx - CENTER + sin((i - lineSmoothness / 2) * 2) / bRadius).toFloat(),
                    (yy + currentY + currentLineY).toFloat(),
                    (zz - CENTER + cos((i - lineSmoothness / 2) * 2) / bRadius).toFloat()
                ).color(red, green, blue, alpha).next()
                buffer.vertex(
                    matrix4f,
                    (xx - CENTER + sin(i * 2) / bRadius).toFloat(), (yy + currentY + currentLineY).toFloat(),
                    (zz - CENTER + cos(i * 2) / bRadius).toFloat()
                ).color(red, green, blue, alpha).next()
                i += lineSmoothness
            }
            tessellator.draw()
        }
        RenderSystem.disableBlend()
        RenderSystem.enableDepthTest()
    }
}