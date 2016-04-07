package WayofTime.bloodmagic.util.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.api.BloodMagicAPI;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.AltarCraftedEvent;
import WayofTime.bloodmagic.api.event.ItemBindEvent;
import WayofTime.bloodmagic.api.event.SacrificeKnifeUsedEvent;
import WayofTime.bloodmagic.api.event.TeleposeEvent;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.iface.IUpgradeTrainer;
import WayofTime.bloodmagic.api.livingArmour.LivingArmourUpgrade;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.api.soul.IDemonWillWeapon;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.demonAura.PosXY;
import WayofTime.bloodmagic.demonAura.WillChunk;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import WayofTime.bloodmagic.item.ItemAltarMaker;
import WayofTime.bloodmagic.item.ItemExperienceBook;
import WayofTime.bloodmagic.item.ItemInscriptionTool;
import WayofTime.bloodmagic.item.ItemUpgradeTome;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.armour.ItemSentientArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerArrowProtect;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerArrowShot;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerDigging;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerExperience;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerFallProtect;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerGraveDigger;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerGrimReaperSprint;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerHealthboost;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerJump;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerMeleeDamage;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerPhysicalProtect;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSolarPowered;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeArrowShot;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeDigging;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeExperience;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeGrimReaperSprint;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeJump;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeSpeed;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeStepAssist;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.registry.ModPotions;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.TextHelper;

import com.google.common.base.Strings;

