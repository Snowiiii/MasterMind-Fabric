package de.snowii.mastermind.mixin.client.gui

import de.snowii.mastermind.ui.LoginScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(TitleScreen::class)
class MixinTitleScreen(title: Text?) : Screen(title) {

    @Inject(
        method = ["addNormalWidgets"],
        at = [At(
            "HEAD"
        )],
    )
    private fun addNormalWidgets(y: Int, spacingY: Int, cir: CallbackInfoReturnable<Int>) {
        this.addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Login")
            ) { button: ButtonWidget ->
                this.client!!.setScreen(
                    LoginScreen()
                )
            }.dimensions(this.width - 60, 2, 50, 20).build()
        )
    }
}