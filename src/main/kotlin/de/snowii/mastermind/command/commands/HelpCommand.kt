package de.snowii.mastermind.command.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import de.snowii.mastermind.command.CommandManager
import de.snowii.mastermind.module.ModuleManager
import de.snowii.mastermind.util.PlayerUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.util.Formatting

class HelpCommand : Command<FabricClientCommandSource> {

    val COMMAND = ClientCommandManager.literal("help")
        .executes(this)


    override fun run(context: CommandContext<FabricClientCommandSource>): Int {
        PlayerUtil.sendMessage(
            context.source,
            Formatting.GRAY.toString() + "-".repeat(10) + (Formatting.GOLD.toString() + "Commands (" + CommandManager.commands.size + ")" + Formatting.GRAY + "-".repeat(
                10
            ))
        )
        for (command in CommandManager.commands) {
            PlayerUtil.sendMessage(
                context.source,
                Formatting.GOLD.toString() + command
            )
        }
        PlayerUtil.sendMessage(
            context.source,
            Formatting.GRAY.toString() + "-".repeat(10) + (Formatting.GOLD.toString() + "Modules (" + ModuleManager.modules.size + ")" + Formatting.GRAY + "-".repeat(
                10
            ))
        )
        for (module in ModuleManager.modules) {
            PlayerUtil.sendMessage(
                context.source,
                Formatting.GOLD.toString() + module.name + Formatting.GRAY + " | " + module.description
            )
        }
        return Command.SINGLE_SUCCESS
    }
}
