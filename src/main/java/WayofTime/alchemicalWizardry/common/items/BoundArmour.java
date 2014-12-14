package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.Constants;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.nodes.IRevealer;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.alchemy.energy.IAlchemyGoggles;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.items.sigil.DivinationSigil;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelOmegaArmour;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.InterfaceList(value = {@Interface(iface = "thaumcraft.api.nodes.IRevealer", modid = "Thaumcraft"), @Interface(iface = "thaumcraft.api.IGoggles", modid = "Thaumcraft"), @Interface(iface = "thaumcraft.api.IRunicArmor", modid = "Thaumcraft")})
public class BoundArmour extends ItemArmor implements IAlchemyGoggles, ISpecialArmor, IBindable, IRevealer, IGoggles, IRunicArmor, ILPGauge
{
    private static int invSize = 9;
    private static IIcon helmetIcon;
    private static IIcon plateIcon;
    private static IIcon leggingsIcon;
    private static IIcon bootsIcon;

    public static boolean tryComplexRendering = true;

    public BoundArmour(int armorType)
    {
        super(ItemArmor.ArmorMaterial.GOLD, 0, armorType);
        setMaxDamage(1000);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    ModelBiped model1 = null;
    ModelBiped model2 = null;
    ModelBiped model = null;

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
    {
        if (tryComplexRendering)
        {
            int type = ((ItemArmor) itemStack.getItem()).armorType;
            if (this.model1 == null)
            {
                this.model1 = new ModelOmegaArmour(1.0f, true, true, false, true);
            }
            if (this.model2 == null)
            {
                this.model2 = new ModelOmegaArmour(0.5f, false, false, true, false);
            }

            if (type == 1 || type == 3 || type == 0)
            {
                this.model = model1;
            } else
            {
                this.model = model2;
            }

            if (this.model != null)
            {
                this.model.bipedHead.showModel = (type == 0);
                this.model.bipedHeadwear.showModel = (type == 0);
                this.model.bipedBody.showModel = ((type == 1) || (type == 2));
                this.model.bipedLeftArm.showModel = (type == 1);
                this.model.bipedRightArm.showModel = (type == 1);
                this.model.bipedLeftLeg.showModel = (type == 2 || type == 3);
                this.model.bipedRightLeg.showModel = (type == 2 || type == 3);
                this.model.isSneak = entityLiving.isSneaking();

                this.model.isRiding = entityLiving.isRiding();
                this.model.isChild = entityLiving.isChild();

                this.model.aimedBow = false;
                this.model.heldItemRight = (entityLiving.getHeldItem() != null ? 1 : 0);

                if ((entityLiving instanceof EntityPlayer))
                {
                    if (((EntityPlayer) entityLiving).getItemInUseDuration() > 0)
                    {
                        EnumAction enumaction = ((EntityPlayer) entityLiving).getItemInUse().getItemUseAction();
                        if (enumaction == EnumAction.block)
                        {
                            this.model.heldItemRight = 3;
                        } else if (enumaction == EnumAction.bow)
                        {
                            this.model.aimedBow = true;
                        }
                    }
                }
            }

            return model;

        } else
        {
            return super.getArmorModel(entityLiving, itemStack, armorSlot);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
        this.helmetIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundHelmet");
        this.plateIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundPlate");
        this.leggingsIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundLeggings");
        this.bootsIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundBoots");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        if (this.equals(ModItems.boundHelmet))
        {
            return this.helmetIcon;
        }

        if (this.equals(ModItems.boundPlate))
        {
            return this.plateIcon;
        }

        if (this.equals(ModItems.boundLeggings))
        {
            return this.leggingsIcon;
        }

        if (this.equals(ModItems.boundBoots))
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
        double armourReduction = 0.0;

        int f = 0;
        int h = 0;
        
        if (player.isPotionActive(AlchemicalWizardry.customPotionSoulFray))
        {
            f = player.getActivePotionEffect(AlchemicalWizardry.customPotionSoulFray).getAmplifier() + 1;
        }

        double damageAmount = 0.25;

        if (player.isPotionActive(AlchemicalWizardry.customPotionSoulHarden))
        {
            h = player.getActivePotionEffect(AlchemicalWizardry.customPotionSoulHarden).getAmplifier() + 1;
        }
        
        armourReduction = 1 - 0.1 * Math.pow(1.0/3.0, Math.max(0, h - f)) - 0.1 * Math.max(0, f-h);

        damageAmount *= (armourReduction);

        int maxAbsorption = 100000;

        if (source.equals(DamageSource.drown))
        {
            return new ArmorProperties(-1, 0, 0);
        }

        if (source.equals(DamageSource.outOfWorld))
        {
            if (isImmuneToVoid(armor))
            {
                return new ArmorProperties(-1, damageAmount * 1000 , maxAbsorption);
            } else
            {
                return new ArmorProperties(-1, 0, 0);
            }
        }

        ItemStack helmet = player.getEquipmentInSlot(4);
        ItemStack plate = player.getEquipmentInSlot(3);
        ItemStack leggings = player.getEquipmentInSlot(2);
        ItemStack boots = player.getEquipmentInSlot(1);

        if (helmet == null || plate == null || leggings == null || boots == null)
        {
            return new ArmorProperties(-1, 0, 0);
        }

        if (helmet.getItem().equals(ModItems.boundHelmet) && plate.getItem().equals(ModItems.boundPlate) && leggings.getItem().equals(ModItems.boundLeggings) && boots.getItem().equals(ModItems.boundBoots))
        {
            if (source.isUnblockable())
            {
                return new ArmorProperties(-1, damageAmount * 0.9d, maxAbsorption);
            }

            return new ArmorProperties(-1, damageAmount, maxAbsorption);
        }

        return new ArmorProperties(-1, 0, 0);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
    {
        if (armor.equals(ModItems.boundHelmet))
        {
            return 3;
        }

        if (armor.equals(ModItems.boundPlate))
        {
            return 8;
        }

        if (armor.equals(ModItems.boundLeggings))
        {
            return 6;
        }

        if (armor.equals(ModItems.boundBoots))
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
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        if (this.tryComplexRendering)
        {
            return "alchemicalwizardry:models/armor/BloodArmour_WIP.png";
        }

        if (entity instanceof EntityLivingBase)
        {
            if (this.getIsInvisible(stack))
            {
                if (this == ModItems.boundHelmet || this == ModItems.boundPlate || this == ModItems.boundBoots)
                {
                    return "alchemicalwizardry:models/armor/boundArmour_invisible_layer_1.png";
                }

                if (this == ModItems.boundLeggings)
                {
                    return "alchemicalwizardry:models/armor/boundArmour_invisible_layer_2.png";
                }
            }
        }

        if (this == ModItems.boundHelmet || this == ModItems.boundPlate || this == ModItems.boundBoots)
        {
            return "alchemicalwizardry:models/armor/boundArmour_layer_1.png";
        }

        if (this == ModItems.boundLeggings)
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
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
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
        }

        if (!player.isPotionActive(AlchemicalWizardry.customPotionInhibit))
        {
            tickInternalInventory(itemStack, world, player, 0, false);
        }

        this.setIsInvisible(itemStack, player.isPotionActive(Potion.invisibility.id));

        if (itemStack.getItemDamage() > 0)
        {
            EnergyItems.checkAndSetItemOwner(itemStack, player);

            if (!player.capabilities.isCreativeMode)
            {
                if(EnergyItems.syphonBatteries(itemStack, player, itemStack.getItemDamage() * 75))
                {
            	   itemStack.setItemDamage(0);
                }
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
                if (itemStack.getItem().equals(ModItems.weakBloodShard))
                {
                    max = Math.max(max, 1);
                }

                if (itemStack.getItem().equals(ModItems.demonBloodShard))
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

        Item item = addedItemStack.getItem();
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
        }
        itemTag = itemStack.stackTagCompound;

        ItemStack[] inv = new ItemStack[9];
        NBTTagList tagList = itemTag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

        if (tagList == null)
        {
            return null;
        }

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
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

            if (item.getItem().equals(ModItems.voidSigil))
            {
                return true;
            }
        }

        return false;
    }

    @Optional.Method(modid = "Thaumcraft")
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

    @Optional.Method(modid = "Thaumcraft")
    public boolean hasIGoggles(ItemStack itemStack)
    {
        ItemStack[] inv = getInternalInventory(itemStack);

        if (inv == null)
        {
            return false;
        }
        
        int blood = getMaxBloodShardLevel(itemStack);

        for (ItemStack item : inv)
        {
            if (item == null)
            {
                continue;
            }
            if (item.getItem() instanceof ArmourUpgrade && blood > 0)
            {
            	if (item.getItem() instanceof IGoggles)
                {
                    return true;
                }
            	
            	if(((ArmourUpgrade)item.getItem()).isUpgrade())
            	{
            		blood--;
            	}
            }    
        }

        return false;
    }
    
    @Override
    public boolean canSeeLPBar(ItemStack itemStack)
    {
        ItemStack[] inv = getInternalInventory(itemStack);

        if (inv == null)
        {
            return false;
        }
        
        int blood = getMaxBloodShardLevel(itemStack);

        for (ItemStack item : inv)
        {
            if (item == null)
            {
                continue;
            }
            if (item.getItem() instanceof ArmourUpgrade && blood > 0)
            {
            	if (item.getItem() instanceof DivinationSigil)
                {
                    return true;
                }
            	
            	if(((ArmourUpgrade)item.getItem()).isUpgrade())
            	{
            		blood--;
            	}
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

            if (item.getItem().equals(ModItems.weakBloodOrb))
            {
                return 0.75f;
            }

            if (item.getItem().equals(ModItems.apprenticeBloodOrb))
            {
                return 0.50f;
            }

            if (item.getItem().equals(ModItems.magicianBloodOrb))
            {
                return 0.25f;
            }

            if (item.getItem().equals(ModItems.masterBloodOrb))
            {
                return 0.0f;
            }

            if (item.getItem().equals(ModItems.archmageBloodOrb))
            {
                return 0.0f;
            }
        }

        return 1.0f;
    }

    public int getItemEnchantability()
    {
        return Integer.MIN_VALUE;
    }

    public boolean getIsInvisible(ItemStack armourStack)
    {
        NBTTagCompound tag = armourStack.getTagCompound();
        if (tag != null)
        {
            return tag.getBoolean("invisible");
        }

        return false;
    }

    public void setIsInvisible(ItemStack armourStack, boolean invisible)
    {
        NBTTagCompound tag = armourStack.getTagCompound();

        if (tag == null)
        {
            armourStack.setTagCompound(new NBTTagCompound());
            tag = armourStack.getTagCompound();
        }

        tag.setBoolean("invisible", invisible);
    }

    @Override
    @Optional.Method(modid = "Thaumcraft")
    public boolean showNodes(ItemStack itemstack, EntityLivingBase player)
    {
        return this.hasIRevealer(itemstack);
    }

    @Override
    @Optional.Method(modid = "Thaumcraft")
    public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player)
    {
        return this.hasIGoggles(itemstack);
    }

    @Override
    @Optional.Method(modid = "Thaumcraft")
    public int getRunicCharge(ItemStack itemstack)
    {
        ItemStack[] inv = this.getInternalInventory(itemstack);
        int shardLevel = this.getMaxBloodShardLevel(itemstack);
        int count = 0;
        int harden = 0;

        if (inv == null)
        {
            return 0;
        }

        for (ItemStack stack : inv)
        {
            if (count >= shardLevel)
            {
                break;
            }

            if (stack == null || !(stack.getItem() instanceof ArmourUpgrade))
            {
                continue;
            }

            if (stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType != this.armorType)
            {
                continue;
            }

            if (stack.hasTagCompound())
            {
                NBTTagCompound tag = stack.getTagCompound();

                int enchLvl = tag.getByte("RS.HARDEN");

                if (stack.getItem() instanceof IRunicArmor)
                {
                    enchLvl += ((IRunicArmor) stack.getItem()).getRunicCharge(stack);
                }

                if (enchLvl > 0)
                {
                    harden += enchLvl;
                    if (((ArmourUpgrade) stack.getItem()).isUpgrade())
                    {
                        count += 1;
                    }
                }
            }
        }

        return harden;
    }

    @Override
    public boolean showIngameHUD(World world, ItemStack stack, EntityPlayer player)
    {
        ItemStack[] inv = getInternalInventory(stack);

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

            if (item.getItem().equals(ModItems.itemSeerSigil))
            {
                return true;
            }
        }

        return false;
    }
}
