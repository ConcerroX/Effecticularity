package concerrox.minecraft.effecticularity.core.extensions

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

internal fun ClientLevel.getBlock(x: Double, y: Double, z: Double): Block {
    return getBlockState(BlockPos.containing(x, y, z)).block
}

internal fun ClientLevel.getBlockState(x: Double, y: Double, z: Double): BlockState {
    return getBlockState(BlockPos.containing(x, y, z))
}

internal fun ClientLevel.isAir(x: Double, y: Double, z: Double): Boolean {
    return getBlockState(BlockPos.containing(x, y, z)).isAir
}

internal fun ClientLevel.isBlock(x: Double, y: Double, z: Double, block: Block): Boolean {
    return getBlockState(BlockPos.containing(x, y, z)).block === block
}

internal fun ClientLevel.isBlock(blockPos: BlockPos, block: Block): Boolean {
    return getBlockState(blockPos).block === block
}