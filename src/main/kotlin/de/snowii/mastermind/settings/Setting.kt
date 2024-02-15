package de.snowii.mastermind.settings

import java.util.*
import java.util.function.BooleanSupplier

abstract class Setting(var displayName: String, var default: Any, private val visible: BooleanSupplier) {
    val name: String
        get() = displayName.lowercase(Locale.getDefault()).replace(" ", "_")

    fun isVisible(): Boolean {
        return visible.asBoolean
    }

}
