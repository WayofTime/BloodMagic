package WayofTime.alchemicalWizardry.common.items.sigil;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.EnergyBattery;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;

public class LavaSigil extends ItemBucket implements ArmourUpgrade
{
    /**
     * field for checking if the bucket has been filled.
     */
    private Block isFull = Blocks.lava;
    private int energyUsed;

    public LavaSigil()
    {
        super(Blocks.lava);
        this.maxStackSize = 1;
        setEnergyUsed(1000);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:LavaSigil");
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage() + getEnergyUsed());
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Contact with liquid is");
        par3List.add("highly unrecommended.");

        if (!(par1ItemStack.getTagCompound() == null))
        {
            par3List.add("Current owner: " + par1ItemStack.getTagCompound().getString("ownerName"));
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        float f = 1.0F;
        double d0 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double) f;
        double d1 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double) f + 1.62D - (double) par3EntityPlayer.yOffset;
        double d2 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double) f;

        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);

        if (movingobjectposition == null)
        {
            return par1ItemStack;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!par2World.canMineBlock(par3EntityPlayer, i, j, k))
                {
                    return par1ItemStack;
                }

                TileEntity tile = par2World.getTileEntity(i, j, k);
                if (tile instanceof IFluidHandler)
                {
                    FluidStack fluid = new FluidStack(FluidRegistry.LAVA, 1000);
                    int amount = ((IFluidHandler) tile).fill(ForgeDirection.getOrientation(movingobjectposition.sideHit), fluid, false);

                    if (amount > 0 && EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
                    {
                        ((IFluidHandler) tile).fill(ForgeDirection.getOrientation(movingobjectposition.sideHit), fluid, true);
                    }

                    return par1ItemStack;
                }
                if (movingobjectposition.sideHit == 0)
                {
                    --j;
                }

                if (movingobjectposition.sideHit == 1)
                {
                    ++j;
                }

                if (movingobjectposition.sideHit == 2)
                {
                    --k;
                }

                if (movingobjectposition.sideHit == 3)
                {
                    ++k;
                }

                if (movingobjectposition.sideHit == 4)
                {
                    --i;
                }

                if (movingobjectposition.sideHit == 5)
                {
                    ++i;
                }

                if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack))
                {
                    return par1ItemStack;
                }

                if(this.canPlaceContainedLiquid(par2World, d0, d1, d2, i, j, k) && EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
                {
                	this.tryPlaceContainedLiquid(par2World, d0, d1, d2, i, j, k);
                }
            }

            return par1ItemStack;
        }
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean tryPlaceContainedLiquid(World par1World, double par2, double par4, double par6, int par8, int par9, int par10)
    {
        if (!par1World.isAirBlock(par8, par9, par10) && par1World.getBlock(par8, par9, par10).getMaterial().isSolid())
        {
            return false;
        } else if ((par1World.getBlock(par8, par9, par10) == Blocks.lava || par1World.getBlock(par8, par9, par10) == Blocks.flowing_lava) && par1World.getBlockMetadata(par8, par9, par10) == 0)
        {
            return false;
        } else
        {
            par1World.setBlock(par8, par9, par10, this.isFull, 0, 3);
            return true;
        }
    }
    
    public boolean canPlaceContainedLiquid(World par1World, double par2, double par4, double par6, int par8, int par9, int par10)
    {
        if (!par1World.isAirBlock(par8, par9, par10) && par1World.getBlock(par8, par9, par10).getMaterial().isSolid())
        {
            return false;
        } else if ((par1World.getBlock(par8, par9, par10) == Blocks.lava || par1World.getBlock(par8, par9, par10) == Blocks.flowing_lava) && par1World.getBlockMetadata(par8, par9, par10) == 0)
        {
            return false;
        } else
        {
            return true;
        }
    }

    protected void setEnergyUsed(int par1int)
    {
        this.energyUsed = par1int;
    }

    protected int getEnergyUsed()
    {
        return this.energyUsed;
    }

    protected boolean syphonBatteries(ItemStack ist, EntityPlayer player, int damageToBeDone)
    {
        if (!player.capabilities.isCreativeMode)
        {
            boolean usedBattery = false;
            IInventory inventory = player.inventory;

            for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
            {
                ItemStack stack = inventory.getStackInSlot(slot);

                if (stack == null)
                {
                    continue;
                }
                if (stack.getItem() instanceof EnergyBattery && !usedBattery)
                {
                    if (stack.getItemDamage() <= stack.getMaxDamage() - damageToBeDone)
                    {
                        stack.setItemDamage(stack.getItemDamage() + damageToBeDone);
                        usedBattery = true;
                    }
                }
            }

            if (!usedBattery)
            {
                return false;
            }

            return true;
        } else
        {
            return true;
        }
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 9, true));
        player.extinguish();
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
