package de.snowii.mastermind.settings

import java.util.*
import java.util.function.BooleanSupplier

class SettingMode : AbstractSetting {
    val modes: List<String>
    var modeOriginal: String
        private set

    constructor(displayName: String, defaultMode: String, vararg modes: String) : super(
        displayName,
        defaultMode,
        BooleanSupplier { true }) {
        modeOriginal = defaultMode
        val list = ArrayList(Arrays.stream(modes).toList())
        list.add(defaultMode)
        this.modes = list
    }

    constructor(displayName: String, defaultMode: String, isVisible: BooleanSupplier, vararg modes: String) : super(
        displayName,
        defaultMode,
        isVisible
    ) {
        modeOriginal = defaultMode
        val list = ArrayList(Arrays.stream(modes).toList())
        list.add(defaultMode)
        this.modes = list
    }

    fun setMode(mode: String) {
        modeOriginal = mode
    }

    fun getMode(): String {
        return modeOriginal.lowercase(Locale.getDefault())
    }

}
