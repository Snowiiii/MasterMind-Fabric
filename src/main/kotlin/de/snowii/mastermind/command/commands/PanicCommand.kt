package de.snowii.mastermind.command.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import de.snowii.mastermind.module.ModuleManager
import de.snowii.mastermind.util.PlayerUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.util.Formatting

class PanicCommand {

    val COMMAND = ClientCommandManager.literal("panic").executes { context -> run(context) }

    fun run(context: CommandContext<FabricClientCommandSource>): Int {
        for (module in ModuleManager.modules) {
            module.toggle(false)
        }
        PlayerUtil.sendMessage(context.source, "${Formatting.RED} Disabled all Modules")
        return Command.SINGLE_SUCCESS
    }


}
