package wayoftime.bloodmagic.util.handler.event;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.demonaura.PosXY;
import wayoftime.bloodmagic.demonaura.WillChunk;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.will.DemonWillHolder;
import wayoftime.bloodmagic.will.EnumDemonWillType;
import wayoftime.bloodmagic.will.IDemonWill;
import wayoftime.bloodmagic.will.IDemonWillWeapon;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

@Mod.EventBusSubscriber(modid = BloodMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WillHandler
{
	private static final HashMap<ResourceLocation, Integer> SERVER_TICKS = new HashMap<>();

	// Adds Will to player
	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event)
	{
		ItemStack stack = event.getItem().getItem();
		if (stack.getItem() instanceof IDemonWill)
		{
			PlayerEntity player = event.getPlayer();
			EnumDemonWillType pickupType = ((IDemonWill) stack.getItem()).getType(stack);
			ItemStack remainder = PlayerDemonWillHandler.addDemonWill(player, stack);

			if (remainder == null || ((IDemonWill) stack.getItem()).getWill(pickupType, stack) < 0.0001
					|| PlayerDemonWillHandler.isDemonWillFull(pickupType, player))
			{
				stack.setCount(0);
				event.setResult(Event.Result.ALLOW);
			}
		}
	}

//	@SubscribeEvent
//	public static void onEntityAttacked(LivingDeathEvent event)
//	{
//		if (event.getSource() instanceof EntityDamageSourceIndirect)
//		{
//			Entity sourceEntity = event.getSource().getImmediateSource();
//
//			if (sourceEntity instanceof EntitySentientArrow)
//			{
//				((EntitySentientArrow) sourceEntity).reimbursePlayer(event.getEntityLiving(), event.getEntityLiving().getMaxHealth());
//			}
//		}
//	}

	// Add/Drop Demon Will for Player
	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event)
	{
		LivingEntity attackedEntity = event.getEntityLiving();
		DamageSource source = event.getSource();
		Entity entity = source.getTrueSource();

		if (attackedEntity.isPotionActive(BloodMagicPotions.SOUL_SNARE) && (attackedEntity instanceof MobEntity
				|| attackedEntity.getEntityWorld().getDifficulty() == Difficulty.PEACEFUL))
		{
			EffectInstance eff = attackedEntity.getActivePotionEffect(BloodMagicPotions.SOUL_SNARE);
			int lvl = eff.getAmplifier();

			double amountOfSouls = attackedEntity.getEntityWorld().rand.nextDouble() * (lvl + 1) * (lvl + 1) * 4 + 1;
			ItemStack soulStack = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_RAW.get()).createWill(amountOfSouls);
			event.getDrops().add(new ItemEntity(attackedEntity.getEntityWorld(), attackedEntity.getPosX(), attackedEntity.getPosY(), attackedEntity.getPosZ(), soulStack));
		}

		if (entity != null && entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) entity;
			ItemStack heldStack = player.getHeldItemMainhand();
			if (heldStack.getItem() instanceof IDemonWillWeapon && !player.getEntityWorld().isRemote)
			{
				IDemonWillWeapon demonWillWeapon = (IDemonWillWeapon) heldStack.getItem();
				List<ItemStack> droppedSouls = demonWillWeapon.getRandomDemonWillDrop(attackedEntity, player, heldStack, event.getLootingLevel());
				if (!droppedSouls.isEmpty())
				{
					ItemStack remainder;
					for (ItemStack willStack : droppedSouls)
					{
						remainder = PlayerDemonWillHandler.addDemonWill(player, willStack);

						if (!remainder.isEmpty())
						{
							EnumDemonWillType pickupType = ((IDemonWill) remainder.getItem()).getType(remainder);
							if (((IDemonWill) remainder.getItem()).getWill(pickupType, remainder) >= 0.0001)
							{
								event.getDrops().add(new ItemEntity(attackedEntity.getEntityWorld(), attackedEntity.getPosX(), attackedEntity.getPosY(), attackedEntity.getPosZ(), remainder));
							}
						}
					}
					player.container.detectAndSendChanges();
				}
			}
		}
	}

	@SubscribeEvent
	public void onServerWorldTick(TickEvent.WorldTickEvent event)
	{
		if (event.world.isRemote)
			return;

		ResourceLocation rl = WorldDemonWillHandler.getDimensionResourceLocation(event.world);
//        int dim = event.world.provider.getDimension();
		if (event.phase == TickEvent.Phase.END)
		{
			if (!SERVER_TICKS.containsKey(rl))
				SERVER_TICKS.put(rl, 0);

			int ticks = (SERVER_TICKS.get(rl));

			if (ticks % 20 == 0)
			{
				CopyOnWriteArrayList<PosXY> dirtyChunks = WorldDemonWillHandler.dirtyChunks.get(rl);
				if ((dirtyChunks != null) && (dirtyChunks.size() > 0))
				{
					for (PosXY pos : dirtyChunks)
						event.world.markChunkDirty(new BlockPos(pos.x * 16, 5, pos.y * 16), null);

					dirtyChunks.clear();
				}
			}

			SERVER_TICKS.put(rl, ticks + 1);
		}

	}

	@SubscribeEvent
	public void chunkSave(ChunkDataEvent.Save event)
	{
		if (!(event.getWorld() instanceof World))
		{
			return;
		}
		ResourceLocation rl = WorldDemonWillHandler.getDimensionResourceLocation((World) event.getWorld());
//		int dim = event.getWorld().provider.getDimension();

		ChunkPos loc = event.getChunk().getPos();

		CompoundNBT nbt = new CompoundNBT();
		event.getData().put("BloodMagic", nbt);

		WillChunk ac = WorldDemonWillHandler.getWillChunk(rl, loc.x, loc.z);
		if (ac != null)
		{
			nbt.putShort("base", ac.getBase());
			ac.getCurrentWill().writeToNBT(nbt, "current");
//			if (event.getChunk() instanceof Chunk && !((Chunk) event.getChunk()).setLoaded(loaded);)
			if (!event.getWorld().getChunkProvider().isChunkLoaded(event.getChunk().getPos()))
				WorldDemonWillHandler.removeWillChunk(rl, loc.x, loc.z);
		}
	}

	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event)
	{
		if (!(event.getWorld() instanceof World))
		{
			return;
		}
		ResourceLocation rl = WorldDemonWillHandler.getDimensionResourceLocation((World) event.getWorld());
//		int dim = event.getWorld().provider.getDimension();
		if (event.getData().getCompound("BloodMagic").contains("base"))
		{
			CompoundNBT nbt = event.getData().getCompound("BloodMagic");
			short base = nbt.getShort("base");
			DemonWillHolder current = new DemonWillHolder();
			current.readFromNBT(nbt, "current");
			WorldDemonWillHandler.addWillChunk(rl, event.getChunk(), base, current);
		} else
		{
			WorldDemonWillHandler.generateWill(event.getChunk(), (World) event.getWorld());
		}
	}
}
