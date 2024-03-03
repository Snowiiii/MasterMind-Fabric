package de.snowii.mastermind.command.argument

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import de.snowii.mastermind.module.Module
import de.snowii.mastermind.module.ModuleManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandSource
import net.minecraft.text.Text
import net.minecraft.world.GameMode
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors
import java.util.stream.Stream

class ModuleArgumentType : ArgumentType<Module> {
    private val EXAMPLES: Collection<String> =
        Stream.of(GameMode.SURVIVAL, GameMode.CREATIVE).map { obj: GameMode -> obj.getName() }
            .collect(Collectors.toList())

    private val INVALID_MODULE_EXCEPTION = DynamicCommandExceptionType { module: Any? ->
        Text.literal(
            "Invalid Module $module"
        )
    }


    override fun parse(reader: StringReader): Module {
        val string: String = reader.readUnquotedString()
        val module = ModuleManager.findModuleByName(string)
            ?: throw INVALID_MODULE_EXCEPTION.createWithContext(reader, string)
        return module
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>?,
        builder: SuggestionsBuilder?
    ): CompletableFuture<Suggestions> {
        if (context!!.source is FabricClientCommandSource) {
            return CommandSource.suggestMatching(
                Arrays.stream(ModuleManager.modules.toTypedArray()).map { obj: Module -> obj.name }, builder
            )
        }
        return Suggestions.empty();
    }

    companion object {
        @Throws(CommandSyntaxException::class)
        @JvmStatic
        fun getModule(context: CommandContext<FabricClientCommandSource?>, name: String?): Module {
            return context.getArgument(name, Module::class.java)
        }
    }

}