package de.snowii.mastermind.util

import de.snowii.mastermind.module.modules.player.MidClick
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import java.util.*

object EntityTracker {

    fun entities(filter: EntityFilter, range: Optional<Float>): Iterable<Entity> {
        val mc = MinecraftClient.getInstance()
        return mc.world!!.entities.filterIsInstance<LivingEntity>().filter { this.allowToAttack(it, filter, range) }
                .sortedBy { it.distanceTo(mc.player) }
    }

    private fun allowToAttack(entity: LivingEntity, filter: EntityFilter, range: Optional<Float>): Boolean {
        val mc = MinecraftClient.getInstance()
        val range = if (range.isPresent) mc.player!!.distanceTo(
            entity) <= range.get() else true
        return if (entity !== mc.player && range && mc.player!!.canTarget(entity) && entity.isAttackable
        ) {
            when (entity) {
                is PlayerEntity -> {
                    return filter.players && !MidClick.friends.contains(entity.uuid)
                }

                is Monster -> {
                    return filter.mobs
                }

                is AnimalEntity -> {
                    return filter.animals
                }

                is VillagerEntity -> {
                    return filter.villagers
                }

                else -> false
            }
        } else false
    }

    class EntityFilter(val players: Boolean, val mobs: Boolean, val animals: Boolean, val villagers: Boolean) {
    }
}