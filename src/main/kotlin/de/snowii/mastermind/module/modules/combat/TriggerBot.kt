package de.snowii.mastermind.module.modules.combat

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.util.TimeHelper
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents
import net.minecraft.util.hit.HitResult

class TriggerBot : Module("TriggerBot", "Automatically Attacks Entities you look at", Category.COMBAT) {
    val MIN_CPS: Int = 8
    val MAX_CPS: Int = 14

    var hitTimer = TimeHelper()
    var current_cps = (MIN_CPS..MAX_CPS).random()

    init {
        // Disable on world change
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(ServerEntityWorldChangeEvents.AfterPlayerChange { player, origin, destination ->
            if (player == mc.player)
                run {
                    toggle(false)
                }
        })
    }

    override fun onKeyboardTick() {
        if (!mc.player!!.isUsingItem && mc.crosshairTarget != null && mc.crosshairTarget!!.type == HitResult.Type.ENTITY) {
            if (hitTimer.hasTimeReached(1000L / current_cps)) {
                mc.doAttack()
                current_cps = (MIN_CPS..MAX_CPS).random()
                hitTimer.reset()
            }
        }
    }

}