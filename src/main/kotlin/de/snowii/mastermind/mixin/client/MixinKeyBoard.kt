package de.snowii.mastermind.mixin.client

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.module.ModuleManager
import net.minecraft.client.Keyboard
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo


@Mixin(Keyboard::class)
class MixinKeyBoard {

    @Shadow
    private val client: MinecraftClient? = null

    @Inject(
        method = ["onKey"],
        at = [At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/util/InputUtil;fromKeyCode(II)Lnet/minecraft/client/util/InputUtil\$Key;",
            shift = At.Shift.AFTER
        )]
    )
    private fun onKey(window: Long, key: Int, scancode: Int, action: Int, modifiers: Int, callback: CallbackInfo) {
        if (client!!.currentScreen == null && action == 0) {
            val inputKey = InputUtil.fromKeyCode(key, scancode)

            ModuleManager.modules.forEach { module: Module ->
                run {
                    if (module.key != InputUtil.UNKNOWN_KEY && inputKey == module.key) {
                        module.toggle()
                    }
                }
            }
        }
    }


}