public class EventHandler
{
    Random random = new Random();
    HashMap<Integer, Integer> serverTicks = new HashMap<Integer, Integer>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityDeath(LivingDeathEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
                if (armour != null)
                {
                    StatTrackerGrimReaperSprint.incrementCounter(armour);

                    LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(Constants.Mod.MODID + ".upgrade.grimReaper", chestStack);

                    if (upgrade instanceof LivingArmourUpgradeGrimReaperSprint && ((LivingArmourUpgradeGrimReaperSprint) upgrade).canSavePlayer(player))
                    {
                        ((LivingArmourUpgradeGrimReaperSprint) upgrade).applyEffectOnRebirth(player);
                        event.setCanceled(true);
                        event.setResult(Result.DENY);
                    }

                    armour.writeDirtyToNBT(ItemLivingArmour.getArmourTag(chestStack));
                }
            }
        }
    }

    @SubscribeEvent
    public void onJumpEvent(LivingEvent.LivingJumpEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
                if (armour != null)
                {
                    StatTrackerJump.incrementCounter(armour);

                    if (!player.isSneaking())
                    {
                        LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(Constants.Mod.MODID + ".upgrade.jump", chestStack);

                        if (upgrade instanceof LivingArmourUpgradeJump)
                        {
                            player.motionY += ((LivingArmourUpgradeJump) upgrade).getJumpModifier();
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onServerWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.side == Side.CLIENT)
        {
            return;
        }
        int dim = event.world.provider.getDimension();
        if (event.phase == TickEvent.Phase.END)
        {
            if (!this.serverTicks.containsKey(Integer.valueOf(dim)))
            {
                this.serverTicks.put(Integer.valueOf(dim), Integer.valueOf(0));
            }

            int ticks = ((Integer) this.serverTicks.get(Integer.valueOf(dim))).intValue();

            if (ticks % 20 == 0)
            {
                CopyOnWriteArrayList<PosXY> dirtyChunks = WorldDemonWillHandler.dirtyChunks.get(Integer.valueOf(dim));
                if ((dirtyChunks != null) && (dirtyChunks.size() > 0))
                {
                    for (PosXY pos : dirtyChunks)
                    {
                        event.world.markChunkDirty(new BlockPos(pos.x * 16, 5, pos.y * 16), null);
                    }

                    dirtyChunks.clear();
                }
            }

            this.serverTicks.put(Integer.valueOf(dim), Integer.valueOf(ticks + 1));
        }

    }

    @SubscribeEvent
    public void chunkSave(ChunkDataEvent.Save event)
    {
        int dim = event.getWorld().provider.getDimension();
        ChunkCoordIntPair loc = event.getChunk().getChunkCoordIntPair();

        NBTTagCompound nbt = new NBTTagCompound();
        event.getData().setTag("BloodMagic", nbt);

        WillChunk ac = WorldDemonWillHandler.getWillChunk(dim, loc.chunkXPos, loc.chunkZPos);
        if (ac != null)
        {
            nbt.setShort("base", ac.getBase());
            ac.getCurrentWill().writeToNBT(nbt, "current");
            if (!event.getChunk().isLoaded())
            {
                WorldDemonWillHandler.removeWillChunk(dim, loc.chunkXPos, loc.chunkZPos);
            }
        }
    }

    @SubscribeEvent
    public void chunkLoad(ChunkDataEvent.Load event)
    {
        int dim = event.getWorld().provider.getDimension();
        if (event.getData().getCompoundTag("BloodMagic").hasKey("base"))
        {
            NBTTagCompound nbt = event.getData().getCompoundTag("BloodMagic");
            short base = nbt.getShort("base");
            DemonWillHolder current = new DemonWillHolder();
            current.readFromNBT(nbt, "current");
            WorldDemonWillHandler.addWillChunk(dim, event.getChunk(), base, current);
        } else
        {
            WorldDemonWillHandler.generateWill(event.getChunk());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (event.getEntityLiving().isPotionActive(ModPotions.boost))
            {
                player.stepHeight = 1.0f;
            } else
            {
                boolean hasAssist = false;
                if (LivingArmour.hasFullSet(player))
                {
                    ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                    LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                    if (armour != null)
                    {
                        LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(Constants.Mod.MODID + ".upgrade.stepAssist", chestStack);

                        if (upgrade instanceof LivingArmourUpgradeStepAssist)
                        {
                            player.stepHeight = ((LivingArmourUpgradeStepAssist) upgrade).getStepAssist();
                            hasAssist = true;
                        }
                    }
                }

                if (!hasAssist)
                    player.stepHeight = 0.6f;
            }

            float percentIncrease = 0;

            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null)
                {
                    LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(Constants.Mod.MODID + ".upgrade.movement", chestStack);

                    if (upgrade instanceof LivingArmourUpgradeSpeed)
                    {
                        percentIncrease += 0.1f * ((LivingArmourUpgradeSpeed) upgrade).getSpeedModifier();
                    }
                }
            }

            if (event.getEntityLiving().isPotionActive(ModPotions.boost))
            {
                int i = event.getEntityLiving().getActivePotionEffect(ModPotions.boost).getAmplifier();
                {
                    percentIncrease += (i + 1) * 0.05f;
                }
            }

            if (percentIncrease > 0 && (player.onGround || player.capabilities.isFlying) && player.moveForward > 0F)
            {
                player.moveFlying(0F, 1F, player.capabilities.isFlying ? (percentIncrease / 2.0f) : percentIncrease);
            }
        }
    }

    @SubscribeEvent
    public void onAltarCrafted(AltarCraftedEvent event)
    {
        if (event.getOutput() == null)
        {
            return;
        }

        if (event.getOutput().getItem() instanceof ItemInscriptionTool)
        {
            NBTHelper.checkNBT(event.getOutput());
            event.getOutput().getTagCompound().setInteger(Constants.NBT.USES, 10);
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
                    pack.addLP(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), totalLP);
            }
        }
    }

    @SubscribeEvent
    public void onAnvil(AnvilUpdateEvent event)
    {
        if (ConfigHandler.thaumcraftGogglesUpgrade)
        {
            if (event.getLeft().getItem() == ModItems.livingArmourHelmet && event.getRight().getItem() == Constants.Compat.THAUMCRAFT_GOGGLES && !event.getRight().isItemDamaged())
            {
                ItemStack output = new ItemStack(ModItems.upgradeTome);
                output = NBTHelper.checkNBT(output);
                ItemUpgradeTome.setKey(output, Constants.Mod.MODID + ".upgrade.revealing");
                ItemUpgradeTome.setLevel(output, 1);
                event.setCost(1);

                event.setOutput(output);

                return;
            }
        }

        if (event.getLeft().getItem() == ModItems.upgradeTome && event.getRight().getItem() == ModItems.upgradeTome)
        {
            LivingArmourUpgrade leftUpgrade = ItemUpgradeTome.getUpgrade(event.getLeft());
            if (leftUpgrade != null && ItemUpgradeTome.getKey(event.getLeft()).equals(ItemUpgradeTome.getKey(event.getRight())))
            {
                int leftLevel = ItemUpgradeTome.getLevel(event.getLeft());
                int rightLevel = ItemUpgradeTome.getLevel(event.getRight());

                if (leftLevel == rightLevel && leftLevel < leftUpgrade.getMaxTier() - 1)
                {
                    ItemStack outputStack = event.getLeft().copy();
                    ItemUpgradeTome.setLevel(outputStack, leftLevel + 1);
                    event.setCost(leftLevel + 2);

                    event.setOutput(outputStack);

                    return;
                }
            }
        }

        if (event.getLeft().getItem() instanceof IUpgradeTrainer && event.getRight().getItem() == ModItems.upgradeTome)
        {
            LivingArmourUpgrade rightUpgrade = ItemUpgradeTome.getUpgrade(event.getRight());
            if (rightUpgrade != null)
            {
                String key = ItemUpgradeTome.getKey(event.getRight());
                ItemStack outputStack = event.getLeft().copy();
                List<String> keyList = new ArrayList<String>();
                keyList.add(key);
                if (((IUpgradeTrainer) event.getLeft().getItem()).setTrainedUpgrades(outputStack, keyList))
                {
                    event.setCost(1);

                    event.setOutput(outputStack);

                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event)
    {
        if (event.getEmptyBucket().getItem() != Items.bucket)
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

    @SubscribeEvent
    public void harvestEvent(PlayerEvent.HarvestCheck event)
    {
        IBlockState state = event.getTargetBlock();
        Block block = state.getBlock();
        if (block != null && block instanceof BlockAltar && event.getEntityPlayer() != null && event.getEntityPlayer() instanceof EntityPlayerMP && event.getEntityPlayer().getActiveItemStack() != null && event.getEntityPlayer().getActiveItemStack().getItem() instanceof ItemAltarMaker)
        {
            ItemAltarMaker altarMaker = (ItemAltarMaker) event.getEntityPlayer().getActiveItemStack().getItem();
            ChatUtil.sendNoSpam(event.getEntityPlayer(), TextHelper.localizeEffect("chat.BloodMagic.altarMaker.destroy", altarMaker.destroyAltar(event.getEntityPlayer())));
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
        if (event.getModID().equals(Constants.Mod.MODID))
            ConfigHandler.syncConfig();
    }

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
                    LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);

                    if (armour != null)
                    {
                        StatTrackerDigging.incrementCounter(armour);
                        LivingArmourUpgradeDigging.hasDug(armour);
                    }
                }
            }
        }
    }

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
                StatTrackerSelfSacrifice.incrementCounter(armour);
                LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(Constants.Mod.MODID + ".upgrade.selfSacrifice", chestStack);

                if (upgrade instanceof LivingArmourUpgradeSelfSacrifice)
                {
                    double modifier = ((LivingArmourUpgradeSelfSacrifice) upgrade).getSacrificeModifier();

                    event.lpAdded = (int) (event.lpAdded * (1 + modifier));
                }
            }
        }
    }

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
            LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
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

    private static float lastPlayerSwingStrength = 0;

    @SubscribeEvent
    public void onLivingAttack(AttackEntityEvent event)
    {
        lastPlayerSwingStrength = event.getEntityPlayer().getCooledAttackStrength(0);
    }

    @SubscribeEvent
    public void onEntityAttacked(LivingHurtEvent event)
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
                LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
                if (armour != null)
                {
                    if (sourceEntity != null && !source.isMagicDamage() && !source.isProjectile())
                    {
                        // Add resistance to the upgrade that protects against non-magic damage
                        StatTrackerPhysicalProtect.incrementCounter(armour, amount);
                    }

                    if (source.equals(DamageSource.fall))
                    {
                        StatTrackerFallProtect.incrementCounter(armour, amount);
                    }

                    if (source.isProjectile())
                    {
                        StatTrackerArrowProtect.incrementCounter(armour, amount);
                    }
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

        if (sourceEntity instanceof EntitySentientArrow)
        {
            // Soul Weapon handling
            ((EntitySentientArrow) sourceEntity).reimbursePlayer();
        }

        if (sourceEntity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) sourceEntity;

            // Living Armor Handling
            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
                if (armour != null)
                {
                    ItemStack mainWeapon = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

                    event.setAmount((float) (event.getAmount() + lastPlayerSwingStrength * armour.getAdditionalDamageOnHit(event.getAmount(), player, attackedEntity, mainWeapon)));

                    float amount = Math.min(Utils.getModifiedDamage(attackedEntity, event.getSource(), event.getAmount()), attackedEntity.getHealth());

                    if (!source.isProjectile())
                    {
                        StatTrackerMeleeDamage.incrementCounter(armour, amount);

                        if (mainWeapon != null && mainWeapon.getItem() instanceof ItemSpade)
                        {
                            StatTrackerGraveDigger.incrementCounter(armour, amount);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onArrowFire(ArrowLooseEvent event)
    {
        World world = event.getEntityPlayer().worldObj;
        ItemStack stack = event.getBow();
        EntityPlayer player = event.getEntityPlayer();

        if (LivingArmour.hasFullSet(player))
        {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
            if (armour != null)
            {
                StatTrackerArrowShot.incrementCounter(armour);

                LivingArmourUpgrade upgrade = ItemLivingArmour.getUpgrade(Constants.Mod.MODID + ".upgrade.arrowShot", chestStack);
                if (upgrade instanceof LivingArmourUpgradeArrowShot)
                {
                    int i = event.getCharge();
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
                        ItemArrow itemarrow = (ItemArrow) Items.arrow;
                        EntityArrow entityarrow = itemarrow.createArrow(world, new ItemStack(Items.arrow), player);

                        double velocityModifier = 0.6 * f;
                        entityarrow.motionX += (random.nextDouble() - 0.5) * velocityModifier;
                        entityarrow.motionY += (random.nextDouble() - 0.5) * velocityModifier;
                        entityarrow.motionZ += (random.nextDouble() - 0.5) * velocityModifier;

                        if (f == 1.0F)
                        {
                            entityarrow.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.power, stack);

                        if (j > 0)
                        {
                            entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.punch, stack);

                        if (k > 0)
                        {
                            entityarrow.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.flame, stack) > 0)
                        {
                            entityarrow.setFire(100);
                        }

                        entityarrow.canBePickedUp = EntityArrow.PickupStatus.CREATIVE_ONLY;

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
        EntityLivingBase attackedEntity = event.getEntityLiving();
        DamageSource source = event.getSource();
        Entity entity = source.getEntity();

        if (attackedEntity.isPotionActive(ModPotions.soulSnare) && attackedEntity instanceof EntityMob)
        {
            PotionEffect eff = attackedEntity.getActivePotionEffect(ModPotions.soulSnare);
            int lvl = eff.getAmplifier();

            double amountOfSouls = random.nextDouble() * (lvl + 1) * (lvl + 1) * 5;
            ItemStack soulStack = ((IDemonWill) ModItems.monsterSoul).createWill(0, amountOfSouls);
            event.getDrops().add(new EntityItem(attackedEntity.worldObj, attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, soulStack));
        }

        if (entity != null && entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack heldStack = player.getHeldItemMainhand();
            if (heldStack != null && heldStack.getItem() instanceof IDemonWillWeapon && !player.worldObj.isRemote)
            {
                IDemonWillWeapon demonWillWeapon = (IDemonWillWeapon) heldStack.getItem();
                List<ItemStack> droppedSouls = demonWillWeapon.getRandomDemonWillDrop(attackedEntity, player, heldStack, event.getLootingLevel());
                if (!droppedSouls.isEmpty())
                {
                    ItemStack remainder;
                    for (ItemStack willStack : droppedSouls)
                    {
                        remainder = PlayerDemonWillHandler.addDemonWill(player, willStack);
                        if (remainder != null && ((IDemonWill) remainder.getItem()).getWill(remainder) >= 0.0001)
                            event.getDrops().add(new EntityItem(attackedEntity.worldObj, attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, remainder));
                    }
                    player.inventoryContainer.detectAndSendChanges();
                }
            }

            if (heldStack != null && heldStack.getItem() == ModItems.boundSword && !(attackedEntity instanceof EntityAnimal))
                for (int i = 0; i <= EnchantmentHelper.getLootingModifier(player); i++)
                    if (this.random.nextDouble() < 0.2)
                        event.getDrops().add(new EntityItem(attackedEntity.worldObj, attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, new ItemStack(ModItems.bloodShard, 1, 0)));
        }
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event)
    {
        ItemStack stack = event.getItem().getEntityItem();
        if (stack != null && stack.getItem() instanceof IDemonWill)
        {
            EntityPlayer player = event.getEntityPlayer();

            ItemStack remainder = PlayerDemonWillHandler.addDemonWill(player, stack);

            if (remainder == null || ((IDemonWill) stack.getItem()).getWill(stack) < 0.0001 || PlayerDemonWillHandler.isDemonWillFull(EnumDemonWillType.DEFAULT, player))
            {
                stack.stackSize = 0;
                event.setResult(Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public void onExperiencePickup(PlayerPickupXpEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();

        if (LivingArmour.hasFullSet(player))
        {
            ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
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
}
