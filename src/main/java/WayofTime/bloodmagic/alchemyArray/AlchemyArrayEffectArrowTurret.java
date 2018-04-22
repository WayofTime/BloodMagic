package WayofTime.bloodmagic.alchemyArray;

import java.util.List;

import javax.vecmath.Vector2d;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import WayofTime.bloodmagic.util.Utils;

public class AlchemyArrayEffectArrowTurret extends AlchemyArrayEffect
{
    public EntityLiving target;
    public int arrowTimer;
    public static final int ARROW_WINDUP = 50;
    private int lastChestSlot = -1;

    private double pitch = 0;
    private double lastPitch = 0;
    private double yaw = 0;
    private double lastYaw = 0;

    public AlchemyArrayEffectArrowTurret(String key)
    {
        super(key);
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive)
    {
        BlockPos pos = tile.getPos();
        World world = tile.getWorld();

        BlockPos chestPos = pos.down();
        TileEntity chestTile = world.getTileEntity(chestPos);
        if (chestTile == null)
        {
            return false;
        }
        IItemHandler itemHandler = Utils.getInventory(chestTile, EnumFacing.UP);
        if (itemHandler == null)
        {
            return false;
        }

        ItemStack arrowStack = new ItemStack(Items.AIR);
        if (lastChestSlot >= 0 && lastChestSlot < itemHandler.getSlots())
        {
            ItemStack testStack = itemHandler.extractItem(lastChestSlot, 1, true);
            if (testStack.isEmpty() || !(testStack.getItem() instanceof ItemArrow))
            {
                lastChestSlot = -1;
            } else
            {
                arrowStack = testStack;
            }
        }

        if (lastChestSlot < 0)
        {
            for (int i = 0; i < itemHandler.getSlots(); i++)
            {
                ItemStack testStack = itemHandler.extractItem(i, 1, true);
                if (!testStack.isEmpty() && testStack.getItem() instanceof ItemArrow)
                {
                    arrowStack = testStack;
                    lastChestSlot = i;
                    break;
                }
            }

        }

        if (lastChestSlot < 0)
        {
            return false; //No arrows in the chest. Welp!
        }

        if (canFireOnMob(world, pos, target))
        {
            Vector2d pitchYaw = getPitchYaw(pos, target);
            lastPitch = pitch;
            lastYaw = yaw;
            pitch = pitchYaw.x;
            yaw = pitchYaw.y;
            arrowTimer++;

            if (arrowTimer >= ARROW_WINDUP)
            {
//                ItemStack arrowStack = new ItemStack(Items.ARROW);
                fireOnTarget(world, pos, arrowStack, target);
                if (!world.isRemote)
                {
                    itemHandler.extractItem(lastChestSlot, 1, false);
                }
                arrowTimer = 0;
            }
            return false;
        } else
        {
            target = null;
            arrowTimer = -1;
        }

        List<EntityMob> mobsInRange = world.getEntitiesWithinAABB(EntityMob.class, getBounds(pos));

        for (EntityMob entity : mobsInRange)
        {
            if (canFireOnMob(world, pos, entity))// && isMobInFilter(ent))
            {
                target = entity;
                arrowTimer = 0;
                return false;
            }
        }
        arrowTimer = -1;
        target = null;

        return false;
    }

    public double getPitch()
    {
        return pitch;
    }

    public double getLastPitch()
    {
        return lastPitch;
    }

    public double getYaw()
    {
        return yaw;
    }

    public double getLastYaw()
    {
        return lastYaw;
    }

    public void fireOnTarget(World world, BlockPos pos, ItemStack arrowStack, EntityLiving targetMob)
    {
        float vel = 3f;
        double damage = 2;
        if (!world.isRemote)
        {
            if (arrowStack.getItem() instanceof ItemArrow)
            {
//                ItemArrow arrow = (ItemArrow) arrowStack.getItem();
//                EntityArrow entityarrow = arrow.createArrow(world, arrowStack, targetMob);
                EntityTippedArrow entityarrow = new EntityTippedArrow(world);
                entityarrow.setPotionEffect(arrowStack);
                entityarrow.posX = pos.getX() + 0.5;
                entityarrow.posY = pos.getY() + 0.5;
                entityarrow.posZ = pos.getZ() + 0.5;

                double d0 = targetMob.posX - (pos.getX() + 0.5);
                double d1 = targetMob.posY + targetMob.height - (pos.getY() + 0.5);
                double d2 = targetMob.posZ - (pos.getZ() + 0.5);
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                entityarrow.setDamage(damage);
                entityarrow.shoot(d0, d1 + d3 * 0.05, d2, vel, 0);
                world.spawnEntity(entityarrow);
            }
        }
    }

    public static Vector2d getPitchYaw(BlockPos pos, Entity entityIn)
    {
        if (entityIn == null)
        {
            return new Vector2d(0, 0);
        }

        double distanceX = entityIn.posX - (pos.getX() + 0.5);
        double distanceY = entityIn.posY + (double) entityIn.getEyeHeight() - (pos.getY() + 0.5);
        double distanceZ = entityIn.posZ - (pos.getZ() + 0.5);
        double radialDistance = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);
        double yaw = Math.atan2(-distanceX, distanceZ) * 180 / Math.PI;
        double pitch = -Math.atan2(distanceY, radialDistance) * 180 / Math.PI;

        return new Vector2d(pitch, yaw);
    }

    public boolean canEntityBeSeen(World world, BlockPos pos, Entity entityIn)
    {
        return world.rayTraceBlocks(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), new Vec3d(entityIn.posX, entityIn.posY + (double) entityIn.getEyeHeight(), entityIn.posZ), false, true, false) == null;
    }

    public boolean canFireOnMob(World world, BlockPos pos, Entity entityIn)
    {
        return entityIn != null && !entityIn.isDead && entityIn.getDistanceSqToCenter(pos) <= getRange() * getRange() && entityIn.getDistanceSqToCenter(pos) >= getMinRange() * getMinRange() && canEntityBeSeen(world, pos, entityIn);
    }

    public AxisAlignedBB getBounds(BlockPos pos)
    {
        return new AxisAlignedBB(pos).grow(getRange(), getRange(), getRange());
    }

    public float getRange()
    {
        return 32;
    }

    public float getMinRange()
    {
        return 3;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {

    }

    @Override
    public AlchemyArrayEffect getNewCopy()
    {
        return new AlchemyArrayEffectArrowTurret(key);
    }

}
