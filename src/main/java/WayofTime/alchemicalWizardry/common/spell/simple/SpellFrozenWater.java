package WayofTime.alchemicalWizardry.common.spell.simple;

import WayofTime.alchemicalWizardry.common.entity.projectile.IceProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class SpellFrozenWater extends HomSpell
{
    public Random itemRand = new Random();

    public SpellFrozenWater()
    {
        super();
        this.setEnergies(100, 200, 150, 100);
    }

    @Override
    public ItemStack onOffensiveRangedRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getOffensiveRangedEnergy());
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote)
        {
            par2World.spawnEntityInWorld(new IceProjectile(par2World, par3EntityPlayer, 6));
        }

        return par1ItemStack;
    }

    @Override
    public ItemStack onOffensiveMeleeRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, this.getOffensiveMeleeEnergy());
        }

        for (int i = -2; i <= 2; i++)
        {
            par2World.spawnEntityInWorld(new IceProjectile(par2World, par3EntityPlayer, 6, 2, par3EntityPlayer.posX, par3EntityPlayer.posY + par3EntityPlayer.getEyeHeight(), par3EntityPlayer.posZ, par3EntityPlayer.rotationYaw + i * 5F, par3EntityPlayer.rotationPitch));
        }

        return par1ItemStack;
    }

    @Override
    public ItemStack onDefensiveRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, getDefensiveEnergy());
        }

        float yaw = par3EntityPlayer.rotationYaw;
        float pitch = par3EntityPlayer.rotationPitch;
        int range = 2;

        if (pitch > 40F)
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = -range; j <= range; j++)
                {
                    if (par2World.isAirBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY - 1, (int) par3EntityPlayer.posZ + j))
                    {
                        par2World.setBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY - 1, (int) par3EntityPlayer.posZ + j, Blocks.ice);
                    }
                }
            }

            return par1ItemStack;
        } else if (pitch < -40F)
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = -range; j <= range; j++)
                {
                    if (par2World.isAirBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + 3, (int) par3EntityPlayer.posZ + j))
                    {
                        par2World.setBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + 3, (int) par3EntityPlayer.posZ + j, Blocks.ice);
                    }
                }
            }

            return par1ItemStack;
        }

        if ((yaw >= 315 && yaw < 360) || (yaw >= 0 && yaw < 45))
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = 0; j < range * 2 + 1; j++)
                {
                    if (par2World.isAirBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + 2))
                    {
                        par2World.setBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + 2, Blocks.ice);
                    }
                }
            }
        } else if (yaw >= 45 && yaw < 135)
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = 0; j < range * 2 + 1; j++)
                {
                    if (par2World.isAirBlock((int) par3EntityPlayer.posX - 2, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + i))
                    {
                        par2World.setBlock((int) par3EntityPlayer.posX - 2, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + i, Blocks.ice);
                    }
                }
            }
        } else if (yaw >= 135 && yaw < 225)
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = 0; j < range * 2 + 1; j++)
                {
                    if (par2World.isAirBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ - 2))
                    {
                        par2World.setBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ - 2, Blocks.ice);
                    }
                }
            }
        } else
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = 0; j < range * 2 + 1; j++)
                {
                    if (par2World.isAirBlock((int) par3EntityPlayer.posX + 2, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + i))
                    {
                        par2World.setBlock((int) par3EntityPlayer.posX + 2, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + i, Blocks.ice);
                    }
                }
            }
        }

        return par1ItemStack;
    }

    @Override
    public ItemStack onEnvironmentalRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        int radius = 3;

        for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                    Block block = par2World.getBlock((int) par3EntityPlayer.posX + i - 1, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + k);
                    if (block == Blocks.water || block == Blocks.flowing_water)
                    {
                        par2World.setBlock((int) par3EntityPlayer.posX + i - 1, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + k, Blocks.ice);
                    }
                }
            }
        }
        return par1ItemStack;
    }
}
