package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilVoid extends ItemBucket implements ArmourUpgrade, ISigil
{
    private int isFull;
    private int energyUsed;

    public SigilVoid()
    {
        super(null);
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilVoidCost);
        isFull = 0;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:VoidSigil");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.voidsigil.desc"));

        if (!(stack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage() + getEnergyUsed());
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    protected void setEnergyUsed(int par1int)
    {
        this.energyUsed = par1int;
    }

    protected int getEnergyUsed()
    {
        return this.energyUsed;
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
    	return stack;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote || !EnergyItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return false;
        }

        float f = 1.0F;

        if (!world.canMineBlock(player, x, y, z))
        {
            return false;
        }

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IFluidHandler)
        {
            FluidStack amount = ((IFluidHandler) tile).drain(ForgeDirection.getOrientation(side), 1000, false);

            if (amount != null && amount.amount > 0 && EnergyItems.syphonBatteries(stack, player, getEnergyUsed()))
            {
                ((IFluidHandler) tile).drain(ForgeDirection.getOrientation(side), 1000, true);
                return true;
            }

            return false;
        }

        if (side == 0)
        {
            --y;
        }

        if (side == 1)
        {
            ++y;
        }

        if (side == 2)
        {
            --z;
        }

        if (side == 3)
        {
            ++z;
        }

        if (side == 4)
        {
            --x;
        }

        if (side == 5)
        {
            ++x;
        }
        
        if (!player.canPlayerEdit(x, y, z, side, stack))
        {
            return false;
        }

        if (SpellHelper.isBlockFluid(world.getBlock(x, y, z)) && EnergyItems.syphonBatteries(stack, player, getEnergyUsed()))
        {
            world.setBlockToAir(x, y, z);
            return true;
        }
        

        return false;
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean tryPlaceContainedLiquid(World par1World, double par2, double par4, double par6, int par8, int par9, int par10)
    {
        return false;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
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
