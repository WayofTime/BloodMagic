package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.IBindable;
import WayofTime.alchemicalWizardry.common.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import thaumcraft.api.IGoggles;
import thaumcraft.api.nodes.IRevealer;

import java.util.List;

public class BoundArmour extends ItemArmor implements ISpecialArmor, IRevealer, IGoggles, IBindable {
    private static int invSize = 9;
    private static Icon helmetIcon;
    private static Icon plateIcon;
    private static Icon leggingsIcon;
    private static Icon bootsIcon;

    public BoundArmour(int par1, int armorType)
    {
        super(par1, EnumArmorMaterial.GOLD, 0, armorType);
        setMaxDamage(1000);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
        this.helmetIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundHelmet");
        this.plateIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundPlate");
        this.leggingsIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundLeggings");
        this.bootsIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundBoots");
    }

    @SideOnly(Side.CLIENT)

    public Icon getIconFromDamage(int par1)
    {
        if (this.itemID == ModItems.boundHelmet.itemID)
        {
            return this.helmetIcon;
        }

        if (this.itemID == ModItems.boundPlate.itemID)
        {
            return this.plateIcon;
        }

        if (this.itemID == ModItems.boundLeggings.itemID)
        {
            return this.leggingsIcon;
        }

        if (this.itemID == ModItems.boundBoots.itemID)
        {
            return this.bootsIcon;
        }

        return this.itemIcon;
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return false;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
    {
        if (source.equals(DamageSource.drown))
        {
            return new ArmorProperties(-1, 0, 0);
        }

        if (source.equals(DamageSource.outOfWorld))
        {
            if (isImmuneToVoid(armor))
            {
                return new ArmorProperties(-1, 3, 100000);
            } else
            {
                return new ArmorProperties(-1, 0, 0);
            }
        }

        ItemStack helmet = player.getCurrentItemOrArmor(4);
        ItemStack plate = player.getCurrentItemOrArmor(3);
        ItemStack leggings = player.getCurrentItemOrArmor(2);
        ItemStack boots = player.getCurrentItemOrArmor(1);

        if (helmet == null || plate == null || leggings == null || boots == null)
        {
            return new ArmorProperties(-1, 0, 0);
        }

        if (helmet.itemID == ModItems.boundHelmet.itemID || plate.itemID == ModItems.boundPlate.itemID || leggings.itemID == ModItems.boundLeggings.itemID || boots.itemID == ModItems.boundBoots.itemID)
        {
            if (source.isUnblockable())
            {
                return new ArmorProperties(-1, 3, 3);
            }

            return new ArmorProperties(-1, 3, 100000);
        }

        return new ArmorProperties(-1, 0, 0);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
    {
        if (armor.itemID == ModItems.boundHelmet.itemID)
        {
            return 3;
        }

        if (armor.itemID == ModItems.boundPlate.itemID)
        {
            return 8;
        }

        if (armor.itemID == ModItems.boundLeggings.itemID)
        {
            return 6;
        }

        if (armor.itemID == ModItems.boundBoots.itemID)
        {
            return 3;
        }

        return 5;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
    {
        if (entity instanceof EntityPlayer)
        {
            EnergyItems.checkAndSetItemOwner(stack, (EntityPlayer) entity);

            if (((EntityPlayer) entity).capabilities.isCreativeMode)
            {
                return;
            }

            //EnergyItems.syphonBatteries(stack, (EntityPlayer)entity, 200);
        }

        stack.setItemDamage(stack.getItemDamage() + damage);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Devilish Protection");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            if (!par1ItemStack.stackTagCompound.getString("ownerName").equals(""))
            {
                par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
            }

            ItemStack[] inv = getInternalInventory(par1ItemStack);

            if (inv == null)
            {
                return;
            }

            for (int i = 0; i < invSize; i++)
            {
                if (inv[i] != null)
                {
                    par3List.add("Item in slot " + i + ": " + inv[i].getDisplayName());
                }
            }
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
        //TODO Make the armour invisible when the player has Invisibility on.
        if (entity instanceof EntityLivingBase)
        {
            if (((EntityLivingBase) entity).isPotionActive(Potion.invisibility.id))
            {
                if (itemID == ModItems.boundHelmet.itemID || itemID == ModItems.boundPlate.itemID || itemID == ModItems.boundBoots.itemID)
                {
                    return "alchemicalwizardry:models/armor/boundArmour_invisible_layer_1.png";
                }

                if (itemID == ModItems.boundLeggings.itemID)
                {
                    return "alchemicalwizardry:models/armor/boundArmour_invisible_layer_2.png";
                }
            }
        }

        if (itemID == ModItems.boundHelmet.itemID || itemID == ModItems.boundPlate.itemID || itemID == ModItems.boundBoots.itemID)
        {
            return "alchemicalwizardry:models/armor/boundArmour_layer_1.png";
        }

        if (itemID == ModItems.boundLeggings.itemID)
        {
            return "alchemicalwizardry:models/armor/boundArmour_layer_2.png";
        } else
        {
            return null;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);
        return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
    }

    @Override
    public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
        if (itemStack.stackTagCompound == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        int maxBloodLevel = getMaxBloodShardLevel(itemStack);
        ItemStack[] inv = getInternalInventory(itemStack);

        if (inv != null)
        {
            int iSize = 0;
            int iBlood = 0;
//        	while(iSize<invSize)//&&iBlood<maxBloodLevel)
//        	{
//        		ItemStack item = inv[iSize];
//        		if(inv[iSize]!=null)
//        		{
//        			if(inv[iSize].getItem()!=null)
//            		{
//            			if(((ArmourUpgrade)inv[iSize].getItem()).isUpgrade())
//            			{
//            				((ArmourUpgrade)inv[iSize].getItem()).onArmourUpdate(world, player, inv[iSize]);
//            				iBlood++;
//            			}
//            		}
//        		}
//
//        		iSize++;
//        	}
        }

        if (!player.isPotionActive(AlchemicalWizardry.customPotionInhibit))
        {
            tickInternalInventory(itemStack, world, player, 0, false);
        }

        if (itemStack.getItemDamage() > 0)
        {
            EnergyItems.checkAndSetItemOwner(itemStack, player);

            if (!player.capabilities.isCreativeMode)
            {
                EnergyItems.syphonBatteries(itemStack, player, itemStack.getItemDamage() * 75);
                itemStack.setItemDamage(0);
            }
        }

        return;
    }

    public void tickInternalInventory(ItemStack par1ItemStack, World par2World, EntityPlayer par3Entity, int par4, boolean par5)
    {
        ItemStack[] inv = getInternalInventory(par1ItemStack);

        if (inv == null)
        {
            return;
        }

        int blood = getMaxBloodShardLevel(par1ItemStack);

        //int blood = 1;
        for (int i = 0; i < invSize; i++)
        {
            if (inv[i] == null)
            {
                continue;
            }

            if (inv[i].getItem() instanceof ArmourUpgrade && blood > 0)
            {
                if (((ArmourUpgrade) inv[i].getItem()).isUpgrade())
                {
                    ((ArmourUpgrade) inv[i].getItem()).onArmourUpdate(par2World, par3Entity, inv[i]);
                    blood--;
                }

                if (par2World.getWorldTime() % 200 == 0)
                {
                    if (getUpgradeCostMultiplier(par1ItemStack) > 0.02f)
                    {
                        EnergyItems.syphonBatteries(par1ItemStack, par3Entity, (int) (((ArmourUpgrade) inv[i].getItem()).getEnergyForTenSeconds() * getUpgradeCostMultiplier(par1ItemStack)));
                    }
                }
            }
        }
    }

    public int getMaxBloodShardLevel(ItemStack armourStack)
    {
        ItemStack[] inv = getInternalInventory(armourStack);

        if (inv == null)
        {
            return 0;
        }

        int max = 0;

        for (int i = 0; i < invSize; i++)
        {
            ItemStack itemStack = inv[i];

            if (itemStack != null)
            {
                if (itemStack.itemID == ModItems.weakBloodShard.itemID)
                {
                    max = Math.max(max, 1);
                }

                if (itemStack.itemID == ModItems.demonBloodShard.itemID)
                {
                    max = Math.max(max, 2);
                }
            }
        }

        return max;
    }

    public boolean hasAddedToInventory(ItemStack sigilItemStack, ItemStack addedItemStack)
    {
        ItemStack[] inv = getInternalInventory(sigilItemStack);

        if (inv == null)
        {
            return false;
        }

        if (addedItemStack == null)
        {
            return false;
        }

        int itemID = addedItemStack.getItem().itemID;
        int candidateSlot = -1;

        for (int i = invSize - 1; i >= 0; i--)
        {
            ItemStack nextItem = inv[i];

            if (nextItem == null)
            {
                candidateSlot = i;
                continue;
            }
        }

        if (candidateSlot == -1)
        {
            return false;
        }

        if (addedItemStack.getItem() instanceof ArmourUpgrade)
        {
            inv[candidateSlot] = addedItemStack;
            saveInternalInventory(sigilItemStack, inv);
            return true;
        }

        return false;
    }

    public ItemStack[] getInternalInventory(ItemStack itemStack)
    {
        NBTTagCompound itemTag = itemStack.stackTagCompound;

        if (itemTag == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
            return null;
        }

        ItemStack[] inv = new ItemStack[9];
        NBTTagList tagList = itemTag.getTagList("Inventory");

        if (tagList == null)
        {
            return null;
        }

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            int slot = tag.getByte("Slot");

            if (slot >= 0 && slot < invSize)
            {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        return inv;
    }

    public void saveInternalInventory(ItemStack itemStack, ItemStack[] inventory)
    {
        NBTTagCompound itemTag = itemStack.stackTagCompound;

        if (itemTag == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < invSize; i++)
        {
            ItemStack stack = inventory[i];

            if (inventory[i] != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        itemTag.setTag("Inventory", itemList);
    }

    public boolean isImmuneToVoid(ItemStack itemStack)
    {
        ItemStack[] inv = getInternalInventory(itemStack);

        if (inv == null)
        {
            return false;
        }

        for (ItemStack item : inv)
        {
            if (item == null)
            {
                continue;
            }

            if (item.itemID == ModItems.voidSigil.itemID)
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasIRevealer(ItemStack itemStack)
    {
        ItemStack[] inv = getInternalInventory(itemStack);

        if (inv == null)
        {
            return false;
        }

        for (ItemStack item : inv)
        {
            if (item == null)
            {
                continue;
            }

            if (item.getItem() instanceof IRevealer)
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasIGoggles(ItemStack itemStack)
    {
        ItemStack[] inv = getInternalInventory(itemStack);

        if (inv == null)
        {
            return false;
        }

        for (ItemStack item : inv)
        {
            if (item == null)
            {
                continue;
            }

            if (item.getItem() instanceof IGoggles)
            {
                return true;
            }
        }

        return false;
    }

    public float getUpgradeCostMultiplier(ItemStack itemStack)
    {
        ItemStack[] inv = getInternalInventory(itemStack);

        if (inv == null)
        {
            return 1.0f;
        }

        for (ItemStack item : inv)
        {
            if (item == null)
            {
                continue;
            }

            if (item.itemID == ModItems.weakBloodOrb.itemID)
            {
                return 0.75f;
            }

            if (item.itemID == ModItems.apprenticeBloodOrb.itemID)
            {
                return 0.50f;
            }

            if (item.itemID == ModItems.magicianBloodOrb.itemID)
            {
                return 0.25f;
            }

            if (item.itemID == ModItems.masterBloodOrb.itemID)
            {
                return 0.0f;
            }

            if (item.itemID == ModItems.archmageBloodOrb.itemID)
            {
                return 0.0f;
            }
        }

        return 1.0f;
    }

    public int getItemEnchantability()
    {
        return 0;
    }

    @Override
    public boolean showNodes(ItemStack itemstack, EntityLivingBase player)
    {
        return this.hasIRevealer(itemstack);
    }

    @Override
    public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player)
    {
        return this.hasIGoggles(itemstack);
    }
}
