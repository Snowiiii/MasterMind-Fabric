package de.snowii.mastermind.module.modules.movement

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingMode

object Fly : Module("Fly", "Allows you to Fly", Category.MOVEMENT) {

    enum class Mode {
        VANILLA
    }

    private val MODE = SettingMode("Mode", Mode.VANILLA, Mode.VANILLA)


    init {
        addSetting(MODE)
    }


    override fun onPreUpdate() {
        if (MODE.getMode() == Mode.VANILLA) {
            mc.player!!.abilities.flying = true
        }
    }

    override fun onDisable() {
        if (MODE.getMode() == Mode.VANILLA) {
            mc.player!!.abilities.flying = false
        }
    }
}