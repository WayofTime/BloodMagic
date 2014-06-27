package WayofTime.alchemicalWizardry.common.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BoundPickaxe extends ItemPickaxe implements IBindable
{
    /**
     * Array of blocks the tool has extra effect against.
     */
   // public static final Block[] blocksEffectiveAgainst = new Block[]{Blocks.cobblestone, Blocks.stoneDoubleSlab, Blocks.stoneSingleSlab, Block.stone, Block.sandStone, Block.cobblestoneMossy, Block.oreIron, Block.blockIron, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond, Block.ice, Block.netherrack, Block.oreLapis, Block.blockLapis, Block.oreRedstone, Block.oreRedstoneGlowing, Block.rail, Block.railDetector, Block.railPowered, Block.railActivator};

    public float efficiencyOnProperMaterial = 12.0F;

    /**
     * Damage versus entities.
     */
    public float damageVsEntity;

    private static IIcon activeIcon;
    private static IIcon passiveIcon;

    private int energyUsed;

    public BoundPickaxe()
    {
        super(AlchemicalWizardry.bloodBoundToolMaterial);
        this.maxStackSize = 1;
        //this.setMaxDamage(par3EnumToolMaterial.getMaxUses());
        this.efficiencyOnProperMaterial = 12.0F;
        this.damageVsEntity = 5;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
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
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("The Souls of the Damned");
        par3List.add("do not like stone...");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            if (par1ItemStack.stackTagCompound.getBoolean("isActive"))
            {
                par3List.add("Activated");
            } else
            {
                par3List.add("Deactivated");
            }

            if (!par1ItemStack.stackTagCompound.getString("ownerName").equals(""))
            {
                par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundPickaxe_activated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundPickaxe_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        if (stack.stackTagCompound == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.stackTagCompound;

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
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            this.setActivated(par1ItemStack, !getActivated(par1ItemStack));
            par1ItemStack.stackTagCompound.setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % 200);
            return par1ItemStack;
        }

        if (!getActivated(par1ItemStack))
        {
            return par1ItemStack;
        }

        if (par3EntityPlayer.isPotionActive(AlchemicalWizardry.customPotionInhibit))
        {
            return par1ItemStack;
        }
        
        Vec3 blockVec = SpellHelper.getEntityBlockVector(par3EntityPlayer);
        int posX = (int)(blockVec.xCoord);
        int posY = (int)(blockVec.yCoord);
        int posZ = (int)(blockVec.zCoord);
        boolean silkTouch = false;
        int so = Enchantment.silkTouch.effectId;
        int fortune = Enchantment.fortune.effectId;
        int fortuneLvl = 0;
        NBTTagList enchants = par1ItemStack.getEnchantmentTagList();

        if (enchants != null)
        {
            for (int i = 0; i < enchants.tagCount(); i++)
            {
                if (enchants.getCompoundTagAt(i) instanceof NBTTagCompound)
                {
                    NBTTagCompound nbt = (NBTTagCompound) enchants.getCompoundTagAt(i);
                    int id = nbt.getShort("id");

                    if (id == so)
                    {
                        silkTouch = true;
                    }

                    if (id == fortune)
                    {
                        fortuneLvl = nbt.getShort("lvl");
                    }
                }
            }
        }

        for (int i = -5; i <= 5; i++)
        {
            for (int j = -5; j <= 5; j++)
            {
                for (int k = -5; k <= 5; k++)
                {
                    Block block = par2World.getBlock(posX + i, posY + j, posZ + k);
                    int meta = par2World.getBlockMetadata(posX + i, posY + j, posZ + k);

                    if (block != null && block.getBlockHardness(par2World, posX + i, posY + j, posZ + k) != -1)
                    {
                        float str = func_150893_a(par1ItemStack, block);

                        if (str > 1.1f && par2World.canMineBlock(par3EntityPlayer, posX + i, posY + j, posZ + k))
                        {
                            //par1ItemStack.getEnchantmentTagList();
                            if (silkTouch)
                            {
                                ItemStack droppedItem = new ItemStack(block, 1, meta);

                                if (!par2World.isRemote)
                                {
                                    par2World.spawnEntityInWorld(new EntityItem(par2World, posX, posY + par3EntityPlayer.getEyeHeight(), posZ, droppedItem));
                                }
                            } else
                            {
                                ArrayList<ItemStack> itemDropList = block.getDrops(par2World, posX + i, posY + j, posZ + k, meta, fortuneLvl);

                                if (itemDropList != null)
                                {
                                    for (ItemStack item : itemDropList)
                                    {
                                        if (!par2World.isRemote)
                                        {
                                            par2World.spawnEntityInWorld(new EntityItem(par2World, posX, posY + par3EntityPlayer.getEyeHeight(), posZ, item));
                                        }
                                    }
                                }
                            }

                            par2World.setBlockToAir(posX + i, posY + j, posZ + k);
                        }
                    }
                }
            }
        }

        EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, 10000);
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

        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

//        if(par1ItemStack.stackTagCompound.getBoolean("isActive"))
//        {
//        	EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, 1);
//        }

        if (par2World.getWorldTime() % 200 == par1ItemStack.stackTagCompound.getInteger("worldTimeDelay") && par1ItemStack.stackTagCompound.getBoolean("isActive"))
        {
            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, 20);
            }
        }

        par1ItemStack.setItemDamage(0);
        return;
    }

    public void setActivated(ItemStack par1ItemStack, boolean newActivated)
    {
        NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

        if (itemTag == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        itemTag.setBoolean("isActive", newActivated);
    }

    public boolean getActivated(ItemStack par1ItemStack)
    {
        NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

        if (itemTag == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        return itemTag.getBoolean("isActive");
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    @Override
    public float func_150893_a(ItemStack par1ItemStack, Block par2Block) //getStrVsBlock
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

        //par1ItemStack.damageItem(2, par3EntityLivingBase);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase)
    {

        if (par7EntityLivingBase instanceof EntityPlayer)
        {
            EnergyItems.syphonBatteries(par1ItemStack, (EntityPlayer) par7EntityLivingBase, getEnergyUsed());
        }

        //TODO Possibly add better functionality for the items?
        //par7EntityLivingBase.getLookVec();
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
     * Return whether this item is repairable in an anvil.
     */
//    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
//    {
//        return false;
//    }

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

//    @Override
//
//    /**
//     * Returns if the item (tool) can harvest results from the block type.
//     */
//    public boolean func_150897_b(Block par1Block) //canHarvestBlock
//    {
//        return par1Block == Blocks.obsidian ? true : (par1Block != Blocks.diamond_block && par1Block != Blocks.diamond_ore ? (par1Block != Blocks.emerald_ore && par1Block != Blocks.emerald_block ? (par1Block != Blocks.gold_block && par1Block != Blocks.gold_ore ? (par1Block != Blocks.iron_block && par1Block != Blocks.iron_ore ? (par1Block != Blocks.lapis_block && par1Block != Blocks.lapis_ore ? (par1Block != Blocks.redstone_ore && par1Block != Blocks.oreRedstoneGlowing ? (par1Block.getMaterial() == Material.rock ? true : (par1Block.blockMaterial == Material.iron ? true : par1Block.blockMaterial == Material.anvil)) : true) : true) : true) : true) : true) : true);
//    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        return !getActivated(stack);
    }
    
    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass)
    {
    	if("pickaxe".equals(toolClass))
    	{
    		return 5;
    	}
    	
    	return 0;
    }
}
