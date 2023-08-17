package wayoftime.bloodmagic.util;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import wayoftime.bloodmagic.api.compat.IDemonWillViewer;
import wayoftime.bloodmagic.common.tile.TileInventory;
import wayoftime.bloodmagic.util.helper.NBTHelper;

public class Utils
{
	/**
	 * @param tile   - The {@link TileInventory} to input the item to
	 * @param player - The player to take the item from.
	 * @return {@code true} if the ItemStack is inserted, {@code false} otherwise
	 * @see #insertItemToTile(TileInventory, PlayerEntity, int)
	 */
	public static boolean insertItemToTile(TileInventory tile, Player player)
	{
		return insertItemToTile(tile, player, 0);
	}

	/**
	 * Used for inserting an ItemStack with a stacksize of 1 to a tile's inventory
	 * at slot 0
	 * <p/>
	 * EG: Block Altar
	 *
	 * @param tile   - The {@link TileInventory} to input the item to
	 * @param player - The player to take the item from.
	 * @param slot   - The slot to attempt to insert to
	 * @return {@code true} if the ItemStack is inserted, {@code false} otherwise
	 */
	public static boolean insertItemToTile(TileInventory tile, Player player, int slot)
	{
		ItemStack slotStack = tile.getItem(slot);
		if (slotStack.isEmpty() && !player.getMainHandItem().isEmpty())
		{
			ItemStack input = player.getMainHandItem().copy();
			input.setCount(1);
			player.getMainHandItem().shrink(1);
			tile.setItem(slot, input);
			return true;
		} else if (!slotStack.isEmpty() && player.getMainHandItem().isEmpty())
		{
			ItemHandlerHelper.giveItemToPlayer(player, slotStack);
			tile.clearContent();
			return false;
		}

		return false;
	}

	public static String toFancyCasing(String input)
	{
		return String.valueOf(input.charAt(0)).toUpperCase(Locale.ROOT) + input.substring(1);
	}

	public static boolean isImmuneToFireDamage(LivingEntity entity)
	{
		return entity.fireImmune() || entity.hasEffect(MobEffects.FIRE_RESISTANCE);
	}

	public static boolean isBlockLiquid(BlockState state)
	{
		return (state instanceof IFluidBlock || state.getMaterial().isLiquid());
	}

	public static boolean isFlowingLiquid(Level world, BlockPos pos, BlockState state)
	{
		Block block = state.getBlock();
		return ((block instanceof IFluidBlock && Math.abs(((IFluidBlock) block).getFilledPercentage(world, pos)) == 1) || (block instanceof LiquidBlock && !((LiquidBlock) block).getFluidState(state).isSource()));
	}

	public static boolean spawnStackAtBlock(Level world, BlockPos pos, @Nullable Direction pushDirection, ItemStack stack)
	{
		BlockPos spawnPos = new BlockPos(pos);

		double velX = 0;
		double velY = 0;
		double velZ = 0;
		double velocity = 0.15D;
		if (pushDirection != null)
		{
			spawnPos = spawnPos.relative(pushDirection);

			switch (pushDirection)
			{
			case DOWN:
			{
				velY = -velocity;
				break;
			}
			case UP:
			{
				velY = velocity;
				break;
			}
			case NORTH:
			{
				velZ = -velocity;
				break;
			}
			case SOUTH:
			{
				velZ = velocity;
				break;
			}
			case WEST:
			{
				velX = -velocity;
				break;
			}
			case EAST:
			{
				velX = velocity;
				break;
			}
			}
		}

		double posX = spawnPos.getX() + 0.5;
		double posY = spawnPos.getY() + 0.5;
		double posZ = spawnPos.getZ() + 0.5;

		ItemEntity entityItem = new ItemEntity(world, posX, posY, posZ, stack);
		entityItem.setDeltaMovement(velX, velY, velZ);

		entityItem.setItem(stack);
		return world.addFreshEntity(entityItem);
	}

	public static boolean swapLocations(Level initialWorld, BlockPos initialPos, Level finalWorld, BlockPos finalPos)
	{
		return swapLocations(initialWorld, initialPos, finalWorld, finalPos, true);
	}

