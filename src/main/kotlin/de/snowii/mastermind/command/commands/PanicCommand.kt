package de.snowii.mastermind.command.commands

import de.snowii.mastermind.command.Command
import de.snowii.mastermind.module.ModuleManager

class PanicCommand : Command("Panic", "Disables all Modules") {
    override fun onCommand(args: Array<String>) {
        for (module in ModuleManager.modules) {
            module.toggle(false)
        }
    }
}
