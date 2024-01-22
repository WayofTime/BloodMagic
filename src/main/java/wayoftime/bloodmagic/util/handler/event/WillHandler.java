package wayoftime.bloodmagic.util.handler.event;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDemonWill;
import wayoftime.bloodmagic.api.compat.IDemonWillWeapon;
import wayoftime.bloodmagic.common.item.BloodMagicItems;
import wayoftime.bloodmagic.demonaura.PosXY;
import wayoftime.bloodmagic.demonaura.WillChunk;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.potion.BloodMagicPotions;
import wayoftime.bloodmagic.will.DemonWillHolder;
import wayoftime.bloodmagic.will.PlayerDemonWillHandler;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

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
			Player player = event.getEntity();
			EnumDemonWillType pickupType = ((IDemonWill) stack.getItem()).getType(stack);
			ItemStack remainder = PlayerDemonWillHandler.addDemonWill(player, stack);

			if (remainder == null || ((IDemonWill) stack.getItem()).getWill(pickupType, stack) < 0.0001 || PlayerDemonWillHandler.isDemonWillFull(pickupType, player))
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
		LivingEntity attackedEntity = event.getEntity();
		DamageSource source = event.getSource();
		Entity entity = source.getEntity();

		if (attackedEntity.hasEffect(BloodMagicPotions.SOUL_SNARE.get()) && (attackedEntity instanceof Mob || attackedEntity.getCommandSenderWorld().getDifficulty() == Difficulty.PEACEFUL))
		{
			MobEffectInstance eff = attackedEntity.getEffect(BloodMagicPotions.SOUL_SNARE.get());
			int lvl = eff.getAmplifier();

			double amountOfSouls = attackedEntity.getCommandSenderWorld().random.nextDouble() * (lvl + 1) * (lvl + 1) * 4 + 1;
			ItemStack soulStack = ((IDemonWill) BloodMagicItems.MONSTER_SOUL_RAW.get()).createWill(amountOfSouls);
			event.getDrops().add(new ItemEntity(attackedEntity.getCommandSenderWorld(), attackedEntity.getX(), attackedEntity.getY(), attackedEntity.getZ(), soulStack));
		}

		if (entity != null && entity instanceof Player)
		{
			Player player = (Player) entity;
			ItemStack heldStack = player.getMainHandItem();
			if (heldStack.getItem() instanceof IDemonWillWeapon && !player.getCommandSenderWorld().isClientSide)
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
								event.getDrops().add(new ItemEntity(attackedEntity.getCommandSenderWorld(), attackedEntity.getX(), attackedEntity.getY(), attackedEntity.getZ(), remainder));
							}
						}
					}
					player.inventoryMenu.broadcastChanges();
				}
			}
		}
	}

	@SubscribeEvent
	public void onServerWorldTick(TickEvent.LevelTickEvent event)
	{
		if (event.level.isClientSide)
			return;

		ResourceLocation rl = WorldDemonWillHandler.getDimensionResourceLocation(event.level);
//        int dim = event.world.provider.getDimension();
		if (event.phase == TickEvent.Phase.END)
		{
			if (!SERVER_TICKS.containsKey(rl))
				SERVER_TICKS.put(rl, 0);

			int ticks = (SERVER_TICKS.get(rl));

			if (ticks % 20 == 0)
			{
//				CopyOnWriteArrayList<PosXY> dirtyChunks = WorldDemonWillHandler.dirtyChunks.get(rl);
				ConcurrentLinkedQueue<PosXY> dirtyChunks = WorldDemonWillHandler.dirtyChunks.get(rl);
				if ((dirtyChunks != null) && (dirtyChunks.size() > 0))
				{
					for (PosXY pos : dirtyChunks)
					{
						ChunkAccess chunk = event.level.getChunk(pos.x, pos.y, ChunkStatus.FULL, false);
						if (chunk != null)
						{
							chunk.setUnsaved(true);
						}
					}

					dirtyChunks.clear();
				}
			}

			SERVER_TICKS.put(rl, ticks + 1);
		}

	}

	public static boolean isBlockLoaded(BlockGetter world, BlockPos pos)
	{

		if (world == null || pos.getY() < world.getMinBuildHeight() || pos.getY() > world.getMaxBuildHeight())
		{
			return false;
		} else if (world instanceof LevelReader)
		{
			// Note: We don't bother checking if it is a world and then isBlockPresent
			// because
			// all that does is also validate the y value is in bounds, and we already check
			// to make
			// sure the position is valid both in the y and xz directions
			return ((LevelReader) world).hasChunkAt(pos);
		}
		return true;
	}

	@SubscribeEvent
	public void chunkSave(ChunkDataEvent.Save event)
	{
		if (!(event.getLevel() instanceof Level))
		{
			return;
		}
		ResourceLocation rl = WorldDemonWillHandler.getDimensionResourceLocation((Level) event.getLevel());
//		int dim = event.getWorld().provider.getDimension();

		ChunkPos loc = event.getChunk().getPos();

		CompoundTag nbt = new CompoundTag();
		event.getData().put("BloodMagic", nbt);

		WillChunk ac = WorldDemonWillHandler.getWillChunk(rl, loc.x, loc.z);
		if (ac != null)
		{
			nbt.putShort("base", ac.getBase());
			ac.getCurrentWill().writeToNBT(nbt, "current");
//			if (event.getChunk() instanceof Chunk && !((Chunk) event.getChunk()).setLoaded(loaded);)
//			event.getWorld().getChunkSource().getChunk(p_62228_, p_62229_, p_62230_)
//			if (!event.getWorld().getChunkSource().isEntityTickingChunk(event.getChunk().getPos()))
//				WorldDemonWillHandler.removeWillChunk(rl, loc.x, loc.z);
		}
	}

	@SubscribeEvent
	public void chunkUnload(ChunkDataEvent.Unload event)
	{
		if (!(event.getLevel() instanceof Level))
		{
			return;
		}
		ResourceLocation rl = WorldDemonWillHandler.getDimensionResourceLocation((Level) event.getLevel());

		ChunkPos loc = event.getChunk().getPos();

		WillChunk ac = WorldDemonWillHandler.getWillChunk(rl, loc.x, loc.z);
		if (ac != null)
		{
			WorldDemonWillHandler.removeWillChunk(rl, loc.x, loc.z);
		}
	}

	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event)
	{
		if (!(event.getLevel() instanceof Level))
		{
			return;
		}
		ResourceLocation rl = WorldDemonWillHandler.getDimensionResourceLocation((Level) event.getLevel());
//		int dim = event.getWorld().provider.getDimension();
		if (event.getData().getCompound("BloodMagic").contains("base"))
		{
			CompoundTag nbt = event.getData().getCompound("BloodMagic");
			short base = nbt.getShort("base");
			DemonWillHolder current = new DemonWillHolder();
			current.readFromNBT(nbt, "current");
			WorldDemonWillHandler.addWillChunk(rl, event.getChunk(), base, current);
		} else
		{
			WorldDemonWillHandler.generateWill(event.getChunk(), (Level) event.getLevel());
		}
	}
}
