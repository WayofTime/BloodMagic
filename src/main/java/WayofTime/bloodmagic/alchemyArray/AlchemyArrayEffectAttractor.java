package WayofTime.bloodmagic.alchemyArray;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.fakePlayer.FakePlayerBM;
import WayofTime.bloodmagic.tile.TileAlchemyArray;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.*;

/**
 * Credits for the initial code go to Crazy Pants of EIO.
 */
public class AlchemyArrayEffectAttractor extends AlchemyArrayEffect {
    private FakePlayer target;
    private Set<EntityLiving> tracking = new HashSet<>();

    private int counter = 0;
    private int maxMobsAttracted = 10000;

    private int cooldown = 50;

    public AlchemyArrayEffectAttractor(String key) {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive) {
        if (tile.getWorld().isRemote) {
            return false;
        }

        BlockPos pos = tile.getPos();
        counter++;
        if (counter < 10) {
            for (EntityLiving ent : tracking) {
                onEntityTick(pos, ent);
            }

            return false;
        }

        counter = 0;

        World world = tile.getWorld();

        Set<EntityLiving> trackingThisTick = new HashSet<>();
        List<EntityLiving> entsInBounds = world.getEntitiesWithinAABB(EntityLiving.class, getBounds(pos));

        for (EntityLiving ent : entsInBounds) {
            if (!ent.isDead)// && isMobInFilter(ent))
            {
                double x = (pos.getX() + 0.5D - ent.posX);
                double y = (pos.getY() + 1D - ent.posY);
                double z = (pos.getZ() + 0.5D - ent.posZ);
                double distance = Math.sqrt(x * x + y * y + z * z);

                if (distance < 2 && tracking.contains(ent)) {
                    setEntityCooldown(pos, ent, cooldown);
                    removeAssignedAITask(pos, ent);
                    continue;
                }

                if (!canEntityBeTracked(pos, ent)) {
//                    System.out.println("Cooldown: " + getEntityCooldown(pos, ent));
                    decrementEntityCooldown(pos, ent);
                    continue;
                }

                if (tracking.contains(ent)) {
                    trackingThisTick.add(ent);
                    onEntityTick(pos, ent);
                } else if (tracking.size() < maxMobsAttracted && trackMob(pos, ent)) {
                    trackingThisTick.add(ent);
                    onTracked(ent);
                }
            }
        }

        for (EntityLiving e : tracking) {
            if (!trackingThisTick.contains(e)) {
                onUntracked(e);
            }
        }
        tracking.clear();
        tracking = trackingThisTick;

        return false;
    }

    public boolean canEntityBeTracked(BlockPos pos, EntityLiving entity) {
        return getEntityCooldown(pos, entity) <= 0;
    }

    private String getPosKey(BlockPos pos) {
        return "BMAttractor:" + pos;
    }

    public int getEntityCooldown(BlockPos pos, EntityLiving entity) {
        return entity.getEntityData().getInteger(getPosKey(pos));
    }

    public void setEntityCooldown(BlockPos pos, EntityLiving entity, int cooldown) {
        entity.getEntityData().setInteger(getPosKey(pos), cooldown);
    }

    public void decrementEntityCooldown(BlockPos pos, EntityLiving entity) {
        int cooldown = getEntityCooldown(pos, entity);
        if (cooldown > 0) {
            setEntityCooldown(pos, entity, cooldown - 1);
        }
    }

    public AxisAlignedBB getBounds(BlockPos pos) {
        return new AxisAlignedBB(pos).expand(getRange(), getRange(), getRange());
    }

    public float getRange() {
        return 10;
    }

    private void onUntracked(EntityLiving e) {
        if (e instanceof EntityEnderman) {
            e.getEntityData().setBoolean("BM:tracked", false);
        }
    }

    private void onTracked(EntityLiving e) {
        if (e instanceof EntityEnderman) {
            e.getEntityData().setBoolean("BM:tracked", true);
        }
    }

    private void onEntityTick(BlockPos pos, EntityLiving ent) {
        if (ent instanceof EntitySlime) {
            ent.faceEntity(getTarget(ent.getEntityWorld(), pos), 10.0F, 20.0F);
        } else if (ent instanceof EntitySilverfish) {
            if (counter < 10) {
                return;
            }
            EntitySilverfish sf = (EntitySilverfish) ent;
            Path pathentity = getPathEntityToEntity(ent, getTarget(ent.getEntityWorld(), pos), getRange());
            sf.getNavigator().setPath(pathentity, sf.getAIMoveSpeed());
        } else if (ent instanceof EntityBlaze) {
            double x = (pos.getX() + 0.5D - ent.posX);
            double y = (pos.getY() + 1D - ent.posY);
            double z = (pos.getZ() + 0.5D - ent.posZ);
            double distance = Math.sqrt(x * x + y * y + z * z);
            if (distance > 1.25) {
                double speed = 0.01;
                ent.motionX += x / distance * speed;
                if (y > 0) {
                    ent.motionY += (0.3 - ent.motionY) * 0.3;
                }
                ent.motionZ += z / distance * speed;
            }
        } else if (ent instanceof EntityPigZombie || ent instanceof EntitySpider) {
            forceMove(pos, ent);
//            ent.setAttackTarget(target);
        } else if (ent instanceof EntityEnderman) {
            ent.setAttackTarget(getTarget(ent.getEntityWorld(), pos));
        }
    }

