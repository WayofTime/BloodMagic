package WayofTime.bloodmagic.item.armour;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IMeshProvider;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.item.types.ComponentTypes;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.livingArmour.StatTracker;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerRepairing;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeElytra;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.PlayerFallDistancePacketProcessor;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
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
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemLivingArmour extends ItemArmor implements ISpecialArmor, IMeshProvider {
    public static final boolean useSpecialArmourCalculation = true;
    public static String[] names = {"helmet", "chest", "legs", "boots"};
    //TODO: Save/delete cache periodically.
    public static Map<UUID, LivingArmour> armourMap = new HashMap<>();
    private static Field _FLAGS = ReflectionHelper.findField(Entity.class, "FLAGS", "field_184240_ax");
    private static DataParameter<Byte> FLAGS = null;

    public ItemLivingArmour(EntityEquipmentSlot armorType) {
        super(ItemArmor.ArmorMaterial.IRON, 0, armorType);
        setUnlocalizedName(BloodMagic.MODID + ".livingArmour.");
//        setMaxDamage(250);
        setMaxDamage((int) (getMaxDamage() * 1.5));
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        if (stack != null && !world.isRemote && stack.getItem() == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST) {
            Utils.setUUID(stack);
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (this == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST || this == RegistrarBloodMagicItems.LIVING_ARMOUR_HELMET || this == RegistrarBloodMagicItems.LIVING_ARMOUR_BOOTS) {
            return "bloodmagic:models/armor/livingArmour_layer_1.png";
        }

        if (this == RegistrarBloodMagicItems.LIVING_ARMOUR_LEGGINGS) {
            return "bloodmagic:models/armor/livingArmour_layer_2.png";
        } else {
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
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return ItemStack.areItemsEqual(repair, ComponentTypes.REAGENT_BINDING.getStack());
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack stack, DamageSource source, double damage, int slot) {
        double armourReduction = 0.0;
        double damageAmount = 0.25;

        if (this == RegistrarBloodMagicItems.LIVING_ARMOUR_BOOTS || this == RegistrarBloodMagicItems.LIVING_ARMOUR_HELMET) {
            damageAmount = 3d / 20d * 0.6;
        } else if (this == RegistrarBloodMagicItems.LIVING_ARMOUR_LEGGINGS) {
            damageAmount = 6d / 20d * 0.6;
        } else if (this == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST) {
            damageAmount = 0.64;
        }

        double armourPenetrationReduction = 0;

        int maxAbsorption = 100000;

        if (source.equals(DamageSource.DROWN)) {
            return new ArmorProperties(-1, 0, 0);
        }

        if (source.equals(DamageSource.OUT_OF_WORLD)) {
            return new ArmorProperties(-1, 0, 0);
        }

        if (this == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST) {
            armourReduction = 0.24 / 0.64; // This values puts it at iron level

            ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

            if (helmet.isEmpty() || leggings.isEmpty() || boots.isEmpty()) {
                damageAmount *= (armourReduction);

                return new ArmorProperties(-1, damageAmount, maxAbsorption);
            }

            if (helmet.getItem() instanceof ItemLivingArmour && leggings.getItem() instanceof ItemLivingArmour && boots.getItem() instanceof ItemLivingArmour) {
                double remainder = 1; // Multiply this number by the armour upgrades for protection

                if (hasLivingArmour(stack)) {
                    LivingArmour armour = getLivingArmour(stack);
                    if (armour != null && isEnabled(stack)) {
                        for (Entry<String, LivingArmourUpgrade> entry : armour.upgradeMap.entrySet()) {
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
        } else {
            if (source.isUnblockable()) {
                return new ArmorProperties(-1, damageAmount * armourPenetrationReduction, maxAbsorption);
            }

            return new ArmorProperties(-1, damageAmount, maxAbsorption);
        }

        return new ArmorProperties(-1, 0, 0);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOUR_HELMET) {
            return 3;
        }

        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST) {
            return 8;
        }

        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOUR_LEGGINGS) {
            return 6;
        }

        if (armor.getItem() == RegistrarBloodMagicItems.LIVING_ARMOUR_BOOTS) {
            return 3;
        }

        return 5;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        if (this == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST) {
            int preDamage = stack.getItemDamage();
            if (source.isUnblockable()) {
                return;
            }

            if (damage > this.getMaxDamage(stack) - this.getDamage(stack)) {
                //TODO: Syphon a load of LP.
                if (entity.getEntityWorld().isRemote && entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;
                    SoulNetwork network = NetworkHelper.getSoulNetwork(player);
                    network.syphonAndDamage(player, damage * 100);
                }

                return;
            }

            stack.damageItem(damage, entity);

            int receivedDamage = stack.getItemDamage() - preDamage;
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (LivingArmour.hasFullSet(player)) {
                    LivingArmour armour = ItemLivingArmour.getLivingArmour(stack);
                    if (armour != null) {
                        StatTrackerRepairing.incrementCounter(armour, receivedDamage);
                    }
                }
            }

        } else {
            stack.damageItem(damage, entity);
        }

        return; // TODO Armour shouldn't get damaged... for now
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;

        if (this == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST) {
            LivingArmour armour = getLivingArmourFromStack(stack);
            for (Entry<String, LivingArmourUpgrade> entry : armour.upgradeMap.entrySet()) {
                LivingArmourUpgrade upgrade = entry.getValue();
                if (upgrade != null) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_M)) {
                        StatTracker tracker = null;
                        for (StatTracker searchTracker : armour.trackerMap.values()) {
                            if (searchTracker != null && searchTracker.providesUpgrade(upgrade.getUniqueIdentifier())) {
                                tracker = searchTracker;
                                break;
                            }
                        }

                        if (tracker != null) {
                            double progress = tracker.getProgress(armour, upgrade.getUpgradeLevel());
                            tooltip.add(TextHelper.localize("tooltip.bloodmagic.livingArmour.upgrade.progress", TextHelper.localize(upgrade.getUnlocalizedName()), MathHelper.clamp((int) (progress * 100D), 0, 100)));
                        }
                    } else {
                        tooltip.add(TextHelper.localize("tooltip.bloodmagic.livingArmour.upgrade.level", TextHelper.localize(upgrade.getUnlocalizedName()), upgrade.getUpgradeLevel() + 1));
                    }
                }
            }

            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.livingArmour.upgrade.points", armour.totalUpgradePoints, armour.maxUpgradePoints));
            if (!(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_M))) {
                tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.livingArmour.extraExtraInfo"));
            }
        }

        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        super.onArmorTick(world, player, stack);

        if (world.isRemote && this == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST) {
            if (player instanceof EntityPlayerSP) //Sanity check
            {
                EntityPlayerSP spPlayer = (EntityPlayerSP) player;

                if (FLAGS == null) {
                    try {
                        FLAGS = (DataParameter<Byte>) _FLAGS.get(null);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                if (FLAGS != null) {
                    if (LivingArmour.hasFullSet(player)) {
                        ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                        LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgradeFromNBT(BloodMagic.MODID + ".upgrade.elytra", chestStack);
                        if (upgrade instanceof LivingArmourUpgradeElytra) {
                            if (spPlayer.movementInput.jump && !spPlayer.onGround && spPlayer.motionY < 0.0D && !spPlayer.capabilities.isFlying) {
                                if (spPlayer.motionY > -0.5D) {
                                    BloodMagicPacketHandler.INSTANCE.sendToServer(new PlayerFallDistancePacketProcessor(1));
                                }

                                if (!spPlayer.isElytraFlying()) {
                                    byte b0 = player.getDataManager().get(FLAGS);
                                    player.getDataManager().set(FLAGS, (byte) (b0 | 1 << 7));
                                }
                            } else if (spPlayer.isElytraFlying() && !spPlayer.movementInput.jump && !spPlayer.onGround) {
                                byte b0 = player.getDataManager().get(FLAGS);
                                player.getDataManager().set(FLAGS, (byte) (b0 & ~(1 << 7)));
                            }
                        }
                    }
                }
            }
        }

        if (this == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST) {
            if (!hasLivingArmour(stack)) {
                setLivingArmour(stack, getLivingArmourFromStack(stack));
            }

            LivingArmour armour = getLivingArmour(stack);
            if (LivingArmour.hasFullSet(player)) {
                this.setIsEnabled(stack, true);
                armour.onTick(world, player);
            }

            setLivingArmour(stack, armour, false);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        if (this == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST && isEnabled(stack) && slot == EntityEquipmentSlot.CHEST) {
            LivingArmour armour = ItemLivingArmour.getLivingArmourFromStack(stack);

            return armour.getAttributeModifiers();
        }

        return HashMultimap.create();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + names[3 - armorType.getIndex()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition() {
        return stack -> {
            if (stack.getItem() == RegistrarBloodMagicItems.LIVING_ARMOUR_HELMET)
                return new ModelResourceLocation(getCustomLocation(), "armour=head");
            else if (stack.getItem() == RegistrarBloodMagicItems.LIVING_ARMOUR_CHEST)
                return new ModelResourceLocation(getCustomLocation(), "armour=body");
            else if (stack.getItem() == RegistrarBloodMagicItems.LIVING_ARMOUR_LEGGINGS)
                return new ModelResourceLocation(getCustomLocation(), "armour=leg");
            else
                return new ModelResourceLocation(getCustomLocation(), "armour=feet");
        };
    }

    @Override
    public ResourceLocation getCustomLocation() {
        return new ResourceLocation(BloodMagic.MODID, "living_armour");
    }

    @Override
    public void gatherVariants(Consumer<String> variants) {
        variants.accept("armour=head");
        variants.accept("armour=body");
        variants.accept("armour=leg");
        variants.accept("armour=feet");
    }

    public void setLivingArmour(ItemStack stack, LivingArmour armour, boolean forceWrite) {
        NBTTagCompound livingTag = new NBTTagCompound();

        if (!forceWrite) {
            livingTag = getArmourTag(stack);
            armour.writeDirtyToNBT(livingTag);
        } else {
            armour.writeToNBT(livingTag);
        }

        setArmourTag(stack, livingTag);
    }

    public void setArmourTag(ItemStack stack, NBTTagCompound livingTag) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        tag.setTag(Constants.NBT.LIVING_ARMOUR, livingTag);
    }

    public void setIsEnabled(ItemStack stack, boolean bool) {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        tag.setBoolean("enabled", bool);
    }

    public boolean isEnabled(ItemStack stack) {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        return tag.getBoolean("enabled");
    }

    public void setIsElytra(ItemStack stack, boolean bool) {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        tag.setBoolean("elytra", bool);
    }

    public boolean isElytra(ItemStack stack) {
        NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        return tag.getBoolean("elytra");
    }

    @Nullable
    public static LivingArmour getLivingArmourFromStack(ItemStack stack) {
        NBTTagCompound livingTag = getArmourTag(stack);

        LivingArmour livingArmour = new LivingArmour();
        livingArmour.readFromNBT(livingTag);

        return livingArmour;
    }

    public static NBTTagCompound getArmourTag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();
        return tag.getCompoundTag(Constants.NBT.LIVING_ARMOUR);
    }

    //TODO: Add the ability to have the armour give an upgrade with a higher level
    public static LivingArmourUpgrade getUpgrade(String uniqueIdentifier, ItemStack stack) {
        if (!hasLivingArmour(stack)) {
            setLivingArmour(stack, getLivingArmourFromStack(stack));
        }

        LivingArmour armour = getLivingArmour(stack);

        for (Entry<String, LivingArmourUpgrade> entry : armour.upgradeMap.entrySet()) {
            if (entry.getKey().equals(uniqueIdentifier)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public static LivingArmourUpgrade getUpgradeFromNBT(String uniqueIdentifier, ItemStack stack) {
        LivingArmour armour = getLivingArmourFromStack(stack);

        for (Entry<String, LivingArmourUpgrade> entry : armour.upgradeMap.entrySet()) {
            if (entry.getKey().equals(uniqueIdentifier)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public static boolean hasLivingArmour(ItemStack stack) {
        UUID uuid = Utils.getUUID(stack);
        return uuid != null && armourMap.containsKey(uuid);
    }

    @Nullable
    public static LivingArmour getLivingArmour(ItemStack stack) {
        UUID uuid = Utils.getUUID(stack);

        return armourMap.get(uuid);
    }

    public static void setLivingArmour(ItemStack stack, LivingArmour armour) {
        if (!Utils.hasUUID(stack)) {
            Utils.setUUID(stack);
        }

        UUID uuid = Utils.getUUID(stack);

        armourMap.put(uuid, armour);
    }

    public static boolean hasUpgrade(String id, ItemStack stack) {
        if (!hasLivingArmour(stack)) {
            setLivingArmour(stack, getLivingArmourFromStack(stack));
        }

        LivingArmour armour = getLivingArmour(stack);

        return armour.upgradeMap.containsKey(id);
    }
}
