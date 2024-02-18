package de.snowii.mastermind.settings

import java.util.function.BooleanSupplier

class SettingString : AbstractSetting {
    var value: String

    constructor(displayName: String, defaultVal: String) : super(displayName, defaultVal, BooleanSupplier { true }) {
        value = defaultVal
    }

    constructor(displayName: String, defaultVal: String, isVisible: BooleanSupplier) : super(
        displayName,
        defaultVal,
        isVisible
    ) {
        value = defaultVal
    }
}