	public static boolean swapLocations(Level initialWorld, BlockPos initialPos, Level finalWorld, BlockPos finalPos, boolean playSound)
	{
		BlockEntity initialTile = initialWorld.getBlockEntity(initialPos);
		BlockEntity finalTile = finalWorld.getBlockEntity(finalPos);
		CompoundTag initialTag = new CompoundTag();
		CompoundTag finalTag = new CompoundTag();
//		if (initialTile != null)
//			initialTile.save(initialTag);
//		if (finalTile != null)
//			finalTile.save(finalTag);

		if (initialTile != null)
			initialTag = initialTile.saveWithFullMetadata();
		if (finalTile != null)
			finalTag = finalTile.saveWithFullMetadata();

		BlockState initialState = initialWorld.getBlockState(initialPos);
		BlockState finalState = finalWorld.getBlockState(finalPos);

		if ((initialState.getBlock().equals(Blocks.AIR) && finalState.getBlock().equals(Blocks.AIR)) || initialState.getBlock() instanceof NetherPortalBlock || finalState.getBlock() instanceof NetherPortalBlock)
			return false;

		if (playSound)
		{
			initialWorld.playSound(null, initialPos.getX(), initialPos.getY(), initialPos.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.AMBIENT, 1.0F, 1.0F);
			finalWorld.playSound(null, finalPos.getX(), finalPos.getY(), finalPos.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.AMBIENT, 1.0F, 1.0F);
		}

		// Finally, we get to do something! (CLEARING TILES)
		if (finalState.getBlock() instanceof EntityBlock)
			finalWorld.removeBlockEntity(finalPos);
		if (initialState.getBlock() instanceof EntityBlock)
			initialWorld.removeBlockEntity(initialPos);

		// TILES CLEARED
		BlockState initialBlockState = initialWorld.getBlockState(initialPos);
		BlockState finalBlockState = finalWorld.getBlockState(finalPos);
		finalWorld.setBlock(finalPos, initialBlockState, 3);
		initialWorld.setBlock(initialPos, finalBlockState, 3);

		if (initialTile != null)
		{
			BlockEntity newTileInitial = finalWorld.getBlockEntity(finalPos);

			// Just in case...
			if (newTileInitial != null)
			{
				newTileInitial.load(initialTag);
			}
		}

		if (finalTile != null)
		{
			BlockEntity newTileFinal = initialWorld.getBlockEntity(initialPos);

			// Just in case...
			if (newTileFinal != null)
			{
				newTileFinal.load(finalTag);
			}
		}

		initialWorld.updateNeighborsAt(initialPos, finalState.getBlock());
		finalWorld.updateNeighborsAt(finalPos, initialState.getBlock());

		// Block tick scheduling
		if (initialWorld.getBlockTicks().hasScheduledTick(initialPos, initialState.getBlock()))
		{
			finalWorld.scheduleTick(finalPos, initialState.getBlock(), 20);
		}

		if (finalWorld.getBlockTicks().hasScheduledTick(finalPos, finalState.getBlock()))
		{
			initialWorld.scheduleTick(initialPos, finalState.getBlock(), 20);
		}

		return true;
	}

	public static ItemStack insertStackIntoTile(ItemStack stack, BlockEntity tile, Direction dir)
	{
		LazyOptional<IItemHandler> capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);
		if (capability.isPresent())
		{
			IItemHandler handler = capability.resolve().get();

			return insertStackIntoTile(stack, handler);
		} else if (tile instanceof Container)
		{
			return insertStackIntoInventory(stack, (Container) tile, dir);
		}

