package de.snowii.mastermind.module

import de.snowii.mastermind.module.modules.combat.KillAura
import de.snowii.mastermind.module.modules.combat.TriggerBot
import de.snowii.mastermind.module.modules.`fun`.Derp
import de.snowii.mastermind.module.modules.movement.Sprint
import de.snowii.mastermind.module.modules.player.AutoArmor
import de.snowii.mastermind.module.modules.player.ChestStealer
import de.snowii.mastermind.module.modules.player.InvManager
import de.snowii.mastermind.module.modules.render.ESP
import de.snowii.mastermind.module.modules.render.Tracers
import de.snowii.mastermind.module.modules.visual.ClickGUI
import de.snowii.mastermind.module.modules.world.Scaffold


object ModuleManager {
    var modules: MutableList<Module> = ArrayList()

    init {
        // COMBAT
        registerModule(KillAura)
        registerModule(TriggerBot)

        // PLAYER
        registerModule(AutoArmor)
        registerModule(ChestStealer)
        registerModule(InvManager)

        // MOVEMENT
        registerModule(Sprint)

        // RENDER
        registerModule(ESP)
        registerModule(Tracers)

        // VISUAL
        registerModule(ClickGUI())

        // FUN
        registerModule(Derp)

        // WORLD
        registerModule(Scaffold)
    }


    private fun registerModule(module: Module) {
        // Register Settings
        modules.add(module)
    }


}