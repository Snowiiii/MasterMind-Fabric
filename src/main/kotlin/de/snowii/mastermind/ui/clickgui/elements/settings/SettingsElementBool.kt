package de.snowii.mastermind.ui.clickgui.elements.settings

import de.snowii.mastermind.settings.SettingBoolean
import net.minecraft.client.gui.DrawContext
import java.awt.Color

class SettingsElementBool(private val settingBoolean: SettingBoolean, x: Int, y: Int) : SettingsElement(x, y) {
    private val color_enabled = Color(255, 200, 0, 200).rgb
    override fun drawScreen(context: DrawContext, mouseX: Int, mouseY: Int) {
        if (!settingBoolean.isVisible()) return
        super.drawScreen(context, mouseX, mouseY)

        // Slider
        context.fill(
            (x + 5),
            (y - 2),
            (x + 5 + width * 2),
            (y + height + 2),
            if (settingBoolean.value) color_enabled else Color(0, 0, 0, 100).rgb
        )
        context.drawText(
            mc.textRenderer,
            settingBoolean.displayName,
            (x + width / 2 + 10),
            (y + (height - 8) / 2),
            Color.WHITE.rgb,
            false
        )
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        if (!settingBoolean.isVisible()) return
        super.mouseClicked(mouseX, mouseY, mouseButton)
        settingBoolean.value = !settingBoolean.value
    }
}
