package de.snowii.mastermind.module.modules.player

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.util.PlayerUtil
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import java.util.*

object MidClick : Module("MidClick", "Allows you to add Friendly Players using Mouse Middle Button", Category.PLAYER) {
    val friends: MutableList<UUID> = ArrayList<UUID>()

    init {
        UseEntityCallback.EVENT.register(UseEntityCallback { player, world, hand, entity, result  ->
            if (this.isToggled)
                if (result!!.type == HitResult.Type.ENTITY) {
                    progressEntity(result.entity)
                }
            return@UseEntityCallback ActionResult::CONSUME.get()
        })
    }

    private fun progressEntity(entity: Entity) {
        if (entity is PlayerEntity) {
            if (friends.contains(entity.uuid)) {
                friends.remove(entity.uuid)
                PlayerUtil.sendMessage("${Formatting.RED} Removed ${entity.name.literalString} as Friend")
            } else {
                friends.add(entity.uuid)
                PlayerUtil.sendMessage("${Formatting.GREEN} Added ${entity.name.literalString} as Friend")
            }
        }
    }


    override fun onDisable() {
        friends.clear()
        PlayerUtil.sendMessage("${Formatting.RED} All friends has been cleared")
    }


}