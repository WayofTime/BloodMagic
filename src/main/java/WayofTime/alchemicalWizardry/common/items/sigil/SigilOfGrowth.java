package WayofTime.alchemicalWizardry.common.items.sigil;

import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.BindableItems;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

public class SigilOfGrowth extends SigilToggleable implements ArmourUpgrade, ISigil
{
    private int tickDelay = 100;

    public SigilOfGrowth()
    {
        super();
        setEnergyUsed(150);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofgrowth.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofgrowth.desc2"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            if (this.getActivated(par1ItemStack))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.activated"));
            } else
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.deactivated"));
            }

            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (BindableItems.checkAndSetItemOwner(stack, playerIn))
        {
            if (applyBonemeal(stack, worldIn, pos, playerIn))
            {
                BindableItems.syphonBatteries(stack, playerIn, getEnergyUsed());

                if (worldIn.isRemote)
                {
                    worldIn.playAuxSFX(2005, pos, 0);
                    return true;
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (par2World.isRemote)
        {
            return par1ItemStack;
        }

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = par1ItemStack.getTagCompound();
        tag.setBoolean("isActive", !(tag.getBoolean("isActive")));

        if (tag.getBoolean("isActive") && BindableItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
        {
            par1ItemStack.setItemDamage(1);
            tag.setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % tickDelay);
        } else
        {
            par1ItemStack.setItemDamage(par1ItemStack.getMaxDamage());
        }

        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!(par3Entity instanceof EntityPlayer) || par2World.isRemote)
        {
            return;
        }

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (this.getActivated(par1ItemStack))
        {
            if (par2World.getWorldTime() % tickDelay == par1ItemStack.getTagCompound().getInteger("worldTimeDelay"))
            {
                if(!BindableItems.syphonBatteries(par1ItemStack, (EntityPlayer) par3Entity, getEnergyUsed()))
                {
                	this.setActivated(par1ItemStack, false);
                }
            }
            int range = 3;
            int verticalRange = 2;
            int posX = (int) Math.round(par3Entity.posX - 0.5f);
            int posY = (int) par3Entity.posY;
            int posZ = (int) Math.round(par3Entity.posZ - 0.5f);

            for (int ix = posX - range; ix <= posX + range; ix++)
            {
                for (int iz = posZ - range; iz <= posZ + range; iz++)
                {
                    for (int iy = posY - verticalRange; iy <= posY + verticalRange; iy++)
                    {
                        IBlockState block = par2World.getBlockState(new BlockPos(ix, iy, iz));

                        if (block instanceof IPlantable || block instanceof IGrowable)
                        {
                            if (par2World.rand.nextInt(50) == 0)
                            {
                                block.getBlock().updateTick(par2World, new BlockPos(ix, iy, iz), block, par2World.rand);
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean applyBonemeal(ItemStack p_179234_0_, World world, BlockPos blockPos, EntityPlayer player)
    {
        IBlockState block = world.getBlockState(blockPos);

        BonemealEvent event = new BonemealEvent(player, world, blockPos, block);
        if (MinecraftForge.EVENT_BUS.post(event))
        {
            return false;
        }

        if (event.getResult() == Event.Result.ALLOW)
        {
            return true;
        }

        if (block instanceof IGrowable)
        {
            IGrowable igrowable = (IGrowable) block;

            if (igrowable.isStillGrowing(world, blockPos, block, world.isRemote))
            {
                if (!world.isRemote)
                {
                    if (igrowable.canUseBonemeal(world, world.rand, blockPos, block))
                    {
                        igrowable.grow(world, world.rand, blockPos, block);
                    }
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        if (world.isRemote)
        {
            return;
        }

        int range = 5;
        int verticalRange = 2;
        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);

        for (int ix = posX - range; ix <= posX + range; ix++)
        {
            for (int iz = posZ - range; iz <= posZ + range; iz++)
            {
                for (int iy = posY - verticalRange; iy <= posY + verticalRange; iy++)
                {
                    IBlockState block = world.getBlockState(new BlockPos(ix, iy, iz));

                    if (block instanceof IPlantable)
                    {
                        if (world.rand.nextInt(100) == 0)
                        {
                            block.getBlock().updateTick(world, new BlockPos(ix, iy, iz), block, world.rand);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 50;
    }
}
