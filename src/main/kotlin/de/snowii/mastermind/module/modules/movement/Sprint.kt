package de.snowii.mastermind.module.modules.movement

import de.snowii.mastermind.module.Module

class Sprint : Module("Sprint", "Enables Auto Sprinting", Category.MOVEMENT) {

    init {
        toggle(true)
        instance = this
    }

    override fun onPreUpdate() {
        if (mc.player!!.forwardSpeed > 0) mc.player!!.isSprinting = true
    }

    companion object {
        @JvmStatic
        lateinit var instance: Sprint
            private set
    }
}