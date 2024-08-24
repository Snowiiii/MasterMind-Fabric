package de.snowii.mastermind.mixin.client

import com.llamalad7.mixinextras.injector.ModifyExpressionValue
import de.snowii.mastermind.module.modules.movement.Sprint
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.client.network.ClientPlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At


@Mixin(ClientPlayerEntity::class)

class MixinClientPlayerEntity {
    @ModifyExpressionValue(
        method = ["tickMovement"],
        at = [At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z")]
    )
    private fun tickMovement(original: Boolean): Boolean {
        return Sprint.instance.isToggled || original
    }
}