package WayofTime.bloodmagic.util.handler.event;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.event.ItemBindEvent;
import WayofTime.bloodmagic.event.SacrificeKnifeUsedEvent;
import WayofTime.bloodmagic.event.TeleposeEvent;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.iface.ISentientTool;
import WayofTime.bloodmagic.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.orb.BloodOrb;
import WayofTime.bloodmagic.orb.IBloodOrb;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.entity.mob.EntitySentientSpecter;
import WayofTime.bloodmagic.item.ItemAltarMaker;
import WayofTime.bloodmagic.item.ItemExperienceBook;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.downgrade.LivingArmourUpgradeBattleHungry;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSelfSacrifice;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.DemonAuraPacketProcessor;
import WayofTime.bloodmagic.potion.BMPotionUtils;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.*;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class GenericHandler {
    public static Map<EntityPlayer, Double> bounceMap = new HashMap<>();
    public static Map<EntityPlayer, Integer> filledHandMap = new HashMap<>();
    private static Map<EntityAnimal, EntityAITarget> targetTaskMap = new HashMap<>();
    private static Map<EntityAnimal, EntityAIBase> attackTaskMap = new HashMap<>();

    @SubscribeEvent
    public static void onEntityFall(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (player.isPotionActive(RegistrarBloodMagic.BOUNCE) && !player.isSneaking() && event.getDistance() > 2) {
                event.setDamageMultiplier(0);

                if (player.getEntityWorld().isRemote) {
                    player.motionY *= -0.9;
                    player.fallDistance = 0;
                    bounceMap.put(player, player.motionY);
                } else {
                    player.fallDistance = 0;
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerTickPost(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && bounceMap.containsKey(event.player)) {
            event.player.motionY = bounceMap.remove(event.player);
        }

        if (event.phase == TickEvent.Phase.END) {
            if (filledHandMap.containsKey(event.player)) {
                int value = filledHandMap.get(event.player) - 1;
                if (value <= 0) {
                    filledHandMap.remove(event.player);
                } else {
                    filledHandMap.put(event.player, value);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClick(PlayerInteractEvent event) {
        if (event.isCancelable() && event.getEntityPlayer().isPotionActive(RegistrarBloodMagic.CONSTRICT)) {
            EntityPlayer player = event.getEntityPlayer();
            int level = player.getActivePotionEffect(RegistrarBloodMagic.CONSTRICT).getAmplifier();
            if (event.getHand() == EnumHand.OFF_HAND || level > 1) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDropItem(ItemTossEvent event) {
        EntityItem itemEntity = event.getEntityItem();
        if (itemEntity != null) {
            ItemStack stack = itemEntity.getItem();
            Item item = stack.getItem();
            if (stack.hasTagCompound() && item instanceof ISentientTool) {
                if (((ISentientTool) item).spawnSentientEntityOnDrop(stack, event.getPlayer())) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Start event) {
        World world = event.getWorld();
        Explosion exp = event.getExplosion();
        Vec3d position = exp.getPosition();
        double radius = 3;

        AxisAlignedBB bb = new AxisAlignedBB(position.x - radius, position.y - radius, position.z - radius, position.x + radius, position.y + radius, position.z + radius);
        List<EntitySentientSpecter> specterList = world.getEntitiesWithinAABB(EntitySentientSpecter.class, bb);
        if (!specterList.isEmpty()) {
            for (EntitySentientSpecter specter : specterList) {
                if (specter.absorbExplosion(exp)) {
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event.getEntity().getEntityWorld().isRemote)
            return;

        if (event.getSource().getTrueSource() instanceof EntityPlayer && !PlayerHelper.isFakePlayer((EntityPlayer) event.getSource().getTrueSource())) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();

            if (!player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemPackSacrifice) {
                ItemPackSacrifice pack = (ItemPackSacrifice) player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem();

                boolean shouldSyphon = pack.getStoredLP(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)) < pack.CAPACITY;
                float damageDone = event.getEntityLiving().getHealth() < event.getAmount() ? event.getAmount() - event.getEntityLiving().getHealth() : event.getAmount();
                int totalLP = Math.round(damageDone * ConfigHandler.values.coatOfArmsConversion);

                if (shouldSyphon)
                    ItemHelper.LPContainer.addLPToItem(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), totalLP, pack.CAPACITY);
            }

            if (LivingArmour.hasFullSet(player)) {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null) {

                    LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.battleHunger", chestStack);
                    if (upgrade instanceof LivingArmourUpgradeBattleHungry) {
                        ((LivingArmourUpgradeBattleHungry) upgrade).resetTimer();
                    }
                }
            }
        }
    }

    // Handles sending the client the Demon Will Aura updates
    @SubscribeEvent
    public static void onLivingUpdate(LivingUpdateEvent event) {
        if (!event.getEntityLiving().getEntityWorld().isRemote) {
            EntityLivingBase entity = event.getEntityLiving();
            if (entity instanceof EntityPlayer && entity.ticksExisted % 50 == 0) //TODO: Change to an incremental counter
            {
                sendPlayerDemonWillAura((EntityPlayer) entity);
            }

            if (event.getEntityLiving() instanceof EntityAnimal) {
                EntityAnimal animal = (EntityAnimal) event.getEntityLiving();
                if (animal.isPotionActive(RegistrarBloodMagic.SACRIFICIAL_LAMB)) {
                    if (!targetTaskMap.containsKey(animal)) {
                        EntityAITarget task = new EntityAINearestAttackableTarget<>(animal, EntityMob.class, false);
                        EntityAIBase attackTask = new EntityAIAttackMelee(animal, 1.0D, false);
                        animal.targetTasks.addTask(1, task);
                        animal.tasks.addTask(1, attackTask);
                        targetTaskMap.put(animal, task);
                        attackTaskMap.put(animal, attackTask);
                    }

                    if (animal.getAttackTarget() != null && animal.getDistanceSq(animal.getAttackTarget()) < 4) {
                        animal.getEntityWorld().createExplosion(null, animal.posX, animal.posY + (double) (animal.height / 16.0F), animal.posZ, 2 + animal.getActivePotionEffect(RegistrarBloodMagic.SACRIFICIAL_LAMB).getAmplifier() * 1.5f, false);
                        targetTaskMap.remove(animal);
                        attackTaskMap.remove(animal);
                    }
                } else if (targetTaskMap.containsKey(animal)) {
                    targetTaskMap.remove(animal);
                    attackTaskMap.remove(animal);
                }
            }
        }

        EntityLivingBase entity = event.getEntityLiving();

        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.isSneaking() && player.isPotionActive(RegistrarBloodMagic.CLING) && Utils.isPlayerBesideSolidBlockFace(player) && !player.onGround) {
                if (player.getEntityWorld().isRemote) {
                    player.motionY = 0;
                    player.motionX *= 0.8;
                    player.motionZ *= 0.8;
                } else {
                    player.fallDistance = 0;
                }
            }
        }

        if (entity.isPotionActive(MobEffects.NIGHT_VISION)) {
            int duration = entity.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
            if (duration == Constants.Misc.NIGHT_VISION_CONSTANT_END) {
                entity.removePotionEffect(MobEffects.NIGHT_VISION);
            }
        }

        if (entity.isPotionActive(RegistrarBloodMagic.FIRE_FUSE)) {
            Random random = entity.getEntityWorld().rand;
            entity.getEntityWorld().spawnParticle(EnumParticleTypes.FLAME, entity.posX + random.nextDouble() * 0.3, entity.posY + random.nextDouble() * 0.3, entity.posZ + random.nextDouble() * 0.3, 0, 0.06d, 0);

            int r = entity.getActivePotionEffect(RegistrarBloodMagic.FIRE_FUSE).getAmplifier();
            int radius = r + 1;

            if (entity.getActivePotionEffect(RegistrarBloodMagic.FIRE_FUSE).getDuration() <= 3) {
                entity.getEntityWorld().createExplosion(null, entity.posX, entity.posY, entity.posZ, radius, false);
            }
        }

        if (entity.isPotionActive(RegistrarBloodMagic.PLANT_LEECH)) {
            int amplifier = entity.getActivePotionEffect(RegistrarBloodMagic.PLANT_LEECH).getAmplifier();
            int timeRemaining = entity.getActivePotionEffect(RegistrarBloodMagic.PLANT_LEECH).getDuration();
            if (timeRemaining % 10 == 0) {
                BMPotionUtils.damageMobAndGrowSurroundingPlants(entity, 2 + amplifier, 1, 0.5 * 3 / (amplifier + 3), 25 * (1 + amplifier));
            }
        }
    }

    //    @SideOnly(Side.SERVER)
    public static void sendPlayerDemonWillAura(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            BlockPos pos = player.getPosition();
            DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(player.getEntityWorld().provider.getDimension(), pos.getX() >> 4, pos.getZ() >> 4);
            if (holder != null) {
                BloodMagicPacketHandler.sendTo(new DemonAuraPacketProcessor(holder), (EntityPlayerMP) player);
            } else {
                BloodMagicPacketHandler.sendTo(new DemonAuraPacketProcessor(new DemonWillHolder()), (EntityPlayerMP) player);
            }
        }
    }

    // Handles destroying altar
    @SubscribeEvent
    public static void harvestEvent(PlayerEvent.HarvestCheck event) {
        IBlockState state = event.getTargetBlock();
        Block block = state.getBlock();
        if (block instanceof BlockAltar && event.getEntityPlayer() != null && event.getEntityPlayer() instanceof EntityPlayerMP && !event.getEntityPlayer().getHeldItemMainhand().isEmpty() && event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemAltarMaker) {
            ItemAltarMaker altarMaker = (ItemAltarMaker) event.getEntityPlayer().getHeldItemMainhand().getItem();
            event.getEntityPlayer().sendStatusMessage(new TextComponentTranslation("chat.bloodmagic.altarMaker.destroy", altarMaker.destroyAltar(event.getEntityPlayer())), true);
        }
    }

    // Handle Teleposer block blacklist
    @SubscribeEvent
    public static void onTelepose(TeleposeEvent event) {
        if (BloodMagicAPI.INSTANCE.getBlacklist().getTeleposer().contains(event.initialState) || BloodMagicAPI.INSTANCE.getBlacklist().getTeleposer().contains(event.finalState))
            event.setCanceled(true);
    }

    // Handle Teleposer entity blacklist
    @SubscribeEvent
    public static void onTeleposeEntity(TeleposeEvent.Ent event) {
        EntityEntry entry = EntityRegistry.getEntry(event.entity.getClass());
        if (entry != null && BloodMagicAPI.INSTANCE.getBlacklist().getTeleposerEntities().contains(entry.getRegistryName()))
            event.setCanceled(true);
    }

    // Sets teleport cooldown for Teleposed entities to 5 ticks (1/4 second) instead of 150 (7.5 seconds)
    @SubscribeEvent
    public static void onTeleposeEntityPost(TeleposeEvent.Ent.Post event) {
        event.entity.timeUntilPortal = 5;
    }

    // Handles binding of IBindable's as well as setting a player's highest orb tier
    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getWorld().isRemote)
            return;

        EntityPlayer player = event.getEntityPlayer();

        if (PlayerHelper.isFakePlayer(player))
            return;

        ItemStack held = event.getItemStack();
        if (!held.isEmpty() && held.getItem() instanceof IBindable) { // Make sure it's bindable
            IBindable bindable = (IBindable) held.getItem();
            Binding binding = bindable.getBinding(held);
            if (binding == null) { // If the binding is null, let's create one
                if (bindable.onBind(player, held)) {
                    ItemBindEvent toPost = new ItemBindEvent(player, held);
                    if (MinecraftForge.EVENT_BUS.post(toPost)) // Allow cancellation of binding
                        return;

                    BindableHelper.applyBinding(held, player); // Bind item to the player
                }
            // If the binding exists, we'll check if the player's name has changed since they last used it and update that if so.
            } else if (binding.getOwnerId().equals(player.getGameProfile().getId()) && !binding.getOwnerName().equals(player.getGameProfile().getName())) {
                binding.setOwnerName(player.getGameProfile().getName());
                BindableHelper.applyBinding(held, binding);
            }
        }

        if (!held.isEmpty() && held.getItem() instanceof IBloodOrb) {
            IBloodOrb bloodOrb = (IBloodOrb) held.getItem();
            SoulNetwork network = NetworkHelper.getSoulNetwork(player);

            BloodOrb orb = bloodOrb.getOrb(held);
            if (orb == null)
                return;

            if (orb.getTier() > network.getOrbTier())
                network.setOrbTier(orb.getTier());
        }
    }

    @SubscribeEvent
    public static void selfSacrificeEvent(SacrificeKnifeUsedEvent event) {
        EntityPlayer player = event.player;

        if (LivingArmour.hasFullSet(player)) {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
            if (armour != null) {
                StatTrackerSelfSacrifice.incrementCounter(armour, event.healthDrained / 2);
                LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(BloodMagic.MODID + ".upgrade.selfSacrifice", chestStack);

                if (upgrade instanceof LivingArmourUpgradeSelfSacrifice) {
                    double modifier = ((LivingArmourUpgradeSelfSacrifice) upgrade).getSacrificeModifier();

                    event.lpAdded = (int) (event.lpAdded * (1 + modifier));
                }
            }
        }
    }

    // Drop Blood Shards
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        EntityLivingBase attackedEntity = event.getEntityLiving();
        DamageSource source = event.getSource();
        Entity entity = source.getTrueSource();

        if (entity != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack heldStack = player.getHeldItemMainhand();

            if (!heldStack.isEmpty() && heldStack.getItem() == RegistrarBloodMagicItems.BOUND_SWORD && !(attackedEntity instanceof EntityAnimal))
                for (int i = 0; i <= EnchantmentHelper.getLootingModifier(player); i++)
                    if (attackedEntity.getEntityWorld().rand.nextDouble() < 0.2)
                        event.getDrops().add(new EntityItem(attackedEntity.getEntityWorld(), attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, new ItemStack(RegistrarBloodMagicItems.BLOOD_SHARD, 1, 0)));
        }
    }

    // Experience Tome
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onExperiencePickup(PlayerPickupXpEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, player);

        if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
            int i = Math.min(xpToDurability(event.getOrb().xpValue), itemstack.getItemDamage());
            event.getOrb().xpValue -= durabilityToXp(i);
            itemstack.setItemDamage(itemstack.getItemDamage() - i);
        }

        if (!player.getEntityWorld().isRemote) {
            for (ItemStack stack : player.inventory.mainInventory) {
                if (stack.getItem() instanceof ItemExperienceBook) {
                    ItemExperienceBook.addExperience(stack, event.getOrb().xpValue);
                    event.getOrb().xpValue = 0;
                    break;
                }
            }
        }
    }

    private static int xpToDurability(int xp) {
        return xp * 2;
    }

    private static int durabilityToXp(int durability) {
        return durability / 2;
    }
}
