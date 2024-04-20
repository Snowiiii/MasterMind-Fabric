package de.snowii.mastermind.module

import de.snowii.mastermind.module.modules.combat.AutoRod
import de.snowii.mastermind.module.modules.combat.KillAura
import de.snowii.mastermind.module.modules.combat.TriggerBot
import de.snowii.mastermind.module.modules.`fun`.Derp
import de.snowii.mastermind.module.modules.movement.Fly
import de.snowii.mastermind.module.modules.movement.Sprint
import de.snowii.mastermind.module.modules.movement.Step
import de.snowii.mastermind.module.modules.player.AutoArmor
import de.snowii.mastermind.module.modules.player.ChestStealer
import de.snowii.mastermind.module.modules.player.InvManager
import de.snowii.mastermind.module.modules.player.MidClick
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
        registerModule(AutoRod)

        // PLAYER
        registerModule(AutoArmor)
        registerModule(MidClick)
        registerModule(ChestStealer)
        registerModule(InvManager)

        // MOVEMENT
        registerModule(Fly)
        registerModule(Step)
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

    fun findModuleByName(name: String): Module? {
        for (module in modules) {
            if (module.name.equals(name, ignoreCase = true)) return module
        }
        return null
    }


}