package de.snowii.mastermind.mixin.client

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.module.ModuleManager
import de.snowii.mastermind.util.RotationUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(MinecraftClient::class)
class MixinMinecraftClient {

    @Shadow
    var player: ClientPlayerEntity? = null

    @Inject(
        method = ["handleInputEvents()V"],
        at = [At(
            value = "HEAD",
        )],
    )
    fun handleInputEvents(ci: CallbackInfo) {
        ModuleManager.modules.forEach { module: Module -> if (module.isToggled) module.onKeyboardTick() }
    }

    @Inject(
        method = ["tick()V"],
        at = [At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/GameRenderer;updateCrosshairTarget(F)V",
            shift = At.Shift.AFTER

        )],
    )
    fun onPreTick(ci: CallbackInfo) {
        if (player == null) return
        RotationUtils.savedYaw = player!!.yaw
        RotationUtils.savedPitch = player!!.pitch
        ModuleManager.modules.forEach { module: Module ->
            run {
                if (module.isToggled) module.onPreUpdate()
            }
        }
    }

    @Inject(
        method = ["tick()V"],
        at = [At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/world/ClientWorld;tick",
            shift = At.Shift.AFTER

        )],
    )
    fun onPostTick(ci: CallbackInfo) {
        player!!.yaw = RotationUtils.savedYaw
        player!!.pitch = RotationUtils.savedPitch
    }


}