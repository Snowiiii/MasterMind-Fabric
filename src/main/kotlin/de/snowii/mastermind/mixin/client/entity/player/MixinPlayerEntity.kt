package de.snowii.mastermind.mixin.client.entity.player

import com.llamalad7.mixinextras.injector.ModifyReturnValue
import de.snowii.mastermind.module.modules.combat.KillAura
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At

@Mixin(PlayerEntity::class)
class MixinPlayerEntity {

    @ModifyReturnValue(
        method = ["getEntityInteractionRange"],
        at = [At(value = "RETURN")]
    )
    private fun getEntityInteractionRange(original: Double): Double {
        if (KillAura.targets != null) {
            println(1.5F + KillAura.RANGE.value)
            return (1.5F + KillAura.RANGE.value).toDouble()
        }
        return original
    }
}