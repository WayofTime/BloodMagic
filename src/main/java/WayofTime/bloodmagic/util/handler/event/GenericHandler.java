package WayofTime.bloodmagic.util.handler.event;

import WayofTime.bloodmagic.annot.Handler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.ItemBindEvent;
import WayofTime.bloodmagic.api.event.SacrificeKnifeUsedEvent;
import WayofTime.bloodmagic.api.event.TeleposeEvent;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.util.helper.*;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.item.ItemAltarMaker;
import WayofTime.bloodmagic.item.ItemExperienceBook;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSelfSacrifice;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.base.Strings;

@Handler
public class GenericHandler
{
    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event)
    {
        if (event.getEntity().worldObj.isRemote)
            return;

        if (event.getSource().getEntity() instanceof EntityPlayer && !PlayerHelper.isFakePlayer((EntityPlayer) event.getSource().getEntity()))
        {
            EntityPlayer player = (EntityPlayer) event.getSource().getEntity();

            if (player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemPackSacrifice)
            {
                ItemPackSacrifice pack = (ItemPackSacrifice) player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem();

                boolean shouldSyphon = pack.getStoredLP(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)) < pack.CAPACITY;
                float damageDone = event.getEntityLiving().getHealth() < event.getAmount() ? event.getAmount() - event.getEntityLiving().getHealth() : event.getAmount();
                int totalLP = Math.round(damageDone * ConfigHandler.sacrificialPackConversion);

                if (shouldSyphon)
                    ItemHelper.LPContainer.addLPToItem(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), totalLP, pack.CAPACITY);
            }
        }
    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event)
    {
        if (event.getEmptyBucket().getItem() != Items.BUCKET)
            return;

        ItemStack result = null;

        if (event.getTarget() == null || event.getTarget().getBlockPos() == null)
        {
            return;
        }

        Block block = event.getWorld().getBlockState(event.getTarget().getBlockPos()).getBlock();

        if (block != null && (block.equals(ModBlocks.lifeEssence)) && block.getMetaFromState(event.getWorld().getBlockState(event.getTarget().getBlockPos())) == 0)
        {
            event.getWorld().setBlockToAir(event.getTarget().getBlockPos());
            result = new ItemStack(ModItems.bucketEssence);
        }

        if (result == null)
            return;

        event.setFilledBucket(result);
        event.setResult(Event.Result.ALLOW);
    }

    // Handles destroying altar
    @SubscribeEvent
    public void harvestEvent(PlayerEvent.HarvestCheck event)
    {
        IBlockState state = event.getTargetBlock();
        Block block = state.getBlock();
        if (block != null && block instanceof BlockAltar && event.getEntityPlayer() != null && event.getEntityPlayer() instanceof EntityPlayerMP && event.getEntityPlayer().getHeldItemMainhand() != null && event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemAltarMaker)
        {
            ItemAltarMaker altarMaker = (ItemAltarMaker) event.getEntityPlayer().getHeldItemMainhand().getItem();
            ChatUtil.sendNoSpam(event.getEntityPlayer(), TextHelper.localizeEffect("chat.BloodMagic.altarMaker.destroy", altarMaker.destroyAltar(event.getEntityPlayer())));
        }
    }

    // Handle Teleposer block blacklist
    @SubscribeEvent
    public void onTelepose(TeleposeEvent event)
    {
        if (ConfigHandler.teleposerBlacklist.contains(event.initialStack) || ConfigHandler.teleposerBlacklist.contains(event.finalStack))
            event.setCanceled(true);

        if (BloodMagicAPI.getTeleposerBlacklist().contains(event.initialStack) || BloodMagicAPI.getTeleposerBlacklist().contains(event.finalStack))
            event.setCanceled(true);
    }

    // Handle Teleposer entity blacklist
    @SubscribeEvent
    public void onTeleposeEntity(TeleposeEvent.Ent event)
    {
        if (ConfigHandler.teleposerBlacklistEntity.contains(event.entity.getClass().getSimpleName()))
            event.setCanceled(true);
    }

    // Sets teleport cooldown for Teleposed entities to 5 ticks (1/4 second) instead of 150 (7.5 seconds)
    @SubscribeEvent
    public void onTeleposeEntityPost(TeleposeEvent.Ent.Post event)
    {
        event.entity.timeUntilPortal = 5;
    }

    // Handles binding of IBindable's as well as setting a player's highest orb tier
    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.RightClickItem event)
    {
        if (event.getWorld().isRemote)
            return;

        EntityPlayer player = event.getEntityPlayer();

        if (PlayerHelper.isFakePlayer(player))
            return;

        ItemStack held = event.getItemStack();
        if (held != null && held.getItem() instanceof IBindable)
        {
            held = NBTHelper.checkNBT(held);
            IBindable bindable = (IBindable) held.getItem();
            if (Strings.isNullOrEmpty(bindable.getOwnerUUID(held)))
            {
                if (bindable.onBind(player, held))
                {
                    String uuid = PlayerHelper.getUUIDFromPlayer(player).toString();
                    ItemBindEvent toPost = new ItemBindEvent(player, uuid, held);
                    if (MinecraftForge.EVENT_BUS.post(toPost) || toPost.getResult() == Result.DENY)
                        return;

                    BindableHelper.setItemOwnerUUID(held, uuid);
                    BindableHelper.setItemOwnerName(held, player.getDisplayNameString());
                }
            } else if (bindable.getOwnerUUID(held).equals(PlayerHelper.getUUIDFromPlayer(player).toString()) && !bindable.getOwnerName(held).equals(player.getDisplayNameString()))
                BindableHelper.setItemOwnerName(held, player.getDisplayNameString());
        }

        if (held != null && held.getItem() instanceof IBloodOrb)
        {
            held = NBTHelper.checkNBT(held);
            IBloodOrb bloodOrb = (IBloodOrb) held.getItem();
            SoulNetwork network = NetworkHelper.getSoulNetwork(player);

            if (bloodOrb.getOrbLevel(held.getItemDamage()) > network.getOrbTier())
                network.setOrbTier(bloodOrb.getOrbLevel(held.getItemDamage()));
        }
    }

    @SubscribeEvent
    public void selfSacrificeEvent(SacrificeKnifeUsedEvent event)
    {
        EntityPlayer player = event.player;

        if (LivingArmour.hasFullSet(player))
        {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
            if (armour != null)
            {
                StatTrackerSelfSacrifice.incrementCounter(armour, event.healthDrained / 2);
                LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(Constants.Mod.MODID + ".upgrade.selfSacrifice", chestStack);

                if (upgrade instanceof LivingArmourUpgradeSelfSacrifice)
                {
                    double modifier = ((LivingArmourUpgradeSelfSacrifice) upgrade).getSacrificeModifier();

                    event.lpAdded = (int) (event.lpAdded * (1 + modifier));
                }
            }
        }
    }

    // Drop Blood Shards
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event)
    {
        EntityLivingBase attackedEntity = event.getEntityLiving();
        DamageSource source = event.getSource();
        Entity entity = source.getEntity();

        if (entity != null && entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack heldStack = player.getHeldItemMainhand();

            if (heldStack != null && heldStack.getItem() == ModItems.boundSword && !(attackedEntity instanceof EntityAnimal))
                for (int i = 0; i <= EnchantmentHelper.getLootingModifier(player); i++)
                    if (attackedEntity.getEntityWorld().rand.nextDouble() < 0.2)
                        event.getDrops().add(new EntityItem(attackedEntity.worldObj, attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, new ItemStack(ModItems.bloodShard, 1, 0)));
        }
    }

    // Experience Tome
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onExperiencePickup(PlayerPickupXpEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, player);

        if (itemstack != null && itemstack.isItemDamaged())
        {
            int i = Math.min(xpToDurability(event.getOrb().xpValue), itemstack.getItemDamage());
            event.getOrb().xpValue -= durabilityToXp(i);
            itemstack.setItemDamage(itemstack.getItemDamage() - i);
        }

        if (!player.worldObj.isRemote)
        {
            for (ItemStack stack : player.inventory.mainInventory)
            {
                if (stack != null && stack.getItem() instanceof ItemExperienceBook)
                {
                    ItemExperienceBook.addExperience(stack, event.getOrb().xpValue);
                    event.getOrb().xpValue = 0;
                    break;
                }
            }
        }
    }

    private int xpToDurability(int xp)
    {
        return xp * 2;
    }

    private int durabilityToXp(int durability)
    {
        return durability / 2;
    }
}
