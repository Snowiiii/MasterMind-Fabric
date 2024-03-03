package de.snowii.mastermind.command.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import de.snowii.mastermind.command.argument.ModuleArgumentType
import de.snowii.mastermind.module.Module
import de.snowii.mastermind.util.PlayerUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.util.Formatting

class ToggleCommand {

    val COMMAND = ClientCommandManager.literal("toggle")
        .then(
            ClientCommandManager.argument("module", ModuleArgumentType())
                .executes { context -> run(context, ModuleArgumentType.getModule(context, "module")) })

    fun run(context: CommandContext<FabricClientCommandSource>, module: Module): Int {
        module.toggle()
        if (module.isToggled) {
            PlayerUtil.sendMessage(
                context.source,
                Formatting.GREEN.toString() + "(Enabled) " + Formatting.RESET + module.name
            )
        } else {
            PlayerUtil.sendMessage(
                context.source,
                Formatting.RED.toString() + "(Disabled) " + Formatting.RESET + module.name
            )
        }
        return Command.SINGLE_SUCCESS
    }
}
