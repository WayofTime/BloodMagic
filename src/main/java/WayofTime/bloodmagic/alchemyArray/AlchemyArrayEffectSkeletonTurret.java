package WayofTime.bloodmagic.alchemyArray;

import WayofTime.bloodmagic.tile.TileAlchemyArray;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Credits for the initial code go to Crazy Pants of EIO.
 */
public class AlchemyArrayEffectSkeletonTurret extends AlchemyArrayEffect {
    public static Predicate<EntityMob> checkSkeleton = input -> !(input instanceof EntitySkeleton);
    private EntitySkeleton turret;

    public AlchemyArrayEffectSkeletonTurret(String key) {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive) {
//        if (tile.getWorld().isRemote)
//        {
//            return false;
//        }

        BlockPos pos = tile.getPos();

        if (turret != null && !turret.isDead) {
            double x = (pos.getX() + 0.5D - turret.posX);
            double y = (pos.getY() + 1D - turret.posY);
            double z = (pos.getZ() + 0.5D - turret.posZ);
            double distance = Math.sqrt(x * x + y * y + z * z);

            if (distance < 2) {
//                turret.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 100));
                return false;
            }
        }

        World world = tile.getWorld();

        List<EntitySkeleton> skeletonsInRange = world.getEntitiesWithinAABB(EntitySkeleton.class, getBounds(pos));

        for (EntitySkeleton entity : skeletonsInRange) {
            if (!entity.isDead)// && isMobInFilter(ent))
            {
                modifyAITargetTasks(entity);
                turret = entity;
                break;
            }
        }

        return false;
    }

    public AxisAlignedBB getBounds(BlockPos pos) {
        return new AxisAlignedBB(pos).expand(getRange(), getRange(), getRange());
    }

    public float getRange() {
        return 0;
    }

//    private void onUntracked(EntityLiving e)
//    {
//        e.getEntityData().setBoolean("BM:tracked", false);
//    }
//
//    private void onTracked(EntityLiving e)
//    {
//        e.getEntityData().setBoolean("BM:tracked", true);
//    }

    private boolean modifyAITargetTasks(EntitySkeleton entity) {
        cancelCurrentTargetTasks(entity);

//        entity.setCombatTask();
        entity.targetTasks.addTask(1, new EntityAINearestAttackableTarget(entity, EntityMob.class, 10, true, false, checkSkeleton));
        entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0);
        entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
        return true;
    }

    private void cancelCurrentTargetTasks(EntityLiving entity) {
        Iterator<EntityAITaskEntry> iterator = entity.targetTasks.taskEntries.iterator();

        List<EntityAITasks.EntityAITaskEntry> currentTasks = new ArrayList<>();
        while (iterator.hasNext()) {
            EntityAITaskEntry entityaitaskentry = iterator.next();
            if (entityaitaskentry != null)// && entityaitaskentry.action instanceof EntityAITarget)
            {
                currentTasks.add(entityaitaskentry);
            }
        }

        for (EntityAITaskEntry task : currentTasks) {
            entity.targetTasks.removeTask(task.action);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public AlchemyArrayEffect getNewCopy() {
        return new AlchemyArrayEffectSkeletonTurret(key);
    }

    private static class AttractTask extends EntityAIBase {
        private EntityLiving mob;
        private BlockPos coord;
        private FakePlayer target;
        private int updatesSincePathing;

        private boolean started = false;

        private AttractTask(EntityLiving mob, FakePlayer target, BlockPos coord) {
            this.mob = mob;
            this.coord = coord;
            this.target = target;
        }

        @Override
        public boolean shouldExecute() {
            boolean res = false;
            //TODO:
            TileEntity te = mob.getEntityWorld().getTileEntity(coord);
            if (te instanceof TileAlchemyArray) {
                res = true;
            }

            return res;
        }

        @Override
        public void resetTask() {
            started = false;
            updatesSincePathing = 0;
        }

        @Override
        public boolean isInterruptible() {
            return true;
        }

        @Override
        public void updateTask() {
            if (!started || updatesSincePathing > 20) {
                started = true;
                int speed = 1;
                // mob.getNavigator().setAvoidsWater(false);
                boolean res = mob.getNavigator().tryMoveToEntityLiving(target, speed);
                if (!res) {
                    mob.getNavigator().tryMoveToXYZ(target.posX, target.posY + 1, target.posZ, speed);
                }
                updatesSincePathing = 0;
            } else {
                updatesSincePathing++;
            }
        }

    }
}