		return stack;
	}

	public static ItemStack insertStackIntoTile(ItemStack stack, IItemHandler handler, boolean doCleanly)
	{
		int numberOfSlots = handler.getSlots();

		ItemStack copyStack = stack.copy();

		if (doCleanly)
		{
			for (int slot = 0; slot < numberOfSlots; slot++)
			{
				ItemStack containedStack = handler.getStackInSlot(slot);
				if (ItemHandlerHelper.canItemStacksStack(stack, containedStack))
				{
					copyStack = handler.insertItem(slot, copyStack, false);
					if (copyStack.isEmpty())
					{
						return ItemStack.EMPTY;
					}
				}
			}
		}

		for (int slot = 0; slot < numberOfSlots; slot++)
		{
			copyStack = handler.insertItem(slot, copyStack, false);
			if (copyStack.isEmpty())
			{
				return ItemStack.EMPTY;
			}
		}

		return copyStack;
	}

	public static ItemStack insertStackIntoTile(ItemStack stack, IItemHandler handler)
	{
		return insertStackIntoTile(stack, handler, false);
	}

	public static int getNumberOfFreeSlots(BlockEntity tile, Direction dir)
	{
		int slots = 0;

		LazyOptional<IItemHandler> capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);
		if (capability.isPresent())
		{
			IItemHandler handler = capability.resolve().get();

			for (int i = 0; i < handler.getSlots(); i++)
			{
				if (handler.getStackInSlot(i).isEmpty())
				{
					slots++;
				}
			}
		} else if (tile instanceof Container)
		{
			for (int i = 0; i < ((Container) tile).getContainerSize(); i++)
			{
				if (((Container) tile).getItem(i).isEmpty())
				{
					slots++;
				}
			}
		}

		return slots;
	}

	public static ItemStack insertStackIntoInventory(ItemStack stack, Container inventory, Direction dir)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		boolean[] canBeInserted = new boolean[inventory.getContainerSize()];

		if (inventory instanceof WorldlyContainer)
		{
			int[] array = ((WorldlyContainer) inventory).getSlotsForFace(dir);
			for (int in : array)
			{
				canBeInserted[in] = inventory.canPlaceItem(in, stack) && ((WorldlyContainer) inventory).canPlaceItemThroughFace(in, stack, dir);
			}
		} else
		{
			for (int i = 0; i < canBeInserted.length; i++)
			{
				canBeInserted[i] = inventory.canPlaceItem(i, stack);
			}
		}

		for (int i = 0; i < inventory.getContainerSize(); i++)
		{
			if (!canBeInserted[i])
			{
				continue;
			}

			ItemStack[] combinedStacks = combineStacks(stack, inventory.getItem(i));
			stack = combinedStacks[0];
			inventory.setItem(i, combinedStacks[1]);

			if (stack.isEmpty())
			{
				return ItemStack.EMPTY;
			}
		}

		return stack;
	}

	public static boolean canInsertStackFullyIntoInventory(ItemStack stack, IItemHandler itemHandler, boolean fillToLimit, int limit)
	{
		if (stack.isEmpty())
		{
			return true;
		}

		int itemsLeft = stack.getCount();

		boolean[] canBeInserted = new boolean[itemHandler.getSlots()];

		for (int i = 0; i < canBeInserted.length; i++)
		{
			canBeInserted[i] = itemHandler.isItemValid(i, stack);
		}

		int numberMatching = 0;

		if (fillToLimit)
		{
			for (int i = 0; i < itemHandler.getSlots(); i++)
			{
				if (!canBeInserted[i])
				{
					continue;
				}

				ItemStack invStack = itemHandler.getStackInSlot(i);

				if (!invStack.isEmpty() && ItemHandlerHelper.canItemStacksStack(stack, invStack))
				{
					numberMatching += invStack.getCount();
				}
			}
		}

		if (fillToLimit && limit < stack.getCount() + numberMatching)
		{
			return false;
		}

		for (int i = 0; i < itemHandler.getSlots(); i++)
		{
			if (!canBeInserted[i])
			{
				continue;
			}

			ItemStack invStack = itemHandler.getStackInSlot(i);
			if (invStack.isEmpty())
			{
				itemsLeft = 0;
			} else
			{
				boolean canCombine = ItemHandlerHelper.canItemStacksStack(stack, invStack);
				if (canCombine)
				{
					if (invStack.isEmpty())
					{
						itemsLeft = 0;
					} else
					{
						itemsLeft -= (Math.min(invStack.getMaxStackSize(), itemHandler.getSlotLimit(i)) - invStack.getCount());
					}
				}
			}

			if (itemsLeft <= 0)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean canInsertStackFullyIntoInventory(ItemStack stack, Container inventory, Direction dir, boolean fillToLimit, int limit)
	{
		if (stack.isEmpty())
		{
			return true;
		}

		int itemsLeft = stack.getCount();

		boolean[] canBeInserted = new boolean[inventory.getContainerSize()];

		if (inventory instanceof WorldlyContainer)
		{
			int[] array = ((WorldlyContainer) inventory).getSlotsForFace(dir);
			for (int in : array)
			{
				canBeInserted[in] = inventory.canPlaceItem(in, stack) && ((WorldlyContainer) inventory).canPlaceItemThroughFace(in, stack, dir);
			}
		} else
		{
			for (int i = 0; i < canBeInserted.length; i++)
			{
				canBeInserted[i] = inventory.canPlaceItem(i, stack);
			}
		}

		int numberMatching = 0;

		if (fillToLimit)
		{
			for (int i = 0; i < inventory.getContainerSize(); i++)
			{
				if (!canBeInserted[i])
				{
					continue;
				}

				ItemStack invStack = inventory.getItem(i);

				if (!invStack.isEmpty() && ItemHandlerHelper.canItemStacksStack(stack, invStack))
				{
					numberMatching += invStack.getCount();
				}
			}
		}

		if (fillToLimit && limit < stack.getCount() + numberMatching)
		{
			return false;
		}

		for (int i = 0; i < inventory.getContainerSize(); i++)
		{
			if (!canBeInserted[i])
			{
				continue;
			}

			ItemStack invStack = inventory.getItem(i);
			boolean canCombine = ItemHandlerHelper.canItemStacksStack(stack, invStack);
			if (canCombine)
			{
				if (invStack.isEmpty())
				{
					itemsLeft = 0;
				} else
				{
					itemsLeft -= (invStack.getMaxStackSize() - invStack.getCount());
				}
			}

			if (itemsLeft <= 0)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * @param stack1 Stack that is placed into a slot
	 * @param stack2 Slot content that stack1 is placed into
	 * @return Stacks after stacking
	 */
	public static ItemStack[] combineStacks(ItemStack stack1, ItemStack stack2)
	{
		ItemStack[] returned = new ItemStack[2];

		if (ItemHandlerHelper.canItemStacksStack(stack1, stack2))
		{
			int transferedAmount = stack2.isEmpty() ? stack1.getCount()
					: Math.min(stack2.getMaxStackSize() - stack2.getCount(), stack1.getCount());
			if (transferedAmount > 0)
			{
				ItemStack copyStack = stack1.split(transferedAmount);
				if (stack2.isEmpty())
				{
					stack2 = copyStack;
				} else
				{
					stack2.grow(transferedAmount);
				}
			}
		}

		returned[0] = stack1;
		returned[1] = stack2;

		return returned;
	}

	public static boolean canPlayerSeeDemonWill(Player player)
	{
		IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());

		for (int i = 0; i < inventory.getSlots(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			if (stack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) stack.getItem()).canSeeDemonWillAura(player.getCommandSenderWorld(), stack, player))
			{
				return true;
			}
		}

		ItemStack offhandStack = player.getOffhandItem();
		if (!offhandStack.isEmpty() && offhandStack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) offhandStack.getItem()).canSeeDemonWillAura(player.getCommandSenderWorld(), offhandStack, player))
		{
			return true;
		}

		return false;
	}

	public static double getDemonWillResolution(Player player)
	{
		IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());

		for (int i = 0; i < inventory.getSlots(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			if (stack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) stack.getItem()).canSeeDemonWillAura(player.getCommandSenderWorld(), stack, player))
			{
				return ((IDemonWillViewer) stack.getItem()).getDemonWillAuraResolution(player.getCommandSenderWorld(), stack, player);
			}
		}

		ItemStack offhandStack = player.getOffhandItem();
		if (!offhandStack.isEmpty() && offhandStack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) offhandStack.getItem()).canSeeDemonWillAura(player.getCommandSenderWorld(), offhandStack, player))
		{
			return ((IDemonWillViewer) offhandStack.getItem()).getDemonWillAuraResolution(player.getCommandSenderWorld(), offhandStack, player);
		}

		return 100;
	}

	public static int plantSeedsInArea(Level world, AABB aabb, int horizontalRadius, int verticalRadius)
	{
		int placedBlocks = 0;
		List<ItemEntity> itemEntities = world.getEntitiesOfClass(ItemEntity.class, aabb);

		for (ItemEntity itemEntity : itemEntities)
		{
			placedBlocks += plantEntityItem(itemEntity, horizontalRadius, verticalRadius);
		}

		return placedBlocks;
	}

	public static int plantItemStack(Level world, BlockPos centralPos, ItemStack stack, int horizontalRadius, int verticalRadius)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		Item item = stack.getItem();
		if (!(item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof IPlantable))
		{
			return 0;
		}

		Block plantBlock = ((BlockItem) item).getBlock();
		PlantType plantType = ((IPlantable) plantBlock).getPlantType(world, centralPos);
		if (!(plantType == PlantType.CROP || plantType == PlantType.NETHER || plantBlock instanceof SugarCaneBlock || plantBlock instanceof CactusBlock))
		{
			return 0;
		}

		int planted = 0;

		for (int i = -horizontalRadius; i <= horizontalRadius; i++)
		{
			for (int k = -horizontalRadius; k <= horizontalRadius; k++)
			{
				for (int j = -verticalRadius; j <= verticalRadius; j++)
				{
					BlockPos newPos = centralPos.offset(i, j, k);
					if (world.isEmptyBlock(newPos))
					{
						BlockPos offsetPos = newPos.relative(Direction.DOWN);
						BlockState state = world.getBlockState(offsetPos);
						IPlantable plantable = (IPlantable) plantBlock;
						if (state.getBlock().canSustainPlant(state, world, offsetPos, Direction.UP, plantable))
						{
							world.setBlockAndUpdate(newPos, plantBlock.defaultBlockState());
							stack.shrink(1);
							planted++;
							if (stack.isEmpty() || stack.getCount() <= 0)
							{
								return planted;
							}
						}
					}
				}
			}
		}

		return planted;
	}

	public static int plantEntityItem(ItemEntity itemEntity, int horizontalRadius, int verticalRadius)
	{
		if (itemEntity == null || !itemEntity.isAlive())
		{
			return 0;
		}

		Level world = itemEntity.getCommandSenderWorld();
		BlockPos pos = itemEntity.blockPosition();
		ItemStack stack = itemEntity.getItem();

		int planted = plantItemStack(world, pos, stack, horizontalRadius, verticalRadius);

		if (stack.isEmpty())
		{
			itemEntity.discard();
		}

		return planted;
	}

	@Nullable
	public static IItemHandler getInventory(BlockEntity tile, @Nullable Direction facing)
	{
		if (facing == null)
			facing = Direction.DOWN;

		IItemHandler itemHandler = null;

		if (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing).isPresent())
			itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing).resolve().get();
		else if (tile instanceof WorldlyContainer)
			itemHandler = ((WorldlyContainer) tile).getSlotsForFace(facing).length != 0
					? new SidedInvWrapper((WorldlyContainer) tile, facing)
					: null;
		else if (tile instanceof Container)
			itemHandler = new InvWrapper((Container) tile);

		return itemHandler;
	}

	public static float addAbsorptionToMaximum(LivingEntity entity, float added, int maximum, int duration)
	{
		float currentAmount = entity.getAbsorptionAmount();
		added = Math.min(maximum - currentAmount, added);

		if (added <= 0)
		{
			return 0;
		}

		if (duration > 0)
		{
			int potionLevel = (int) ((currentAmount + added) / 4);
			entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, potionLevel, true, false));
		}

		entity.setAbsorptionAmount(currentAmount + added);

		return added;
	}

