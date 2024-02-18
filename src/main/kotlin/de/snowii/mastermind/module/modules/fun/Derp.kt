package de.snowii.mastermind.module.modules.`fun`

import de.snowii.mastermind.module.Module

object Derp : Module("Derp", "Spins you around", Category.FUN) {

    override fun onPreUpdate() {
        val player = mc.player!!
        val yaw = player.yaw + (0..1).random() * 360f - 180f
        val pitch = (0..1).random() * 180f - 90f
        player.yaw = yaw
        player.pitch = pitch

    }

    override fun onDisable() {

    }
}