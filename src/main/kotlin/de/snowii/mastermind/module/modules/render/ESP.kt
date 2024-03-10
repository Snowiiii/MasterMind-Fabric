package de.snowii.mastermind.module.modules.render

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingBoolean
import de.snowii.mastermind.settings.SettingFloat
import de.snowii.mastermind.util.RenderUtil
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity

object ESP : Module("ESP", "Allows to see Entities throw Walls", Category.RENDER) {

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
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(WorldRenderEvents.DebugRender { context: WorldRenderContext ->
            if (this.isToggled)
                for (entity in mc.world!!.entities.filterIsInstance<LivingEntity>()) {
                    if (allowToESP(entity)) {
                        RenderUtil.draw3DLine(
                            context,
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
        })
    }


    private fun allowToESP(entity: LivingEntity): Boolean {
        val cam = mc.gameRenderer.camera.pos
        return if (entity !== mc.player && entity.shouldRender(
                cam.x,
                cam.y,
                cam.z
            ) && entity.isAlive
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