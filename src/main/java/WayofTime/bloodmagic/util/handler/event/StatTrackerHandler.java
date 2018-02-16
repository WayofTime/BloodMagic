package WayofTime.bloodmagic.util.handler.event;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.armour.ItemSentientArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.tracker.*;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeDigging;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeExperience;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class StatTrackerHandler {

    private static float lastPlayerSwingStrength = 0;

    // Tracks: Digging, DigSlowdown
    @SubscribeEvent
    public static void blockBreakEvent(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player != null) {
            if (LivingArmour.hasFullSet(player)) {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (chestStack.getItem() instanceof ItemLivingArmour) {
                    LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);

                    if (armour != null) {
                        StatTrackerDigging.incrementCounter(armour);
                        LivingArmourUpgradeDigging.hasDug(armour);
                    }
                }
            }
        }
    }

    // Tracks: Health Boost
    @SubscribeEvent
    public static void onEntityHealed(LivingHealEvent event) {
        EntityLivingBase healedEntity = event.getEntityLiving();
        if (!(healedEntity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) healedEntity;

        if (LivingArmour.hasFullSet(player)) {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
            if (armour != null) {
                StatTrackerHealthboost.incrementCounter(armour, event.getAmount());
                if (player.getEntityWorld().canSeeSky(player.getPosition()) && player.getEntityWorld().provider.isDaytime()) {
                    StatTrackerSolarPowered.incrementCounter(armour, event.getAmount());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(AttackEntityEvent event) {
        lastPlayerSwingStrength = event.getEntityPlayer().getCooledAttackStrength(0);
    }

    // Tracks: Fall Protect, Arrow Protect, Physical Protect, Grave Digger, Sprint Attack, Critical Strike, Nocturnal Prowess
    @SubscribeEvent
    public static void entityHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        Entity sourceEntity = event.getSource().getTrueSource();
        EntityLivingBase attackedEntity = event.getEntityLiving();

        if (attackedEntity instanceof EntityPlayer) {
            EntityPlayer attackedPlayer = (EntityPlayer) attackedEntity;

            // Living Armor Handling
            if (LivingArmour.hasFullSet(attackedPlayer)) {
                float amount = Math.min(Utils.getModifiedDamage(attackedPlayer, event.getSource(), event.getAmount()), attackedPlayer.getHealth());
                ItemStack chestStack = attackedPlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null) {
                    if (sourceEntity != null && !source.isMagicDamage() && !source.isProjectile())
                        StatTrackerPhysicalProtect.incrementCounter(armour, amount);

                    if (source.equals(DamageSource.FALL))
                        StatTrackerFallProtect.incrementCounter(armour, amount);

                    if (source.isProjectile())
                        StatTrackerArrowProtect.incrementCounter(armour, amount);
                }
            } else {
                ItemStack chestStack = attackedPlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (chestStack.getItem() instanceof ItemSentientArmour) {
                    ItemSentientArmour armour = (ItemSentientArmour) chestStack.getItem();
                    armour.onPlayerAttacked(chestStack, source, attackedPlayer);
                }
            }
        }

        if (sourceEntity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sourceEntity;

            // Living Armor Handling
            if (LivingArmour.hasFullSet(player)) {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null) {
                    ItemStack mainWeapon = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

                    event.setAmount((float) (event.getAmount() + lastPlayerSwingStrength * armour.getAdditionalDamageOnHit(event.getAmount(), player, attackedEntity, mainWeapon)));

                    float amount = Math.min(Utils.getModifiedDamage(attackedEntity, event.getSource(), event.getAmount()), attackedEntity.getHealth());

                    if (!source.isProjectile()) {
                        StatTrackerMeleeDamage.incrementCounter(armour, amount);

                        if (player.getEntityWorld().getLight(player.getPosition()) <= 9)
                            StatTrackerNightSight.incrementCounter(armour, amount);

                        if (mainWeapon.getItem() instanceof ItemSpade)
                            StatTrackerGraveDigger.incrementCounter(armour, amount);

                        if (player.isSprinting())
                            StatTrackerSprintAttack.incrementCounter(armour, amount);

                        boolean isCritical = lastPlayerSwingStrength > 0.9 && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding() && !player.isSprinting();
                        if (isCritical)
                            StatTrackerCriticalStrike.incrementCounter(armour, amount);

                        double kb = armour.getKnockbackOnHit(player, attackedEntity, mainWeapon);
                        if (kb > 0)
                            attackedEntity.knockBack(player, (float) kb * 0.5F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                    }
                }
            }
        }
    }

    // Tracks: Experienced
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onExperiencePickup(PlayerPickupXpEvent event) {
        EntityPlayer player = event.getEntityPlayer();

        if (LivingArmour.hasFullSet(player)) {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
            if (armour != null) {
                LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.experienced", chestStack);
                if (upgrade instanceof LivingArmourUpgradeExperience) {
                    double modifier = ((LivingArmourUpgradeExperience) upgrade).getExperienceModifier();
                    double exp = event.getOrb().xpValue * (1 + modifier);

                    event.getOrb().xpValue = (int) Math.floor(exp) + (Math.random() < exp % 1 ? 1 : 0);
                }

                StatTrackerExperience.incrementCounter(armour, event.getOrb().xpValue);
            }
        }
    }
}
