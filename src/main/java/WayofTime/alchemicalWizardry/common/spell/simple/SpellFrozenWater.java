package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.entity.projectile.IceProjectile;
import WayofTime.alchemicalWizardry.common.items.BindableItems;

public class SpellFrozenWater extends HomSpell
{
    public Random itemRand = new Random();

    public SpellFrozenWater()
    {
        super();
        this.setEnergies(100, 200, 150, 100);
    }

    @Override
    public ItemStack onOffensiveRangedRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!BindableItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return stack;
        }

        if (!player.capabilities.isCreativeMode)
        {
            BindableItems.syphonAndDamageWhileInContainer(stack, player, this.getOffensiveRangedEnergy());
        }

        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            world.spawnEntityInWorld(new IceProjectile(world, player, 6));
        }

        return stack;
    }

    @Override
    public ItemStack onOffensiveMeleeRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!BindableItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return stack;
        }

        if (!player.capabilities.isCreativeMode)
        {
            BindableItems.syphonAndDamageWhileInContainer(stack, player, this.getOffensiveMeleeEnergy());
        }

        for (int i = -2; i <= 2; i++)
        {
            world.spawnEntityInWorld(new IceProjectile(world, player, 6, 2, player.posX, player.posY + player.getEyeHeight(), player.posZ, player.rotationYaw + i * 5F, player.rotationPitch));
        }

        return stack;
    }

    @Override
    public ItemStack onDefensiveRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!player.capabilities.isCreativeMode)
        {
            BindableItems.syphonAndDamageWhileInContainer(stack, player, getDefensiveEnergy());
        }

        float yaw = player.rotationYaw;
        float pitch = player.rotationPitch;
        int range = 2;
        
        BlockPos pos = player.getPosition();
        BlockPos newPos = pos;

        if (pitch > 40F)
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = -range; j <= range; j++)
                {
                	newPos = pos.add(i, -1, j);
                	
                    if (world.isAirBlock(newPos))
                    {
                        world.setBlockState(newPos, Blocks.ice.getDefaultState());
                    }
                }
            }

            return stack;
        } else if (pitch < -40F)
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = -range; j <= range; j++)
                {
                	newPos = pos.add(i, 3, j);
                	
                	if (world.isAirBlock(newPos))
                    {
                        world.setBlockState(newPos, Blocks.ice.getDefaultState());
                    }
                }
            }

            return stack;
        }

        if ((yaw >= 315 && yaw < 360) || (yaw >= 0 && yaw < 45))
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = 0; j < range * 2 + 1; j++)
                {
                	newPos = pos.add(i, j, 2);
                	
                	if (world.isAirBlock(newPos))
                    {
                        world.setBlockState(newPos, Blocks.ice.getDefaultState());
                    }
                }
            }
        } else if (yaw >= 45 && yaw < 135)
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = 0; j < range * 2 + 1; j++)
                {
                	newPos = pos.add(-2, j, i);
                	
                	if (world.isAirBlock(newPos))
                    {
                        world.setBlockState(newPos, Blocks.ice.getDefaultState());
                    }
                }
            }
        } else if (yaw >= 135 && yaw < 225)
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = 0; j < range * 2 + 1; j++)
                {
                	newPos = pos.add(i, j, -2);
                	
                	if (world.isAirBlock(newPos))
                    {
                        world.setBlockState(newPos, Blocks.ice.getDefaultState());
                    }
                }
            }
        } else
        {
            for (int i = -range; i <= range; i++)
            {
                for (int j = 0; j < range * 2 + 1; j++)
                {
                	newPos = pos.add(2, j, i);
                	
                	if (world.isAirBlock(newPos))
                    {
                        world.setBlockState(newPos, Blocks.ice.getDefaultState());
                    }
                }
            }
        }

        return stack;
    }

    @Override
    public ItemStack onEnvironmentalRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!BindableItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return stack;
        }

        int radius = 3;
        int posX = (int) player.posX;
        int posY = (int) player.posY;
        int posZ = (int) player.posZ;

        for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                	BlockPos pos = player.getPosition().add(i, j, k);
                	
                    IBlockState state = world.getBlockState(pos);
                    if (state.getBlock() == Blocks.water || state.getBlock() == Blocks.flowing_water)
                    {
                        world.setBlockState(pos, Blocks.ice.getDefaultState());
                    }
                }
            }
        }
        return stack;
    }
}
