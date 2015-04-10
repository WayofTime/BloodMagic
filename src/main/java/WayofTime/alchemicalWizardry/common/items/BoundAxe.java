package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.ItemType;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashMultiset;

public class BoundAxe extends ItemAxe implements IBindable
{
    public float efficiencyOnProperMaterial = 12.0F;
    public float damageVsEntity;
    private static IIcon activeIcon;
    private static IIcon passiveIcon;
    private int energyUsed;

    public BoundAxe()
    {
        super(AlchemicalWizardry.bloodBoundToolMaterial);
        this.maxStackSize = 1;
        this.efficiencyOnProperMaterial = 12.0F;
        this.damageVsEntity = 5;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setEnergyUsed(5);
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
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.boundaxe.desc"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            if (par1ItemStack.getTagCompound().getBoolean("isActive"))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.activated"));
            } else
            {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.deactivated"));
            }

            if (!par1ItemStack.getTagCompound().getString("ownerName").equals(""))
            {
                par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundAxe_activated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundAxe_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        if (tag.getBoolean("isActive"))
        {
            return this.activeIcon;
        } else
        {
            return this.passiveIcon;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking())
        {
            this.setActivated(par1ItemStack, !getActivated(par1ItemStack));
            par1ItemStack.getTagCompound().setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % 200);
            return par1ItemStack;
        }

        if (!getActivated(par1ItemStack) || SpellHelper.isFakePlayer(par2World, par3EntityPlayer))
        {
            return par1ItemStack;
        }

        if (par3EntityPlayer.isPotionActive(AlchemicalWizardry.customPotionInhibit))
        {
            return par1ItemStack;
        }
        
        if(!EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, 10000))
        {
        	return par1ItemStack;
        }

        Vec3 blockVec = SpellHelper.getEntityBlockVector(par3EntityPlayer);
        int posX = (int) (blockVec.xCoord);
        int posY = (int) (blockVec.yCoord);
        int posZ = (int) (blockVec.zCoord);
        boolean silkTouch = EnchantmentHelper.getSilkTouchModifier(par3EntityPlayer);
        int fortuneLvl = EnchantmentHelper.getFortuneModifier(par3EntityPlayer);

        HashMultiset<ItemType> dropMultiset = HashMultiset.create();
        
        for (int i = -5; i <= 5; i++)
        {
            for (int j = 0; j <= 10; j++)
            {
                for (int k = -5; k <= 5; k++)
                {
                    Block block = par2World.getBlock(posX + i, posY + j, posZ + k);
                    int meta = par2World.getBlockMetadata(posX + i, posY + j, posZ + k);

                    if (block != null)
                    {
                        float str = func_150893_a(par1ItemStack, block);

                        if (str > 1.1f || block instanceof BlockLeavesBase && par2World.canMineBlock(par3EntityPlayer, posX + i, posY + j, posZ + k))
                        {
                            if (silkTouch && block.canSilkHarvest(par2World, par3EntityPlayer, posX + i, posY + j, posZ + k, meta))
                            {
                                dropMultiset.add(new ItemType(block, meta));
                            } else
                            {
                                ArrayList<ItemStack> itemDropList = block.getDrops(par2World, posX + i, posY + j, posZ + k, meta, fortuneLvl);

                                if (itemDropList != null)
                                {
                                    for (ItemStack stack : itemDropList)
                                        dropMultiset.add(ItemType.fromStack(stack), stack.stackSize);
                                }
                            }

                            par2World.setBlockToAir(posX + i, posY + j, posZ + k);
                        }
                    }
                }
            }
        }

        BoundPickaxe.dropMultisetStacks(dropMultiset, par2World, posX, posY + par3EntityPlayer.getEyeHeight(), posZ);

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
        if (par2World.getWorldTime() % 200 == par1ItemStack.getTagCompound().getInteger("worldTimeDelay") && par1ItemStack.getTagCompound().getBoolean("isActive"))
        {
            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                if(!EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, 20))
                {
                	this.setActivated(par1ItemStack, false);
                }
            }
        }

        par1ItemStack.setItemDamage(0);
        return;
    }

    public void setActivated(ItemStack par1ItemStack, boolean newActivated)
    {
        NBTTagCompound itemTag = par1ItemStack.getTagCompound();

        if (itemTag == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        itemTag.setBoolean("isActive", newActivated);
    }

    public boolean getActivated(ItemStack par1ItemStack)
    {
        if (!par1ItemStack.hasTagCompound())
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound itemTag = par1ItemStack.getTagCompound();

        return itemTag.getBoolean("isActive");
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    @Override
    public float func_150893_a(ItemStack par1ItemStack, Block par2Block)
    {
        if (!getActivated(par1ItemStack))
        {
            return 0.0F;
        }

        return super.func_150893_a(par1ItemStack, par2Block);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        if (!getActivated(par1ItemStack))
        {
            return false;
        }
        return true;
    }

    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase)
    {
        if ((double) par3.getBlockHardness(par2World, par4, par5, par6) != 0.0D)
        {
        }

        return true;
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
    public float getDigSpeed(ItemStack stack, Block block, int meta)
    {
        if (!getActivated(stack))
        {
            return 0.0F;
        }

        if (ForgeHooks.isToolEffective(stack, block, meta))
        {
            return efficiencyOnProperMaterial;
        }

        return func_150893_a(stack, block);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        return !getActivated(stack);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass)
    {
        if (getActivated(stack) && "axe".equals(toolClass))
        {
            return 5;
        }

        return 0;
    }
}
