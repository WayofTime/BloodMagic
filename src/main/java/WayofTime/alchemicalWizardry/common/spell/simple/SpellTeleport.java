package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import WayofTime.alchemicalWizardry.common.entity.projectile.TeleportProjectile;
import WayofTime.alchemicalWizardry.common.items.BindableItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SpellTeleport extends HomSpell
{
    public static Random itemRand = new Random();

    public SpellTeleport()
    {
        super();
        this.setEnergies(500, 300, 500, 1000);
    }

    @Override
    public ItemStack onOffensiveRangedRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            BindableItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getOffensiveRangedEnergy());
        }

        par2World.spawnEntityInWorld(new TeleportProjectile(par2World, par3EntityPlayer, 8, true));
        par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        EntityEnderman g;
        return par1ItemStack;
    }

    @Override
    public ItemStack onOffensiveMeleeRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            BindableItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getOffensiveMeleeEnergy());
        }

        par2World.spawnEntityInWorld(new TeleportProjectile(par2World, par3EntityPlayer, 8, false));
        return par1ItemStack;
    }

    @Override
    public ItemStack onDefensiveRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            BindableItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getDefensiveEnergy());
        }

        double xCoord = par3EntityPlayer.posX;
        double yCoord = par3EntityPlayer.posY;
        double zCoord = par3EntityPlayer.posZ;
        SpellTeleport.teleportRandomly(par3EntityPlayer, 128);

        for (int i = 0; i < 20; i++)
        {
            SpellHelper.sendParticleToAllAround(par2World, xCoord, yCoord, zCoord, 30, par2World.provider.getDimensionId(), EnumParticleTypes.PORTAL, xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, itemRand.nextFloat(), itemRand.nextFloat(), itemRand.nextFloat());
        }

        return par1ItemStack;
    }

    @Override
    public ItemStack onEnvironmentalRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            BindableItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getEnvironmentalEnergy());
        }

        if (!par2World.isRemote)
        {
            int d0 = 3;
            AxisAlignedBB axisalignedbb = new AxisAlignedBB((double) par3EntityPlayer.posX, (double) par3EntityPlayer.posY, (double) par3EntityPlayer.posZ, (double) (par3EntityPlayer.posX + 1), (double) (par3EntityPlayer.posY + 2), (double) (par3EntityPlayer.posZ + 1)).expand(d0, d0, d0);
            List list = par3EntityPlayer.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                EntityLivingBase entityLiving = (EntityLivingBase) iterator.next();

                if (entityLiving instanceof EntityPlayer)
                {
                    if (entityLiving.equals(par3EntityPlayer))
                    {
                        continue;
                    }
                }
                SpellTeleport.teleportRandomly(entityLiving, 128);
            }
        }

        double xCoord = par3EntityPlayer.posX;
        double yCoord = par3EntityPlayer.posY;
        double zCoord = par3EntityPlayer.posZ;

        for (int i = 0; i < 32; i++)
        {
            SpellHelper.sendParticleToAllAround(par2World, xCoord, yCoord, zCoord, 30, par2World.provider.getDimensionId(), EnumParticleTypes.PORTAL, xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, itemRand.nextFloat(), itemRand.nextFloat(), itemRand.nextFloat());
        }

        return par1ItemStack;
    }

    public static boolean teleportRandomly(EntityLivingBase entityLiving, double distance)
    {
        double x = entityLiving.posX;
        double y = entityLiving.posY;
        double z = entityLiving.posZ;
        Random rand = new Random();
        double d0 = x + (rand.nextDouble() - 0.5D) * distance;
        double d1 = y + (double) (rand.nextInt((int) distance) - (distance) / 2);
        double d2 = z + (rand.nextDouble() - 0.5D) * distance;
        int i = 0;

        while (!SpellTeleport.teleportTo(entityLiving, d0, d1, d2, x, y, z) && i < 100)
        {
            d0 = x + (rand.nextDouble() - 0.5D) * distance;
            d1 = y + (double) (rand.nextInt((int) distance) - (distance) / 2);
            d2 = z + (rand.nextDouble() - 0.5D) * distance;
            i++;
        }

        if (i >= 100)
        {
            return false;
        }

        return true;
    }

    private static boolean teleportTo(EntityLivingBase entityLiving, double par1, double par3, double par5, double lastX, double lastY, double lastZ)
    {
    	net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(entityLiving, par1, par3, par5, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        double d3 = entityLiving.posX;
        double d4 = entityLiving.posY;
        double d5 = entityLiving.posZ;
        entityLiving.posX = event.targetX;
        entityLiving.posY = event.targetY;
        entityLiving.posZ = event.targetZ;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(entityLiving.posX, entityLiving.posY, entityLiving.posZ);

        if (entityLiving.worldObj.isBlockLoaded(blockpos))
        {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0)
            {
                BlockPos blockpos1 = blockpos.offsetDown();
                Block block = entityLiving.worldObj.getBlockState(blockpos1).getBlock();

                if (block.getMaterial().blocksMovement())
                {
                    flag1 = true;
                }
                else
                {
                    --entityLiving.posY;
                    blockpos = blockpos1;
                }
            }

            if (flag1)
            {
            	entityLiving.setPositionAndUpdate(entityLiving.posX, entityLiving.posY, entityLiving.posZ);

                if (entityLiving.worldObj.getCollidingBoundingBoxes(entityLiving, entityLiving.getEntityBoundingBox()).isEmpty() && !entityLiving.worldObj.isAnyLiquid(entityLiving.getEntityBoundingBox()))
                {
                    flag = true;
                }
            }
        }

        if (!flag)
        {
            entityLiving.setPosition(d3, d4, d5);
            return false;
        }
        else
        {
            short short1 = 128;

            for (int i = 0; i < short1; ++i)
            {
                double d9 = (double)i / ((double)short1 - 1.0D);
                float f = (itemRand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (itemRand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (itemRand.nextFloat() - 0.5F) * 0.2F;
                double d6 = d3 + (entityLiving.posX - d3) * d9 + (itemRand.nextDouble() - 0.5D) * (double)entityLiving.width * 2.0D;
                double d7 = d4 + (entityLiving.posY - d4) * d9 + itemRand.nextDouble() * (double)entityLiving.height;
                double d8 = d5 + (entityLiving.posZ - d5) * d9 + (itemRand.nextDouble() - 0.5D) * (double)entityLiving.width * 2.0D;
                entityLiving.worldObj.spawnParticle(EnumParticleTypes.PORTAL, d6, d7, d8, (double)f, (double)f1, (double)f2, new int[0]);
            }

            entityLiving.worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
            entityLiving.playSound("mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }

    public static void moveEntityViaTeleport(EntityLivingBase entityLiving, double x, double y, double z)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            if (entityLiving != null && entityLiving instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP) entityLiving;
                if (entityplayermp.worldObj == entityLiving.worldObj)
                {
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, x, y, z, 5.0F);

                    if (!MinecraftForge.EVENT_BUS.post(event))
                    {
                        if (entityLiving.isRiding())
                        {
                            entityLiving.mountEntity((Entity) null);
                        }
                        entityLiving.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
                    }
                }
            }
        } else if (entityLiving != null)
        {
            entityLiving.setPosition(x, y, z);
        }
    }
}
