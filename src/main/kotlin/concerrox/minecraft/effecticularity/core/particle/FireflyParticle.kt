package concerrox.minecraft.effecticularity.core.particle

import concerrox.minecraft.effecticularity.core.utils.EffectiveUtils
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.ParticleEngine
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max
import kotlin.math.min


open class FireflyParticle(
    world: ClientLevel,
    data: WorldParticleOptions,
    spriteSet: ParticleEngine.MutableSpriteSet,
    x: Double,
    y: Double,
    z: Double,
    xd: Double,
    yd: Double,
    zd: Double
) :
    LodestoneWorldParticle(world, data, spriteSet, x, y, z, xd, yd, zd) {
    protected var nextAlphaGoal: Float = 0f
    protected var xTarget: Double = 0.0
    protected var yTarget: Double = 0.0
    protected var zTarget: Double = 0.0
    protected var targetChangeCooldown: Int = 0
    protected var maxHeight: Int
    private var lightTarget: BlockPos? = null

    init {
//        this.sprite = spriteSet.get(1, 1)
        this.lifetime = ThreadLocalRandom.current().nextInt(400, 600) // live between 20 seconds and one minute
        this.maxHeight = 4
        this.alpha = 1f
        this.hasPhysics = false
    }

    override fun tick() {
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z

        // fade and die on daytime or if old enough unless fireflies can spawn any time of day
        if ((!level.dimensionType().hasFixedTime() && !EffectiveUtils.isNightTime(level)) || age++ >= this.lifetime) {
            nextAlphaGoal = 0f
            if (this.alpha <= 0.01f) {
                this.remove()
            }
        }

        // blinking
        if (this.alpha > nextAlphaGoal - BLINK_STEP && this.alpha < nextAlphaGoal + BLINK_STEP) {
            nextAlphaGoal = random.nextFloat()
        } else {
            if (nextAlphaGoal > this.alpha) {
                this.alpha =
                    min((this.alpha + BLINK_STEP).toDouble(), 1.0).toFloat()
            } else if (nextAlphaGoal < this.alpha) {
                this.alpha =
                    max((this.alpha - BLINK_STEP).toDouble(), 0.0).toFloat()
            }
        }

        //		this.targetChangeCooldown -= (new Vec3d(x, y, z).squaredDistanceTo(prevPosX, prevPosY, prevPosZ) < 0.0125) ? 10 : 1;
        if (random.nextInt(20) == 0 || (xTarget == 0.0 && yTarget == 0.0 && zTarget == 0.0) || Vec3(
                x,
                y,
                z
            ).distanceToSqr(xTarget, yTarget, zTarget) < 9
        ) {
            selectBlockTarget()
        }

        var targetVector: Vec3 = Vec3(this.xTarget - this.x, this.yTarget - this.y, this.zTarget - this.z)
        val length = targetVector.length()
        targetVector = targetVector.scale(0.1 / length)

        val blockPos: BlockPos = BlockPos.containing(this.x, this.y - 0.1, this.z)
        if (!canFlyThroughBlock(this.level, blockPos, this.level.getBlockState(blockPos))) {
            xd = (0.9) * xd + (0.1) * targetVector.x
            yd = 0.05
            zd = (0.9) * zd + (0.1) * targetVector.z
        } else {
            xd = (0.9) * xd + (0.1) * targetVector.x
            yd = (0.9) * yd + (0.1) * targetVector.y
            zd = (0.9) * zd + (0.1) * targetVector.z
        }
        if (!BlockPos.containing(x, y, z).equals(this.targetPosition)) {
            this.move(xd, yd, zd)
        }
//        cout("tick${xd} ${x}")
    }

    private fun selectBlockTarget() {
        if (this.lightTarget == null) {
            // Behaviour
            var groundLevel = 0.0
            for (i in 0..19) {
                val checkedPos: BlockPos = BlockPos.containing(this.x, this.y - i, this.z)
                val checkedBlock: BlockState = this.level.getBlockState(checkedPos)
                if (canFlyThroughBlock(this.level, checkedPos, checkedBlock)) {
                    groundLevel = this.y - i
                }
                if (groundLevel != 0.0) break
            }

            this.xTarget = this.x + random.nextGaussian() * 10
            this.yTarget = min(max(this.y + random.nextGaussian() * 2, groundLevel), groundLevel + maxHeight)
            this.zTarget = this.z + random.nextGaussian() * 10

            val targetPos = BlockPos.containing(this.xTarget, this.yTarget, this.zTarget)
            if (!canFlyThroughBlock(this.level, targetPos, this.level.getBlockState(targetPos))) {
                this.yTarget += 1.0
            }

            this.lightTarget = this.mostLitBlockAround
        } else {
            this.xTarget = lightTarget!!.x + random.nextGaussian()
            this.yTarget = lightTarget!!.y + random.nextGaussian()
            this.zTarget = lightTarget!!.z + random.nextGaussian()

            if (this.level.getBrightness(LightLayer.BLOCK, BlockPos.containing(x, y, z)) > 0 && !this.level.isDay) {
                this.lightTarget = this.mostLitBlockAround
            } else {
                this.lightTarget = null
            }
        }

        //		targetChangeCooldown = random.nextInt() % 100;
    }

    val targetPosition: BlockPos
        get() = BlockPos.containing(this.xTarget, this.yTarget + 0.5, this.zTarget)

    private val mostLitBlockAround: BlockPos
        get() {
            val randBlocks = HashMap<BlockPos, Int>()

            // get blocks adjacent to the fly
            for (x in -1..1) {
                for (y in -1..1) {
                    for (z in -1..1) {
                        val bp: BlockPos = BlockPos.containing(this.x + x, this.y + y, this.z + z)
                        randBlocks[bp] = this.level.getBrightness(LightLayer.BLOCK, bp)
                    }
                }
            }

            // get other random blocks to find a different light source
            for (i in 0..14) {
                val randBP: BlockPos = BlockPos.containing(
                    this.x + random.nextGaussian() * 10,
                    this.y + random.nextGaussian() * 10,
                    this.z + random.nextGaussian() * 10
                )
                randBlocks[randBP] = this.level.getBrightness(LightLayer.BLOCK, randBP)
            }

            return randBlocks.entries.stream()
                .max { entry1: Map.Entry<BlockPos, Int>, entry2: Map.Entry<BlockPos, Int> -> if (entry1.value > entry2.value) 1 else -1 }
                .get().key
        }

    companion object {
        protected const val BLINK_STEP: Float = 0.01f
        @JvmStatic
        fun canFlyThroughBlock(world: Level, blockPos: BlockPos, blockState: BlockState): Boolean {
            return !blockState.isSuffocating(world, blockPos) && blockState.fluidState.isEmpty
        }
    }
}