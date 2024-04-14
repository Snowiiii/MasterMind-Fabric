package de.snowii.mastermind.ui.clickgui.elements.settings

import de.snowii.mastermind.settings.SettingMode
import net.minecraft.client.gui.DrawContext
import java.awt.Color


class SettingsElementMode(private val settingMode: SettingMode<*>, x: Int, y: Int) : SettingsElement(x, y) {
    private val color_enabled = Color(255, 200, 0, 200).rgb
    private var index = 0
    override fun drawScreen(context: DrawContext, mouseX: Int, mouseY: Int) {
        if (!settingMode.isVisible()) return
        super.drawScreen(context, mouseX, mouseY)

        // Slider
        context.fill(
            (x + 5),
            (y - 2),
            (x + 5 + width * 2),
            (y + height + 2),
            color_enabled
        )
        context.drawText(
            mc.textRenderer,
            settingMode.displayName + ":" + settingMode.current_mode,
            (x + width / 2),
            (y + (height - 8) / 2),
            Color.WHITE.rgb,
            false
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        if (!settingMode.isVisible()) return
        super.mouseClicked(mouseX, mouseY, mouseButton)
        settingMode.setMode(settingMode.modes[index++])
        if (index >= settingMode.modes.size) index = 0
    }
}
