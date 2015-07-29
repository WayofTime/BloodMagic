package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.entity.projectile.FireProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;

public class SpellFireBurst extends HomSpell
{
    public Random itemRand = new Random();

    public SpellFireBurst()
    {
        super();
        this.setEnergies(100, 300, 400, 100);
    }

    @Override
    public ItemStack onOffensiveRangedRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!EnergyItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return stack;
        }

        if (!player.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(stack, player, this.getOffensiveRangedEnergy());
        }

        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            FireProjectile proj = new FireProjectile(world, player, 7);
            world.spawnEntityInWorld(proj);
        }

        return stack;
    }

    @Override
    public ItemStack onOffensiveMeleeRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!EnergyItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return stack;
        }

        if (!player.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(stack, player, this.getOffensiveMeleeEnergy());
        }

        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    world.spawnEntityInWorld(new FireProjectile(world, player, 8, 2, player.posX, player.posY + player.getEyeHeight(), player.posZ, player.rotationYaw + i * 10F, player.rotationPitch + j * 10F));
                }
            }
        }

        return stack;
    }

    @Override
    public ItemStack onDefensiveRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!EnergyItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return stack;
        }

        if (!player.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(stack, player, this.getDefensiveEnergy());
        }

        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        int d0 = 2;
        AxisAlignedBB axisalignedbb = new AxisAlignedBB((double) player.posX, (double) player.posY, (double) player.posZ, (double) (player.posX + 1), (double) (player.posY + 2), (double) (player.posZ + 1)).expand(d0, d0, d0);
        List list = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
        Iterator iterator = list.iterator();

        while (iterator.hasNext())
        {
            EntityLivingBase entityLiving = (EntityLivingBase) iterator.next();

            if (entityLiving instanceof EntityPlayer)
            {
                if (entityLiving.equals(player))
                {
                    continue;
                }
            }

            entityLiving.setFire(100);
            entityLiving.attackEntityFrom(DamageSource.inFire, 2);
        }
        return stack;
    }

    @Override
    public ItemStack onEnvironmentalRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!EnergyItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return stack;
        }

        if (!player.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(stack, player, this.getEnvironmentalEnergy());
        }

        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        World worldObj = world;

        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                for (int k = -1; k <= 1; k++)
                {
                	BlockPos pos = player.getPosition().add(i, k, k);
                    if (worldObj.isAirBlock(pos))
                    {
                        if (worldObj.rand.nextFloat() < 0.8F)
                        {
                            worldObj.setBlockState(pos, Blocks.fire.getDefaultState());
                        }
                    }
                }
            }
        }

        return stack;
    }
}
