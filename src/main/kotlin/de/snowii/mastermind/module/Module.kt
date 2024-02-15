package de.snowii.mastermind.module

import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.InputUtil.Key


abstract class Module(val name: String, val description: String, val category: Category) {
    var key: Key = InputUtil.UNKNOWN_KEY
    var isToggled = false
        private set

    protected val mc: MinecraftClient = MinecraftClient.getInstance()

    open fun onEnable() {}

    @Synchronized
    open fun onPreUpdate() {
    }

    open fun onKeyboardTick() {
    }

    open fun onDisable() {}

    open fun renderModule(): Boolean {
        return true
    }

    fun key(key: Int) {
        this.key = InputUtil.Type.KEYSYM.createFromCode(key)
    }

    @JvmOverloads
    fun toggle(toggled: Boolean = !isToggled) {
        isToggled = toggled
        if (toggled) {
            onEnable()
        } else {
            onDisable()
        }
    }

    override fun toString(): String {
        return name + description
    }

    enum class Category {
        WORLD,
        COMBAT,
        PLAYER,
        MOVEMENT,
        FUN,
        RENDER,
        VISUAL
    }

}