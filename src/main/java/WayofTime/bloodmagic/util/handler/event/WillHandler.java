package WayofTime.bloodmagic.util.handler.event;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import WayofTime.bloodmagic.annot.Handler;
import WayofTime.bloodmagic.api.soul.DemonWillHolder;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.IDemonWill;
import WayofTime.bloodmagic.api.soul.IDemonWillWeapon;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.demonAura.PosXY;
import WayofTime.bloodmagic.demonAura.WillChunk;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.entity.projectile.EntitySentientArrow;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.registry.ModPotions;

@Handler
public class WillHandler
{

    private final HashMap<Integer, Integer> serverTicks = new HashMap<Integer, Integer>();

    // Adds Will to player
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
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public void onEntityAttacked(LivingDeathEvent event)
    {
        if (event.getSource() instanceof EntityDamageSourceIndirect)
        {
            Entity sourceEntity = ((EntityDamageSourceIndirect) event.getSource()).getSourceOfDamage();

            if (sourceEntity instanceof EntitySentientArrow)
            {
                ((EntitySentientArrow) sourceEntity).reimbursePlayer(event.getEntityLiving(), event.getEntityLiving().getMaxHealth());
            }
        }
    }

    // Add/Drop Demon Will for Player
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event)
    {
        EntityLivingBase attackedEntity = event.getEntityLiving();
        DamageSource source = event.getSource();
        Entity entity = source.getEntity();

        if (attackedEntity.isPotionActive(ModPotions.soulSnare) && (attackedEntity instanceof EntityMob || attackedEntity.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL))
        {
            PotionEffect eff = attackedEntity.getActivePotionEffect(ModPotions.soulSnare);
            int lvl = eff.getAmplifier();

            double amountOfSouls = attackedEntity.getEntityWorld().rand.nextDouble() * (lvl + 1) * (lvl + 1) * 5;
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
        }
    }

    @SubscribeEvent
    public void onServerWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.world.isRemote)
            return;

        int dim = event.world.provider.getDimension();
        if (event.phase == TickEvent.Phase.END)
        {
            if (!this.serverTicks.containsKey(dim))
                this.serverTicks.put(dim, 0);

            int ticks = (this.serverTicks.get(dim));

            if (ticks % 20 == 0)
            {
                CopyOnWriteArrayList<PosXY> dirtyChunks = WorldDemonWillHandler.dirtyChunks.get(dim);
                if ((dirtyChunks != null) && (dirtyChunks.size() > 0))
                {
                    for (PosXY pos : dirtyChunks)
                        event.world.markChunkDirty(new BlockPos(pos.x * 16, 5, pos.y * 16), null);

                    dirtyChunks.clear();
                }
            }

            this.serverTicks.put(dim, ticks + 1);
        }

    }

    @SubscribeEvent
    public void chunkSave(ChunkDataEvent.Save event)
    {
        int dim = event.getWorld().provider.getDimension();
        ChunkPos loc = event.getChunk().getChunkCoordIntPair();

        NBTTagCompound nbt = new NBTTagCompound();
        event.getData().setTag("BloodMagic", nbt);

        WillChunk ac = WorldDemonWillHandler.getWillChunk(dim, loc.chunkXPos, loc.chunkZPos);
        if (ac != null)
        {
            nbt.setShort("base", ac.getBase());
            ac.getCurrentWill().writeToNBT(nbt, "current");
            if (!event.getChunk().isLoaded())
                WorldDemonWillHandler.removeWillChunk(dim, loc.chunkXPos, loc.chunkZPos);
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
}
