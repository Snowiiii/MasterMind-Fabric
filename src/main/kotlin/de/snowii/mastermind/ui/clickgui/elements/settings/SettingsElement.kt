package de.snowii.mastermind.ui.clickgui.elements.settings

import de.snowii.mastermind.settings.SettingFloat
import de.snowii.mastermind.settings.SettingInt
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.MathHelper
import kotlin.math.roundToInt

open class SettingsElement(var x: Int, var y: Int) {
    val mc = MinecraftClient.getInstance()
    var width = 50
    var height = 5
    protected var dragging = false
    var hovered = false
    var isVisible = false

    open fun mouseDragged(context: DrawContext, mouseX: Int, mouseY: Int) {}
    open fun drawScreen(context: DrawContext, mouseX: Int, mouseY: Int) {
        hovered = mouseX >= x - 2 && mouseY >= y - 2 && mouseX < x + width * 2 + 5 && mouseY < y + height
    }

    open fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        dragging = true
    }

    fun mouseReleased(mouseX: Double, mouseY: Double, state: Int) {
        dragging = false
    }

    fun normalizeValue(setting: SettingInt, value: Float): Float {
        return MathHelper.clamp(
            (this.snapToStepClamp(setting, value) - setting.min) / (setting.max - setting.min),
            0.0f,
            1.0f
        )
    }

    fun normalizeValue(setting: SettingFloat, value: Float): Float {
        return MathHelper.clamp(
            (this.snapToStepClamp(setting, value) - setting.min) / (setting.max - setting.min),
            0.0f,
            1.0f
        )
    }

    fun denormalizeValue(setting: SettingInt, value: Float): Float {
        return this.snapToStepClamp(
            setting,
            setting.min + (setting.max - setting.min) * MathHelper.clamp(value, 0.0f, 1.0f)
        )
    }

    fun denormalizeValue(setting: SettingFloat, value: Float): Float {
        return this.snapToStepClamp(
            setting,
            setting.min + (setting.max - setting.min) * MathHelper.clamp(value, 0.0f, 1.0f)
        )
    }

    fun snapToStepClamp(setting: SettingInt, value: Float): Float {
        var value = value
        value = snapToStep(value, 1.0f)
        return MathHelper.clamp(value, setting.min.toFloat(), setting.max.toFloat())
    }

    fun snapToStepClamp(setting: SettingFloat, value: Float): Float {
        var value = value
        value = snapToStep(value, 0.1f)
        return MathHelper.clamp(value, setting.min, setting.max)
    }

    private fun snapToStep(value: Float, add: Float): Float {
        return add * (value / add).roundToInt().toFloat()
    }

}
