package WayofTime.bloodmagic.item.armour;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.iface.IMultiWillTool;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.registry.ModItems;

public class ItemSentientArmour extends ItemArmor implements ISpecialArmor, IMeshProvider, IMultiWillTool
{
    public static String[] names = { "helmet", "chest", "legs", "boots" };

    public static double[] willBracket = new double[] { 30, 200, 600, 1500, 4000, 6000, 8000, 16000 };
    public static double[] consumptionPerHit = new double[] { 0.1, 0.12, 0.15, 0.2, 0.3, 0.35, 0.4, 0.5 };
    public static double[] extraProtectionLevel = new double[] { 0, 0.25, 0.5, 0.6, 0.7, 0.75, 0.85, 0.9 };

    public static double[] healthBonus = new double[] { 3, 6, 9, 12, 15, 20, 25 };

    public ItemSentientArmour(EntityEquipmentSlot armorType)
    {
        super(ItemArmor.ArmorMaterial.IRON, 0, armorType);
        setUnlocalizedName(Constants.Mod.MODID + ".sentientArmour.");
        setMaxDamage(250);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        if (this == ModItems.sentientArmourChest || this == ModItems.sentientArmourHelmet || this == ModItems.sentientArmourBoots)
        {
            switch (this.getCurrentType(stack))
            {
            case DEFAULT:
                return "bloodmagic:models/armor/sentientArmour_layer_1.png";
            case CORROSIVE:
                return "bloodmagic:models/armor/sentientArmour_corrosive_layer_1.png";
            case VENGEFUL:
                return "bloodmagic:models/armor/sentientArmour_vengeful_layer_1.png";
            case DESTRUCTIVE:
                return "bloodmagic:models/armor/sentientArmour_destructive_layer_1.png";
            case STEADFAST:
                return "bloodmagic:models/armor/sentientArmour_steadfast_layer_1.png";
            }
            return "bloodmagic:models/armor/sentientArmour_layer_1.png";
        }

        if (this == ModItems.sentientArmourLegs)
        {
            switch (this.getCurrentType(stack))
            {
            case DEFAULT:
                return "bloodmagic:models/armor/sentientArmour_layer_2.png";
            case CORROSIVE:
                return "bloodmagic:models/armor/sentientArmour_corrosive_layer_2.png";
            case VENGEFUL:
                return "bloodmagic:models/armor/sentientArmour_vengeful_layer_2.png";
            case DESTRUCTIVE:
                return "bloodmagic:models/armor/sentientArmour_destructive_layer_2.png";
            case STEADFAST:
                return "bloodmagic:models/armor/sentientArmour_steadfast_layer_2.png";
            }
            return "bloodmagic:models/armor/sentientArmour_layer_1.png";
        } else
        {
            return null;
        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
    {
        if (this.armorType == EntityEquipmentSlot.CHEST)
        {
            EnumDemonWillType type = this.getCurrentType(stack);
            switch (type)
            {
            case CORROSIVE:
                if (player.isPotionActive(MobEffects.poison))
                {
                    player.removeActivePotionEffect(MobEffects.poison);
                }
                if (player.isPotionActive(MobEffects.wither))
                {
                    player.removeActivePotionEffect(MobEffects.wither);
                }
                break;
            default:
            }
        }
    }

    public void onPlayerAttacked(ItemStack stack, DamageSource source, EntityPlayer attackedPlayer)
    {
        if (source.getEntity() instanceof EntityLivingBase)
        {
            EntityLivingBase attacker = (EntityLivingBase) source.getEntity();
            EnumDemonWillType type = this.getCurrentType(stack);
            switch (type)
            {
            case CORROSIVE:
                break;
            case DEFAULT:
                break;
            case DESTRUCTIVE:
                break;
            case STEADFAST:
                break;
            case VENGEFUL:
                break;
            }
        }
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack stack, DamageSource source, double damage, int slot)
    {
        double armourReduction = 0.0;
        double damageAmount = 0.25;

        if (this == ModItems.sentientArmourBoots || this == ModItems.sentientArmourHelmet)
        {
            damageAmount = 3d / 20d * 0.6;
        } else if (this == ModItems.sentientArmourLegs)
        {
            damageAmount = 6d / 20d * 0.6;
        } else if (this == ModItems.sentientArmourChest)
        {
            damageAmount = 0.64;
        }

        double armourPenetrationReduction = 0;

        int maxAbsorption = 100000;

        if (source.equals(DamageSource.drown))
        {
            return new ArmorProperties(-1, 0, 0);
        }

        if (source.equals(DamageSource.outOfWorld))
        {
            return new ArmorProperties(-1, 0, 0);
        }

        if (this == ModItems.sentientArmourChest)
        {
            armourReduction = 0.24 / 0.64; // This values puts it at iron level

            ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

            if (helmet == null || leggings == null || boots == null)
            {
                damageAmount *= (armourReduction);

                return new ArmorProperties(-1, damageAmount, maxAbsorption);
            }

            if (helmet.getItem() instanceof ItemSentientArmour && leggings.getItem() instanceof ItemSentientArmour && boots.getItem() instanceof ItemSentientArmour)
            {
                double remainder = 1; // Multiply this number by the armour upgrades for protection
                remainder *= (1 - this.getArmourModifier(stack));

                armourReduction = armourReduction + (1 - remainder) * (1 - armourReduction);
                damageAmount *= (armourReduction);

                if (source.isUnblockable())
                {
                    return new ArmorProperties(-1, damageAmount * armourPenetrationReduction, maxAbsorption);
                }

                return new ArmorProperties(-1, damageAmount, maxAbsorption);
            }
        } else
        {
            if (source.isUnblockable())
            {
                return new ArmorProperties(-1, damageAmount * armourPenetrationReduction, maxAbsorption);
            }

            return new ArmorProperties(-1, damageAmount, maxAbsorption);
        }

        return new ArmorProperties(-1, 0, 0);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
    {
        if (armor.getItem() == ModItems.sentientArmourHelmet)
        {
            return 3;
        }

        if (armor.getItem() == ModItems.sentientArmourChest)
        {
            return 8;
        }

        if (armor.getItem() == ModItems.sentientArmourLegs)
        {
            return 6;
        }

        if (armor.getItem() == ModItems.sentientArmourBoots)
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
            EntityPlayer player = (EntityPlayer) entity;

            EnumDemonWillType type = getCurrentType(stack);

            double willRequired = this.getCostModifier(stack) * damage;
            double willLeft = PlayerDemonWillHandler.getTotalDemonWill(type, player);
            if (willLeft >= willRequired && canSustainArmour(type, willLeft))
            {
                this.setAbilitiesOfArmour(type, willLeft - willRequired, stack);
                PlayerDemonWillHandler.consumeDemonWill(type, player, willRequired);
            } else
            {
                this.revertArmour(player, stack);
            }
        }
    }

    public double getCostModifier(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        return tag.getDouble("costModifier");
    }

    public void setCostModifier(ItemStack stack, double modifier)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble("costModifier", modifier);
    }

    public double getArmourModifier(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        return tag.getDouble("armourModifier");
    }

    public void setArmourModifier(ItemStack stack, double modifier)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble("armourModifier", modifier);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names[3 - armorType.getIndex()];
    }

