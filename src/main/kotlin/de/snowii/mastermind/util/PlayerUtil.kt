package de.snowii.mastermind.util

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object PlayerUtil {
    private val mc = MinecraftClient.getInstance()
    private val PREFIX = "[" + Formatting.GOLD + "MasterMind" + Formatting.RESET + "] "

    @Deprecated("")
    fun sendMessage(message: Any) {
        mc.player!!.sendMessage(Text.literal(PREFIX + message.toString()), false)
    }

    fun sendMessage(source: FabricClientCommandSource, message: Any) {
        source.sendFeedback(Text.literal(PREFIX + message.toString()))
    }
}