package de.snowii.mastermind.module.modules.player

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingInt
import de.snowii.mastermind.util.TimeHelper
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.slot.SlotActionType

class ChestStealer : Module("CheatStealer", "Steals Cheast content", Category.PLAYER) {

    private val timeHelper = TimeHelper()
    private val DELAY_MIN = SettingInt("Delay Min", 1, 0, 10) // 10 = 1 sec
    private val DELAY_MAX = SettingInt("Delay Max", 2, 0, 10) // 10 = 1 sec
    private val slots: MutableList<Int> = ArrayList()
    private var delay = (DELAY_MIN.value..DELAY_MAX.value).random()

    init {
        toggle(true)
        ScreenEvents.AFTER_INIT.register(ScreenEvents.AfterInit { client, screen, scaledWidth, scaledHeight ->
            run {
                if (screen is GenericContainerScreen) {
                    slots.clear()
                    ScreenEvents.afterTick(screen)
                        .register(ScreenEvents.AfterTick { screen ->
                            val chest = mc.player!!.currentScreenHandler as GenericContainerScreenHandler
                            // Search
                            for (i in 0 until chest.inventory.size()) {
                                if (slots.contains(i)) continue
                                if (!chest.inventory.getStack(i).isEmpty) slots.add(i)
                            }
                            slots.shuffle()
                            // Currently very dump ChestStealer that just grabs everything out, But i will make a "smart" one in the future
                            for (slot in slots.reversed()) {
                                if (timeHelper.hasTimeReached(100L * delay)) {
                                    mc.interactionManager!!.clickSlot(
                                        chest.syncId,
                                        slot,
                                        0,
                                        SlotActionType.QUICK_MOVE,
                                        mc.player
                                    )
                                    timeHelper.reset()
                                    delay = (DELAY_MIN.value..DELAY_MAX.value).random()
                                    slots.remove(slot)
                                }
                            }
                           if (slots.isEmpty()) mc.player!!.closeHandledScreen()
                        })
                }
            }
        })
    }
}


