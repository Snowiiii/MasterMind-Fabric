package de.snowii.mastermind.command.argument

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.InputUtil.Key
import net.minecraft.text.Text
import java.util.*

class KeyBoardArgumentType : ArgumentType<InputUtil.Key> {
    private val INVALID_KEY_EXCEPTION = DynamicCommandExceptionType { key: Any? ->
        Text.literal(
            "Invalid Key $key"
        )
    }

    override fun parse(reader: StringReader): InputUtil.Key {
        val string: String = reader.readUnquotedString()
        val key =
            InputUtil.fromTranslationKey("key.keyboard." + string[2].lowercase(Locale.getDefault())) ?: throw INVALID_KEY_EXCEPTION.createWithContext(reader, string)
        return key
    }

    // TODO: Add suggenstions

    companion object {
        @Throws(CommandSyntaxException::class)
        @JvmStatic
        fun getKey(context: CommandContext<FabricClientCommandSource?>, name: String?): Key {
            return context.getArgument(name, Key::class.java)
        }
    }
}