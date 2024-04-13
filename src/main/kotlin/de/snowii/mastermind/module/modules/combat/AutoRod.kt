package de.snowii.mastermind.module.modules.combat

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingBoolean
import de.snowii.mastermind.settings.SettingFloat
import de.snowii.mastermind.settings.SettingInt
import de.snowii.mastermind.util.EntityTracker
import de.snowii.mastermind.util.TimeHelper
import net.minecraft.entity.Entity
import net.minecraft.item.FishingRodItem
import java.util.*

object AutoRod : Module("AutoRod", "Attacks Entities with an Fishing Rod", Category.COMBAT) {
    private val CUSTOM_RANGE = SettingBoolean("Use Custom Range", false)
    private val RANGE = SettingFloat("Min Range", 5.5f, 3.3F, 8.0f, CUSTOM_RANGE::value)
    private val TIME_MIN = SettingInt("Switch Min ms", 1, 1, 10)
    private val TIME_MAX = SettingInt("Switch Max ms", 2, 2, 20)

    private val WAIT = SettingInt("Wait Max ms", 1, 1, 10)

    private val TARGET_PLAYERS = SettingBoolean("Players", true)
    private val TARGET_ANIMAL = SettingBoolean("Animals", false)
    private val TARGET_MOBS = SettingBoolean("Mobs", false)
    private val TARGET_VILLAGER = SettingBoolean("Villager", false)

    private var current_delay = (TIME_MIN.value..TIME_MAX.value).random()
    private var current_wait = 0
    private var timer = TimeHelper()
    private var timer_wait = TimeHelper()

    private var shouldWait: Boolean = false
    private var oldSlot: Int = -1

    init {
        addSetting(CUSTOM_RANGE)
        addSetting(RANGE)
        addSetting(TIME_MIN)
        addSetting(TIME_MAX)
        addSetting(WAIT)
        addSetting(TARGET_MOBS)
        addSetting(TARGET_ANIMAL)
        addSetting(TARGET_PLAYERS)
        addSetting(TARGET_VILLAGER)
    }

    override fun onKeyboardTick() {
        if (!shouldWait) {
            if (!mc.player!!.isUsingItem && timer.hasTimeReached((100 * current_delay).toLong())) {
                val rod_slot = searchRod()
                if (rod_slot != null) {
                    val range = if (CUSTOM_RANGE.value) RANGE.value else { KillAura.RANGE.value + 0.1F }
                    val targets = EntityTracker.entities(
                        EntityTracker.EntityFilter(
                            TARGET_PLAYERS.value,
                            TARGET_MOBS.value,
                            TARGET_ANIMAL.value,
                            TARGET_VILLAGER.value
                        ), Optional.of(RANGE.value)
                    ).filter { mc.player!!.distanceTo(it) >= range } // We dont want to Rod when attacking
                    targets.firstOrNull { entity: Entity? ->
                        if (entity != null) {
                            oldSlot = mc.player!!.inventory.selectedSlot;
                            mc.player!!.inventory.selectedSlot = rod_slot
                            mc.doItemUse()
                            shouldWait = true
                            val distance = mc.player!!.distanceTo(entity).toInt()
                            current_wait =
                                (distance..distance + WAIT.value).random() // We should wait longer when entity is further
                        }
                        return@firstOrNull true
                    }
                }
                current_delay = (TIME_MIN.value..TIME_MAX.value).random()
                timer.reset()
            }
        } else if (timer_wait.hasTimeReached((100 * current_wait).toLong())) {
            mc.player!!.inventory.selectedSlot = oldSlot
            shouldWait = false
            timer_wait.reset()
        }
    }

    private fun searchRod(): Int? {
        for (i in 0..8) {
            val stack = mc.player!!.inventory.getStack(i) ?: continue
            if (stack.item is FishingRodItem) {
                return i
            }
        }
        return null
    }
}