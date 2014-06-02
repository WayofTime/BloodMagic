package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.entity.projectile.MudProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SpellEarthBender extends HomSpell
{
    Random itemRand = new Random();

    public SpellEarthBender()
    {
        super();
        this.setEnergies(100, 150, 350, 200);
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

        par2World.spawnEntityInWorld(new MudProjectile(par2World, par3EntityPlayer, 8, false));
        par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
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

        if (!par2World.isRemote)
        {
            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    par2World.spawnEntityInWorld(new MudProjectile(par2World, par3EntityPlayer, 3, 3, par3EntityPlayer.posX, par3EntityPlayer.posY + par3EntityPlayer.getEyeHeight(), par3EntityPlayer.posZ, par3EntityPlayer.rotationYaw + i * 10F, par3EntityPlayer.rotationPitch + j * 5F, true));
                }
            }
        }

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
        int posX = (int) xCoord;
        int posY = (int) yCoord;
        int posZ = (int) zCoord;
        Block blockID = Blocks.stone;

        if (par2World.isAirBlock(posX, posY + 3, posZ))
        {
            par2World.setBlock(posX, posY + 3, posZ, Blocks.glass);
        }

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (par2World.isAirBlock(posX + i - 1, posY + j, posZ - 2))
                {
                    par2World.setBlock(posX + i - 1, posY + j, posZ - 2, blockID);
                }

                if (par2World.isAirBlock(posX + 2, posY + j, posZ - 1 + i))
                {
                    par2World.setBlock(posX + 2, posY + j, posZ - 1 + i, blockID);
                }

                if (par2World.isAirBlock(posX - i + 1, posY + j, posZ + 2))
                {
                    par2World.setBlock(posX - i + 1, posY + j, posZ + 2, blockID);
                }

                if (par2World.isAirBlock(posX - 2, posY + j, posZ + 1 - i))
                {
                    par2World.setBlock(posX - 2, posY + j, posZ + 1 - i, blockID);
                }

                {
                    if (par2World.isAirBlock(posX - 1 + i, posY + 3, posZ - 1 + j))
                    {
                        par2World.setBlock(posX - 1 + i, posY + 3, posZ - 1 + j, blockID);
                    }
                }
            }
        }

        for (int i = 0; i < 20; i++)
        {
            //PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, PacketHandler.getCustomParticlePacket("mobSpell", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, 0.0F, 0.410F, 1.0F));
            SpellHelper.sendParticleToAllAround(par2World, xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, "mobSpell", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, 0.0F, 0.410F, 1.0F);
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

        int range = 3;

        if (!par2World.isRemote)
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    for (int k = -range; k <= range; k++)
                    {
                        if (par2World.getBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + k) == Blocks.water || par2World.getBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + k) == Blocks.flowing_water)
                        {
                            int x = par2World.rand.nextInt(2);

                            if (x == 0)
                            {
                                par2World.setBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + k, Blocks.sand);
                            } else
                            {
                                par2World.setBlock((int) par3EntityPlayer.posX + i, (int) par3EntityPlayer.posY + j, (int) par3EntityPlayer.posZ + k, Blocks.dirt);
                            }
                        }
                    }
                }
            }
        }

        double xCoord = par3EntityPlayer.posX;
        double yCoord = par3EntityPlayer.posY;
        double zCoord = par3EntityPlayer.posZ;

        for (int i = 0; i < 16; i++)
        {
            //PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, PacketHandler.getCustomParticlePacket("mobSpell", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, 0.0F, 0.410F, 1.0F));
            SpellHelper.sendParticleToAllAround(par2World, xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, "mobSpell", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, 0.0F, 0.410F, 1.0F);
        }

        return par1ItemStack;
    }
}
