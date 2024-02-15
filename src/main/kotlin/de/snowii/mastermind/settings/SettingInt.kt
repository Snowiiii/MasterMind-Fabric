package de.snowii.mastermind.settings

import java.util.function.BooleanSupplier

class SettingInt : Setting {
    var value: Int
    val min: Int
    val max: Int

    constructor(displayName: String, defaultVal: Int, min: Int, max: Int) : super(
        displayName,
        defaultVal,
        BooleanSupplier { true }) {
        value = defaultVal
        this.min = min
        this.max = max
    }

    constructor(displayName: String, defaultVal: Int, min: Int, max: Int, isVisible: BooleanSupplier) : super(
        displayName,
        defaultVal,
        isVisible
    ) {
        value = defaultVal
        this.min = min
        this.max = max
    }
}
