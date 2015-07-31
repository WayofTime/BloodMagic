package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IHolding;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityBloodLightProjectile;
import WayofTime.alchemicalWizardry.common.items.BindableItems;

public class SigilBloodLight extends BindableItems implements IHolding, ArmourUpgrade, ISigil
{
    public SigilBloodLight()
    {
        super();
        setEnergyUsed(10);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.bloodlightsigil.desc"));

        if (!(itemStack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + itemStack.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(!BindableItems.checkAndSetItemOwner(itemStack, player) || !BindableItems.syphonBatteries(itemStack, player, getEnergyUsed()))
        {
        	return true;
        }

        if (world.isRemote)
        {
            return true;
        }

        IBlockState light = ModBlocks.blockBloodLight.getBlockState().getBaseState();

        if (side.getIndex() == 0 && world.isAirBlock(blockPos.add(0, -1, 0)))
        {
            world.setBlockState(blockPos.add(0, -1, 0), light);
        }

        if (side.getIndex() == 1 && world.isAirBlock(blockPos.add(0, 1, 0)))
        {
            world.setBlockState(blockPos.add(0, 1, 0), light);
        }

        if (side.getIndex() == 2 && world.isAirBlock(blockPos.add(0, 0, -1)))
        {
            world.setBlockState(blockPos.add(0, 0, -1), light);
        }

        if (side.getIndex() == 3 && world.isAirBlock(blockPos.add(0, 0, 1)))
        {
            world.setBlockState(blockPos.add(0, 0, 1), light);
        }

        if (side.getIndex() == 4 && world.isAirBlock(blockPos.add(-1, 0, 0)))
        {
            world.setBlockState(blockPos.add(-1, 0, 0), light);
        }

        if (side.getIndex() == 5 && world.isAirBlock(blockPos.add(1, 0, 0)))
        {
            world.setBlockState(blockPos.add(1, 0, 0), light);
        }

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!BindableItems.checkAndSetItemOwner(itemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            return itemStack;
        }

        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        if(!BindableItems.syphonBatteries(itemStack, par3EntityPlayer, getEnergyUsed() * 5))
        {
        	return itemStack;
        }

        if (!par2World.isRemote)
        {
            par2World.spawnEntityInWorld(new EntityBloodLightProjectile(par2World, par3EntityPlayer, 10));
        }

        return itemStack;
    }
    
    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 400, 9, true, false));
    }

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 25;
    }
}