//	public static float getModifiedDamage(LivingEntity attackedEntity, DamageSource source, float amount)
//	{
//		if (!attackedEntity.isInvulnerableTo(source))
//		{
//			if (amount <= 0)
//				return 0;
//
//			amount = applyArmor(attackedEntity, Iterables.toArray(attackedEntity.getEquipmentAndArmor(), ItemStack.class), source, amount);
//			if (amount <= 0)
//				return 0;
//			amount = applyPotionDamageCalculations(attackedEntity, source, amount);
//
//			return amount;
//		}
//
//		return 0;
//	}
//
//	public static float applyArmor(LivingEntity entity, ItemStack[] inventory, DamageSource source, double damage)
//	{
//		damage *= 25;
//		ArrayList<ArmorProperties> dmgVals = new ArrayList<>();
//		for (int x = 0; x < inventory.length; x++)
//		{
//			ItemStack stack = inventory[x];
//			if (stack.isEmpty())
//			{
//				continue;
//			}
//			ArmorProperties prop = null;
//			if (stack.getItem() instanceof ISpecialArmor)
//			{
//				ISpecialArmor armor = (ISpecialArmor) stack.getItem();
//				prop = armor.getProperties(entity, stack, source, damage / 25D, x).copy();
//			} else if (stack.getItem() instanceof ArmorItem && !source.isUnblockable())
//			{
//				ArmorItem armor = (ArmorItem) stack.getItem();
//				prop = new ArmorProperties(0, armor.damageReduceAmount / 25D, Integer.MAX_VALUE);
//			}
//			if (prop != null)
//			{
//				prop.Slot = x;
//				dmgVals.add(prop);
//			}
//		}
//		if (dmgVals.size() > 0)
//		{
//			ArmorProperties[] props = dmgVals.toArray(new ArmorProperties[dmgVals.size()]);
//			int level = props[0].Priority;
//			double ratio = 0;
//			for (ArmorProperties prop : props)
//			{
//				if (level != prop.Priority)
//				{
//					damage -= (damage * ratio);
//					ratio = 0;
//					level = prop.Priority;
//				}
//				ratio += prop.AbsorbRatio;
//
//			}
//			damage -= (damage * ratio);
//		}
//
//		return (float) (damage / 25.0F);
//	}
//
//	public static float applyPotionDamageCalculations(LivingEntity attackedEntity, DamageSource source, float damage)
//	{
//		Effect resistance = Effects.RESISTANCE;
//
//		if (source.isDamageAbsolute())
//		{
//			return damage;
//		} else
//		{
//			if (attackedEntity.isPotionActive(resistance) && source != DamageSource.OUT_OF_WORLD)
//			{
//				int i = (attackedEntity.getActivePotionEffect(resistance).getAmplifier() + 1) * 5;
//				int j = 25 - i;
//				float f = damage * (float) j;
//				damage = f / 25.0F;
//			}
//
//			if (damage <= 0.0F)
//			{
//				return 0.0F;
//			} else
//			{
//				int k = EnchantmentHelper.getEnchantmentModifierDamage(attackedEntity.getArmorInventoryList(), source);
//
//				if (k > 20)
//				{
//					k = 20;
//				}
//
//				if (k > 0 && k <= 20)
//				{
//					int l = 25 - k;
//					float f1 = damage * (float) l;
//					damage = f1 / 25.0F;
//				}
//
//				return damage;
//			}
//		}
//	}

	public static boolean hasUUID(ItemStack stack)
	{
		return stack.hasTag() && stack.getTag().contains(Constants.NBT.MOST_SIG) && stack.getTag().contains(Constants.NBT.LEAST_SIG);
	}

	public static UUID getUUID(ItemStack stack)
	{
		if (!hasUUID(stack))
		{
			return null;
		}

		return new UUID(stack.getTag().getLong(Constants.NBT.MOST_SIG), stack.getTag().getLong(Constants.NBT.LEAST_SIG));
	}

	public static void setUUID(ItemStack stack)
	{
		stack = NBTHelper.checkNBT(stack);

		if (!stack.getTag().contains(Constants.NBT.MOST_SIG) && !stack.getTag().contains(Constants.NBT.LEAST_SIG))
		{
			UUID itemUUID = UUID.randomUUID();
			stack.getTag().putLong(Constants.NBT.MOST_SIG, itemUUID.getMostSignificantBits());
			stack.getTag().putLong(Constants.NBT.LEAST_SIG, itemUUID.getLeastSignificantBits());
		}
	}

	public static boolean isMeleeDamage(DamageSource source)
	{
		if (source.isProjectile() || source.isExplosion() || source.isFall() || source.isFire())
		{
			return false;
		}

		return source instanceof EntityDamageSource;
	}
}
