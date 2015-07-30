package WayofTime.alchemicalWizardry.common.items.potion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyPotionHelper;

import com.google.common.collect.HashMultimap;

public class AlchemyFlask extends Item
{
    public AlchemyFlask()
    {
        super();
        this.setMaxDamage(8);
        this.setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    public static ArrayList<AlchemyPotionHelper> getEffects(ItemStack par1ItemStack)
    {
        if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("CustomFlaskEffects"))
        {
            ArrayList<AlchemyPotionHelper> arraylist = new ArrayList();
            NBTTagList nbttaglist = par1ItemStack.getTagCompound().getTagList("CustomFlaskEffects", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                arraylist.add(AlchemyPotionHelper.readEffectFromNBT(nbttagcompound));
            }
            return arraylist;
        } else
        {
            return null;
        }
    }

    public static ArrayList<PotionEffect> getPotionEffects(ItemStack par1ItemStack)
    {
        ArrayList<AlchemyPotionHelper> list = AlchemyFlask.getEffects(par1ItemStack);

        if (list != null)
        {
            ArrayList<PotionEffect> newList = new ArrayList();

            for (AlchemyPotionHelper aph : list)
            {
                newList.add(aph.getPotionEffect());
            }

            return newList;
        } else
        {
            return null;
        }
    }

