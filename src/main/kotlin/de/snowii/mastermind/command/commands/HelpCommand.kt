package de.snowii.mastermind.command.commands

import de.snowii.mastermind.command.Command
import de.snowii.mastermind.command.CommandManager
import de.snowii.mastermind.module.ModuleManager
import de.snowii.mastermind.util.PlayerUtil
import net.minecraft.util.Formatting

class HelpCommand : Command("Help", "Sends a list of all commands", "h") {
    override fun onCommand(args: Array<String>) {
        PlayerUtil.sendMessage(
            Formatting.GRAY.toString() + "-".repeat(10) + (Formatting.GOLD.toString() + "Commands (" + CommandManager.commands.size + ")" + Formatting.GRAY + "-".repeat(
                10
            ))
        )
        for (command in CommandManager.commands) {
            PlayerUtil.sendMessage(Formatting.GOLD.toString() + command.name + Formatting.GRAY + " | " + command.description)
        }
        PlayerUtil.sendMessage(
            Formatting.GRAY.toString() + "-".repeat(10) + (Formatting.GOLD.toString() + "Modules (" + ModuleManager.modules.size + ")" + Formatting.GRAY + "-".repeat(
                10
            ))
        )
        for (module in ModuleManager.modules) {
            PlayerUtil.sendMessage(Formatting.GOLD.toString() + module.name + Formatting.GRAY + " | " + module.description)
        }
    }
}
