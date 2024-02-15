package de.snowii.mastermind.module.modules.combat

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingBoolean
import de.snowii.mastermind.settings.SettingFloat
import de.snowii.mastermind.settings.SettingInt
import de.snowii.mastermind.util.RenderUtil
import de.snowii.mastermind.util.RotationUtils
import de.snowii.mastermind.util.TimeHelper
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.random.Random
import org.lwjgl.glfw.GLFW

class KillAura : Module("KillAura", "Attacks Entities nearby", Category.COMBAT) {
    private val random: Random = Random.create()

    private val CPS_MIN = SettingInt("CPS Min", 8, 1, 20)
    private val CPS_MAX = SettingInt("CPS Max", 14, 2, 40)
    private val RANGE = SettingFloat("Range", 3.4f, 3.0F, 8.0f)

    private val TARGET_PLAYERS = SettingBoolean("Players", true)
    private val TARGET_ANIMAL = SettingBoolean("Animals", true)
    private val TARGET_MOBS = SettingBoolean("Mobs", true)
    private val TARGET_VILLAGER = SettingBoolean("Villager", true)

    private val RAY_TRACE = SettingBoolean("RayTrace", true)

    val ROTATION_SPEED: Float = 30F

    var hitTimer = TimeHelper()
    var rotations: FloatArray = FloatArray(2)
    var current_cps = (CPS_MIN.value..CPS_MAX.value).random()
    private var targets: Iterable<Entity>? = null


    init {
        key(GLFW.GLFW_KEY_R)
        // Disable on world change
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(ServerEntityWorldChangeEvents.AfterPlayerChange { player, origin, destination ->
            if (player == mc.player)
                run {
                    toggle(false)
                }
        })
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(WorldRenderEvents.DebugRender { context: WorldRenderContext ->
            if (targets != null)
                run {
                    targets!!.forEach { entity: Entity ->
                        run {
                            RenderUtil.renderTargetESPCircle(context, entity, 1.0f, 1.0f, 0.0f, 1.0f, 2, 3F, 1F)
                        }
                    }
                }
        })
    }

    override fun onPreUpdate() {
        targets = mc.world!!.entities.filter { entity: Entity -> allowToAttack(entity) }
        targets!!.forEach { entity: Entity ->
            run {
                if (allowToAttack(entity)) {
                    rotations =
                        RotationUtils.getRotationsToEntity(
                            entity,
                            ROTATION_SPEED
                        )
                    RotationUtils.setRotation(
                        rotations[0] + MathHelper.nextFloat(random, 0.0F, entity.width / 2),
                        rotations[1] + (0..1).random()
                    )
                }
            }
        }
    }

    override fun onKeyboardTick() {
        if (targets == null) return
        targets!!.forEach { entity: Entity ->
            if (RAY_TRACE.value) {
                RotationUtils.rayTrace(rotations[0], rotations[1])
                if (mc.crosshairTarget != null && mc.crosshairTarget!!.type == HitResult.Type.ENTITY) {
                    attackEntity(entity as LivingEntity)
                }
            } else {
                attackEntity(entity as LivingEntity)
            }
        }
    }

    private fun allowToAttack(entity: Entity): Boolean {
        return if (entity is LivingEntity && entity !== mc.player && mc.player!!.distanceTo(
                entity
            ) <= RANGE.value && entity.health != 0f && entity.isAlive
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

    private fun attackEntity(entity: LivingEntity) {
        if (!mc.player!!.isUsingItem && hitTimer.hasTimeReached((1000 / current_cps).toLong())) {
            if (mc.attackCooldown <= 0) {
                if (RAY_TRACE.value) {
                    mc.doAttack()
                } else {
                    mc.interactionManager!!.attackEntity(mc.player, entity)
                    mc.player!!.swingHand(Hand.MAIN_HAND)
                }
                current_cps = (CPS_MIN.value..CPS_MAX.value).random()
                hitTimer.reset()
            }
        }
    }

    override fun onDisable() {
        targets = null
    }

}