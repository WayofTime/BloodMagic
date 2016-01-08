package WayofTime.bloodmagic.util.handler;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.SacrificeKnifeUsedEvent;
import WayofTime.bloodmagic.api.event.TeleposeEvent;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.soul.ISoul;
import WayofTime.bloodmagic.api.soul.ISoulWeapon;
import WayofTime.bloodmagic.api.soul.PlayerSoulHandler;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.item.ItemAltarMaker;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeArrowShot;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeDigging;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.StatTrackerArrowShot;
import WayofTime.bloodmagic.livingArmour.StatTrackerDigging;
import WayofTime.bloodmagic.livingArmour.StatTrackerHealthboost;
import WayofTime.bloodmagic.livingArmour.StatTrackerMeleeDamage;
import WayofTime.bloodmagic.livingArmour.StatTrackerPhysicalProtect;
import WayofTime.bloodmagic.livingArmour.StatTrackerSelfSacrifice;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.registry.ModPotions;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class EventHandler
{
    Random random = new Random();

    @SubscribeEvent
    public void onEntityDeath(LivingHurtEvent event)
    {
        int chestIndex = 2;

        if (event.source.getEntity() instanceof EntityPlayer && !PlayerHelper.isFakePlayer((EntityPlayer) event.source.getEntity()))
        {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();

            if (player.getCurrentArmor(chestIndex) != null && player.getCurrentArmor(chestIndex).getItem() instanceof ItemPackSacrifice)
            {
                ItemPackSacrifice pack = (ItemPackSacrifice) player.getCurrentArmor(chestIndex).getItem();

                boolean shouldSyphon = pack.getStoredLP(player.getCurrentArmor(chestIndex)) < pack.CAPACITY;
                float damageDone = event.entityLiving.getHealth() < event.ammount ? event.ammount - event.entityLiving.getHealth() : event.ammount;
                int totalLP = Math.round(damageDone * pack.CONVERSION);

                if (shouldSyphon)
                    pack.addLP(player.getCurrentArmor(chestIndex), totalLP);
            }
        }
    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event)
    {
        if (event.current.getItem() != Items.bucket)
            return;

        ItemStack result = null;

        Block block = event.world.getBlockState(event.target.getBlockPos()).getBlock();

        if (block != null && (block.equals(ModBlocks.lifeEssence)) && block.getMetaFromState(event.world.getBlockState(event.target.getBlockPos())) == 0)
        {
            event.world.setBlockToAir(event.target.getBlockPos());
            result = new ItemStack(ModItems.bucketEssence);
        }

        if (result == null)
            return;

        event.result = result;
        event.setResult(Event.Result.ALLOW);
    }

    @SubscribeEvent
    public void harvestEvent(PlayerEvent.HarvestCheck event)
    {
        if (event.block != null && event.block instanceof BlockAltar && event.entityPlayer != null && event.entityPlayer instanceof EntityPlayerMP && event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemAltarMaker)
        {
            ItemAltarMaker altarMaker = (ItemAltarMaker) event.entityPlayer.getCurrentEquippedItem().getItem();
            ChatUtil.sendNoSpam(event.entityPlayer, TextHelper.localizeEffect("chat.BloodMagic.altarMaker.destroy", altarMaker.destroyAltar(event.entityPlayer)));
        }
    }

    @SubscribeEvent
    public void onTelepose(TeleposeEvent event)
    {
        if (ConfigHandler.teleposerBlacklist.contains(event.initialStack) || ConfigHandler.teleposerBlacklist.contains(event.finalStack))
            event.setCanceled(true);

        if (BloodMagicAPI.getTeleposerBlacklist().contains(event.initialStack) || BloodMagicAPI.getTeleposerBlacklist().contains(event.finalStack))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.modID.equals(Constants.Mod.MODID))
            ConfigHandler.syncConfig();
    }

    @SubscribeEvent
    public void blockBreakEvent(BlockEvent.BreakEvent event)
    {
        EntityPlayer player = event.getPlayer();
        if (player != null)
        {
            for (int i = 0; i < 4; i++)
            {
                ItemStack stack = player.getCurrentArmor(i);
                if (stack == null || !(stack.getItem() instanceof ItemLivingArmour))
                {
                    return;
                }
            }

            ItemStack chestStack = player.getCurrentArmor(2);
            if (chestStack != null && chestStack.getItem() instanceof ItemLivingArmour)
            {
                LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);

                if (armour != null)
                {
                    StatTrackerDigging.incrementCounter(armour);
                    LivingArmourUpgradeDigging.hasDug(armour);
                }
            }
        }
    }

    @SubscribeEvent
    public void selfSacrificeEvent(SacrificeKnifeUsedEvent event)
    {
        EntityPlayer player = event.player;

        for (int i = 0; i < 4; i++)
        {
            ItemStack stack = player.getCurrentArmor(i);
            if (stack == null || !(stack.getItem() instanceof ItemLivingArmour))
            {
                return;
            }
        }

        ItemStack chestStack = player.getCurrentArmor(2);
        LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
        if (armour != null)
        {
            StatTrackerSelfSacrifice.incrementCounter(armour);
            LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(Constants.Mod.MODID + ".upgrade.selfSacrifice", chestStack);

            if (upgrade instanceof LivingArmourUpgradeSelfSacrifice)
            {
                double modifier = ((LivingArmourUpgradeSelfSacrifice) upgrade).getSacrificeModifier();

                event.lpAdded = (int) (event.lpAdded * (1 + modifier));
            }
        }
    }

    @SubscribeEvent
    public void onEntityHealed(LivingHealEvent event)
    {
        EntityLivingBase healedEntity = event.entityLiving;
        if (!(healedEntity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) healedEntity;

        for (int i = 0; i < 4; i++)
        {
            ItemStack stack = player.getCurrentArmor(i);
            if (stack == null || !(stack.getItem() instanceof ItemLivingArmour))
            {
                return;
            }
        }

        ItemStack chestStack = player.getCurrentArmor(2);
        LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
        if (armour != null)
        {
            StatTrackerHealthboost.incrementCounter(armour, event.amount);
        }
    }

    @SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event)
    {
        DamageSource source = event.source;
        Entity sourceEntity = event.source.getEntity();
        EntityLivingBase attackedEntity = event.entityLiving;

        if (attackedEntity.hurtResistantTime > 0)
        {
            return;
        }

        if (attackedEntity instanceof EntityPlayer)
        {
            EntityPlayer attackedPlayer = (EntityPlayer) attackedEntity;

            boolean hasFullSet = true;
            for (int i = 0; i < 4; i++)
            {
                ItemStack stack = attackedPlayer.getCurrentArmor(i);
                if (stack == null || !(stack.getItem() instanceof ItemLivingArmour))
                {
                    hasFullSet = false;
                    break;
                }
            }

            float amount = Math.min(Utils.getModifiedDamage(attackedPlayer, event.source, event.ammount), attackedPlayer.getHealth());

            if (hasFullSet)
            {
                ItemStack chestStack = attackedPlayer.getCurrentArmor(2);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    if (sourceEntity != null && !source.isMagicDamage())
                    {
                        // Add resistance to the upgrade that protects against non-magic damage
                        StatTrackerPhysicalProtect.incrementCounter(armour, amount);
                    }
                }
            }
        }

        if (sourceEntity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) sourceEntity;

            boolean hasFullSet = true;
            for (int i = 0; i < 4; i++)
            {
                ItemStack stack = player.getCurrentArmor(i);
                if (stack == null || !(stack.getItem() instanceof ItemLivingArmour))
                {
                    hasFullSet = false;
                    break;
                }
            }

            float amount = Math.min(Utils.getModifiedDamage(attackedEntity, event.source, event.ammount), attackedEntity.getHealth());

            if (hasFullSet)
            {
                ItemStack chestStack = player.getCurrentArmor(2);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    if (sourceEntity != null && !source.isProjectile())
                    {
                        StatTrackerMeleeDamage.incrementCounter(armour, amount);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onArrowFire(ArrowLooseEvent event)
    {
        World world = event.entityPlayer.worldObj;
        ItemStack stack = event.bow;
        EntityPlayer player = event.entityPlayer;

        boolean hasFullSet = true;
        for (int i = 0; i < 4; i++)
        {
            ItemStack armourStack = player.getCurrentArmor(i);
            if (armourStack == null || !(armourStack.getItem() instanceof ItemLivingArmour))
            {
                hasFullSet = false;
                break;
            }
        }

        if (hasFullSet)
        {
            ItemStack chestStack = player.getCurrentArmor(2);
            LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
            if (armour != null)
            {
                StatTrackerArrowShot.incrementCounter(armour);

                LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(Constants.Mod.MODID + ".upgrade.arrowShot", chestStack);
                if (upgrade instanceof LivingArmourUpgradeArrowShot)
                {
                    int i = event.charge;
                    float f = (float) i / 20.0F;
                    f = (f * f + f * 2.0F) / 3.0F;

                    if ((double) f < 0.1D)
                    {
                        return;
                    }

                    if (f > 1.0F)
                    {
                        f = 1.0F;
                    }

                    int numberExtra = ((LivingArmourUpgradeArrowShot) upgrade).getExtraArrows();
                    for (int n = 0; n < numberExtra; n++)
                    {
                        EntityArrow entityarrow = new EntityArrow(world, player, f * 2.0F);

                        double velocityModifier = 0.6 * f;
                        entityarrow.motionX += (random.nextDouble() - 0.5) * velocityModifier;
                        entityarrow.motionY += (random.nextDouble() - 0.5) * velocityModifier;
                        entityarrow.motionZ += (random.nextDouble() - 0.5) * velocityModifier;

                        if (f == 1.0F)
                        {
                            entityarrow.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);

                        if (j > 0)
                        {
                            entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);

                        if (k > 0)
                        {
                            entityarrow.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
                        {
                            entityarrow.setFire(100);
                        }

                        entityarrow.canBePickedUp = 2;

                        if (!world.isRemote)
                        {
                            world.spawnEntityInWorld(entityarrow);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event)
    {
        EntityLivingBase attackedEntity = event.entityLiving;
        DamageSource source = event.source;
        Entity entity = source.getEntity();

        if (attackedEntity.isPotionActive(ModPotions.soulSnare))
        {
            PotionEffect eff = attackedEntity.getActivePotionEffect(ModPotions.soulSnare);
            int lvl = eff.getAmplifier();

            double amountOfSouls = random.nextDouble() * (lvl + 1) * (lvl + 1) * 5;
            ItemStack soulStack = ((ISoul) ModItems.monsterSoul).createSoul(0, amountOfSouls);
            event.drops.add(new EntityItem(attackedEntity.worldObj, attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, soulStack));
        }

        if (entity != null && entity instanceof EntityLivingBase)
        {
            EntityLivingBase attackingEntity = (EntityLivingBase) entity;
            ItemStack heldStack = attackingEntity.getHeldItem();
            if (heldStack != null && heldStack.getItem() instanceof ISoulWeapon)
            {
                List<ItemStack> droppedSouls = ((ISoulWeapon) heldStack.getItem()).getRandomSoulDrop(attackedEntity, attackingEntity, heldStack, event.lootingLevel);
                if (!droppedSouls.isEmpty())
                {
                    for (ItemStack soulStack : droppedSouls)
                    {
                        event.drops.add(new EntityItem(attackedEntity.worldObj, attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, soulStack));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event)
    {
        ItemStack stack = event.item.getEntityItem();
        if (stack != null && stack.getItem() instanceof ISoul)
        {
            EntityPlayer player = event.entityPlayer;

            ItemStack remainder = PlayerSoulHandler.addSouls(player, stack);

            if (remainder == null || ((ISoul) stack.getItem()).getSouls(stack) < 0.0001)
            {
                stack.stackSize = 0;
                event.setResult(Result.ALLOW);
            }
        }
    }
}
