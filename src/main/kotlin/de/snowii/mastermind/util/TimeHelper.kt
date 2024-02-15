package de.snowii.mastermind.util

import net.minecraft.util.Util


class TimeHelper {
    private var lastMS = 0L
    fun hasTimeReached(delay: Long): Boolean {
        return Util.getEpochTimeMs() - lastMS >= delay
    }

    val delay: Long
        get() = Util.getEpochTimeMs() - lastMS

    fun reset() {
        lastMS = Util.getEpochTimeMs()
    }
}
