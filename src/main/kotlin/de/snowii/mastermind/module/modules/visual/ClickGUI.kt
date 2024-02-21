package de.snowii.mastermind.module.modules.visual

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.ui.clickgui.ClickGUIScreen
import org.lwjgl.glfw.GLFW

class ClickGUI : Module("ClickGUI", "", Category.VISUAL) {

    init {
        key(GLFW.GLFW_KEY_X)
    }

    override fun onEnable() {
        mc.setScreen(ClickGUIScreen())
        toggle(false)
    }
}