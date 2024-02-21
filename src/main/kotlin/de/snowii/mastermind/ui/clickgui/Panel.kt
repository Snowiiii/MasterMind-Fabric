package de.snowii.mastermind.ui.clickgui

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.ui.clickgui.elements.ModuleButton
import de.snowii.mastermind.util.RenderUtil
import de.snowii.mastermind.util.SmoothAnimator
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import java.awt.Color

class Panel(val title: String, var x: Int, var y: Int, modules: List<Module>, val mc: MinecraftClient) {
    var width = 20
    val height = 10
    private val moduleButtons: MutableList<ModuleButton> = ArrayList()
    private var isDragging = false
    protected var hovered = false
    private var current_height: Float
    private val animator = SmoothAnimator()

    init {
        width = (width + mc.textRenderer.getWidth(title))
        var i = y
        for (module in modules) {
            i += height
            moduleButtons.add(ModuleButton(module, x, i, width, height, mc))
        }
        current_height = (height / 4).toFloat()
    }

    fun init() {
        current_height = (height / 4).toFloat()
        animator.reset()
    }

    fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + current_height
        if (current_height < height) {
            current_height += 0.2f
        }
        if (isDragging) {
            x = mouseX
            y = mouseY
            var i = y
            for (moduleButton in moduleButtons) {
                i = (i + current_height).toInt()
                moduleButton.setX(x)
                moduleButton.setY(i)
            }
        }

        // Border
        context.matrices.push()
        animator.runGLAnim(context.matrices)

        // Top
        // Gui.drawRect(this.x, this.y - border_size, this.x + this.width, this.y, border_color);
        var i = 1f
        for (ignored in moduleButtons) {
            i += current_height

            // Box
            RenderUtil.drawRoundedRect(
                context,
                (x - 5).toFloat(),
                (y - 10).toFloat(),
                width.toFloat(),
                current_height + i,
                10f,
                background_color
            )

            // Left
            //  Gui.drawRect(this.x - border_size, this.y - border_size, this.x, this.y + this.current_height + i, border_color);

            // Right
            // Gui.drawRect(this.x + this.width, this.y - border_size, this.x + this.width + border_size, this.y + this.current_height + i, border_color);
        }
        i += current_height

        // Bottom
        //  Gui.drawRect(this.x, this.y + i - border_size, this.x + this.width, this.y + i, border_color);

        // Draw Modules
        for (moduleButton in moduleButtons) {
            moduleButton.render(context, mouseX, mouseY, isDragging)
        }

        // TODO: Make Icons work
        /**   Minecraft.getInstance().getTextureManager().bindTexture(icon);
         * GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         *
         * int x = this.x;
         * int y = (int) (this.y + (this.current_height - 8) / 2);
         * this.drawTexturedModalRect(x, y, 100, 100, 20, 20);
         */
        context.drawText(
            mc.textRenderer,
            title,
            (x + width / 2 + 5),
            (y + (current_height - 8) / 2 + 1).toInt(),
            Color.ORANGE.rgb,
            false
        )
        context.matrices.pop()
    }

    fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        if (mouseButton == 0) {
            if (hovered) isDragging = true
        }
        for (moduleButton in moduleButtons) {
            moduleButton.mouseClicked(mouseX, mouseY, mouseButton)
        }
    }

    fun keyTyped(keyCode: Int, scanCode: Int, modifiers: Int) {
        for (moduleButton in moduleButtons) {
            moduleButton.keyTyped(keyCode, scanCode, modifiers)
        }
    }

    fun mouseReleased(mouseX: Double, mouseY: Double, state: Int) {
        isDragging = false
        for (moduleButton in moduleButtons) {
            moduleButton.mouseReleased(mouseX, mouseY, state)
        }
    }

    companion object {
        val background_color = Color(20, 20, 25, 120).rgb
    }

}