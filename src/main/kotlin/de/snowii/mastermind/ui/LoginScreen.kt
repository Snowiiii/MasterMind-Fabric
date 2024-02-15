package de.snowii.mastermind.ui

import de.snowii.mastermind.util.LoginHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW

class LoginScreen : Screen(Text.literal("Login")) {
    private var field: TextFieldWidget? = null
    private var status: String = ""
    private var updateStatus = false

    private lateinit var loginButton: ButtonWidget

    private var currentMode = LoginHelper.LoginMode.MICROSOFT
    override fun init() {
        updateStatus = true
        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Login")
            ) { button: ButtonWidget ->
                logIn()
            }.dimensions(width / 2 - 50, height - 52, 100, 20).build()
        )
        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Microsoft")
            ) { button: ButtonWidget ->
                if (currentMode == LoginHelper.LoginMode.CRACKED) {
                    currentMode = LoginHelper.LoginMode.MICROSOFT
                    button.message = Text.literal("Microsoft")
                } else if (currentMode == LoginHelper.LoginMode.MICROSOFT) {
                    currentMode = LoginHelper.LoginMode.CRACKED
                    button.message = Text.literal("Cracked")
                }
            }.dimensions(5, 5, 100, 20).build()
        )
        field = TextFieldWidget(this.textRenderer, width / 2 - 100, height / 2, 200, 20, Text.empty())
        field!!.setMaxLength(100)
        this.addSelectableChild(this.field)
        this.setInitialFocus(field)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (super.keyPressed(keyCode, scanCode, modifiers)) return true
        field!!.keyPressed(keyCode, scanCode, modifiers)
        if (field!!.isFocused && keyCode == GLFW.GLFW_KEY_ENTER) { // ENTER
            loginButton.onPress()
        }
        return true
    }

    override fun charTyped(codePoint: Char, modifiers: Int): Boolean {
        return field!!.charTyped(codePoint, modifiers)
    }

    private fun logIn() {
        if (currentMode == LoginHelper.LoginMode.MICROSOFT) {
            LoginHelper.loginMicrosoft()
        } else if (currentMode == LoginHelper.LoginMode.CRACKED) {
            updateStatus = false
            LoginHelper.loginCracked()
        }
    }

}