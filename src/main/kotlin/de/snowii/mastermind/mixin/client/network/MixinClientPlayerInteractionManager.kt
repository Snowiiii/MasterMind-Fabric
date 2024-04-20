package de.snowii.mastermind.mixin.client.network

import com.llamalad7.mixinextras.injector.ModifyReturnValue
import de.snowii.mastermind.module.modules.combat.KillAura
import net.minecraft.client.network.ClientPlayerInteractionManager
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At

@Mixin(ClientPlayerInteractionManager::class)
class MixinClientPlayerInteractionManager {

    @ModifyReturnValue(
        method = ["getReachDistance"],
        at = [At(value = "RETURN")]
    )
    private fun getReachDistance(original: Float): Float {
        if (KillAura.targets != null) {
            return 1.5F + KillAura.RANGE.value
        }
        return original
    }
}