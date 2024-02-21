package de.snowii.mastermind.ui.clickgui

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.module.ModuleManager
import de.snowii.mastermind.module.modules.visual.ClickGUI
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW

class ClickGUIScreen : Screen(Text.literal("Click GUI")) {
    val panels: MutableList<Panel?> = ArrayList()

    override fun init() {
        if (panels.isEmpty()) {
            var i = 50
            for (category in Module.Category.entries) {
                val modules: MutableList<Module> = ArrayList()
                for (module in ModuleManager.modules) {
                    // A Pretty way to do this (cap)
                    if (module is ClickGUI) continue
                    if (module.category == category) modules.add(module)
                }
                val panel = Panel(category.name, i, height / 2 - 100, modules, client!!)
                panels.add(panel)
                assert(panel != null)
                i += 20 + panel.width
            }
        } else {
            for (panel in panels) {
                panel!!.init()
            }
        }
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)
        for (panel in panels) {
            panel!!.render(context, mouseX, mouseY, delta)
        }
    }


    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        super.keyPressed(keyCode, scanCode, modifiers)
        if (keyCode == GLFW.GLFW_KEY_F5) { // Reload if pressing F5
            panels.clear()
            this.init()
        } else {
            for (panel in panels) {
                panel!!.keyTyped(keyCode, scanCode, modifiers)
            }
        }
        return true
    }

    override fun shouldPause(): Boolean {
        return false
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        for (panel in panels) {
            panel!!.mouseClicked(mouseX, mouseY, mouseButton)
        }
        return true
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, state: Int): Boolean {
        for (panel in panels) {
            panel!!.mouseReleased(mouseX, mouseY, state)
        }
        return super.mouseReleased(mouseX, mouseY, state)
    }

}