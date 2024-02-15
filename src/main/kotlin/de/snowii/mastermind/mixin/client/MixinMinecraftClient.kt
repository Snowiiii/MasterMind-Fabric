package de.snowii.mastermind.mixin.client

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.module.ModuleManager
import net.minecraft.client.MinecraftClient
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(MinecraftClient::class)
class MixinMinecraftClient {

    @Inject(
        method = ["handleInputEvents()V"],
        at = [At(
            value = "HEAD",
        )],
    )
    fun handleInputEvents(ci: CallbackInfo) {
        ModuleManager.modules.forEach { module: Module -> if (module.isToggled) module.onKeyboardTick() }
    }


}