package de.snowii.mastermind

import de.snowii.mastermind.command.CommandManager
import de.snowii.mastermind.module.ModuleManager
import de.snowii.mastermind.ui.HUDScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import org.slf4j.LoggerFactory
import java.io.File


class MasterMind : ClientModInitializer {

    override fun onInitializeClient() {
        directory = File(MinecraftClient.getInstance().runDirectory, "/mastermind/")
        if (!directory.exists()) directory.mkdir()
        ModuleManager
        CommandManager
        HudRenderCallback.EVENT.register(HUDScreen())
    }

    companion object {
        val LOGGER = LoggerFactory.getLogger("mastermind")!!
        lateinit var directory: File
    }
}