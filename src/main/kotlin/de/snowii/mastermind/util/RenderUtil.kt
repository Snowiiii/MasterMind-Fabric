package de.snowii.mastermind.util

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.Util
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d
import org.joml.Matrix4f
import kotlin.math.cos
import kotlin.math.sin


object RenderUtil {

    fun draw3DLine(
        context: WorldRenderContext,
        xPar: Double,
        yPar: Double,
        zPar: Double,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float,
        lineWdith: Float
    ) {
        val camera = context.camera()
        val center = Vec3d(0.0, 0.0, 1.0)
            .rotateX(-Math.toRadians(camera.pitch.toDouble()).toFloat())
            .rotateY(-Math.toRadians(camera.yaw.toDouble()).toFloat())
            .add(camera.pos)

        val matrix = context.matrixStack()!!;
        val matrix4f: Matrix4f = matrix.peek().positionMatrix

        RenderSystem.enableBlend()
        val vertexConsumer: VertexConsumer =
            context.consumers()!!.getBuffer(RenderLayer.getDebugLineStrip(lineWdith.toDouble()))
        RenderSystem.defaultBlendFunc()
        matrix.push()
        vertexConsumer.vertex(matrix4f, center.x.toFloat(), center.y.toFloat(), center.z.toFloat())
            .color(red, green, blue, alpha)
        vertexConsumer.vertex(
            matrix4f,
            (xPar - center.x).toFloat(),
            (yPar - center.y).toFloat(),
            (zPar - center.z).toFloat()
        ).color(red, green, blue, alpha)
        matrix.pop()
        RenderSystem.disableBlend()
    }

    fun smoothTrans(current: Double, last: Double): Float {
        return (current + (last - current) / (MinecraftClient.getInstance().currentFps / 10)).toFloat()
    }

    fun drawRoundedRect(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        radius: Int,
        color: Int
    ) {
        context.fill(
            (x + radius),
            (y + 2 * radius),
            (x + width + radius),
            (y + height),
            color
        )
        context.fill(
            (x + 2 * radius),
            (y + radius),
            (x + width),
            (y + 2 * radius),
            color
        )
        context.fill(
            (x + 2 * radius),
            (y + height),
            (x + width),
            (y + height + radius),
            color
        )

        val alpha = ColorHelper.Argb.getAlpha(color) / 255.0f
        val red = ColorHelper.Argb.getRed(color) / 255.0f
        val green = ColorHelper.Argb.getGreen(color) / 255.0f
        val blue = ColorHelper.Argb.getBlue(color) / 255.0f

        val cx = x + radius
        val cy = y + radius
        val angles = doubleArrayOf(Math.PI * 3.5, Math.PI * 3.0, Math.PI * 2.5, 0.0)
        val radius = radius.toFloat();
        val offsets = arrayOf(
            floatArrayOf(radius, radius),
            floatArrayOf(width - radius, radius),
            floatArrayOf(width - radius, height - radius),
            floatArrayOf(radius, height - radius)
        )

        val vertexconsumer: VertexConsumer = context.vertexConsumers.getBuffer(RenderLayer.getGui())
        for (index in 0..3) {
            val startAngle = angles[index]
            val offset = offsets[index]
            vertexconsumer.vertex((cx + offset[0]), (cy + offset[1]), 0.0F)
                .color(red, green, blue, alpha)
            var angle = startAngle
            while (angle <= startAngle + Math.PI / 2.0 + 0.01) {
                vertexconsumer.vertex((cx - radius * cos(angle) + offset[0]).toFloat(),
                    (cy + radius * sin(angle) + offset[1]).toFloat(), 0.0F)
                    .color(red, green, blue, alpha)
                angle += Math.PI / 2.0 * 0.01
            }
        }
        context.draw()
    }

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
        val CENTER = (entity.width / 2)
        val bRadius = 1.5 / entity.width
        val boxHeight = entity.height
        val lineSmoothness = 0.2F
        val currentY = sin(Util.getMeasuringTimeMs() / (150 - speed)) / 1.4 - 0.8
        val aa = entity.boundingBox
        val camera = context.camera().pos
        val xx = aa.maxX - camera.x
        val yy = aa.maxY - camera.y
        val zz = aa.maxZ - camera.z
        val matrices: MatrixStack = context.matrixStack()!!

        var currentLineY: Double

        // We dont use vertex consumers so we can disable depth
        RenderSystem.setShader { GameRenderer.getPositionColorProgram() }
        RenderSystem.depthMask(false)
        RenderSystem.defaultBlendFunc()
        RenderSystem.lineWidth(width)
        RenderSystem.disableDepthTest()
        RenderSystem.enableBlend()

        val matrix4f: Matrix4f = matrices.peek().positionMatrix
        for (line in 0 until lines) {
            matrices.push()
            matrices.translate(-camera.x, -camera.y, -camera.z)
            currentLineY = line * lineSpacing
            var i = -boxHeight
            while (i <= boxHeight + 1f) {
                val bufferBuilder =
                    Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR)
                bufferBuilder.vertex(
                    matrix4f,
                    (xx - CENTER + sin((i - lineSmoothness) * 2) / bRadius).toFloat(),
                    (yy + currentY + currentLineY).toFloat(),
                    (zz - CENTER + cos((i - lineSmoothness) * 2) / bRadius).toFloat()
                ).color(red, green, blue, alpha)
                bufferBuilder.vertex(
                    matrix4f,
                    (xx - CENTER + sin((i - lineSmoothness / 2) * 2) / bRadius).toFloat(),
                    (yy + currentY + currentLineY).toFloat(),
                    (zz - CENTER + cos((i - lineSmoothness / 2) * 2) / bRadius).toFloat()
                ).color(red, green, blue, alpha)
                bufferBuilder.vertex(
                    matrix4f,
                    (xx - CENTER + sin(i * 2) / bRadius).toFloat(), (yy + currentY + currentLineY).toFloat(),
                    (zz - CENTER + cos(i * 2) / bRadius).toFloat()
                ).color(red, green, blue, alpha)
                i += lineSmoothness
                BufferRenderer.drawWithGlobalProgram(bufferBuilder.end())
            }
            matrices.pop()
        }
        RenderSystem.depthMask(true)
        RenderSystem.disableBlend()
        RenderSystem.enableDepthTest()
    }

}