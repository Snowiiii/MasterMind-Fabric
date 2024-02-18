package de.snowii.mastermind.mixin.client

import com.mojang.authlib.GameProfile
import de.snowii.mastermind.module.Module
import de.snowii.mastermind.module.ModuleManager
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo


@Mixin(ClientPlayerEntity::class)

class MixinClientPlayerEntity(world: ClientWorld?, profile: GameProfile?) : AbstractClientPlayerEntity(
    world,
    profile
) {
    var saved_yaw: Float = 0.0F
    var saved_pitch: Float = 0.0F

    @Inject(
        method = ["tick()V"],
        at = [At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V",
            shift = At.Shift.BEFORE,
            ordinal = 0
        )],
    )
    private fun tickPre(ci: CallbackInfo) {
        saved_yaw = this.yaw
        saved_pitch = this.pitch
        ModuleManager.modules.forEach { module: Module ->
            run {
                if (module.isToggled) module.onPreUpdate()
            }
        }
    }

    @Inject(
        method = ["tick()V"],
        at = [At(
            value = "TAIL",
        )],
    )
    private fun tickPost(ci: CallbackInfo) {
        yaw = saved_yaw
        pitch = saved_pitch
    }

    /*@Redirect(
        method = ["tickMovement()V"],
        at = At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/options/KeyBinding;isPressed()Z"
        )
    )
    fun injectSprint(keyBinding: KeyBinding): Boolean {
         if (Sprint.instance.isToggled) {
           return true;
        } else return keyBinding.isPressed
    }*/
}