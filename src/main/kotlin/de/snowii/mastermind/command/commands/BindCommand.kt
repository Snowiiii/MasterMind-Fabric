package de.snowii.mastermind.command.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import de.snowii.mastermind.command.argument.KeyBoardArgumentType
import de.snowii.mastermind.command.argument.ModuleArgumentType
import de.snowii.mastermind.module.Module
import de.snowii.mastermind.util.PlayerUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.util.InputUtil.Key
import net.minecraft.util.Formatting

class BindCommand {
    val COMMAND =
        ClientCommandManager.literal("bind").then(ClientCommandManager.argument("module", ModuleArgumentType()))
            .then(
                ClientCommandManager.argument("key", KeyBoardArgumentType()).executes { context ->
                    run(
                        context,
                        ModuleArgumentType.getModule(context, "module"),
                        KeyBoardArgumentType.getKey(context, "key")
                    )
                })

    fun run(context: CommandContext<FabricClientCommandSource>, module: Module, key: Key): Int {
        module.key = key
        PlayerUtil.sendMessage(
            context.source,
            Formatting.GREEN.toString() + "Bound " + module.name + " to " + key.localizedText
        )
        return Command.SINGLE_SUCCESS
    }
}
