package WayofTime.alchemicalWizardry.common.items.sigil;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.BindableItems;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class SigilOfTheBridge extends SigilToggleable implements ArmourUpgrade, ISigil
{
    private int tickDelay = 200;

    public SigilOfTheBridge()
    {
        super();
        setEnergyUsed(100);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofthebridge.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofthebridge.desc2"));

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
        if (!BindableItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = par1ItemStack.getTagCompound();
        this.setActivated(par1ItemStack, !(this.getActivated(par1ItemStack)));

        if (this.getActivated(par1ItemStack) && BindableItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
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
        if (!(par3Entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (this.getActivated(par1ItemStack))
        {
            if (par2World.getWorldTime() % tickDelay == par1ItemStack.getTagCompound().getInteger("worldTimeDelay"))
            {
                if(BindableItems.syphonBatteries(par1ItemStack, (EntityPlayer) par3Entity, this.getLPUsed(par1ItemStack)))
                {
                    this.setLPUsed(par1ItemStack, 0);
                }else
                {
                	this.setActivated(par1ItemStack, false);
                }
            }
            if (!par3EntityPlayer.onGround && !par3EntityPlayer.isSneaking())
            {
                return;
            }

            int range = 2;
            int verticalOffset = -1;

            if (par3EntityPlayer.isSneaking())
            {
                verticalOffset--;
            }

            if (par2World.isRemote)
            {
                verticalOffset--;
            }

            int posX = (int) Math.round(par3Entity.posX - 0.5f);
            int posY = (int) par3Entity.posY;
            int posZ = (int) Math.round(par3Entity.posZ - 0.5f);
            int incremented = 0;

            for (int ix = posX - range; ix <= posX + range; ix++)
            {
                for (int iz = posZ - range; iz <= posZ + range; iz++)
                {
                    {
                        IBlockState block = par2World.getBlockState(new BlockPos(ix, posY + verticalOffset, iz));

                        if (par2World.isAirBlock(new BlockPos(ix, posY + verticalOffset, iz)))
                        {
                            par2World.setBlockState(new BlockPos(ix, posY + verticalOffset, iz), ModBlocks.spectralBlock.getDefaultState(), 3);

                            TileEntity tile = par2World.getTileEntity(new BlockPos(ix, posY + verticalOffset, iz));
                            if (tile instanceof TESpectralBlock)
                            {
                                ((TESpectralBlock) tile).setDuration(100);
                            }

                            if (par2World.rand.nextInt(2) == 0)
                            {
                                incremented++;
                            }
                        } else if (block == ModBlocks.spectralBlock)
                        {
                            TileEntity tile = par2World.getTileEntity(new BlockPos(ix, posY + verticalOffset, iz));
                            if (tile instanceof TESpectralBlock)
                            {
                                ((TESpectralBlock) tile).setDuration(100);
                            }
                        }
                    }
                }
            }

            this.incrimentLPUSed(par1ItemStack, incremented);
        }
    }

    public int getLPUsed(ItemStack par1ItemStack)
    {
        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        return par1ItemStack.getTagCompound().getInteger("LPUsed");
    }

    public void incrimentLPUSed(ItemStack par1ItemStack, int addedLP)
    {
        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.getTagCompound().setInteger("LPUsed", par1ItemStack.getTagCompound().getInteger("LPUsed") + addedLP);
    }

    public void setLPUsed(ItemStack par1ItemStack, int newLP)
    {
        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.getTagCompound().setInteger("LPUsed", newLP);
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        if (!player.onGround && !player.isSneaking())
        {
            return;
        }

        int range = 2;
        int verticalOffset = -1;

        if (player.isSneaking())
        {
            verticalOffset--;
        }

        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);
        for (int ix = posX - range; ix <= posX + range; ix++)
        {
            for (int iz = posZ - range; iz <= posZ + range; iz++)
            {
                IBlockState block = world.getBlockState(new BlockPos(ix, posY + verticalOffset, iz));

                if (world.isAirBlock(new BlockPos(ix, posY + verticalOffset, iz)))
                {
                    world.setBlockState(new BlockPos(ix, posY + verticalOffset, iz), ModBlocks.spectralBlock.getDefaultState(), 3);

                    TileEntity tile = world.getTileEntity(new BlockPos(ix, posY + verticalOffset, iz));
                    if (tile instanceof TESpectralBlock)
                    {
                        ((TESpectralBlock) tile).setDuration(100);
                    }
                } else if (block == ModBlocks.spectralBlock)
                {
                    TileEntity tile = world.getTileEntity(new BlockPos(ix, posY + verticalOffset, iz));
                    if (tile instanceof TESpectralBlock)
                    {
                        ((TESpectralBlock) tile).setDuration(100);
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
        return 100;
    }
}
