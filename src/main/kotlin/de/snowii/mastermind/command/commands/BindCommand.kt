package de.snowii.mastermind.command.commands

import de.snowii.mastermind.command.Command
import de.snowii.mastermind.module.ModuleManager
import de.snowii.mastermind.util.PlayerUtil
import net.minecraft.client.util.InputUtil
import net.minecraft.util.Formatting
import java.util.*

class BindCommand : Command("Bind", "Binds a module to a key", "b") {
    override fun onCommand(args: Array<String>) {
        if (args.size >= 3) {
            for (module in ModuleManager.modules) {
                if (module.name.equals(args[1], ignoreCase = true)) {
                    try {
                        val key =
                            InputUtil.fromTranslationKey("key.keyboard." + args[2].lowercase(Locale.getDefault()))

                        module.key = key
                        PlayerUtil.sendMessage(
                            Formatting.GREEN.toString() + "Bound " + module.name + " to " + args[2].uppercase(
                                Locale.getDefault()
                            )
                        )
                    } catch (e: Exception) {
                        PlayerUtil.sendMessage(Formatting.RED.toString() + "Invalid Key")
                    }
                    return
                }
            }
            PlayerUtil.sendMessage("Unknown Module: " + args[1])
        } else PlayerUtil.sendMessage(".b <module> <key>")
    }
}
