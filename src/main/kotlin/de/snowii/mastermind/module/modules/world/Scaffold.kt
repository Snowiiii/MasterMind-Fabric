package de.snowii.mastermind.module.modules.world

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingBoolean
import de.snowii.mastermind.util.RotationUtils
import de.snowii.mastermind.util.TimeHelper
import net.minecraft.block.Block
import net.minecraft.block.ChestBlock
import net.minecraft.block.FallingBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import org.lwjgl.glfw.GLFW

object Scaffold : Module("Scaffold", "Makes you an professional bridger", Category.WORLD) {
    private val BLOCK_SWITCH = SettingBoolean("Block Switch", true)
    private val ROTATION = SettingBoolean("Rotation", true)

    private var currentPos: BlockPos? = null
    private var currentFacing: Direction? = null
    private val lookTime = TimeHelper()
    private var shouldLook = false
    private var shouldPlace = false
    private var oldSlot = 0

    private var current_rot: Float? = null

    init {
        key(GLFW.GLFW_KEY_V)
        addSetting(BLOCK_SWITCH)
        addSetting(ROTATION)
    }

    override fun onPreUpdate() {
        val pos = mc.player!!.blockPos.down()
        if (mc.world!!.getBlockState(pos).isAir) {
            setBlockAndFacing(pos)
            if (currentPos != null) {
                oldSlot = mc.player!!.inventory.selectedSlot
                shouldLook = true
            }
        }

        if (currentPos != null) {
            mc.player!!.isSprinting = false
            current_rot = getRotation()
            shouldPlace = true
        }

        if (ROTATION.value && shouldLook && current_rot != null) {
            mc.player!!.isSprinting = false
            val pitch = 80.0f // god bridge
            RotationUtils.setRotation(
                current_rot!!,
                pitch
            )
        }

        if (shouldLook && lookTime.hasTimeReached(1000)) {
            if (!BLOCK_SWITCH.value) mc.player!!.inventory.selectedSlot = oldSlot
            shouldLook = false
            current_rot = null
            lookTime.reset()
            // mc.player.moveCamera = false
        }
    }

    override fun onKeyboardTick() {
        if (currentPos != null && currentFacing != null && shouldPlace) {
            val stack_main = mc.player!!.getStackInHand(Hand.MAIN_HAND)
            val stack_off = mc.player!!.getStackInHand(Hand.OFF_HAND)
            if ((stack_main.isEmpty || stack_main.item !is BlockItem) && (stack_off.isEmpty || stack_off.item !is BlockItem)) {
                if (!searchAndSelectBlock()) return
            } else if (isBlockBad((stack_main.item as BlockItem).block) && isBlockBad((stack_off.item as BlockItem).block)) if (!searchAndSelectBlock()) return
            // mc.player.moveCamera = true
            placeBlock()
            if (BLOCK_SWITCH.value) mc.player!!.inventory.selectedSlot = oldSlot
            shouldPlace = false
            lookTime.reset()
            currentPos = null
        }
    }

    private fun searchAndSelectBlock(): Boolean {
        for (i in 0..8) {
            val stack = mc.player!!.inventory.getStack(i) ?: continue
            if (stack.item is BlockItem) {
                if (!isBlockBad((stack.item as BlockItem).block)) {
                    mc.player!!.inventory.selectedSlot = i
                    return true
                }
            }
        }
        return false
    }

    private fun getRotation(): Float {
        return RotationUtils.getRotationsTo(currentPos!!.toCenterPos())[0]
    }

    private fun isBlockBad(block: Block): Boolean {
        return block is FallingBlock
    }

    private fun setBlockAndFacing(pos: BlockPos) {
        val world = mc.world!!
        if (!mc.world!!.getBlockState(pos.down()).isAir && shouldLook) {
            currentPos = pos.down()
            currentFacing = Direction.UP
        } else if (!world.getBlockState(pos.west()).isAir) {
            currentPos = pos.west()
            currentFacing = Direction.EAST
        } else if (!world.getBlockState(pos.east()).isAir) {
            currentPos = pos.east()
            currentFacing = Direction.WEST
        } else if (!world.getBlockState(pos.north()).isAir) {
            currentPos = pos.north()
            currentFacing = Direction.SOUTH
        } else if (!world.getBlockState(pos.south()).isAir) {
            currentPos = pos.south()
            currentFacing = Direction.NORTH
        } else if (!world.getBlockState(pos.add(0, -2, 0)).isAir) {
            currentPos = pos.add(0, -2, 0)
            currentFacing = Direction.UP
        } else {
            currentPos = null
            currentFacing = null
        }
    }

    private fun placeBlock() {
        val player = mc.player!!
        if (!player.isUsingItem) {
            val from = player.pos
            val d: Double = currentPos!!.x - from.x
            val e: Double = currentPos!!.y - from.y
            val f: Double = currentPos!!.z - from.z
            for (hand in Hand.entries) {
                val itemStack: ItemStack = player.getStackInHand(hand)
                if (!itemStack.isItemEnabled(mc.world!!.enabledFeatures)) {
                    return
                }
                val i = itemStack.count
                val actionResult2: ActionResult = mc.interactionManager!!.interactBlock(
                    player, hand,
                    BlockHitResult(from.add(d, e, f), currentFacing, currentPos, false)
                )
                if (actionResult2.isAccepted) {
                    if (actionResult2.shouldSwingHand()) {
                        player.swingHand(hand)
                        if (!itemStack.isEmpty && (itemStack.count != i || mc.interactionManager!!.hasCreativeInventory())) {
                            mc.gameRenderer.firstPersonRenderer.resetEquipProgress(hand)
                        }
                    }

                }
            }
        }
    }
}