package de.snowii.mastermind.ui.clickgui.elements

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.ui.clickgui.elements.settings.ModuleSettingsPage
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import java.awt.Color
import java.io.IOException
import java.util.*

class ModuleButton(
    private val module: Module,
    private var x: Int,
    private var y: Int,
    private val width: Int,
    private val height: Int,
    private val mc: MinecraftClient
) {
    private val color_enabled = Color(255, 200, 0, 200).rgb
    private val color_search = Color.ORANGE.rgb
    private var page: ModuleSettingsPage? = null
    protected var hovered = false
    private var current_width: Float

    init {
        current_width = width.toFloat()
    }

    fun render(context: DrawContext, mouseX: Int, mouseY: Int, isDragging: Boolean) {
        this.hovered = (mouseX >= this.x) && (mouseY >= this.y) && (mouseX < this.x + this.width) && (mouseY < this.y + this.height)
        if (current_width < width) {
            current_width += 2f
        }
        if (module.isToggled) {
            context.fill(
                (x + 5),
                y,
                ((x + 5 + current_width).toInt()),
                (y + height),
                color_enabled
            )
        }
        context.drawText(
            mc.textRenderer,
            module.name,
            (x - mc.textRenderer.getWidth(
                module.name
            ) + width),
            (y + (height - 8) / 2),
            Color.WHITE.rgb,
            false
        )
        if (page != null) {
            page!!.x = ((x + 10 + width))
            page!!.y = ((y + (height - 8) / 2))
            page!!.render(context, mouseX, mouseY, isDragging)
            var i = y
            for (element in page!!.elements) {
                i += 12
                element.x = (x + 10 + width)
                element.y = i
            }
        }
    }

    @Throws(IOException::class)
    fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        if (mouseButton == 0) {
            if (hovered) {
                module.toggle()
                current_width = 0f
            }
        } else if (module.settings.isNotEmpty() && mouseButton == 1) {
            if (hovered) {
                page = if (page == null) {
                    ModuleSettingsPage(module, x + 10 + width, y + (height - 8) / 2)
                } else null
            }
        }
        if (page != null) page!!.mouseClicked(mouseX, mouseY, mouseButton)
    }

    fun keyTyped(keyCode: Int, scanCode: Int, modifiers: Int) {
    }

    fun mouseReleased(mouseX: Double, mouseY: Double, state: Int) {
        if (page != null) page!!.mouseReleased(mouseX, mouseY, state)
    }

    fun setX(x: Int) {
        this.x = x
    }

    fun setY(y: Int) {
        this.y = y
    }

}
