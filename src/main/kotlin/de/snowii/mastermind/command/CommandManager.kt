package de.snowii.mastermind.command

import de.snowii.mastermind.command.commands.*
import de.snowii.mastermind.util.PlayerUtil
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents

object CommandManager {
    val commands: MutableList<Command> = ArrayList()
    const val PREFIX = "."

    init {
        registerCommand(ToggleCommand())
        registerCommand(HelpCommand())
        registerCommand(SettingCommand())
        registerCommand(BindCommand())
        registerCommand(ConfigCommand())
        registerCommand(PanicCommand())

        ClientSendMessageEvents.ALLOW_CHAT.register(ClientSendMessageEvents.AllowChat { message: String ->
            parseMessage(message)
        });
    }

    /**
     * False = No Send
     * True = Send
     */
    private fun parseMessage(message: String): Boolean {
        if (message.startsWith(PREFIX)) {
            println("a")
            val text = message.removePrefix(PREFIX).split(" ".toRegex())
                .toTypedArray()
            for (command in commands) {
                if (command.name.equals(text[0], ignoreCase = true)) {
                    command.onCommand(text)
                    return false
                } else if (command.aliases != null) {
                    for (alias in command.aliases!!) {
                        if (alias.equals(text[0], ignoreCase = true)) {
                            command.onCommand(text)
                            return false
                        }
                    }
                }
            }
            PlayerUtil.sendMessage("Unknown Command")
            return false
        }
        return true
    }


    private fun registerCommand(module: Command) {
        commands.add(module)
    }
}
