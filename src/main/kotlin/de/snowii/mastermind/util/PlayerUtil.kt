package de.snowii.mastermind.util

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object PlayerUtil {
    private val mc = MinecraftClient.getInstance()
    private val PREFIX = "[" + Formatting.GOLD + "Vent" + Formatting.RESET + "] "
    fun sendMessage(message: Any) {
        mc.player!!.sendMessage(Text.literal(PREFIX + message.toString()))
    }
}