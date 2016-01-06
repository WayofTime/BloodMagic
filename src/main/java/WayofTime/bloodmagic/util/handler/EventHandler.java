package WayofTime.bloodmagic.util.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.SacrificeKnifeUsedEvent;
import WayofTime.bloodmagic.api.event.TeleposeEvent;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.item.ItemAltarMaker;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeDigging;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgradeSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.StatTrackerDigging;
import WayofTime.bloodmagic.livingArmour.StatTrackerSelfSacrifice;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.TextHelper;

public class EventHandler
{
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
                int totalLP = Math.round(event.ammount * pack.CONVERSION);

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
    public void onEntityAttacked(LivingAttackEvent event)
    {
        Entity sourceEntity = event.source.getEntity();
        EntityLivingBase attackedEntity = event.entityLiving;

        if (attackedEntity.hurtResistantTime > 0)
        {
            return;
        }

        if (sourceEntity != null && attackedEntity instanceof EntityPlayer)
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
                System.out.println(amount);

            }
        }
    }
}