    public static void setEffects(ItemStack par1ItemStack, List<AlchemyPotionHelper> list)
    {
        NBTTagCompound itemTag = par1ItemStack.getTagCompound();

        if (itemTag == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagList nbttaglist = new NBTTagList();

        for (AlchemyPotionHelper aph : list)
        {
            nbttaglist.appendTag(AlchemyPotionHelper.setEffectToNBT(aph));
        }

        par1ItemStack.getTagCompound().setTag("CustomFlaskEffects", nbttaglist);
    }

    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() + 1);
        }

        if (!par2World.isRemote)
        {
            ArrayList<AlchemyPotionHelper> list = getEffects(par1ItemStack);

            if (list != null)
            {
                for (AlchemyPotionHelper aph : list)
                {
                    PotionEffect pe = aph.getPotionEffect();

                    if (pe != null)
                    {
                        //if(pe.get)
                        par3EntityPlayer.addPotionEffect(pe);
                    }
                }
            }
        }

        return par1ItemStack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        if (this.isPotionThrowable(par1ItemStack))
        {
            return EnumAction.NONE;
        }

        return EnumAction.DRINK;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par1ItemStack.getItemDamage() < par1ItemStack.getMaxDamage())
        {
            if (this.isPotionThrowable(par1ItemStack))
            {
                if (!par2World.isRemote)
                {
                    EntityPotion entityPotion = this.getEntityPotion(par1ItemStack, par2World, par3EntityPlayer);

                    if (entityPotion != null)
                    {
                        float velocityChange = 2.0f;
                        entityPotion.motionX *= velocityChange;
                        entityPotion.motionY *= velocityChange;
                        entityPotion.motionZ *= velocityChange;
                        par2World.spawnEntityInWorld(entityPotion);
                        par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() + 1);
                    }
                }

                return par1ItemStack;
            }

            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        }
        return par1ItemStack;
    }

    public void setConcentrationOfPotion(ItemStack par1ItemStack, int potionID, int concentration)
    {
        ArrayList<AlchemyPotionHelper> list = getEffects(par1ItemStack);
        if (list != null)
        {
            for (AlchemyPotionHelper aph : list)
            {
                if (aph.getPotionID() == potionID)
                {
                    aph.setConcentration(concentration);
                    break;
                }
            }
            setEffects(par1ItemStack, list);
        }
    }

    public void setDurationFactorOfPotion(ItemStack par1ItemStack, int potionID, int durationFactor)
    {
        ArrayList<AlchemyPotionHelper> list = getEffects(par1ItemStack);
        if (list != null)
        {
            for (AlchemyPotionHelper aph : list)
            {
                if (aph.getPotionID() == potionID)
                {
                    aph.setDurationFactor(durationFactor);
                    break;
                }
            }
            setEffects(par1ItemStack, list);
        }
    }

    public boolean hasPotionEffect(ItemStack par1ItemStack, int potionID)
    {
        return false;
    }

    public int getNumberOfPotionEffects(ItemStack par1ItemStack)
    {
        if (getEffects(par1ItemStack) != null)
        {
            return getEffects(par1ItemStack).size();
        } else
        {
            return 0;
        }
    }

    public boolean addPotionEffect(ItemStack par1ItemStack, int potionID, int tickDuration)
    {
        ArrayList<AlchemyPotionHelper> list = getEffects(par1ItemStack);
        if (list != null)
        {
            for (AlchemyPotionHelper aph : list)
            {
                if (aph.getPotionID() == potionID)
                {
                    return false;
                }
            }
            list.add(new AlchemyPotionHelper(potionID, tickDuration, 0, 0));
            setEffects(par1ItemStack, list);
            return true;
        } else
        {
            list = new ArrayList();
            list.add(new AlchemyPotionHelper(potionID, tickDuration, 0, 0));
            setEffects(par1ItemStack, list);
            return true;
        }
    }

    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("tooltip.alchemyflask.swigsleft") + " " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()) + "/" + par1ItemStack.getMaxDamage());

        if (this.isPotionThrowable(par1ItemStack))
        {
            par3List.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("tooltip.alchemyflask.caution"));
        }

        List list1 = AlchemyFlask.getPotionEffects(par1ItemStack);
        HashMultimap hashmultimap = HashMultimap.create();
        Iterator iterator;

        if (list1 != null && !list1.isEmpty())
        {
            iterator = list1.iterator();

            while (iterator.hasNext())
            {
                PotionEffect potioneffect = (PotionEffect) iterator.next();
                String s = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                Map map = potion.getAttributeModifierMap();

                if (map != null && map.size() > 0)
                {
                    Iterator iterator1 = map.entrySet().iterator();

                    while (iterator1.hasNext())
                    {
                        Entry entry = (Entry) iterator1.next();
                        AttributeModifier attributemodifier = (AttributeModifier) entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        hashmultimap.put(((IAttribute) entry.getKey()).getAttributeUnlocalizedName(), attributemodifier1);
                    }
                }

                if (potioneffect.getAmplifier() > 0)
                {
                    s = s + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
                }

                if (potioneffect.getDuration() > 20)
                {
                    s = s + " (" + Potion.getDurationString(potioneffect) + ")";
                }

                if (potion.isBadEffect())
                {
                    par3List.add(EnumChatFormatting.RED + s);
                } else
                {
                    par3List.add(EnumChatFormatting.GRAY + s);
                }
            }
        } else
        {
            String s1 = StatCollector.translateToLocal("potion.empty").trim();
            par3List.add(EnumChatFormatting.GRAY + s1);
        }

        if (!hashmultimap.isEmpty())
        {
            par3List.add("");
            par3List.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
            iterator = hashmultimap.entries().iterator();

            while (iterator.hasNext())
            {
                Entry entry1 = (Entry) iterator.next();
                AttributeModifier attributemodifier2 = (AttributeModifier) entry1.getValue();
                double d0 = attributemodifier2.getAmount();
                double d1;

                if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2)
                {
                    d1 = attributemodifier2.getAmount();
                } else
                {
                    d1 = attributemodifier2.getAmount() * 100.0D;
                }

                if (d0 > 0.0D)
                {
                    par3List.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), new Object[]{ItemStack.DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String) entry1.getKey())}));
                } else if (d0 < 0.0D)
                {
                    d1 *= -1.0D;
                    par3List.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), new Object[]{ItemStack.DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String) entry1.getKey())}));
                }
            }
        }
    }

    public boolean isPotionThrowable(ItemStack par1ItemStack)
    {
        return par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().getBoolean("throwable");
    }

    public void setIsPotionThrowable(boolean flag, ItemStack par1ItemStack)
    {
        if (!par1ItemStack.hasTagCompound())
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.getTagCompound().setBoolean("throwable", flag);
    }

    public EntityPotion getEntityPotion(ItemStack par1ItemStack, World worldObj, EntityLivingBase entityLivingBase)
    {
        ItemStack potionStack = new ItemStack(Items.potionitem, 1, 0);
        potionStack.setTagCompound(new NBTTagCompound());
        ArrayList<PotionEffect> potionList = getPotionEffects(par1ItemStack);

        if (potionList == null)
        {
            return null;
        }
        NBTTagList nbttaglist = new NBTTagList();
        for (PotionEffect pe : potionList)
        {
            NBTTagCompound d = new NBTTagCompound();
            d.setByte("Id", (byte) pe.getPotionID());
            d.setByte("Amplifier", (byte) pe.getAmplifier());
            d.setInteger("Duration", pe.getDuration());
            d.setBoolean("Ambient", pe.getIsAmbient());
            nbttaglist.appendTag(d);
        }
        potionStack.getTagCompound().setTag("CustomPotionEffects", nbttaglist);
        return new EntityPotion(worldObj, entityLivingBase, potionStack);
    }
}