    public void revertArmour(EntityPlayer player, ItemStack itemStack)
    {
        ItemStack stack = this.getContainedArmourStack(itemStack);
        player.setItemStackToSlot(armorType, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition()
    {
        return new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                assert getCustomLocation() != null;
                EnumDemonWillType type = ((ItemSentientArmour) ModItems.sentientArmourHelmet).getCurrentType(stack);
                String additional = "_" + type.getName().toLowerCase();
                if (stack.getItem() == ModItems.sentientArmourHelmet)
                    return new ModelResourceLocation(getCustomLocation(), "armour=head" + additional);
                else if (stack.getItem() == ModItems.sentientArmourChest)
                    return new ModelResourceLocation(getCustomLocation(), "armour=body" + additional);
                else if (stack.getItem() == ModItems.sentientArmourLegs)
                    return new ModelResourceLocation(getCustomLocation(), "armour=leg" + additional);
                else
                    return new ModelResourceLocation(getCustomLocation(), "armour=feet" + additional);
            }
        };
    }

    @Override
    public ResourceLocation getCustomLocation()
    {
        return new ResourceLocation(Constants.Mod.MODID, "item/ItemSentientArmour");
    }

    @Override
    public List<String> getVariants()
    {
        List<String> ret = new ArrayList<String>();
        for (EnumDemonWillType type : EnumDemonWillType.values())
        {
            String additional = "_" + type.getName().toLowerCase();

            ret.add("armour=head" + additional);
            ret.add("armour=body" + additional);
            ret.add("armour=leg" + additional);
            ret.add("armour=feet" + additional);
        }

        return ret;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
        if (slot == EntityEquipmentSlot.CHEST)
        {
            multimap.put(SharedMonsterAttributes.MAX_HEALTH.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(0, 318145), "Armor modifier", this.getHealthBonus(stack), 0));
        }
        return multimap;
    }

    public static void revertAllArmour(EntityPlayer player)
    {
        ItemStack[] armourInventory = player.inventory.armorInventory;
        for (ItemStack stack : armourInventory)
        {
            if (stack != null && stack.getItem() instanceof ItemSentientArmour)
            {
                ((ItemSentientArmour) stack.getItem()).revertArmour(player, stack);
            }
        }
    }

    public void setContainedArmourStack(ItemStack newArmour, ItemStack previousArmour)
    {
        if (newArmour == null || previousArmour == null)
        {
            return;
        }

        NBTTagCompound tag = new NBTTagCompound();
        previousArmour.writeToNBT(tag);

        NBTTagCompound omegaTag = newArmour.getTagCompound();
        if (omegaTag == null)
        {
            omegaTag = new NBTTagCompound();
            newArmour.setTagCompound(omegaTag);
        }

        omegaTag.setTag("armour", tag);
        Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(previousArmour);
        EnchantmentHelper.setEnchantments(enchantmentMap, newArmour);
    }

    public ItemStack getContainedArmourStack(ItemStack newArmour)
    {
        NBTTagCompound omegaTag = newArmour.getTagCompound();
        if (omegaTag == null)
        {
            return null;
        }

        NBTTagCompound tag = omegaTag.getCompoundTag("armour");
        ItemStack armourStack = ItemStack.loadItemStackFromNBT(tag);

        return armourStack;
    }

    public static boolean convertPlayerArmour(EnumDemonWillType type, double will, EntityPlayer player)
    {
        if (!canSustainArmour(type, will))
        {
            return false;
        }

        ItemStack helmetStack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack leggingsStack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack bootsStack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        {
            ItemStack omegaHelmetStack = ((ItemSentientArmour) ModItems.sentientArmourHelmet).getSubstituteStack(type, will, helmetStack);
            ItemStack omegaChestStack = ((ItemSentientArmour) ModItems.sentientArmourChest).getSubstituteStack(type, will, chestStack);
            ItemStack omegaLeggingsStack = ((ItemSentientArmour) ModItems.sentientArmourLegs).getSubstituteStack(type, will, leggingsStack);
            ItemStack omegaBootsStack = ((ItemSentientArmour) ModItems.sentientArmourBoots).getSubstituteStack(type, will, bootsStack);

            player.setItemStackToSlot(EntityEquipmentSlot.HEAD, omegaHelmetStack);
            player.setItemStackToSlot(EntityEquipmentSlot.CHEST, omegaChestStack);
            player.setItemStackToSlot(EntityEquipmentSlot.LEGS, omegaLeggingsStack);
            player.setItemStackToSlot(EntityEquipmentSlot.FEET, omegaBootsStack);

            return true;
        }
    }

    public ItemStack getSubstituteStack(EnumDemonWillType type, double will, ItemStack previousArmour)
    {
        ItemStack newArmour = new ItemStack(this);

        this.setContainedArmourStack(newArmour, previousArmour);
        this.setAbilitiesOfArmour(type, will, newArmour);

        return newArmour;
    }

    @Override
    public EnumDemonWillType getCurrentType(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        if (!tag.hasKey(Constants.NBT.WILL_TYPE))
        {
            return EnumDemonWillType.DEFAULT;
        }

        return EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE));
    }

    public void setCurrentType(EnumDemonWillType type, ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setString(Constants.NBT.WILL_TYPE, type.toString());
    }

    public void setAbilitiesOfArmour(EnumDemonWillType type, double willValue, ItemStack armourStack)
    {
        int willBracket = getWillBracket(willValue);
        if (willBracket >= 0)
        {
            double recurringCost = consumptionPerHit[willBracket];

            this.setCostModifier(armourStack, recurringCost);
            this.setCurrentType(type, armourStack);

            if (this.armorType == EntityEquipmentSlot.CHEST)
            {
                this.setArmourModifier(armourStack, getArmourModifier(type, willBracket));
                this.setHealthBonus(armourStack, this.getHealthModifier(type, willBracket));
            }
        }
    }

    public double getArmourModifier(EnumDemonWillType type, int willBracket)
    {
        switch (type)
        {
        case STEADFAST:
        default:
            return extraProtectionLevel[willBracket];
        }
    }

    public double getHealthModifier(EnumDemonWillType type, int willBracket)
    {
        switch (type)
        {
        case STEADFAST:
            return healthBonus[willBracket];
        default:
            return 0;
        }
    }

    public static boolean canSustainArmour(EnumDemonWillType type, double willValue)
    {
        return getWillBracket(willValue) >= 0;
    }

    public static int getWillBracket(double will)
    {
        int bracket = -1;

        for (int i = 0; i < willBracket.length; i++)
        {
            if (will >= willBracket[i])
            {
                bracket = i;
            }
        }

        return bracket;
    }

    public double getHealthBonus(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getDouble(Constants.NBT.SOUL_SWORD_HEALTH);
    }

    public void setHealthBonus(ItemStack stack, double hp)
    {
        NBTHelper.checkNBT(stack);

        NBTTagCompound tag = stack.getTagCompound();

        tag.setDouble(Constants.NBT.SOUL_SWORD_HEALTH, hp);
    }
}
