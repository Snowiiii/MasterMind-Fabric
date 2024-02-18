package de.snowii.mastermind.module.modules.combat

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingInt
import de.snowii.mastermind.util.TimeHelper
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents
import net.minecraft.util.hit.HitResult

object TriggerBot : Module("TriggerBot", "Automatically Attacks Entities you look at", Category.COMBAT) {
    private val CPS_MIN = SettingInt("CPS Min", 8, 1, 20)
    private val CPS_MAX = SettingInt("CPS Max", 14, 2, 40)

    var hitTimer = TimeHelper()
    var current_cps = (CPS_MIN.value..CPS_MAX.value).random()

    init {
        // Disable on world change
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(ServerEntityWorldChangeEvents.AfterPlayerChange { player, origin, destination ->
            if (player == mc.player)
                run {
                    toggle(false)
                }
        })
        addSetting(CPS_MIN)
        addSetting(CPS_MAX)
    }

    override fun onKeyboardTick() {
        if (!mc.player!!.isUsingItem && mc.crosshairTarget != null && mc.crosshairTarget!!.type == HitResult.Type.ENTITY) {
            if (hitTimer.hasTimeReached(1000L / current_cps)) {
                mc.doAttack()
                current_cps = (CPS_MIN.value..CPS_MAX.value).random()
                hitTimer.reset()
            }
        }
    }

}