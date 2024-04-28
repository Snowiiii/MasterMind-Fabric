package de.snowii.mastermind.module.modules.movement

import de.snowii.mastermind.module.Module

object Step : Module("Step", "Increases your Step height", Category.MOVEMENT) {

    override fun onPreUpdate() {
       // mc.player!!.stepHeight = 1.0F; TODO
    }

    override fun onDisable() {
      //  mc.player!!.stepHeight = 0.6F
    }
}