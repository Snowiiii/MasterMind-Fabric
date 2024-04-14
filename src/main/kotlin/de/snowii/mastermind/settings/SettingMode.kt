package de.snowii.mastermind.settings

import java.util.*
import java.util.function.BooleanSupplier

class SettingMode<T : Enum<T>> : AbstractSetting {
    val modes: List<Enum<T>>
    var current_mode: Enum<T>
        private set

    constructor(displayName: String, defaultMode: Enum<T>, vararg modes: Enum<T>) : super(
        displayName,
        defaultMode,
        BooleanSupplier { true }) {
        current_mode = defaultMode
        val list = ArrayList(Arrays.stream(modes).toList())
        list.add(defaultMode)
        this.modes = list
    }

    constructor(displayName: String, defaultMode: Enum<T>, isVisible: BooleanSupplier, vararg modes: Enum<T>) : super(
        displayName,
        defaultMode,
        isVisible
    ) {
        current_mode = defaultMode
        val list = ArrayList(Arrays.stream(modes).toList())
        list.add(defaultMode)
        this.modes = list
    }

    fun setMode(mode: Enum<out Enum<*>>) {
        current_mode = mode as Enum<T>
    }

    fun getMode(): Enum<T> {
        return current_mode
    }

}
