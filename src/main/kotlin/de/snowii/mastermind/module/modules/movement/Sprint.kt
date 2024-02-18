package de.snowii.mastermind.module.modules.movement

import de.snowii.mastermind.module.Module

object Sprint : Module("Sprint", "Enables Auto Sprinting", Category.MOVEMENT) {
    var instance: Sprint

    init {
        toggle(true)
        instance = this
    }

    override fun onPreUpdate() {
        if (mc.player!!.forwardSpeed > 0) mc.player!!.isSprinting = true
    }

}