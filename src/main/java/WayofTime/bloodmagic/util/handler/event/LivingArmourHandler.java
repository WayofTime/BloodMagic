package WayofTime.bloodmagic.util.handler.event;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeCrippledArm;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeQuenched;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeSlowHeal;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeStormTrooper;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerArrowShot;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerGrimReaperSprint;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerJump;
import WayofTime.bloodmagic.livingArmour.upgrade.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class LivingArmourHandler
{

    @SubscribeEvent
    public static void onEntityHealed(LivingHealEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    double modifier = 1;
                    LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.slowHeal", chestStack);

                    if (upgrade instanceof LivingArmourUpgradeSlowHeal)
                    {
                        modifier *= ((LivingArmourUpgradeSlowHeal) upgrade).getHealingModifier();
                    }

                    if (modifier != 1)
                    {
                        event.setAmount((float) (event.getAmount() * modifier));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMiningSpeedCheck(PlayerEvent.BreakSpeed event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (LivingArmour.hasFullSet(player))
        {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
            if (armour != null)
            {
                double modifier = 1;
                for (LivingArmourUpgrade upgrade : armour.upgradeMap.values())
                {
                    modifier *= upgrade.getMiningSpeedModifier(player);
                }

                if (modifier != 1)
                {
                    event.setNewSpeed((float) (event.getOriginalSpeed() * modifier));
                }
            }
        }
    }

    // Applies: Storm Trooper
    @SubscribeEvent
    public static void onEntityJoinedWorld(EntityJoinWorldEvent event)
    {
        Entity owner = null;
        if (event.getEntity() instanceof EntityArrow)
        {
            owner = ((EntityArrow) event.getEntity()).shootingEntity;
        } else if (event.getEntity() instanceof EntityThrowable)
        {
            owner = ((EntityThrowable) event.getEntity()).getThrower();
        }

        if (owner instanceof EntityPlayer)
        {
            Entity projectile = event.getEntity();
            EntityPlayer player = (EntityPlayer) owner;
            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.stormTrooper", chestStack);

                    if (upgrade instanceof LivingArmourUpgradeStormTrooper)
                    {
                        float velocityModifier = (float) (((LivingArmourUpgradeStormTrooper) upgrade).getArrowJiggle(player) * Math.sqrt(projectile.motionX * projectile.motionX + projectile.motionY * projectile.motionY + projectile.motionZ * projectile.motionZ));

                        projectile.motionX += 2 * (event.getWorld().rand.nextDouble() - 0.5) * velocityModifier;
                        projectile.motionY += 2 * (event.getWorld().rand.nextDouble() - 0.5) * velocityModifier;
                        projectile.motionZ += 2 * (event.getWorld().rand.nextDouble() - 0.5) * velocityModifier;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClick(PlayerInteractEvent event)
    {
        if (event.isCancelable())
        {
            EntityPlayer player = event.getEntityPlayer();

            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    if (event.getHand() == EnumHand.OFF_HAND)
                    {
                        LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.crippledArm", chestStack);

                        if (upgrade instanceof LivingArmourUpgradeCrippledArm)
                        {
                            event.setCanceled(true);
                        }
                    }

                    if (event.getItemStack().getItemUseAction() == EnumAction.DRINK)
                    {
                        ItemStack drinkStack = event.getItemStack();

                        //TODO: See if the item is a splash potion? Those should be usable.
                        LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.quenched", chestStack);

                        if (upgrade instanceof LivingArmourUpgradeQuenched)
                        {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    // Applies: Grim Reaper
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityDeath(LivingDeathEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    StatTrackerGrimReaperSprint.incrementCounter(armour);

                    LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.grimReaper", chestStack);

                    if (upgrade instanceof LivingArmourUpgradeGrimReaperSprint && ((LivingArmourUpgradeGrimReaperSprint) upgrade).canSavePlayer(player))
                    {
                        ((LivingArmourUpgradeGrimReaperSprint) upgrade).applyEffectOnRebirth(player);
                        event.setCanceled(true);
                        event.setResult(Event.Result.DENY);
                    }

                    armour.writeDirtyToNBT(ItemLivingArmour.getArmourTag(chestStack));
                }
            }
        }
    }

    // Applies: Jump
    @SubscribeEvent
    public static void onJumpEvent(LivingEvent.LivingJumpEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    StatTrackerJump.incrementCounter(armour);

                    if (!player.isSneaking())
                    {
                        LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgradeFromNBT(BloodMagic.MODID + ".upgrade.jump", chestStack);

                        if (upgrade instanceof LivingArmourUpgradeJump)
                        {
                            player.motionY += ((LivingArmourUpgradeJump) upgrade).getJumpModifier();
                        }
                    }
                }
            }
        }
    }

    // Applies: Step Assist, Speed Boost
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            boolean hasAssist = false;
            if (event.getEntityLiving().isPotionActive(RegistrarBloodMagic.BOOST))
            {
                hasAssist = true;
                player.stepHeight = Constants.Misc.ALTERED_STEP_HEIGHT;
            } else
            {
                if (LivingArmour.hasFullSet(player))
                {
                    ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                    LivingArmour armour = ItemLivingArmour.getLivingArmourFromStack(chestStack);
                    if (armour != null)
                    {
                        LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.stepAssist", chestStack);

                        if (upgrade instanceof LivingArmourUpgradeStepAssist)
                        {
                            player.stepHeight = ((LivingArmourUpgradeStepAssist) upgrade).getStepAssist();
                            hasAssist = true;
                        }
                    }
                }
            }

            if (!hasAssist && player.stepHeight == Constants.Misc.ALTERED_STEP_HEIGHT)
                player.stepHeight = 0.6f;

            float percentIncrease = 0;

            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmourFromStack(chestStack);
                if (armour != null)
                {
                    LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgradeFromNBT(BloodMagic.MODID + ".upgrade.movement", chestStack);

                    if (upgrade instanceof LivingArmourUpgradeSpeed)
                    {
                        percentIncrease += ((LivingArmourUpgradeSpeed) upgrade).getSpeedModifier();
                    }
                }
            }

            if (event.getEntityLiving().isPotionActive(RegistrarBloodMagic.BOOST))
            {
                int i = event.getEntityLiving().getActivePotionEffect(RegistrarBloodMagic.BOOST).getAmplifier();
                {
                    percentIncrease += (i + 1) * 0.5f;
                }
            }

            if (percentIncrease > 0 && (player.onGround || player.capabilities.isFlying) && (Math.abs(player.moveForward) > 0 || Math.abs(player.moveStrafing) > 0))
            {
                player.travel(player.moveStrafing * percentIncrease, 0, player.moveForward * percentIncrease);
            }
        }
    }

    // Applies: Arrow Shot
    // Tracks: Arrow Shot
    @SubscribeEvent
    public static void onArrowFire(ArrowLooseEvent event)
    {
        World world = event.getEntityPlayer().getEntityWorld();
        ItemStack stack = event.getBow();
        EntityPlayer player = event.getEntityPlayer();

        if (world.isRemote)
            return;

        if (LivingArmour.hasFullSet(player))
        {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
            if (armour != null)
            {
                StatTrackerArrowShot.incrementCounter(armour);

                LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.arrowShot", chestStack);
                if (upgrade instanceof LivingArmourUpgradeArrowShot)
                {
                    int charge = event.getCharge();
                    float velocity = (float) charge / 20.0F;
                    velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;

                    if ((double) velocity < 0.1D)
                        return;

                    if (velocity > 1.0F)
                        velocity = 1.0F;

                    int extraArrows = ((LivingArmourUpgradeArrowShot) upgrade).getExtraArrows();
                    for (int n = 0; n < extraArrows; n++)
                    {
                        ItemStack arrowStack = new ItemStack(Items.ARROW);
                        ItemArrow itemarrow = (ItemArrow) ((stack.getItem() instanceof ItemArrow ? arrowStack.getItem() : Items.ARROW));
                        EntityArrow entityarrow = itemarrow.createArrow(world, arrowStack, player);
                        entityarrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);

                        float velocityModifier = 0.6f * velocity;

                        entityarrow.motionX += (event.getWorld().rand.nextDouble() - 0.5) * velocityModifier;
                        entityarrow.motionY += (event.getWorld().rand.nextDouble() - 0.5) * velocityModifier;
                        entityarrow.motionZ += (event.getWorld().rand.nextDouble() - 0.5) * velocityModifier;

                        if (velocity == 1.0F)
                            entityarrow.setIsCritical(true);

                        int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                        if (powerLevel > 0)
                            entityarrow.setDamage(entityarrow.getDamage() + (double) powerLevel * 0.5D + 0.5D);

                        int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                        if (punchLevel > 0)
                            entityarrow.setKnockbackStrength(punchLevel);

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                            entityarrow.setFire(100);

                        entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;

                        world.spawnEntity(entityarrow);
                    }
                }
            }
        }
    }
}
