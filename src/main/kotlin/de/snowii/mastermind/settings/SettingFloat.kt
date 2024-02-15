package de.snowii.mastermind.settings

import java.util.function.BooleanSupplier

class SettingFloat : Setting {
    var value: Float
    val min: Float
    val max: Float

    constructor(displayName: String, defaultVal: Float, min: Float, max: Float) : super(
        displayName,
        defaultVal,
        BooleanSupplier { true }) {
        value = defaultVal
        this.min = min
        this.max = max
    }

    constructor(displayName: String, defaultVal: Float, min: Float, max: Float, isVisible: BooleanSupplier) : super(
        displayName,
        defaultVal,
        isVisible
    ) {
        value = defaultVal
        this.min = min
        this.max = max
    }
}
