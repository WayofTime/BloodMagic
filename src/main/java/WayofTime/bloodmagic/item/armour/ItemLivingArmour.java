package WayofTime.bloodmagic.item.armour;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import WayofTime.bloodmagic.api.livingArmour.StatTracker;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeElytra;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.PlayerFallDistancePacketProcessor;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.lwjgl.input.Keyboard;

public class ItemLivingArmour extends ItemArmor implements ISpecialArmor, IMeshProvider
{
    private static Field _FLAGS = ReflectionHelper.findField(Entity.class, "FLAGS", "field_184240_ax");

    private static DataParameter<Byte> FLAGS = null;
    public static String[] names = { "helmet", "chest", "legs", "boots" };

    public static final boolean useSpecialArmourCalculation = true;

    //TODO: Save/delete cache periodically.
    public static Map<UUID, LivingArmour> armourMap = new HashMap<UUID, LivingArmour>();

    public ItemLivingArmour(EntityEquipmentSlot armorType)
    {
        super(ItemArmor.ArmorMaterial.IRON, 0, armorType);
        setUnlocalizedName(Constants.Mod.MODID + ".livingArmour.");
//        setMaxDamage(250);
        setMaxDamage((int) (getMaxDamage() * 1.5));
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        if (stack != null && !world.isRemote && stack.getItem() == ModItems.livingArmourChest)
        {
            Utils.setUUID(stack);
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        if (this == ModItems.livingArmourChest || this == ModItems.livingArmourHelmet || this == ModItems.livingArmourBoots)
        {
            return "bloodmagic:models/armor/livingArmour_layer_1.png";
        }

        if (this == ModItems.livingArmourLegs)
        {
            return "bloodmagic:models/armor/livingArmour_layer_2.png";
        } else
        {
            return null;
        }
    }

//    public double getRemainderForDamage(double damage, double plating) //TODO: Add plating, which shifts the damage
//    {
//        if (damage <= 0)
//        {
//            return 1;
//        }
//
//        double protectionAmount = 1 - Math.max(3, 15 - damage / 2) / 25; //This puts the base armour protection at vanilla iron level
//
//        return 0;
//    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return ItemStack.areItemsEqual(repair, ItemComponent.getStack(ItemComponent.REAGENT_BINDING));
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack stack, DamageSource source, double damage, int slot)
    {
        double armourReduction = 0.0;
        double damageAmount = 0.25;

        if (this == ModItems.livingArmourBoots || this == ModItems.livingArmourHelmet)
        {
            damageAmount = 3d / 20d * 0.6;
        } else if (this == ModItems.livingArmourLegs)
        {
            damageAmount = 6d / 20d * 0.6;
        } else if (this == ModItems.livingArmourChest)
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

        if (this == ModItems.livingArmourChest)
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

            if (helmet.getItem() instanceof ItemLivingArmour && leggings.getItem() instanceof ItemLivingArmour && boots.getItem() instanceof ItemLivingArmour)
            {
                double remainder = 1; // Multiply this number by the armour upgrades for protection

                if (hasLivingArmour(stack))
                {
                    LivingArmour armour = getLivingArmour(stack);
                    if (armour != null && isEnabled(stack))
                    {
                        for (Entry<String, LivingArmourUpgrade> entry : armour.upgradeMap.entrySet())
                        {
                            LivingArmourUpgrade upgrade = entry.getValue();
                            remainder *= (1 - upgrade.getArmourProtection(player, source));
                        }
                    }
                }

                armourReduction = armourReduction + (1 - remainder) * (1 - armourReduction);
                damageAmount *= (armourReduction);

//                if (source.isUnblockable())
//                {
//                    return new ArmorProperties(-1, damageAmount * armourPenetrationReduction, maxAbsorption);
//                }

                return new ArmorProperties(-1, source.isUnblockable() ? 1 - remainder : damageAmount, maxAbsorption);
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
        if (armor.getItem() == ModItems.livingArmourHelmet)
        {
            return 3;
        }

        if (armor.getItem() == ModItems.livingArmourChest)
        {
            return 8;
        }

        if (armor.getItem() == ModItems.livingArmourLegs)
        {
            return 6;
        }

        if (armor.getItem() == ModItems.livingArmourBoots)
        {
            return 3;
        }

        return 5;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
    {
        if (this == ModItems.livingArmourChest)
        {
            if (source.isUnblockable())
            {
                return;
            }

            if (damage > this.getMaxDamage(stack) - this.getDamage(stack))
            {
                //TODO: Syphon a load of LP.
                if (entity.worldObj.isRemote && entity instanceof EntityPlayer)
                {
                    EntityPlayer player = (EntityPlayer) entity;
                    SoulNetwork network = NetworkHelper.getSoulNetwork(player);
                    network.syphonAndDamage(player, damage * 100);
                }

                return;
            }

            stack.damageItem(damage, entity);
        } else
        {
            stack.damageItem(damage, entity);
        }

        return; // TODO Armour shouldn't get damaged... for now
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        stack = NBTHelper.checkNBT(stack);

        if (this == ModItems.livingArmourChest)
        {
            LivingArmour armour = getLivingArmourFromStack(stack);
            for (Entry<String, LivingArmourUpgrade> entry : armour.upgradeMap.entrySet())
            {
                LivingArmourUpgrade upgrade = entry.getValue();
                if (upgrade != null)
                {
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_M))
                    {
                        StatTracker tracker = null;
                        for (StatTracker searchTracker : armour.trackerMap.values())
                        {
                            if (searchTracker != null && searchTracker.providesUpgrade(upgrade.getUniqueIdentifier()))
                            {
                                tracker = searchTracker;
                                break;
                            }
                        }

                        if (tracker != null)
                        {
                            double progress = tracker.getProgress(armour, upgrade.getUpgradeLevel());
                            tooltip.add(TextHelper.localize("tooltip.BloodMagic.livingArmour.upgrade.progress", TextHelper.localize(upgrade.getUnlocalizedName()), MathHelper.clamp_int((int) (progress * 100D), 0, 100)));
                        }
                    } else
                    {
                        tooltip.add(TextHelper.localize("tooltip.BloodMagic.livingArmour.upgrade.level", TextHelper.localize(upgrade.getUnlocalizedName()), upgrade.getUpgradeLevel() + 1));
                    }
                }
            }

            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.livingArmour.upgrade.points", armour.totalUpgradePoints, armour.maxUpgradePoints));
        }

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
    {
        super.onArmorTick(world, player, stack);

        if (world.isRemote && this == ModItems.livingArmourChest)
        {
            if (player instanceof EntityPlayerSP) //Sanity check
            {
                EntityPlayerSP spPlayer = (EntityPlayerSP) player;

                if (FLAGS == null)
                {
                    try
                    {
                        FLAGS = (DataParameter<Byte>) _FLAGS.get(null);
                    } catch (IllegalArgumentException e)
                    {
                        e.printStackTrace();
                    } catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                }

                if (FLAGS != null)
                {
                    if (LivingArmour.hasFullSet(player))
                    {
                        ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                        LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgradeFromNBT(Constants.Mod.MODID + ".upgrade.elytra", chestStack);
                        if (upgrade instanceof LivingArmourUpgradeElytra)
                        {
                            if (spPlayer.motionY > -0.5D)
                            {
                                BloodMagicPacketHandler.INSTANCE.sendToServer(new PlayerFallDistancePacketProcessor(1));
                            }

                            if (spPlayer.movementInput.jump && !spPlayer.onGround && spPlayer.motionY < 0.0D && !spPlayer.capabilities.isFlying)
                            {
                                if (spPlayer.motionY > -0.5D)
                                {
                                    BloodMagicPacketHandler.INSTANCE.sendToServer(new PlayerFallDistancePacketProcessor(1));
                                }

                                if (!spPlayer.isElytraFlying())
                                {
                                    byte b0 = player.getDataManager().get(FLAGS);
                                    player.getDataManager().set(FLAGS, (byte) (b0 | 1 << 7));
                                }
                            } else if (spPlayer.isElytraFlying() && !spPlayer.movementInput.jump && !spPlayer.onGround)
                            {
                                byte b0 = player.getDataManager().get(FLAGS);
                                player.getDataManager().set(FLAGS, (byte) (b0 & ~(1 << 7)));
                            }
                        }
                    }
                }
            }

            return;
        }

        if (this == ModItems.livingArmourChest)
        {
            if (!hasLivingArmour(stack))
            {
                setLivingArmour(stack, getLivingArmourFromStack(stack));
            }

            LivingArmour armour = getLivingArmour(stack);
            if (LivingArmour.hasFullSet(player))
            {
                this.setIsEnabled(stack, true);
                armour.onTick(world, player);
            }

            setLivingArmour(stack, armour, false);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
    {
        if (this == ModItems.livingArmourChest && isEnabled(stack) && slot == EntityEquipmentSlot.CHEST)
        {
            LivingArmour armour = ItemLivingArmour.getLivingArmourFromStack(stack);

            return armour.getAttributeModifiers();
        }

        return HashMultimap.<String, AttributeModifier>create();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + names[3 - armorType.getIndex()];
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
                if (stack.getItem() == ModItems.livingArmourHelmet)
                    return new ModelResourceLocation(getCustomLocation(), "armour=head");
                else if (stack.getItem() == ModItems.livingArmourChest)
                    return new ModelResourceLocation(getCustomLocation(), "armour=body");
                else if (stack.getItem() == ModItems.livingArmourLegs)
                    return new ModelResourceLocation(getCustomLocation(), "armour=leg");
                else
                    return new ModelResourceLocation(getCustomLocation(), "armour=feet");
            }
        };
    }

    @Override
    public ResourceLocation getCustomLocation()
    {
        return new ResourceLocation(Constants.Mod.MODID, "item/ItemLivingArmour");
    }

    @Override
    public List<String> getVariants()
    {
        List<String> ret = new ArrayList<String>();
        ret.add("armour=head");
        ret.add("armour=body");
        ret.add("armour=leg");
        ret.add("armour=feet");
        return ret;
    }

    public static LivingArmour getLivingArmourFromStack(ItemStack stack)
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

    public static NBTTagCompound getArmourTag(ItemStack stack)
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

    //TODO: Add the ability to have the armour give an upgrade with a higher level
    public static LivingArmourUpgrade getUpgrade(String uniqueIdentifier, ItemStack stack)
    {
        if (!hasLivingArmour(stack))
        {
            setLivingArmour(stack, getLivingArmourFromStack(stack));
        }

        LivingArmour armour = getLivingArmour(stack);

        for (Entry<String, LivingArmourUpgrade> entry : armour.upgradeMap.entrySet())
        {
            if (entry.getKey().equals(uniqueIdentifier))
            {
                return entry.getValue();
            }
        }

        return null;
    }

    public static LivingArmourUpgrade getUpgradeFromNBT(String uniqueIdentifier, ItemStack stack)
    {
        LivingArmour armour = getLivingArmourFromStack(stack);

        for (Entry<String, LivingArmourUpgrade> entry : armour.upgradeMap.entrySet())
        {
            if (entry.getKey().equals(uniqueIdentifier))
            {
                return entry.getValue();
            }
        }

        return null;
    }

    public static boolean hasLivingArmour(ItemStack stack)
    {
        UUID uuid = Utils.getUUID(stack);
        return uuid != null && armourMap.containsKey(uuid);
    }

    public static LivingArmour getLivingArmour(ItemStack stack)
    {
        UUID uuid = Utils.getUUID(stack);

        return armourMap.get(uuid);
    }

    public static void setLivingArmour(ItemStack stack, LivingArmour armour)
    {
        if (!Utils.hasUUID(stack))
        {
            Utils.setUUID(stack);
        }

        UUID uuid = Utils.getUUID(stack);

        armourMap.put(uuid, armour);
    }

    public static boolean hasUpgrade(String id, ItemStack stack)
    {
        if (!hasLivingArmour(stack))
        {
            setLivingArmour(stack, getLivingArmourFromStack(stack));
        }

        LivingArmour armour = getLivingArmour(stack);

        return armour.upgradeMap.containsKey(id);
    }

    public void setIsEnabled(ItemStack stack, boolean bool)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        tag.setBoolean("enabled", bool);
    }

    public boolean isEnabled(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        return tag.getBoolean("enabled");
    }

    public void setIsElytra(ItemStack stack, boolean bool)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        tag.setBoolean("elytra", bool);
    }

    public boolean isElytra(ItemStack stack)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        return tag.getBoolean("elytra");
    }
}
