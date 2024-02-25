package de.snowii.mastermind.module.modules.render

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingMode

object ESP : Module("ESP", "Allows to see Entities throw Walls", Category.RENDER) {

    val MODE = SettingMode("Mode", "Vanilla")

    init {
        addSetting(MODE)
    }
}