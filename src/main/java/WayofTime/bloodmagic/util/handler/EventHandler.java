package WayofTime.bloodmagic.util.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import WayofTime.bloodmagic.network.PacketSyncConfig;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
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
import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.api.soul.IDemonWillWeapon;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.block.BlockAltar;
import WayofTime.bloodmagic.demonAura.PosXY;
import WayofTime.bloodmagic.demonAura.WillChunk;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import WayofTime.bloodmagic.item.ItemAltarMaker;
import WayofTime.bloodmagic.item.ItemInscriptionTool;
import WayofTime.bloodmagic.item.ItemUpgradeTome;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.item.gear.ItemPackSacrifice;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerArrowShot;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerDigging;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerGrimReaperSprint;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerHealthboost;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerMeleeDamage;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerPhysicalProtect;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSelfSacrifice;
import WayofTime.bloodmagic.livingArmour.tracker.StatTrackerSolarPowered;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeArrowShot;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeDigging;
import WayofTime.bloodmagic.livingArmour.upgrade.LivingArmourUpgradeGrimReaperSprint;
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
        if (event.entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entityLiving;

            if (LivingArmour.hasFullSet(player))
            {
                ItemStack chestStack = player.getCurrentArmor(2);
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
    public void onServerWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.side == Side.CLIENT)
        {
            return;
        }
        int dim = event.world.provider.getDimensionId();
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
        int dim = event.world.provider.getDimensionId();
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
        int dim = event.world.provider.getDimensionId();
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

    @SubscribeEvent
    public void onLoggedIn(PlayerLoggedInEvent event)
    {
        BloodMagicPacketHandler.sendTo(new PacketSyncConfig(), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if (event.entityLiving.isPotionActive(ModPotions.boost))
            {
                player.stepHeight = 1.0f;
            } else
            {
                boolean hasAssist = false;
                if (LivingArmour.hasFullSet(player))
                {
                    ItemStack chestStack = player.getCurrentArmor(2);
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
                ItemStack chestStack = player.getCurrentArmor(2);
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

            if (event.entityLiving.isPotionActive(ModPotions.boost))
            {
                int i = event.entityLiving.getActivePotionEffect(ModPotions.boost).getAmplifier();
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
        int chestIndex = 2;

        if (event.entity.worldObj.isRemote)
            return;

        if (event.source.getEntity() instanceof EntityPlayer && !PlayerHelper.isFakePlayer((EntityPlayer) event.source.getEntity()))
        {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();

            if (player.getCurrentArmor(chestIndex) != null && player.getCurrentArmor(chestIndex).getItem() instanceof ItemPackSacrifice)
            {
                ItemPackSacrifice pack = (ItemPackSacrifice) player.getCurrentArmor(chestIndex).getItem();

                boolean shouldSyphon = pack.getStoredLP(player.getCurrentArmor(chestIndex)) < pack.CAPACITY;
                float damageDone = event.entityLiving.getHealth() < event.ammount ? event.ammount - event.entityLiving.getHealth() : event.ammount;
                int totalLP = Math.round(damageDone * ConfigHandler.sacrificialPackConversion);

                if (shouldSyphon)
                    pack.addLP(player.getCurrentArmor(chestIndex), totalLP);
            }
        }
    }

    @SubscribeEvent
    public void onAnvil(AnvilUpdateEvent event)
    {
        if (ConfigHandler.thaumcraftGogglesUpgrade)
        {
            if (event.left.getItem() == ModItems.livingArmourHelmet && event.right.getItem() == Constants.Compat.THAUMCRAFT_GOGGLES && !event.right.isItemDamaged())
            {
                ItemStack output = new ItemStack(ModItems.upgradeTome);
                output = NBTHelper.checkNBT(output);
                ItemUpgradeTome.setKey(output, Constants.Mod.MODID + ".upgrade.revealing");
                ItemUpgradeTome.setLevel(output, 1);
                event.cost = 1;

                event.output = output;

                return;
            }
        }

        if (event.left.getItem() == ModItems.upgradeTome && event.right.getItem() == ModItems.upgradeTome)
        {
            LivingArmourUpgrade leftUpgrade = ItemUpgradeTome.getUpgrade(event.left);
            if (leftUpgrade != null && ItemUpgradeTome.getKey(event.left).equals(ItemUpgradeTome.getKey(event.right)))
            {
                int leftLevel = ItemUpgradeTome.getLevel(event.left);
                int rightLevel = ItemUpgradeTome.getLevel(event.right);

                if (leftLevel == rightLevel && leftLevel < leftUpgrade.getMaxTier() - 1)
                {
                    ItemStack outputStack = event.left.copy();
                    ItemUpgradeTome.setLevel(outputStack, leftLevel + 1);
                    event.cost = leftLevel + 2;

                    event.output = outputStack;

                    return;
                }
            }
        }

        if (event.left.getItem() instanceof IUpgradeTrainer && event.right.getItem() == ModItems.upgradeTome)
        {
            LivingArmourUpgrade rightUpgrade = ItemUpgradeTome.getUpgrade(event.right);
            if (rightUpgrade != null)
            {
                String key = ItemUpgradeTome.getKey(event.right);
                ItemStack outputStack = event.left.copy();
                List<String> keyList = new ArrayList<String>();
                keyList.add(key);
                if (((IUpgradeTrainer) event.left.getItem()).setTrainedUpgrades(outputStack, keyList))
                {
                    event.cost = 1;

                    event.output = outputStack;

                    return;
                }
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
            if (LivingArmour.hasFullSet(player))
            {
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
    }

    @SubscribeEvent
    public void interactEvent(PlayerInteractEvent event)
    {
        if (event.world.isRemote)
            return;

        EntityPlayer player = event.entityPlayer;

        if (PlayerHelper.isFakePlayer(player))
            return;

        if (event.useBlock == Result.DENY && event.useItem != Result.DENY)
        {
            ItemStack held = player.getHeldItem();
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
    }

    @SubscribeEvent
    public void selfSacrificeEvent(SacrificeKnifeUsedEvent event)
    {
        EntityPlayer player = event.player;

        if (LivingArmour.hasFullSet(player))
        {
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

        if (LivingArmour.hasFullSet(player))
        {
            ItemStack chestStack = player.getCurrentArmor(2);
            LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
            if (armour != null)
            {
                StatTrackerHealthboost.incrementCounter(armour, event.amount);
                if (player.worldObj.canSeeSky(player.getPosition()) && player.worldObj.provider.isDaytime())
                {
                    StatTrackerSolarPowered.incrementCounter(armour, event.amount);
                }
            }
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

            // Living Armor Handling
            if (LivingArmour.hasFullSet(attackedPlayer))
            {
                float amount = Math.min(Utils.getModifiedDamage(attackedPlayer, event.source, event.ammount), attackedPlayer.getHealth());
                ItemStack chestStack = attackedPlayer.getCurrentArmor(2);
                LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
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
                float amount = Math.min(Utils.getModifiedDamage(attackedEntity, event.source, event.ammount), attackedEntity.getHealth());
                ItemStack chestStack = player.getCurrentArmor(2);
                LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
                if (armour != null)
                {
                    if (!source.isProjectile())
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

        if (LivingArmour.hasFullSet(player))
        {
            ItemStack chestStack = player.getCurrentArmor(2);
            LivingArmour armour = ItemLivingArmour.armourMap.get(chestStack);
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
            ItemStack soulStack = ((IDemonWill) ModItems.monsterSoul).createWill(0, amountOfSouls);
            event.drops.add(new EntityItem(attackedEntity.worldObj, attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, soulStack));
        }

        if (entity != null && entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack heldStack = player.getHeldItem();
            if (heldStack != null && heldStack.getItem() instanceof IDemonWillWeapon && !player.worldObj.isRemote)
            {
                IDemonWillWeapon demonWillWeapon = (IDemonWillWeapon) heldStack.getItem();
                List<ItemStack> droppedSouls = demonWillWeapon.getRandomDemonWillDrop(attackedEntity, player, heldStack, event.lootingLevel);
                if (!droppedSouls.isEmpty())
                {
                    ItemStack remainder;
                    for (ItemStack willStack : droppedSouls)
                    {
                        remainder = PlayerDemonWillHandler.addDemonWill(player, willStack);
                        if (remainder != null && ((IDemonWill) remainder.getItem()).getWill(remainder) >= 0.0001)
                            event.drops.add(new EntityItem(attackedEntity.worldObj, attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, remainder));
                    }
                    player.inventoryContainer.detectAndSendChanges();
                }
            }

            if (heldStack != null && heldStack.getItem() == ModItems.boundSword && !(attackedEntity instanceof EntityAnimal))
                for (int i = 0; i <= EnchantmentHelper.getLootingModifier(player); i++)
                    if (this.random.nextDouble() < 0.2)
                        event.drops.add(new EntityItem(attackedEntity.worldObj, attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, new ItemStack(ModItems.bloodShard, 1, 0)));
        }
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event)
    {
        ItemStack stack = event.item.getEntityItem();
        if (stack != null && stack.getItem() instanceof IDemonWill)
        {
            EntityPlayer player = event.entityPlayer;

            ItemStack remainder = PlayerDemonWillHandler.addDemonWill(player, stack);

            if (remainder == null || ((IDemonWill) stack.getItem()).getWill(stack) < 0.0001 || PlayerDemonWillHandler.isDemonWillFull(EnumDemonWillType.DEFAULT, player))
            {
                stack.stackSize = 0;
                event.setResult(Result.ALLOW);
            }
        }
    }
}
