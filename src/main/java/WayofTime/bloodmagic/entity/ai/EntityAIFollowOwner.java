package WayofTime.bloodmagic.entity.ai;

import WayofTime.bloodmagic.entity.mob.EntityDemonBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowOwner extends Goal {
    World theWorld;
    float maxDist;
    float minDist;
    private EntityDemonBase thePet;
    private LivingEntity theOwner;
    private double followSpeed;
    private PathNavigator petPathfinder;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public EntityAIFollowOwner(EntityDemonBase thePetIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.thePet = thePetIn;
        this.theWorld = thePetIn.getEntityWorld();
        this.followSpeed = followSpeedIn;
        this.petPathfinder = thePetIn.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);

        if (!(thePetIn.getNavigator() instanceof GroundPathNavigator)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        LivingEntity entitylivingbase = this.thePet.getOwner();

        if (entitylivingbase == null) {
            return false;
        } else if (entitylivingbase instanceof PlayerEntity && ((PlayerEntity) entitylivingbase).isSpectator()) {
            return false;
        } else if (this.thePet.isStationary()) {
            return false;
        } else if (this.thePet.getDistanceSq(entitylivingbase) < (double) (this.minDist * this.minDist)) {
            return false;
        } else {
            this.theOwner = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return !this.petPathfinder.noPath() && this.thePet.getDistanceSq(this.theOwner) > (double) (this.maxDist * this.maxDist) && !this.thePet.isStationary();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.thePet.getPathPriority(PathNodeType.WATER);
        this.thePet.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.theOwner = null;
        this.petPathfinder.clearPath();
        this.thePet.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    private boolean isEmptyBlock(BlockPos pos) {
        BlockState iblockstate = this.theWorld.getBlockState(pos);
        Block block = iblockstate.getBlock();
        return block == Blocks.AIR || !iblockstate.isFullCube();
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        this.thePet.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float) this.thePet.getVerticalFaceSpeed());

        if (!this.thePet.isStationary()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;

                if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.followSpeed)) {
                    if (!this.thePet.getLeashed()) {
                        if (this.thePet.getDistanceSq(this.theOwner) >= 144.0D) {
                            int i = MathHelper.floor(this.theOwner.posX) - 2;
                            int j = MathHelper.floor(this.theOwner.posZ) - 2;
                            int k = MathHelper.floor(this.theOwner.getEntityBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l) {
                                for (int i1 = 0; i1 <= 4; ++i1) {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isTopSolid() && this.isEmptyBlock(new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1))) {
                                        this.thePet.setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.thePet.rotationYaw, this.thePet.rotationPitch);
                                        this.petPathfinder.clearPath();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}