package WayofTime.bloodmagic.util.handler.event;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.soul.*;
import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.demonAura.PosXY;
import WayofTime.bloodmagic.demonAura.WillChunk;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID)
public class WillHandler {

    private static final HashMap<Integer, Integer> SERVER_TICKS = new HashMap<>();

    // Adds Will to player
    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        ItemStack stack = event.getItem().getItem();
        if (stack.getItem() instanceof IDemonWill) {
            EntityPlayer player = event.getEntityPlayer();
            EnumDemonWillType pickupType = ((IDemonWill) stack.getItem()).getType(stack);
            ItemStack remainder = PlayerDemonWillHandler.addDemonWill(player, stack);

            if (remainder == null || ((IDemonWill) stack.getItem()).getWill(pickupType, stack) < 0.0001 || PlayerDemonWillHandler.isDemonWillFull(pickupType, player)) {
                stack.setCount(0);
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingDeathEvent event) {
        if (event.getSource() instanceof EntityDamageSourceIndirect) {
            Entity sourceEntity = event.getSource().getImmediateSource();

            if (sourceEntity instanceof EntitySentientArrow) {
                ((EntitySentientArrow) sourceEntity).reimbursePlayer(event.getEntityLiving(), event.getEntityLiving().getMaxHealth());
            }
        }
    }

    // Add/Drop Demon Will for Player
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        EntityLivingBase attackedEntity = event.getEntityLiving();
        DamageSource source = event.getSource();
        Entity entity = source.getTrueSource();

        if (attackedEntity.isPotionActive(RegistrarBloodMagic.SOUL_SNARE) && (attackedEntity instanceof EntityMob || attackedEntity.getEntityWorld().getDifficulty() == EnumDifficulty.PEACEFUL)) {
            PotionEffect eff = attackedEntity.getActivePotionEffect(RegistrarBloodMagic.SOUL_SNARE);
            int lvl = eff.getAmplifier();

            double amountOfSouls = attackedEntity.getEntityWorld().rand.nextDouble() * (lvl + 1) * (lvl + 1) * 5;
            ItemStack soulStack = ((IDemonWill) RegistrarBloodMagicItems.MONSTER_SOUL).createWill(0, amountOfSouls);
            event.getDrops().add(new EntityItem(attackedEntity.getEntityWorld(), attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, soulStack));
        }

        if (entity != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack heldStack = player.getHeldItemMainhand();
            if (heldStack.getItem() instanceof IDemonWillWeapon && !player.getEntityWorld().isRemote) {
                IDemonWillWeapon demonWillWeapon = (IDemonWillWeapon) heldStack.getItem();
                List<ItemStack> droppedSouls = demonWillWeapon.getRandomDemonWillDrop(attackedEntity, player, heldStack, event.getLootingLevel());
                if (!droppedSouls.isEmpty()) {
                    ItemStack remainder;
                    for (ItemStack willStack : droppedSouls) {
                        remainder = PlayerDemonWillHandler.addDemonWill(player, willStack);

                        if (!remainder.isEmpty()) {
                            EnumDemonWillType pickupType = ((IDemonWill) remainder.getItem()).getType(remainder);
                            if (((IDemonWill) remainder.getItem()).getWill(pickupType, remainder) >= 0.0001) {
                                event.getDrops().add(new EntityItem(attackedEntity.getEntityWorld(), attackedEntity.posX, attackedEntity.posY, attackedEntity.posZ, remainder));
                            }
                        }
                    }
                    player.inventoryContainer.detectAndSendChanges();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote)
            return;

        int dim = event.world.provider.getDimension();
        if (event.phase == TickEvent.Phase.END) {
            if (!SERVER_TICKS.containsKey(dim))
                SERVER_TICKS.put(dim, 0);

            int ticks = (SERVER_TICKS.get(dim));

            if (ticks % 20 == 0) {
                CopyOnWriteArrayList<PosXY> dirtyChunks = WorldDemonWillHandler.dirtyChunks.get(dim);
                if ((dirtyChunks != null) && (dirtyChunks.size() > 0)) {
                    for (PosXY pos : dirtyChunks)
                        event.world.markChunkDirty(new BlockPos(pos.x * 16, 5, pos.y * 16), null);

                    dirtyChunks.clear();
                }
            }

            SERVER_TICKS.put(dim, ticks + 1);
        }

    }

    @SubscribeEvent
    public static void chunkSave(ChunkDataEvent.Save event) {
        int dim = event.getWorld().provider.getDimension();
        ChunkPos loc = event.getChunk().getPos();

        NBTTagCompound nbt = new NBTTagCompound();
        event.getData().setTag("BloodMagic", nbt);

        WillChunk ac = WorldDemonWillHandler.getWillChunk(dim, loc.x, loc.z);
        if (ac != null) {
            nbt.setShort("base", ac.getBase());
            ac.getCurrentWill().writeToNBT(nbt, "current");
            if (!event.getChunk().isLoaded())
                WorldDemonWillHandler.removeWillChunk(dim, loc.x, loc.z);
        }
    }

    @SubscribeEvent
    public static void chunkLoad(ChunkDataEvent.Load event) {
        int dim = event.getWorld().provider.getDimension();
        if (event.getData().getCompoundTag("BloodMagic").hasKey("base")) {
            NBTTagCompound nbt = event.getData().getCompoundTag("BloodMagic");
            short base = nbt.getShort("base");
            DemonWillHolder current = new DemonWillHolder();
            current.readFromNBT(nbt, "current");
            WorldDemonWillHandler.addWillChunk(dim, event.getChunk(), base, current);
        } else {
            WorldDemonWillHandler.generateWill(event.getChunk());
        }
    }
}
