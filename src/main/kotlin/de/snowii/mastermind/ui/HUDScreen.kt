package de.snowii.mastermind.ui

import de.snowii.mastermind.module.ModuleManager
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import java.awt.Color

class HUDScreen : HudRenderCallback {

    override fun onHudRender(drawContext: DrawContext, tickDelta: Float) {
        renderArrayList(drawContext)
        renderFPS(drawContext)
    }

    private fun renderArrayList(drawContext: DrawContext) {
        val mc = MinecraftClient.getInstance()
        val i = mc.window.scaledWidth
        var count = 0
        for (module in ModuleManager.modules) {
            if (module.isToggled && module.renderModule()) {
                val offset = (count * (mc.textRenderer.fontHeight + 5))
                //  if (module.openSlide < 6) module.openSlide++
                drawContext.fill(
                    i - mc.textRenderer.getWidth(module.name) - 8,
                    offset,
                    i,
                    (mc.textRenderer.fontHeight + offset),
                    Color(0, 0, 0, 40).rgb
                )
                drawContext.drawText(
                    mc.textRenderer,
                    module.name,
                    (i - mc.textRenderer.getWidth(module.name) - 4),
                    (2 + offset),
                    Color.WHITE.rgb,
                    false
                )
                count++
            }
        }
    }

    private fun renderFPS(drawContext: DrawContext) {
        val mc = MinecraftClient.getInstance()
        drawContext.drawText(mc.textRenderer, "FPS: ${mc.currentFps}", 1, 1, Color.WHITE.rgb, false)
    }
}