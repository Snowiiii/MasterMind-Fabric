package de.snowii.mastermind.module.modules.world

import de.snowii.mastermind.module.Module
import de.snowii.mastermind.settings.SettingBoolean
import de.snowii.mastermind.settings.SettingFloat
import de.snowii.mastermind.util.RotationUtils
import de.snowii.mastermind.util.TimeHelper
import net.minecraft.block.Block
import net.minecraft.block.FallingBlock
import net.minecraft.item.BlockItem
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import org.lwjgl.glfw.GLFW

class Scaffold : Module("Scaffold", "Makes you an professional bridger", Category.WORLD) {
    private val SILENT = SettingBoolean("Silent Switch", true)
    private val SENSITIVITY_ROTATION = SettingBoolean("Sensitivity Rotation", true)
    private val ROTATION_SPEED = SettingFloat("Rotation Speed", 50f, 10f, 100f) { SENSITIVITY_ROTATION.value }
    private var currentPos: BlockPos? = null
    private var currentFacing: Direction? = null
    private val lookTime = TimeHelper()
    private var shouldLook = false
    private var shouldPlace = false
    private var currentPlayerFacing: Direction? = null
    private var oldSlot = 0

    val HAND: Hand = Hand.MAIN_HAND

    init {
        key(GLFW.GLFW_KEY_V)
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
        if (shouldLook) {
            mc.player!!.isSprinting = false
            val speed = ROTATION_SPEED.value
            val pitch = 80.0f // god bridge
            val yaw = getRotation(currentFacing == currentPlayerFacing)
            RotationUtils.setRotation(
                RotationUtils.updateRotation(mc.player!!.yaw, yaw, speed),
                RotationUtils.updateRotation(mc.player!!.pitch, pitch, speed)
            )
            if (currentPos != null) {
                shouldPlace = true
            }
        }
        if (shouldLook && lookTime.hasTimeReached(1000)) {
            if (!SILENT.value) mc.player!!.inventory.selectedSlot = oldSlot
            shouldLook = false
            lookTime.reset()
            // mc.player.moveCamera = false
        }
        if (currentPos != null && currentFacing != null && shouldPlace) {
            val stack = mc.player!!.getStackInHand(Hand.MAIN_HAND)
            if (stack == null || stack.item !is BlockItem) {
                if (!searchAndSelectBlock()) return
            } else if (isBlockBad((stack.item as BlockItem).block)) if (!searchAndSelectBlock()) return
            // mc.player.moveCamera = true
            placeBlock()
            if (SILENT.value) mc.player!!.inventory.selectedSlot = oldSlot
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

    private fun getRotation(forward: Boolean): Float {
        val rotation = MathHelper.wrapDegrees(RotationUtils.camera_yaw) // TODO Camera rot
        var i = -180
        while (i < 220) {
            if (rotation < i + 25.0 && rotation > i - 25.0) {
                return if (forward) i.toFloat() - 180f else i.toFloat()
            }
            i += 45
        }
        return mc.player!!.pitch
    }

    private fun isBlockBad(block: Block): Boolean {
        return block.defaultState.isSolidBlock(
            mc.player!!.world,
            BlockPos.ORIGIN,
        ) || block is FallingBlock
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
        val actionResult2: ActionResult = mc.interactionManager!!.interactBlock(
            mc.player, HAND,
            BlockHitResult(currentPos!!.toCenterPos(), currentFacing, currentPos, false)
        )
        if (actionResult2.isAccepted) {
            if (actionResult2.shouldSwingHand()) {
                mc.player!!.swingHand(HAND)
                mc.gameRenderer.firstPersonRenderer.resetEquipProgress(HAND)
            }

        }
    }
}