package WayofTime.bloodmagic.util.handler.event;

import java.util.List;

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
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.annot.Handler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.ItemBindEvent;
import WayofTime.bloodmagic.api.event.SacrificeKnifeUsedEvent;
import WayofTime.bloodmagic.api.event.TeleposeEvent;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.iface.ISentientTool;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.api.util.helper.ItemHelper;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;
import WayofTime.bloodmagic.item.ItemAltarMaker;
import WayofTime.bloodmagic.item.ItemExperienceBook;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSelfSacrifice;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.DemonAuraPacketProcessor;
import WayofTime.bloodmagic.potion.BMPotionUtils;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.registry.ModPotions;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.base.Strings;

@Handler
public class GenericHandler
{
    @SubscribeEvent
    public void onPlayerClick(PlayerInteractEvent event)
    {
        if (event.isCancelable() && event.getEntityPlayer().isPotionActive(ModPotions.constrict))
        {
            EntityPlayer player = event.getEntityPlayer();
            int level = player.getActivePotionEffect(ModPotions.constrict).getAmplifier();
            if (event.getHand() == EnumHand.OFF_HAND || level > 1)
            {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDropItem(ItemTossEvent event)
    {
        EntityItem itemEntity = event.getEntityItem();
        if (itemEntity != null)
        {
            ItemStack stack = itemEntity.getEntityItem();
            Item item = stack.getItem();
            if (item instanceof ISentientTool)
            {
                if (((ISentientTool) item).spawnSentientEntityOnDrop(stack, event.getPlayer()))
                {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Start event)
    {
        World world = event.getWorld();
        Explosion exp = event.getExplosion();
        Vec3d position = exp.getPosition();
        double radius = 3;

        AxisAlignedBB bb = new AxisAlignedBB(position.xCoord - radius, position.yCoord - radius, position.zCoord - radius, position.xCoord + radius, position.yCoord + radius, position.zCoord + radius);
        List<EntitySentientSpecter> specterList = world.getEntitiesWithinAABB(EntitySentientSpecter.class, bb);
        if (!specterList.isEmpty())
        {
            for (EntitySentientSpecter specter : specterList)
            {
                if (specter.absorbExplosion(exp))
                {
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

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

    // Handles sending the client the Demon Will Aura updates
    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event)
    {
        if (!event.getEntityLiving().worldObj.isRemote)
        {
            EntityLivingBase entity = event.getEntityLiving();
            if (entity instanceof EntityPlayer && entity.ticksExisted % 50 == 0) //TODO: Change to an incremental counter
            {
                sendPlayerDemonWillAura((EntityPlayer) entity);
            }
        }

        EntityLivingBase entity = event.getEntityLiving();

        if (entity.isPotionActive(MobEffects.NIGHT_VISION))
        {
            int duration = entity.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
            if (duration == Constants.Misc.NIGHT_VISION_CONSTANT_END)
            {
                entity.removePotionEffect(MobEffects.NIGHT_VISION);
            }
        }

        if (entity.isPotionActive(ModPotions.fireFuse))
        {
            entity.worldObj.spawnParticle(EnumParticleTypes.FLAME, entity.posX + entity.worldObj.rand.nextDouble() * 0.3, entity.posY + entity.worldObj.rand.nextDouble() * 0.3, entity.posZ + entity.worldObj.rand.nextDouble() * 0.3, 0, 0.06d, 0);

            int r = entity.getActivePotionEffect(ModPotions.fireFuse).getAmplifier();
            int radius = 1 * r + 1;

            if (entity.getActivePotionEffect(ModPotions.fireFuse).getDuration() <= 3)
            {
                entity.worldObj.createExplosion(null, entity.posX, entity.posY, entity.posZ, radius, false);
            }
        }

        if (entity.isPotionActive(ModPotions.plantLeech))
        {
            int amplifier = entity.getActivePotionEffect(ModPotions.plantLeech).getAmplifier();
            int timeRemaining = entity.getActivePotionEffect(ModPotions.plantLeech).getDuration();
            if (timeRemaining % 10 == 0)
            {
                BMPotionUtils.damageMobAndGrowSurroundingPlants(entity, 2 + amplifier, 1, 0.5 * 3 / (amplifier + 3), 25 * (1 + amplifier));
            }
        }
    }

//    @SideOnly(Side.SERVER)
    public void sendPlayerDemonWillAura(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            BlockPos pos = player.getPosition();
            DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(player.worldObj.provider.getDimension(), pos.getX() >> 4, pos.getZ() >> 4);
            if (holder != null)
            {
                BloodMagicPacketHandler.sendTo(new DemonAuraPacketProcessor(holder), (EntityPlayerMP) player);
            } else
            {
                BloodMagicPacketHandler.sendTo(new DemonAuraPacketProcessor(new DemonWillHolder()), (EntityPlayerMP) player);
            }
        }
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
            LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
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
