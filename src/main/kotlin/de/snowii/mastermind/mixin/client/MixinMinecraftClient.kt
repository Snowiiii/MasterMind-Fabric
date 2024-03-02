package de.snowii.mastermind.mixin.client

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.module.ModuleManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(MinecraftClient::class)
class MixinMinecraftClient {

    var saved_yaw: Float = 0.0F
    var saved_pitch: Float = 0.0F

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
            target = "Lnet/minecraft/client/render/GameRenderer;updateTargetedEntity(F)V",
            shift = At.Shift.AFTER

        )],
    )
    fun onPreTick(ci: CallbackInfo) {
        if (player == null) return
        saved_yaw = player!!.yaw
        saved_pitch = player!!.pitch
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
        player!!.yaw = saved_yaw
        player!!.pitch = saved_pitch
    }



}