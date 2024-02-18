package de.snowii.mastermind.module.modules.render

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingBoolean
import de.snowii.mastermind.settings.SettingFloat
import de.snowii.mastermind.util.RenderUtil
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity

object Tracers : Module("Tracers", "Displays 2D lines to Targets", Category.RENDER), HudRenderCallback {
    private val RED = SettingFloat("Red", 1f, 0f, 1f)
    private val GREEN = SettingFloat("Green", 0.5f, 0f, 1f)
    private val BLUE = SettingFloat("Blue", 0f, 0f, 1f)
    private val ALPHA = SettingFloat("Alpha", 0.5f, 0.1f, 1f)
    private val WIDTH = SettingFloat("Width", 0.3f, 0.1f, 4f)
    private val TARGET_PLAYERS = SettingBoolean("Players", true)
    private val TARGET_ANIMAL = SettingBoolean("Animals", true)
    private val TARGET_MOBS = SettingBoolean("Mobs", true)
    private val TARGET_VILLAGER = SettingBoolean("Villager", true)

    init {
        addSetting(TARGET_PLAYERS)
        addSetting(TARGET_ANIMAL)
        addSetting(TARGET_MOBS)
        addSetting(TARGET_VILLAGER)
        addSetting(RED)
        addSetting(GREEN)
        addSetting(BLUE)
        addSetting(ALPHA)
        addSetting(WIDTH)
        HudRenderCallback.EVENT.register(this)
    }

    override fun onHudRender(drawContext: DrawContext, tickDelta: Float) {
        for (entity in mc.world!!.entities) {
            if (allowToESP(entity)) {
                RenderUtil.draw2DLine(
                    drawContext,
                    entity.x,
                    entity.eyeY,
                    entity.z,
                    RED.value,
                    GREEN.value,
                    BLUE.value,
                    ALPHA.value,
                    WIDTH.value
                )
            }
        }
    }

    private fun allowToESP(entity: net.minecraft.entity.Entity): Boolean {
        return if (entity is LivingEntity && entity !== mc.player && entity.isAlive
        ) {
            when (entity) {
                is PlayerEntity -> {
                    return TARGET_PLAYERS.value && mc.player!!.canTarget(entity) // TODO Mid click
                }

                is Monster -> {
                    return TARGET_MOBS.value
                }

                is AnimalEntity -> {
                    return TARGET_ANIMAL.value
                }

                is VillagerEntity -> {
                    return TARGET_VILLAGER.value
                }

                else -> false
            }
        } else false
    }

}