    private void forceMove(BlockPos pos, EntityLiving ent) {
        double x = (pos.getX() + 0.5D - ent.posX);
        double y = (pos.getY() + 1D - ent.posY);
        double z = (pos.getZ() + 0.5D - ent.posZ);
        double distance = Math.sqrt(x * x + y * y + z * z);
        if (distance > 2) {
            EntityMob mod = (EntityMob) ent;
            mod.faceEntity(getTarget(ent.getEntityWorld(), pos), 180, 0);
            mod.getMoveHelper().strafe(0, 0.3f);
            if (mod.posY < pos.getY()) {
                mod.setJumping(true);
            } else {
                mod.setJumping(false);
            }
        }
    }

    public Path getPathEntityToEntity(Entity entity, Entity targetEntity, float range) {
        int targX = MathHelper.floor(targetEntity.posX);
        int targY = MathHelper.floor(targetEntity.posY + 1.0D);
        int targZ = MathHelper.floor(targetEntity.posZ);

        PathFinder pf = new PathFinder(new WalkNodeProcessor());
        return pf.findPath(targetEntity.getEntityWorld(), (EntityLiving) entity, new BlockPos(targX, targY, targZ), range);
    }

    private boolean trackMob(BlockPos pos, EntityLiving ent) {
        //TODO: Figure out if this crud is needed
        if (useSetTarget(ent)) {
            ent.setAttackTarget(getTarget(ent.getEntityWorld(), pos));
            return true;
        } else if (useSpecialCase(ent)) {
            return applySpecialCase(pos, ent);
        } else {
            return attractUsingAITask(pos, ent);
        }
    }

    private boolean useSetTarget(EntityLiving ent) {
        return ent instanceof EntityPigZombie || ent instanceof EntitySpider || ent instanceof EntitySilverfish;
    }

    public void removeAssignedAITask(BlockPos pos, EntityLiving ent) {
        Set<EntityAITaskEntry> entries = ent.tasks.taskEntries;
        EntityAIBase remove = null;
        for (EntityAITaskEntry entry : entries) {
            if (entry.action instanceof AttractTask) {
                AttractTask at = (AttractTask) entry.action;
                if (at.coord.equals(pos)) {
                    remove = entry.action;
                } else {
                    continue;
                }
            }
        }
        if (remove != null) {
            ent.tasks.removeTask(remove);
        }
    }

    private boolean attractUsingAITask(BlockPos pos, EntityLiving ent) {
        tracking.add(ent);
        Set<EntityAITaskEntry> entries = ent.tasks.taskEntries;
        // boolean hasTask = false;
        EntityAIBase remove = null;
        // boolean isTracked;
        for (EntityAITaskEntry entry : entries) {
            if (entry.action instanceof AttractTask) {
                AttractTask at = (AttractTask) entry.action;
                if (at.coord.equals(pos) || !at.shouldExecute()) {
                    remove = entry.action;
                } else {
                    return false;
                }
            }
        }
        if (remove != null) {
            ent.tasks.removeTask(remove);
        }

        cancelCurrentTasks(ent);
        ent.tasks.addTask(0, new AttractTask(ent, getTarget(ent.getEntityWorld(), pos), pos));

        return true;
    }

    private void cancelCurrentTasks(EntityLiving ent) {
        Iterator<EntityAITaskEntry> iterator = ent.tasks.taskEntries.iterator();

        List<EntityAITasks.EntityAITaskEntry> currentTasks = new ArrayList<>();
        while (iterator.hasNext()) {
            EntityAITaskEntry entityaitaskentry = iterator.next();
            if (entityaitaskentry != null) {
                if (entityaitaskentry.action instanceof AttractTask) {
                    continue;
                }

                currentTasks.add(entityaitaskentry);
            }
        }
        // Only available way to stop current execution is to remove all current
        // tasks, then re-add them
        for (EntityAITaskEntry task : currentTasks) {
            ent.tasks.removeTask(task.action);
            ent.tasks.addTask(task.priority, task.action);
        }
    }

    private boolean applySpecialCase(BlockPos pos, EntityLiving ent) {
        if (ent instanceof EntitySlime) {
            ent.faceEntity(getTarget(ent.getEntityWorld(), pos), 10.0F, 20.0F);
//            ent.setAttackTarget(getTarget(ent.worldObj, pos));
            return true;
        } else if (ent instanceof EntitySilverfish) {
            EntitySilverfish es = (EntitySilverfish) ent;
            Path pathentity = getPathEntityToEntity(ent, getTarget(ent.getEntityWorld(), pos), getRange());
            es.getNavigator().setPath(pathentity, es.getAIMoveSpeed());
            return true;
        } else if (ent instanceof EntityBlaze) {
            return true;
        }
        return false;
    }

    private boolean useSpecialCase(EntityLiving ent) {
        return ent instanceof EntitySlime || ent instanceof EntitySilverfish || ent instanceof EntityBlaze;
    }

    public FakePlayer getTarget(World world, BlockPos pos) {
        if (target == null) {
//            System.out.println("...Hi? " + pos);
            target = new Target(world, pos);
        }

        return target;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public AlchemyArrayEffect getNewCopy() {
        return new AlchemyArrayEffectAttractor(key);
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

    private class Target extends FakePlayerBM {
        public Target(World world, BlockPos pos) {
            super(world, pos, new GameProfile(null, BloodMagic.MODID + "ArrayAttractor" + ":" + pos));
            posY += 1;
        }
    }
}
