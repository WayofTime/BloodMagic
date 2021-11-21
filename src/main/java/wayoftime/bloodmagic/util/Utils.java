package wayoftime.bloodmagic.util;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import wayoftime.bloodmagic.api.compat.IDemonWillViewer;
import wayoftime.bloodmagic.tile.TileInventory;
import wayoftime.bloodmagic.util.helper.NBTHelper;

public class Utils
{
	/**
	 * @param tile   - The {@link TileInventory} to input the item to
	 * @param player - The player to take the item from.
	 * @return {@code true} if the ItemStack is inserted, {@code false} otherwise
	 * @see #insertItemToTile(TileInventory, PlayerEntity, int)
	 */
	public static boolean insertItemToTile(TileInventory tile, PlayerEntity player)
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
	public static boolean insertItemToTile(TileInventory tile, PlayerEntity player, int slot)
	{
		ItemStack slotStack = tile.getStackInSlot(slot);
		if (slotStack.isEmpty() && !player.getHeldItemMainhand().isEmpty())
		{
			ItemStack input = player.getHeldItemMainhand().copy();
			input.setCount(1);
			player.getHeldItemMainhand().shrink(1);
			tile.setInventorySlotContents(slot, input);
			return true;
		} else if (!slotStack.isEmpty() && player.getHeldItemMainhand().isEmpty())
		{
			ItemHandlerHelper.giveItemToPlayer(player, slotStack);
			tile.clear();
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
		return entity.isImmuneToFire() || entity.isPotionActive(Effects.FIRE_RESISTANCE);
	}

	public static boolean isBlockLiquid(BlockState state)
	{
		return (state instanceof IFluidBlock || state.getMaterial().isLiquid());
	}

	public static boolean isFlowingLiquid(World world, BlockPos pos, BlockState state)
	{
		Block block = state.getBlock();
		return ((block instanceof IFluidBlock && Math.abs(((IFluidBlock) block).getFilledPercentage(world, pos)) == 1) || (block instanceof FlowingFluidBlock && !((FlowingFluidBlock) block).getFluidState(state).isSource()));
	}

	public static boolean spawnStackAtBlock(World world, BlockPos pos, @Nullable Direction pushDirection, ItemStack stack)
	{
		BlockPos spawnPos = new BlockPos(pos);

		double velX = 0;
		double velY = 0;
		double velZ = 0;
		double velocity = 0.15D;
		if (pushDirection != null)
		{
			spawnPos = spawnPos.offset(pushDirection);

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
		entityItem.setMotion(velX, velY, velZ);

		entityItem.setItem(stack);
		return world.addEntity(entityItem);
	}

	public static boolean swapLocations(World initialWorld, BlockPos initialPos, World finalWorld, BlockPos finalPos)
	{
		return swapLocations(initialWorld, initialPos, finalWorld, finalPos, true);
	}

	public static boolean swapLocations(World initialWorld, BlockPos initialPos, World finalWorld, BlockPos finalPos, boolean playSound)
	{
		TileEntity initialTile = initialWorld.getTileEntity(initialPos);
		TileEntity finalTile = finalWorld.getTileEntity(finalPos);
		CompoundNBT initialTag = new CompoundNBT();
		CompoundNBT finalTag = new CompoundNBT();
		if (initialTile != null)
			initialTile.write(initialTag);
		if (finalTile != null)
			finalTile.write(finalTag);

		BlockState initialState = initialWorld.getBlockState(initialPos);
		BlockState finalState = finalWorld.getBlockState(finalPos);

		if ((initialState.getBlock().equals(Blocks.AIR) && finalState.getBlock().equals(Blocks.AIR)) || initialState.getBlock() instanceof NetherPortalBlock || finalState.getBlock() instanceof NetherPortalBlock)
			return false;

		if (playSound)
		{
			initialWorld.playSound(null, initialPos.getX(), initialPos.getY(), initialPos.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F);
			finalWorld.playSound(null, finalPos.getX(), finalPos.getY(), finalPos.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F);
		}

		// Finally, we get to do something! (CLEARING TILES)
		if (finalState.getBlock().hasTileEntity(finalState))
			finalWorld.removeTileEntity(finalPos);
		if (initialState.getBlock().hasTileEntity(initialState))
			initialWorld.removeTileEntity(initialPos);

		// TILES CLEARED
		BlockState initialBlockState = initialWorld.getBlockState(initialPos);
		BlockState finalBlockState = finalWorld.getBlockState(finalPos);
		finalWorld.setBlockState(finalPos, initialBlockState, 3);

		if (initialTile != null)
		{
//			TileEntity newTileInitial = TileEntity.create(finalWorld, initialTag);
			TileEntity newTileInitial = TileEntity.readTileEntity(finalBlockState, initialTag);

			finalWorld.setTileEntity(finalPos, newTileInitial);
//			newTileInitial.setPos(finalPos);
			newTileInitial.setWorldAndPos(finalWorld, finalPos);
		}

		initialWorld.setBlockState(initialPos, finalBlockState, 3);

		if (finalTile != null)
		{
//			TileEntity newTileFinal = TileEntity.create(initialWorld, finalTag);
			TileEntity newTileFinal = TileEntity.readTileEntity(initialBlockState, finalTag);

			initialWorld.setTileEntity(initialPos, newTileFinal);
//			newTileFinal.setPos(initialPos);
			newTileFinal.setWorldAndPos(initialWorld, initialPos);
		}

		initialWorld.notifyNeighborsOfStateChange(initialPos, finalState.getBlock());
		finalWorld.notifyNeighborsOfStateChange(finalPos, initialState.getBlock());

		return true;
	}

	public static ItemStack insertStackIntoTile(ItemStack stack, TileEntity tile, Direction dir)
	{
		LazyOptional<IItemHandler> capability = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir);
		if (capability.isPresent())
		{
			IItemHandler handler = capability.resolve().get();

			return insertStackIntoTile(stack, handler);
		} else if (tile instanceof IInventory)
		{
			return insertStackIntoInventory(stack, (IInventory) tile, dir);
		}

		return stack;
	}

	public static ItemStack insertStackIntoTile(ItemStack stack, IItemHandler handler)
	{
		int numberOfSlots = handler.getSlots();

		ItemStack copyStack = stack.copy();

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

	public static int getNumberOfFreeSlots(TileEntity tile, Direction dir)
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
		} else if (tile instanceof IInventory)
		{
			for (int i = 0; i < ((IInventory) tile).getSizeInventory(); i++)
			{
				if (((IInventory) tile).getStackInSlot(i).isEmpty())
				{
					slots++;
				}
			}
		}

		return slots;
	}

