package de.snowii.mastermind.mixin.client

import com.llamalad7.mixinextras.injector.ModifyExpressionValue
import com.llamalad7.mixinextras.injector.ModifyReturnValue
import de.snowii.mastermind.module.modules.movement.Sprint
import net.minecraft.client.network.ClientPlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At


@Mixin(ClientPlayerEntity::class)

class MixinClientPlayerEntity {
    @ModifyReturnValue(
        method = ["shouldStopSprinting"],
        at = [At(value = "RETURN")]
    )
    private fun shouldStopSprinting(original: Boolean): Boolean {
        return Sprint.instance.isToggled || original
    }
}