package WayofTime.bloodmagic.entity.ai;

import WayofTime.bloodmagic.entity.mob.EntityAspectedDemonBase;
import WayofTime.bloodmagic.inversion.CorruptionHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIEatAndCorruptBlock extends EntityAIBase {
    /**
     * The entity owner of this AITask
     */
    private final EntityAspectedDemonBase grassEaterEntity;
    /**
     * The world the grass eater entity is eating from
     */
    private final World world;
    /**
     * Number of ticks since the entity started to eat grass
     */
    int eatingGrassTimer;

    public EntityAIEatAndCorruptBlock(EntityAspectedDemonBase entity) {
        this.grassEaterEntity = entity;
        this.world = entity.getEntityWorld();
        this.setMutexBits(7);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.grassEaterEntity.getRNG().nextInt(50) != 0) {
            return false;
        } else {
            BlockPos pos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ).down();
            IBlockState offsetState = world.getBlockState(pos);
            Block offsetBlock = offsetState.getBlock();
            return CorruptionHandler.isBlockCorruptible(world, grassEaterEntity.getType(), pos, offsetState, offsetBlock);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.eatingGrassTimer = 40;
        this.world.setEntityState(this.grassEaterEntity, (byte) 10);
        this.grassEaterEntity.getNavigator().clearPath();
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.eatingGrassTimer = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return this.eatingGrassTimer > 0;
    }

    /**
     * Number of ticks since the entity started to eat grass
     */
    public int getEatingGrassTimer() {
        return this.eatingGrassTimer;
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);

        if (this.eatingGrassTimer == 4) {
            BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);

            BlockPos offsetPos = blockpos.down();
            IBlockState offsetState = world.getBlockState(offsetPos);
            Block offsetBlock = offsetState.getBlock();

            if (CorruptionHandler.isBlockCorruptible(world, grassEaterEntity.getType(), offsetPos, offsetState, offsetBlock)) {
//                if (this.world.getGameRules().getBoolean("mobGriefing"))
                {
                    this.world.playEvent(2001, offsetPos, Block.getIdFromBlock(offsetBlock));
                    CorruptionHandler.corruptSurroundingBlocks(world, grassEaterEntity.getType(), offsetPos, 2, 0.5, 0.5);
                }

                this.grassEaterEntity.eatGrassBonus();
            }
        }
    }
}