	public static ItemStack insertStackIntoInventory(ItemStack stack, IInventory inventory, Direction dir)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		boolean[] canBeInserted = new boolean[inventory.getSizeInventory()];

		if (inventory instanceof ISidedInventory)
		{
			int[] array = ((ISidedInventory) inventory).getSlotsForFace(dir);
			for (int in : array)
			{
				canBeInserted[in] = inventory.isItemValidForSlot(in, stack) && ((ISidedInventory) inventory).canInsertItem(in, stack, dir);
			}
		} else
		{
			for (int i = 0; i < canBeInserted.length; i++)
			{
				canBeInserted[i] = inventory.isItemValidForSlot(i, stack);
			}
		}

		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			if (!canBeInserted[i])
			{
				continue;
			}

			ItemStack[] combinedStacks = combineStacks(stack, inventory.getStackInSlot(i));
			stack = combinedStacks[0];
			inventory.setInventorySlotContents(i, combinedStacks[1]);

			if (stack.isEmpty())
			{
				return ItemStack.EMPTY;
			}
		}

		return stack;
	}

	public static boolean canInsertStackFullyIntoInventory(ItemStack stack, IInventory inventory, Direction dir, boolean fillToLimit, int limit)
	{
		if (stack.isEmpty())
		{
			return true;
		}

		int itemsLeft = stack.getCount();

		boolean[] canBeInserted = new boolean[inventory.getSizeInventory()];

		if (inventory instanceof ISidedInventory)
		{
			int[] array = ((ISidedInventory) inventory).getSlotsForFace(dir);
			for (int in : array)
			{
				canBeInserted[in] = inventory.isItemValidForSlot(in, stack) && ((ISidedInventory) inventory).canInsertItem(in, stack, dir);
			}
		} else
		{
			for (int i = 0; i < canBeInserted.length; i++)
			{
				canBeInserted[i] = inventory.isItemValidForSlot(i, stack);
			}
		}

		int numberMatching = 0;

		if (fillToLimit)
		{
			for (int i = 0; i < inventory.getSizeInventory(); i++)
			{
				if (!canBeInserted[i])
				{
					continue;
				}

				ItemStack invStack = inventory.getStackInSlot(i);

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

		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			if (!canBeInserted[i])
			{
				continue;
			}

			ItemStack invStack = inventory.getStackInSlot(i);
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

	public static boolean canPlayerSeeDemonWill(PlayerEntity player)
	{
		IItemHandler inventory = new PlayerMainInvWrapper(player.inventory);

		for (int i = 0; i < inventory.getSlots(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			if (stack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) stack.getItem()).canSeeDemonWillAura(player.getEntityWorld(), stack, player))
			{
				return true;
			}
		}

		ItemStack offhandStack = player.getHeldItemOffhand();
		if (!offhandStack.isEmpty() && offhandStack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) offhandStack.getItem()).canSeeDemonWillAura(player.getEntityWorld(), offhandStack, player))
		{
			return true;
		}

		return false;
	}

	public static double getDemonWillResolution(PlayerEntity player)
	{
		IItemHandler inventory = new PlayerMainInvWrapper(player.inventory);

		for (int i = 0; i < inventory.getSlots(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack.isEmpty())
			{
				continue;
			}

			if (stack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) stack.getItem()).canSeeDemonWillAura(player.getEntityWorld(), stack, player))
			{
				return ((IDemonWillViewer) stack.getItem()).getDemonWillAuraResolution(player.getEntityWorld(), stack, player);
			}
		}

		ItemStack offhandStack = player.getHeldItemOffhand();
		if (!offhandStack.isEmpty() && offhandStack.getItem() instanceof IDemonWillViewer && ((IDemonWillViewer) offhandStack.getItem()).canSeeDemonWillAura(player.getEntityWorld(), offhandStack, player))
		{
			return ((IDemonWillViewer) offhandStack.getItem()).getDemonWillAuraResolution(player.getEntityWorld(), offhandStack, player);
		}

		return 100;
	}

	public static int plantSeedsInArea(World world, AxisAlignedBB aabb, int horizontalRadius, int verticalRadius)
	{
		int placedBlocks = 0;
		List<ItemEntity> itemEntities = world.getEntitiesWithinAABB(ItemEntity.class, aabb);

		for (ItemEntity itemEntity : itemEntities)
		{
			placedBlocks += plantEntityItem(itemEntity, horizontalRadius, verticalRadius);
		}

		return placedBlocks;
	}

	public static int plantItemStack(World world, BlockPos centralPos, ItemStack stack, int horizontalRadius, int verticalRadius)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		Item item = stack.getItem();
		if (!(item instanceof IPlantable))
		{
			return 0;
		}

		int planted = 0;

		for (int hR = 0; hR <= horizontalRadius; hR++)
		{
			for (int vR = 0; vR <= verticalRadius; vR++)
			{
				for (int i = -hR; i <= hR; i++)
				{
					for (int k = -hR; k <= hR; k++)
					{
						for (int j = -vR; j <= vR; j += 2 * vR + (vR > 0 ? 0 : 1))
						{
							if (!(Math.abs(i) == hR || Math.abs(k) == hR))
							{
								continue;
							}

							BlockPos newPos = centralPos.add(i, j, k);
							if (world.isAirBlock(newPos))
							{
								BlockPos offsetPos = newPos.offset(Direction.DOWN);
								BlockState state = world.getBlockState(offsetPos);
								if (state.getBlock().canSustainPlant(state, world, offsetPos, Direction.UP, (IPlantable) item))
								{
									BlockState plantState = ((IPlantable) item).getPlant(world, newPos);
									world.setBlockState(newPos, plantState, 3);
//									Block.
									world.playEvent(2001, newPos, Block.getStateId(plantState));
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

		World world = itemEntity.getEntityWorld();
		BlockPos pos = itemEntity.getPosition();
		ItemStack stack = itemEntity.getItem();

		int planted = plantItemStack(world, pos, stack, horizontalRadius, verticalRadius);

		if (stack.isEmpty())
		{
			itemEntity.remove();
		}

		return planted;
	}

	@Nullable
	public static IItemHandler getInventory(TileEntity tile, @Nullable Direction facing)
	{
		if (facing == null)
			facing = Direction.DOWN;

		IItemHandler itemHandler = null;

		if (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing).isPresent())
			itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing).resolve().get();
		else if (tile instanceof ISidedInventory)
			itemHandler = ((ISidedInventory) tile).getSlotsForFace(facing).length != 0
					? new SidedInvWrapper((ISidedInventory) tile, facing)
					: null;
		else if (tile instanceof IInventory)
			itemHandler = new InvWrapper((IInventory) tile);

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
			entity.addPotionEffect(new EffectInstance(Effects.ABSORPTION, duration, potionLevel, true, false));
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
}
