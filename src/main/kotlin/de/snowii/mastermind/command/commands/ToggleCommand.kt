package de.snowii.mastermind.command.commands

import de.snowii.mastermind.command.Command
import de.snowii.mastermind.module.ModuleManager
import de.snowii.mastermind.util.PlayerUtil
import net.minecraft.util.Formatting

class ToggleCommand : Command("Toggle", "Toggle a module", "t") {
    override fun onCommand(args: Array<String>) {
        if (args.size >= 2) {
            for (module in ModuleManager.modules) {
                if (module.name.equals(args[1], ignoreCase = true)) {
                    module.toggle()
                    if (module.isToggled) {
                        PlayerUtil.sendMessage(Formatting.GREEN.toString() + "(Enabled) " + Formatting.RESET + module.name)
                    } else {
                        PlayerUtil.sendMessage(Formatting.RED.toString() + "(Disabled) " + Formatting.RESET + module.name)
                    }
                    return
                }
            }
            PlayerUtil.sendMessage("Unknown Module: " + args[1])
        } else PlayerUtil.sendMessage(".t <module>")
    }
}
