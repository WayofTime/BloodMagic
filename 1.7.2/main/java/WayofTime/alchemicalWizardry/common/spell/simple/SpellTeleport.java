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
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.entity.projectile.TeleportProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SpellTeleport extends HomSpell
{
    Random itemRand = new Random();

    public SpellTeleport()
    {
        super();
        this.setEnergies(500, 300, 500, 1000);
        //this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public ItemStack onOffensiveRangedRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getOffensiveRangedEnergy());
        }

        par2World.spawnEntityInWorld(new TeleportProjectile(par2World, par3EntityPlayer, 8, true));
        par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        EntityEnderman g;
        return par1ItemStack;
    }

    @Override
    public ItemStack onOffensiveMeleeRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getOffensiveMeleeEnergy());
        }

        par2World.spawnEntityInWorld(new TeleportProjectile(par2World, par3EntityPlayer, 8, false));
        return par1ItemStack;
    }

    @Override
    public ItemStack onDefensiveRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getDefensiveEnergy());
        }

        double xCoord = par3EntityPlayer.posX;
        double yCoord = par3EntityPlayer.posY;
        double zCoord = par3EntityPlayer.posZ;
        SpellTeleport.teleportRandomly(par3EntityPlayer, 128);

        for (int i = 0; i < 20; i++)
        {
            //PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, PacketHandler.getCustomParticlePacket("portal", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, itemRand.nextFloat(), itemRand.nextFloat(), itemRand.nextFloat()));
            SpellHelper.sendParticleToAllAround(par2World, xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, "portal", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, itemRand.nextFloat(), itemRand.nextFloat(), itemRand.nextFloat());
        }

        return par1ItemStack;
    }

    @Override
    public ItemStack onEnvironmentalRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getEnvironmentalEnergy());
        }

        if (!par2World.isRemote)
        {
            int d0 = 3;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double) par3EntityPlayer.posX, (double) par3EntityPlayer.posY, (double) par3EntityPlayer.posZ, (double) (par3EntityPlayer.posX + 1), (double) (par3EntityPlayer.posY + 2), (double) (par3EntityPlayer.posZ + 1)).expand(d0, d0, d0);
            //axisalignedbb.maxY = (double)this.worldObj.getHeight();
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
                //entityLiving.attackEntityFrom(DamageSource.inFire, 5);
            }
        }

        double xCoord = par3EntityPlayer.posX;
        double yCoord = par3EntityPlayer.posY;
        double zCoord = par3EntityPlayer.posZ;

        for (int i = 0; i < 32; i++)
        {
            SpellHelper.sendParticleToAllAround(par2World, xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, "portal", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, itemRand.nextFloat(), itemRand.nextFloat(), itemRand.nextFloat());
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
        //return SpellTeleport.teleportTo(entityLiving, d0, d1, d2,x,y,z);
    }

    private static boolean teleportTo(EntityLivingBase entityLiving, double par1, double par3, double par5, double lastX, double lastY, double lastZ)
    {
        EnderTeleportEvent event = new EnderTeleportEvent(entityLiving, par1, par3, par5, 0);

        if (MinecraftForge.EVENT_BUS.post(event))
        {
            return false;
        }

        double d3 = lastX;
        double d4 = lastY;
        double d5 = lastZ;
        SpellTeleport.moveEntityViaTeleport(entityLiving, event.targetX, event.targetY, event.targetZ);
        boolean flag = false;
        int i = MathHelper.floor_double(entityLiving.posX);
        int j = MathHelper.floor_double(entityLiving.posY);
        int k = MathHelper.floor_double(entityLiving.posZ);
        Block l;

        if (entityLiving.worldObj.blockExists(i, j, k))
        {
            boolean flag1 = false;

            while (!flag1 && j > 0)
            {
                l = entityLiving.worldObj.getBlock(i, j - 1, k);

                if (l != null && l.getMaterial().blocksMovement())
                {
                    flag1 = true;
                } else
                {
                    --entityLiving.posY;
                    --j;
                }
            }

            if (flag1)
            {
                SpellTeleport.moveEntityViaTeleport(entityLiving, entityLiving.posX, entityLiving.posY, entityLiving.posZ);

                if (entityLiving.worldObj.getCollidingBoundingBoxes(entityLiving, entityLiving.boundingBox).isEmpty() && !entityLiving.worldObj.isAnyLiquid(entityLiving.boundingBox))
                {
                    flag = true;
                }
            }
        }

        if (!flag)
        {
            SpellTeleport.moveEntityViaTeleport(entityLiving, d3, d4, d5);
            return false;
        } else
        {
            short short1 = 128;

            for (j = 0; j < short1; ++j)
            {
                double d6 = (double) j / ((double) short1 - 1.0D);
                float f = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (entityLiving.posX - d3) * d6 + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width * 2.0D;
                double d8 = d4 + (entityLiving.posY - d4) * d6 + entityLiving.worldObj.rand.nextDouble() * (double) entityLiving.height;
                double d9 = d5 + (entityLiving.posZ - d5) * d6 + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width * 2.0D;
                entityLiving.worldObj.spawnParticle("portal", d7, d8, d9, (double) f, (double) f1, (double) f2);
            }

//            this.worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
//            this.playSound("mob.endermen.portal", 1.0F, 1.0F);
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

                //if (!entityplayermp.playerNetServerHandler.connectionClosed && entityplayermp.worldObj == entityLiving.worldObj)
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
//	                    this.getThrower().fallDistance = 0.0F;
//	                    this.getThrower().attackEntityFrom(DamageSource.fall, event.attackDamage);
                    }
                }
            }
        } else if (entityLiving != null)
        {
            entityLiving.setPosition(x, y, z);
        }
    }
}
