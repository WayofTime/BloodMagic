package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.ItemType;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class BoundPickaxe extends ItemPickaxe implements IBindable
{
    public float efficiencyOnProperMaterial = 12.0F;
    public float damageVsEntity;

    private int energyUsed;

    public BoundPickaxe()
    {
        super(AlchemicalWizardry.bloodBoundToolMaterial);
        setMaxStackSize(1);
        this.efficiencyOnProperMaterial = 12.0F;
        this.damageVsEntity = 5;
        this.setEnergyUsed(5);
    }

    public void setEnergyUsed(int i)
    {
        energyUsed = i;
    }

    public int getEnergyUsed()
    {
        return this.energyUsed;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.boundpickaxe.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.boundpickaxe.desc2"));

        if (!(stack.getTagCompound() == null))
        {
            if (stack.getTagCompound().getBoolean("isActive"))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.activated"));
            } else
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.deactivated"));
            }

            if (!stack.getTagCompound().getString("ownerName").equals(""))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer par3EntityPlayer)
    {
        if (!BindableItems.checkAndSetItemOwner(stack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            this.setActivated(stack, !getActivated(stack));
            stack.getTagCompound().setInteger("worldTimeDelay", (int) (world.getWorldTime() - 1) % 200);
            return stack;
        }
        
        if (world.isRemote)
        {
            return stack;
        }

        if (!getActivated(stack) || SpellHelper.isFakePlayer(world, par3EntityPlayer))
        {
            return stack;
        }

        if (par3EntityPlayer.isPotionActive(AlchemicalWizardry.customPotionInhibit))
        {
            return stack;
        }
        
        if(!BindableItems.syphonBatteries(stack, par3EntityPlayer, 10000))
        {
        	return stack;
        }

        BlockPos pos = par3EntityPlayer.getPosition();
        boolean silkTouch = EnchantmentHelper.getSilkTouchModifier(par3EntityPlayer);
        int fortuneLvl = EnchantmentHelper.getFortuneModifier(par3EntityPlayer);

        HashMultiset<ItemType> dropMultiset = HashMultiset.create();
        
        for (int i = -5; i <= 5; i++)
        {
            for (int j = 0; j <= 10; j++)
            {
                for (int k = -5; k <= 5; k++)
                {
                	BlockPos newPos = pos.add(i, j, k);
                	IBlockState state = world.getBlockState(newPos);
                    Block block = state.getBlock();

                    if (block != null)
                    {
                        float str = getStrVsBlock(stack, block);

                        if (str > 1.1f && world.canMineBlockBody(par3EntityPlayer, newPos))
                        {
                            if (silkTouch && block.canSilkHarvest(world, newPos, state, par3EntityPlayer))
                            {
                                dropMultiset.add(new ItemType(block, block.getMetaFromState(state)));
                            } else
                            {
                                List<ItemStack> itemDropList = block.getDrops(world, newPos, state, fortuneLvl);

                                if (itemDropList != null)
                                {
                                    for (ItemStack stacky : itemDropList)
                                        dropMultiset.add(ItemType.fromStack(stacky), stacky.stackSize);
                                }
                            }

                            world.setBlockToAir(newPos);
                        }
                    }
                }
            }
        }
        
        BoundPickaxe.dropMultisetStacks(dropMultiset, world, pos.getX(), pos.getY() + par3EntityPlayer.getEyeHeight(), pos.getZ());
        
        return stack;
    }
    
    public static void dropMultisetStacks(Multiset<ItemType> dropMultiset, World world, double x, double y, double z)
    {
        for (Multiset.Entry<ItemType> entry : dropMultiset.entrySet())
        {
            int count = entry.getCount();
            ItemType type = entry.getElement();
            int maxStackSize = type.item.getItemStackLimit(type.createStack(1));
            
            //Drop in groups of maximum size
            while (count >= maxStackSize)
            {
                world.spawnEntityInWorld(new EntityItem(world, x, y, z, type.createStack(maxStackSize)));
                count -= maxStackSize;
            }
            //Drop remainder
            if (count > 0)
                world.spawnEntityInWorld(new EntityItem(world, x, y, z, type.createStack(count)));
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity par3Entity, int par4, boolean par5)
    {
        if (!(par3Entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        if (world.getWorldTime() % 200 == stack.getTagCompound().getInteger("worldTimeDelay") && stack.getTagCompound().getBoolean("isActive"))
        {
            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                if(!BindableItems.syphonBatteries(stack, par3EntityPlayer, 20))
                {
                	this.setActivated(stack, false);
                }
            }
        }

        stack.setItemDamage(0);
    }

    public void setActivated(ItemStack stack, boolean newActivated)
    {
        stack.setItemDamage(newActivated ? 1 : 0);
    }

    public boolean getActivated(ItemStack stack)
    {
        return stack.getItemDamage() == 1;
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    @Override
    public float getStrVsBlock(ItemStack stack, Block par2Block) //getStrVsBlock
    {
        if (!getActivated(stack))
        {
            return 0.0F;
        }

        return super.getStrVsBlock(stack, par2Block);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack stack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        return getActivated(stack);
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    @Override
    public int getItemEnchantability()
    {
        return 30;
    }

    /**
     * FORGE: Overridden to allow custom tool effectiveness
     */
    @Override
    public float getDigSpeed(ItemStack stack, IBlockState state)
    {
        if (!getActivated(stack))
        {
            return 0.0F;
        }

        for (String type : getToolClasses(stack))
        {
            if (state.getBlock().isToolEffective(type, state))
                return efficiencyOnProperMaterial;
        }
        return super.getDigSpeed(stack, state);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        return !getActivated(stack);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass)
    {
        if (getActivated(stack) && "pickaxe".equals(toolClass))
        {
            return 5;
        }

        return 0;
    }
}
