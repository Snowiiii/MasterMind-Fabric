package de.snowii.mastermind.util

import net.minecraft.util.Util


class TimeHelper {
    private var lastMS = 0L
    fun hasTimeReached(delay: Long): Boolean {
        return Util.getMeasuringTimeMs() - lastMS >= delay
    }

    val delay: Long
        get() = Util.getMeasuringTimeMs() - lastMS

    fun reset() {
        lastMS = Util.getMeasuringTimeMs()
    }
}
