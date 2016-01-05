package WayofTime.bloodmagic.item.armour;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.collect.Multimap;

public class ItemLivingArmour extends ItemArmor
{
    public static String[] names = { "helmet", "chest", "legs", "boots" };

    //TODO: Save/delete cache periodically.
    public static Map<ItemStack, LivingArmour> armourMap = new HashMap<ItemStack, LivingArmour>();

    public ItemLivingArmour(int armorType)
    {
        super(ItemArmor.ArmorMaterial.IRON, 0, armorType);
        setUnlocalizedName(Constants.Mod.MODID + ".livingArmour.");
        setMaxDamage(250);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        if (this == ModItems.livingArmourChest)
        {
            LivingArmour armour = this.getLivingArmour(stack);
            for (Entry<String, LivingArmourUpgrade> entry : armour.upgradeMap.entrySet())
            {
                LivingArmourUpgrade upgrade = entry.getValue();
                if (upgrade != null)
                {
                    tooltip.add(TextHelper.localize(upgrade.getUnlocalizedName()) + " " + TextHelper.localize("tooltip.BloodMagic.livingArmour.upgrade.level", (upgrade.getUpgradeLevel() + 1)));
                }
            }
        }

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        if (this == ModItems.livingArmourChest || this == ModItems.livingArmourHelmet || this == ModItems.livingArmourBoots)
        {
            return "bloodmagic:models/armor/boundArmour_layer_1.png";
        }

        if (this == ModItems.livingArmourLegs)
        {
            return "bloodmagic:models/armor/boundArmour_layer_2.png";
        } else
        {
            return null;
        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
    {
        super.onArmorTick(world, player, stack);

        if (world.isRemote)
        {
            return;
        }

        if (this == ModItems.livingArmourChest)
        {
            if (!armourMap.containsKey(stack))
            {
                armourMap.put(stack, getLivingArmour(stack));
            }

            LivingArmour armour = armourMap.get(stack);
            armour.onTick(world, player);
            setLivingArmour(stack, armour, false);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack stack)
    {
        if (this == ModItems.livingArmourChest)
        {
            LivingArmour armour = getLivingArmour(stack);

            return armour.getAttributeModifiers();
        }

        return super.getAttributeModifiers(stack);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names[armorType];
    }

    public LivingArmour getLivingArmour(ItemStack stack)
    {
        NBTTagCompound livingTag = getArmourTag(stack);

        LivingArmour livingArmour = new LivingArmour();
        livingArmour.readFromNBT(livingTag);

        return livingArmour;
    }

    public void setLivingArmour(ItemStack stack, LivingArmour armour, boolean forceWrite)
    {
        NBTTagCompound livingTag = new NBTTagCompound();

        if (!forceWrite)
        {
            livingTag = getArmourTag(stack);
            armour.writeDirtyToNBT(livingTag);
        } else
        {
            armour.writeToNBT(livingTag);
        }

        setArmourTag(stack, livingTag);
    }

    public NBTTagCompound getArmourTag(ItemStack stack)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getCompoundTag(Constants.NBT.LIVING_ARMOUR);
    }

    public void setArmourTag(ItemStack stack, NBTTagCompound livingTag)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        tag.setTag(Constants.NBT.LIVING_ARMOUR, livingTag);
    }

    public LivingArmourUpgrade getUpgrade(String uniqueIdentifier, ItemStack stack)
    {
        if (!armourMap.containsKey(stack))
        {
            armourMap.put(stack, getLivingArmour(stack));
        }

        LivingArmour armour = armourMap.get(stack);

        for (Entry<String, LivingArmourUpgrade> entry : armour.upgradeMap.entrySet())
        {
            if (entry.getKey().equals(uniqueIdentifier))
            {
                return entry.getValue();
            }
        }

        return null;
    }
}
