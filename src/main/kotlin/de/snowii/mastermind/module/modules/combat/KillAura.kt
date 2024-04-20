package de.snowii.mastermind.module.modules.combat

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingBoolean
import de.snowii.mastermind.settings.SettingFloat
import de.snowii.mastermind.settings.SettingInt
import de.snowii.mastermind.util.EntityTracker
import de.snowii.mastermind.util.RenderUtil
import de.snowii.mastermind.util.RotationUtils
import de.snowii.mastermind.util.TimeHelper
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import org.lwjgl.glfw.GLFW
import java.util.*


object KillAura : Module("KillAura", "Attacks Entities nearby", Category.COMBAT) {
    private val CPS_MIN = SettingInt("CPS Min", 8, 1, 20)
    private val CPS_MAX = SettingInt("CPS Max", 14, 2, 40)
    val RANGE = SettingFloat("Range", 3.3f, 3.0F, 8.0f)

    private val TARGET_PLAYERS = SettingBoolean("Players", true)
    private val TARGET_ANIMAL = SettingBoolean("Animals", false)
    private val TARGET_MOBS = SettingBoolean("Mobs", false)
    private val TARGET_VILLAGER = SettingBoolean("Villager", false)

    private val ROTATION = SettingBoolean("Rotation", true)
    private val PRE_AIM = SettingBoolean("Pre Aim", true) { ROTATION.value }
    private val PRE_AIM_RANGE =
        SettingFloat("Pre Aim Range", 1.5F, 0.5F, 4.0F) { PRE_AIM.value }
    private val RAY_TRACE = SettingBoolean("RayTrace", true)
    private val NEW_PVP = SettingBoolean("New PVP", true)

    private val RED = SettingFloat("ESP Red", 1f, 0f, 1f)
    private val GREEN = SettingFloat("ESP Green", 0.5f, 0f, 1f)
    private val BLUE = SettingFloat("ESP Blue", 0f, 0f, 1f)
    private val ALPHA = SettingFloat("ESP Alpha", 1f, 0.1f, 1f)
    private val PRE_RED = SettingFloat("Pre Aim Red", 1f, 0f, 1f)
    private val PRE_GREEN = SettingFloat("Pre Aim Green", 0.3f, 0f, 1f)
    private val PRE_BLUE = SettingFloat("Pre Aim Blue", 0f, 0f, 1f)
    private val PRE_ALPHA = SettingFloat("Pre Aim Alpha", 1f, 0.1f, 1f)
    private val WIDTH = SettingFloat("ESP Width", 3f, 0.1f, 4f)

    var hitTimer = TimeHelper()
    var rotations: FloatArray = FloatArray(2)
    var current_cps = (CPS_MIN.value..CPS_MAX.value).random()
     var targets: Iterable<Entity>? = null


    init {
        key(GLFW.GLFW_KEY_R)
        // Disable on world change
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(ServerEntityWorldChangeEvents.AfterPlayerChange { player, origin, destination ->
            if (player == mc.player)
                run {
                    toggle(false)
                }
        })
        ServerPlayerEvents.AFTER_RESPAWN.register(ServerPlayerEvents.AfterRespawn { oldPlayer: ServerPlayerEntity?, newPlayer: ServerPlayerEntity?, alive: Boolean ->
            if (oldPlayer == mc.player)
                run {
                    toggle(false)
                }
        })
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(WorldRenderEvents.DebugRender { context: WorldRenderContext ->
            if (targets != null)
                run {
                    targets!!.forEach { entity: Entity ->
                        run {
                            if (PRE_AIM.value && mc.player!!.distanceTo(entity) > RANGE.value) {
                                RenderUtil.renderTargetESPCircle(
                                    context,
                                    entity,
                                    PRE_RED.value,
                                    PRE_RED.value,
                                    PRE_RED.value,
                                    PRE_RED.value,
                                    2,
                                    WIDTH.value,
                                    1F
                                )
                            } else {
                                RenderUtil.renderTargetESPCircle(
                                    context,
                                    entity,
                                    RED.value,
                                    GREEN.value,
                                    BLUE.value,
                                    ALPHA.value,
                                    2,
                                    WIDTH.value,
                                    1F
                                )
                            }
                        }
                    }
                }
        })
        addSetting(CPS_MIN)
        addSetting(CPS_MAX)
        addSetting(RANGE)
        addSetting(TARGET_MOBS)
        addSetting(TARGET_ANIMAL)
        addSetting(TARGET_PLAYERS)
        addSetting(TARGET_VILLAGER)
        addSetting(RAY_TRACE)
        addSetting(ROTATION)
        addSetting(PRE_AIM)
        addSetting(PRE_AIM_RANGE)
        addSetting(NEW_PVP)

        // ESP
        addSetting(RED)
        addSetting(GREEN)
        addSetting(BLUE)
        addSetting(ALPHA)
        addSetting(PRE_RED)
        addSetting(PRE_GREEN)
        addSetting(PRE_BLUE)
        addSetting(PRE_ALPHA)
        addSetting(WIDTH)
    }

    override fun onPreUpdate() {
        val RANGE = if (PRE_AIM.value) RANGE.value + PRE_AIM_RANGE.value else RANGE.value
        targets = EntityTracker.entities(EntityTracker.EntityFilter(TARGET_PLAYERS.value, TARGET_MOBS.value, TARGET_ANIMAL.value, TARGET_VILLAGER.value), Optional.of(RANGE))
        targets!!.forEach { entity: Entity ->
            run {
                if (ROTATION.value) {
                    rotations =
                        RotationUtils.getRotationsTo(
                            entity.pos
                        )
                    rotations = RotationUtils.fixedSensitivity(rotations[0], rotations[1])
                    RotationUtils.setRotation(
                        rotations[0],
                        rotations[1]
                    )
                }
            }
        }
    }

    override fun onKeyboardTick() {
        if (targets == null) return
        targets!!.forEach { entity: Entity ->
            // Don't Attack when just Pre Aiming
            if (PRE_AIM.value && mc.player!!.distanceTo(entity) > RANGE.value) return
            if (RAY_TRACE.value) {
                RotationUtils.rayTrace(rotations[0], rotations[1])
                if (mc.crosshairTarget != null && mc.crosshairTarget!!.type == HitResult.Type.ENTITY) {
                    attackEntity((mc.crosshairTarget as EntityHitResult).entity)
                }
            } else {
                attackEntity(entity)
            }
        }
    }

    private fun attackEntity(entity: Entity) {
        if (!mc.player!!.isUsingItem && hitTimer.hasTimeReached((1000 / current_cps).toLong())) {
            if (NEW_PVP.value && mc.player!!.getAttackCooldownProgress(1.0F) < 1.0F) return
            // keep in mind that when attacking, Velocity packets will be sent, calculated using yaw & pitch
            if (RAY_TRACE.value) {
                mc.doAttack()
            } else {
                if (mc.attackCooldown <= 0) {
                    mc.interactionManager!!.attackEntity(mc.player, entity)
                    mc.player!!.swingHand(Hand.MAIN_HAND)
                }
            }
            current_cps = (CPS_MIN.value..CPS_MAX.value).random()
            hitTimer.reset()
        }
    }

    override fun onDisable() {
        targets = null
    }



}