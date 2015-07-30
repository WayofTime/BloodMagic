package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Random;

import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
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

        world.spawnEntityInWorld(new MudProjectile(world, player, 8, false));
        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
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

        if (!world.isRemote)
        {
            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    world.spawnEntityInWorld(new MudProjectile(world, player, 3, 3, player.posX, player.posY + player.getEyeHeight(), player.posZ, player.rotationYaw + i * 10F, player.rotationPitch + j * 5F, true));
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
            SoulNetworkHandler.syphonAndDamageFromNetwork(stack, player, this.getDefensiveEnergy());
        }

        double xCoord = player.posX;
        double yCoord = player.posY;
        double zCoord = player.posZ;
        int posX = (int) xCoord;
        int posY = (int) yCoord;
        int posZ = (int) zCoord;
        BlockPos pos = player.getPosition();
        IBlockState blockID = Blocks.stone.getDefaultState();

        if (world.isAirBlock(pos.offsetUp(3)))
        {
            world.setBlockState(pos.offsetUp(3), Blocks.glass.getDefaultState());
        }

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (world.isAirBlock(new BlockPos(posX + i - 1, posY + j, posZ - 2)))
                {
                    world.setBlockState(new BlockPos(posX + i - 1, posY + j, posZ - 2), blockID);
                }

                if (world.isAirBlock(new BlockPos(posX + 2, posY + j, posZ - 1 + i)))
                {
                    world.setBlockState(new BlockPos(posX + 2, posY + j, posZ - 1 + i), blockID);
                }

                if (world.isAirBlock(new BlockPos(posX - i + 1, posY + j, posZ + 2)))
                {
                    world.setBlockState(new BlockPos(posX - i + 1, posY + j, posZ + 2), blockID);
                }

                if (world.isAirBlock(new BlockPos(posX - 2, posY + j, posZ + 1 - i)))
                {
                    world.setBlockState(new BlockPos(posX - 2, posY + j, posZ + 1 - i), blockID);
                }

                if (world.isAirBlock(new BlockPos(posX - 1 + i, posY + 3, posZ - 1 + j)))
                {
                    world.setBlockState(new BlockPos(posX - 1 + i, posY + 3, posZ - 1 + j), blockID);
                }
            }
        }

        for (int i = 0; i < 20; i++)
        {
            SpellHelper.sendParticleToAllAround(world, xCoord, yCoord, zCoord, 30, world.provider.getDimensionId(), EnumParticleTypes.SPELL_MOB, xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, 0.0F, 0.410F, 1.0F);
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
            SoulNetworkHandler.syphonAndDamageFromNetwork(stack, player, this.getEnvironmentalEnergy());
        }

        int range = 3;

        if (!world.isRemote)
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    for (int k = -range; k <= range; k++)
                    {
                    	BlockPos pos = player.getPosition().add(i, j, k);
                        if (world.getBlockState(pos).getBlock() == Blocks.water || world.getBlockState(pos).getBlock() == Blocks.flowing_water)
                        {
                            int x = world.rand.nextInt(2);

                            if (x == 0)
                            {
                                world.setBlockState(pos, Blocks.sand.getDefaultState());
                            } else
                            {
                                world.setBlockState(pos, Blocks.dirt.getDefaultState());
                            }
                        }
                    }
                }
            }
        }

        double xCoord = player.posX;
        double yCoord = player.posY;
        double zCoord = player.posZ;

        for (int i = 0; i < 16; i++)
        {
            SpellHelper.sendParticleToAllAround(world, xCoord, yCoord, zCoord, 30, world.provider.getDimensionId(), EnumParticleTypes.SPELL_MOB, xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, 0.0F, 0.410F, 1.0F);
        }

        return stack;
    }
}
