package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.summoningRegistry.SummoningRegistry;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityDemon;

public class DemonCrystal extends Item
{
    public DemonCrystal()
    {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMisc);
        setMaxStackSize(1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }
        else if (!playerIn.func_175151_a(pos.offset(side), side, stack))
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = world.getBlockState(pos);

            pos = pos.offset(side);
            double d0 = 0.0D;

            if (side == EnumFacing.UP && iblockstate instanceof BlockFence)
            {
                d0 = 0.5D;
            }

            String demonName = DemonCrystal.getDemonString(stack);
            Entity entity = spawnCreature(world, demonName, (double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D, stack);

            if (entity != null)
            {
                if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                {
                    entity.setCustomNameTag(stack.getDisplayName());
                }

                if (!playerIn.capabilities.isCreativeMode)
                {
                    --stack.stackSize;
                }
            }

            return true;
        }
    }

    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * Parameters: world, entityID, x, y, z.
     */
    public static Entity spawnCreature(World par0World, String par1, double par2, double par4, double par6, ItemStack itemStack)
    {
        Entity entity = null;

        for (int j = 0; j < 1; ++j)
        {
            entity = SummoningRegistry.getEntityWithID(par0World, par1);

            if (entity != null)
            {
                EntityLiving entityliving = (EntityLiving) entity;
                entity.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
                entityliving.rotationYawHead = entityliving.rotationYaw;
                entityliving.renderYawOffset = entityliving.rotationYaw;
                if (entityliving instanceof EntityDemon)
                {
                    ((EntityDemon) entityliving).func_152115_b(DemonCrystal.getOwnerName(itemStack));

                    if (!DemonCrystal.getOwnerName(itemStack).equals(""))
                    {
                        ((EntityDemon) entityliving).setTamed(true);
                    }
                }

                par0World.spawnEntityInWorld(entity);
                entityliving.playLivingSound();
            }
        }
        return entity;
    }

    public static void setOwnerName(ItemStack par1ItemStack, String ownerName)
    {
        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.getTagCompound().setString("ownerName", ownerName);
    }

    public static String getOwnerName(ItemStack par1ItemStack)
    {
        if (par1ItemStack.getTagCompound() == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        return par1ItemStack.getTagCompound().getString("ownerName");
    }
    
    public static void setDemonString(ItemStack itemStack, String demonName)
    {
    	if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        itemStack.getTagCompound().setString("demonName", demonName);
    }
    
    public static String getDemonString(ItemStack itemStack)
    {
    	if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemStack.getTagCompound().getString("demonName");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.demonplacer.desc"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            if (!par1ItemStack.getTagCompound().getString("ownerName").equals(""))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.owner.demonsowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
            }
        }
    }
}
