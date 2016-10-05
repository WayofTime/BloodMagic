package WayofTime.bloodmagic.util.handler.event;

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
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import WayofTime.bloodmagic.annot.Handler;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.armour.ItemSentientArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerArrowProtect;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerCriticalStrike;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerDigging;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerExperience;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerFallProtect;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerGraveDigger;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerHealthboost;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerMeleeDamage;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerNightSight;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerPhysicalProtect;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSolarPowered;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSprintAttack;
import WayofTime.bloodmagic.livingArmour.tracker.downgrade.StatTrackerBattleHungry;
import WayofTime.bloodmagic.livingArmour.tracker.downgrade.StatTrackerDigSlowdown;
import WayofTime.bloodmagic.livingArmour.tracker.downgrade.StatTrackerMeleeDecrease;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeDigging;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeExperience;
import WayofTime.bloodmagic.util.Utils;

@Handler
public class StatTrackerHandler
{

    private static float lastPlayerSwingStrength = 0;

    // Tracks: Digging, DigSlowdown
    @SubscribeEvent
    public void blockBreakEvent(BlockEvent.BreakEvent event)
    {
        EntityPlayer player = event.getPlayer();
        if (player != null)
        {
            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (chestStack != null && chestStack.getItem() instanceof ItemLivingArmour)
                {
                    LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);

                    if (armour != null)
                    {
                        StatTrackerDigging.incrementCounter(armour);
                        LivingArmourUpgradeDigging.hasDug(armour);

                        if (player.isPotionActive(MobEffects.MINING_FATIGUE))
                        {
                            StatTrackerDigSlowdown.incrementCounter(armour);
                        }
                    }
                }
            }
        }
    }

    // Tracks: Health Boost
    @SubscribeEvent
    public void onEntityHealed(LivingHealEvent event)
    {
        EntityLivingBase healedEntity = event.getEntityLiving();
        if (!(healedEntity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) healedEntity;

        if (LivingArmour.hasFullSet(player))
        {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
            if (armour != null)
            {
                StatTrackerHealthboost.incrementCounter(armour, event.getAmount());
                if (player.worldObj.canSeeSky(player.getPosition()) && player.worldObj.provider.isDaytime())
                {
                    StatTrackerSolarPowered.incrementCounter(armour, event.getAmount());
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingAttack(AttackEntityEvent event)
    {
        lastPlayerSwingStrength = event.getEntityPlayer().getCooledAttackStrength(0);
    }

    // Tracks: Fall Protect, Arrow Protect, Physical Protect, Grave Digger, Sprint Attack, Critical Strike, Nocturnal Prowess
    @SubscribeEvent
    public void entityHurt(LivingHurtEvent event)
    {
        DamageSource source = event.getSource();
        Entity sourceEntity = event.getSource().getEntity();
        EntityLivingBase attackedEntity = event.getEntityLiving();

        if (attackedEntity instanceof EntityPlayer)
        {
            EntityPlayer attackedPlayer = (EntityPlayer) attackedEntity;

            // Living Armor Handling
            if (LivingArmour.hasFullSet(attackedPlayer))
            {
                float amount = Math.min(Utils.getModifiedDamage(attackedPlayer, event.getSource(), event.getAmount()), attackedPlayer.getHealth());
                ItemStack chestStack = attackedPlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    if (sourceEntity != null && !source.isMagicDamage() && !source.isProjectile())
                        StatTrackerPhysicalProtect.incrementCounter(armour, amount);

                    if (source.equals(DamageSource.fall))
                        StatTrackerFallProtect.incrementCounter(armour, amount);

                    if (source.isProjectile())
                        StatTrackerArrowProtect.incrementCounter(armour, amount);
                }
            } else
            {
                ItemStack chestStack = attackedPlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (chestStack != null && chestStack.getItem() instanceof ItemSentientArmour)
                {
                    ItemSentientArmour armour = (ItemSentientArmour) chestStack.getItem();
                    armour.onPlayerAttacked(chestStack, source, attackedPlayer);
                }
            }
        }

        if (sourceEntity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) sourceEntity;

            // Living Armor Handling
            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    ItemStack mainWeapon = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

                    event.setAmount((float) (event.getAmount() + lastPlayerSwingStrength * armour.getAdditionalDamageOnHit(event.getAmount(), player, attackedEntity, mainWeapon)));

                    float amount = Math.min(Utils.getModifiedDamage(attackedEntity, event.getSource(), event.getAmount()), attackedEntity.getHealth());

                    if (!source.isProjectile())
                    {
                        StatTrackerMeleeDamage.incrementCounter(armour, amount);

                        if (player.isPotionActive(MobEffects.WEAKNESS))
                            StatTrackerMeleeDecrease.incrementCounter(armour, amount);

                        if (player.isPotionActive(MobEffects.HUNGER))
                            StatTrackerBattleHungry.incrementCounter(armour, amount);

                        if (player.worldObj.getLight(player.getPosition()) <= 9)
                            StatTrackerNightSight.incrementCounter(armour, amount);

                        if (mainWeapon != null && mainWeapon.getItem() instanceof ItemSpade)
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
    public void onExperiencePickup(PlayerPickupXpEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();

        if (LivingArmour.hasFullSet(player))
        {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
            if (armour != null)
            {
                LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(Constants.Mod.MODID + ".upgrade.experienced", chestStack);
                if (upgrade instanceof LivingArmourUpgradeExperience)
                {
                    double modifier = ((LivingArmourUpgradeExperience) upgrade).getExperienceModifier();
                    double exp = event.getOrb().xpValue * (1 + modifier);

                    event.getOrb().xpValue = (int) Math.floor(exp) + (Math.random() < exp % 1 ? 1 : 0);
                }

                StatTrackerExperience.incrementCounter(armour, event.getOrb().xpValue);
            }
        }
    }
}
