package de.snowii.mastermind.module.modules.player

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingInt
import de.snowii.mastermind.util.TimeHelper
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.SlotActionType


object AutoArmor : Module("AutoArmor", "Manages your Armor", Category.PLAYER) {
    private val timeHelper = TimeHelper()
    private val DELAY_MIN = SettingInt("Delay Min", 1, 0, 10) // 10 = 1 sec
    private val DELAY_MAX = SettingInt("Delay Max", 2, 0, 10) // 10 = 1 sec

    private var delay = (DELAY_MIN.value..DELAY_MAX.value).random()

    init {
        toggle(true)
        // Fabric API i love you, Did no tough i can do this without own mixins but events are great
        ScreenEvents.BEFORE_INIT.register(ScreenEvents.BeforeInit { client, screen, scaledWidth, scaledHeight ->
            run {
                if (screen is InventoryScreen) {
                    ScreenEvents.remove(screen).register(ScreenEvents.Remove { screen ->
                        run {
                            delay = 0
                            timeHelper.reset()
                        }
                    })
                }
            }
        })
        ScreenEvents.AFTER_INIT.register(ScreenEvents.AfterInit { client, screen, scaledWidth, scaledHeight ->
            if (!this.isToggled) return@AfterInit
            run {
                if (screen is InventoryScreen) {
                    ScreenEvents.afterTick(screen)
                        .register(ScreenEvents.AfterTick { screen ->
                            run {
                                if (timeHelper.hasTimeReached(100L * delay)) {
                                    val bestArmorSlots = IntArray(4) { -1 }
                                    val bestArmorValues = IntArray(4)

                                    for (type in 0..3) {
                                        val stack = mc.player!!.inventory.getArmorStack(type)
                                        if (stack == null || stack.item !is ArmorItem) continue
                                        val item: ArmorItem = stack.item as ArmorItem
                                        bestArmorValues[type] = getArmorValue(item, stack)
                                    }

                                    for (slot in 0..PlayerInventory.MAIN_SIZE) {
                                        val stack = mc.player!!.inventory.getStack(slot)
                                        if (stack == null || stack.item !is ArmorItem) continue
                                        val item = stack.item as ArmorItem
                                        val armorType = item.type.equipmentSlot.entitySlotId
                                        val armorValue = getArmorValue(item, stack)
                                        if (armorValue > bestArmorValues[armorType]) {
                                            bestArmorSlots[armorType] = slot
                                            bestArmorValues[armorType] = armorValue
                                        }
                                    }

                                    val types = mutableListOf(0, 1, 2, 3).shuffled()
                                    for (type in types) {
                                        val slot = bestArmorSlots[type]
                                        if (slot == -1) continue

                                        val adjustedSlot =
                                            if (slot < PlayerInventory.getHotbarSize()) slot + PlayerInventory.MAIN_SIZE else slot

                                        mc.interactionManager!!.clickSlot(
                                            mc.player!!.currentScreenHandler.syncId,
                                            8 - type,
                                            0,
                                            SlotActionType.QUICK_MOVE,
                                            mc.player
                                        )
                                        mc.interactionManager!!.clickSlot(
                                            mc.player!!.currentScreenHandler.syncId,
                                            adjustedSlot,
                                            0,
                                            SlotActionType.QUICK_MOVE,
                                            mc.player
                                        )
                                        break
                                    }
                                    delay = (DELAY_MIN.value..DELAY_MAX.value).random()
                                    timeHelper.reset()
                                }
                            }
                        })
                }
            }
        })
        addSetting(DELAY_MIN)
        addSetting(DELAY_MAX)
    }

    private fun getArmorValue(item: ArmorItem, stack: ItemStack): Int {
        val armorPoints = item.protection
        val armorToughness = item.toughness.toInt()
        val armorType = item.material.getProtection(item.type)

        val protection = Enchantments.PROTECTION

        val dmgSource =
            mc.player!!.damageSources.playerAttack(mc.player!!)
        val prtPoints = protection.getProtectionAmount(EnchantmentHelper.getLevel(protection, stack), dmgSource)

        return armorPoints + prtPoints + armorToughness + armorType
    }
}