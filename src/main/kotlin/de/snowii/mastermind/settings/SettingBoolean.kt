package de.snowii.mastermind.settings

import java.util.function.BooleanSupplier

class SettingBoolean : Setting {
    var value: Boolean

    constructor(displayName: String, defaultVal: Boolean) : super(displayName, defaultVal, BooleanSupplier { true }) {
        value = defaultVal
    }

    constructor(displayName: String, defaultVal: Boolean, isVisible: BooleanSupplier) : super(
        displayName,
        defaultVal,
        isVisible
    ) {
        value = defaultVal
    }
}
