package WayofTime.alchemicalWizardry.common.items.sigil;

import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.BindableItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class SigilOfSupression extends SigilToggleable implements ArmourUpgrade, ISigil
{
    private int radius = 5;
    private int refresh = 100;

    public SigilOfSupression()
    {
        super();
        setEnergyUsed(400);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofsupression.desc"));

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
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        int tickDelay = 200;

        if (!BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || SpellHelper.isFakePlayer(par2World, par3EntityPlayer))
        {
            return par1ItemStack;
        }

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = par1ItemStack.getTagCompound();
        this.setActivated(par1ItemStack, !(this.getActivated(par1ItemStack)));

        if (this.getActivated(par1ItemStack))
        {
            par1ItemStack.setItemDamage(1);
            tag.setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % tickDelay);

            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                if(!BindableItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
                {
                	this.setActivated(par1ItemStack, false);
                }
            }
        } else
        {
            par1ItemStack.setItemDamage(par1ItemStack.getMaxDamage());
        }

        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!(par3Entity instanceof EntityPlayer))
        {
            return;
        }

        if (SpellHelper.isFakePlayer(par2World, (EntityPlayer) par3Entity))
        {
            return;
        }

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (this.getActivated(par1ItemStack) && (!par2World.isRemote))
        {
            Vec3 blockVec = new Vec3((int) Math.round(par3EntityPlayer.posX - 0.5F), par3EntityPlayer.posY, (int) Math.round(par3EntityPlayer.posZ - 0.5F));
            int x = (int) blockVec.xCoord;
            int y = (int) blockVec.yCoord;
            int z = (int) blockVec.zCoord;

            for (int i = -radius; i <= radius; i++)
            {
                for (int j = -radius; j <= radius; j++)
                {
                    for (int k = -radius; k <= radius; k++)
                    {
                        if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
                        {
                            continue;
                        }

                        BlockPos blockPos = new BlockPos(x + i, y + j, z + k);
                        IBlockState block = par2World.getBlockState(blockPos);

                        if (SpellHelper.isBlockFluid(block.getBlock()))
                        {
                            if (par2World.getTileEntity(blockPos) != null)
                            {
                                par2World.setBlockToAir(blockPos);
                            }
                            TESpectralContainer.createSpectralBlockAtLocation(par2World, blockPos, refresh);
                        } else
                        {
                            TileEntity tile = par2World.getTileEntity(blockPos);
                            if (tile instanceof TESpectralContainer)
                            {
                                ((TESpectralContainer) tile).resetDuration(refresh);
                            }
                        }
                    }
                }
            }
        }

        if (par2World.getWorldTime() % 200 == par1ItemStack.getTagCompound().getInteger("worldTimeDelay") && this.getActivated(par1ItemStack))
        {
            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                if(!BindableItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
                {
                	this.setActivated(par1ItemStack, false);
                }
            }
        }
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        Vec3 blockVec = new Vec3((int) Math.round(player.posX - 0.5F), player.posY, (int) Math.round(player.posZ - 0.5F));
        int x = (int) blockVec.xCoord;
        int y = (int) blockVec.yCoord;
        int z = (int) blockVec.zCoord;

        for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                    if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
                    {
                        continue;
                    }

                    BlockPos blockPos = new BlockPos(x + i, y + j, z + k);
                    IBlockState block = world.getBlockState(blockPos);

                    if (SpellHelper.isBlockFluid(block.getBlock()))
                    {
                        if (world.getTileEntity(blockPos) != null)
                        {
                            world.setBlockToAir(blockPos);
                        }
                        TESpectralContainer.createSpectralBlockAtLocation(world, blockPos, refresh);
                    } else
                    {
                        TileEntity tile = world.getTileEntity(blockPos);
                        if (tile instanceof TESpectralContainer)
                        {
                            ((TESpectralContainer) tile).resetDuration(refresh);
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
        return 200;
    }
}
