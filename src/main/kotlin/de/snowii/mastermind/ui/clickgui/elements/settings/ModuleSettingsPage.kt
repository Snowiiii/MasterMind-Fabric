package de.snowii.mastermind.ui.clickgui.elements.settings

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingBoolean
import de.snowii.mastermind.settings.SettingFloat
import de.snowii.mastermind.settings.SettingInt
import de.snowii.mastermind.settings.SettingMode
import de.snowii.mastermind.ui.clickgui.Panel
import de.snowii.mastermind.util.RenderUtil
import de.snowii.mastermind.util.SmoothAnimator
import net.minecraft.client.gui.DrawContext

class ModuleSettingsPage(module: Module, var x: Int, var y: Int) {
    private val width = 100
    private val height: Int
    val elements: MutableList<SettingsElement> = ArrayList()
    private val animator = SmoothAnimator()

    init {
        animator.reset()
        var i = y
        for (setting in module.settings) {
            i += 10
            when (setting) {
                is SettingInt -> {
                    elements.add(SettingsElementInt(setting, x, i))
                }

                is SettingFloat -> {
                    elements.add(SettingsElementFloat(setting, x, i))
                }

                is SettingBoolean -> {
                    elements.add(SettingsElementBool(setting, x, i))
                }

                is SettingMode<*> -> {
                    elements.add(SettingsElementMode(setting, x, i))
                }
            }
        }
        height = i
    }

    fun render(context: DrawContext, mouseX: Int, mouseY: Int, dragging: Boolean) {
        animator.runGLAnim(context.matrices)
        RenderUtil.drawRoundedRect(
            context,
            x - 5,
            y - 10,
            width,
            (height / 2 - 20),
            10,
            Panel.background_color
        )
        for (element in elements) {
            element.drawScreen(context, mouseX, mouseY)
        }
        for (element in elements) {
            if (element.hovered && dragging) element.mouseDragged(context, mouseX, mouseY)
        }
    }

    fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        for (element in elements) {
            if (element.hovered) element.mouseClicked(mouseX, mouseY, mouseButton)
        }
    }

    fun mouseReleased(mouseX: Double, mouseY: Double, state: Int) {
        for (element in elements) {
            if (element.hovered) element.mouseReleased(mouseX, mouseY, state)
        }
    }

}