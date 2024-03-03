package de.snowii.mastermind.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.CommandSyntaxException
import de.snowii.mastermind.MasterMind
import de.snowii.mastermind.command.commands.BindCommand
import de.snowii.mastermind.command.commands.HelpCommand
import de.snowii.mastermind.command.commands.PanicCommand
import de.snowii.mastermind.command.commands.ToggleCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals
import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.Text
import net.minecraft.text.Texts


object CommandManager {
    val dispatcher = CustomCommandDispatcher()
    val commands: MutableList<String> = ArrayList()
    const val PREFIX = "."

    init {
        registerCommand(ToggleCommand().COMMAND)
        //  registerCommand(SettingCommand())
        registerCommand(BindCommand().COMMAND)
        registerCommand(HelpCommand().COMMAND)
        //    registerCommand(ConfigCommand())
        registerCommand(PanicCommand().COMMAND)
        ClientSendMessageEvents.ALLOW_CHAT.register(ClientSendMessageEvents.AllowChat { message: String ->
            parseMessage(dispatcher, message)
        })

        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { _: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandRegistryAccess? ->
            ClientCommandInternals.setActiveDispatcher(dispatcher)
        })
    }


    /**
     * False = No Send
     * True = Send
     */
    private fun parseMessage(dispatcher: CommandDispatcher<FabricClientCommandSource>, message: String): Boolean {
        if (message.startsWith(PREFIX)) {
            val command = message.removePrefix(PREFIX)
            // The interface is implemented on ClientCommandSource with a mixin.
            // noinspection ConstantConditions
            val client = MinecraftClient.getInstance()
            val LOGGER = MasterMind.LOGGER
            val commandSource = client.networkHandler!!.commandSource as FabricClientCommandSource
            try {
                // TODO: Check for server commands before executing.
                //   This requires parsing the command, checking if they match a server command
                //   and then executing the command with the parse results.
                dispatcher.execute(command, commandSource);
                return false;
            } catch (e: CommandSyntaxException) {
                LOGGER.warn("Syntax exception for client-sided command '{}'", command, e);
                commandSource.sendError(getErrorMessage(e));
                return false;
            } catch (e: Exception) {
                LOGGER.warn("Error while executing client-sided command '{}'", command, e);
                commandSource.sendError(Text.of(e.message));
                return false
            }
        }
        return true
    }

    private fun getErrorMessage(e: CommandSyntaxException): Text {
        val message = Texts.toText(e.rawMessage)
        val context = e.context

        return if (context != null) Text.translatable(
            "command.context.parse_error",
            message,
            e.cursor,
            context
        ) else message
    }


    private fun registerCommand(command: LiteralArgumentBuilder<FabricClientCommandSource>) {
        dispatcher.register(
            command
        )
        commands.add(command.literal)
    }

    class CustomCommandDispatcher : CommandDispatcher<FabricClientCommandSource>() {

        override fun execute(input: String, source: FabricClientCommandSource?): Int {
            var input = input
            input = input.removePrefix(PREFIX)
            return super.execute(input, source)
        }
    }
}