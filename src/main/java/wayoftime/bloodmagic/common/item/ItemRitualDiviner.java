package wayoftime.bloodmagic.common.item;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BlockRitualStone;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.common.tile.TileMasterRitualStone;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.RitualHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemRitualDiviner extends Item
{
	final int type;
	public static final String tooltipBase = "tooltip.bloodmagic.diviner.";
	public static String[] names = { "normal", "dusk", "dawn" };

	public ItemRitualDiviner(int type)
	{
		super(new Item.Properties().stacksTo(1));
		this.type = type;
	}

	public boolean getActivated(ItemStack stack)
	{
		return !stack.isEmpty() && NBTHelper.checkNBT(stack).getTag().getBoolean(Constants.NBT.ACTIVATED);
	}

	public ItemStack setActivatedState(ItemStack stack, boolean activated)
	{
		if (!stack.isEmpty())
		{
			NBTHelper.checkNBT(stack).getTag().putBoolean(Constants.NBT.ACTIVATED, activated);
			return stack;
		}

		return stack;
	}

	public void setStoredPos(ItemStack stack, BlockPos pos)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}

		CompoundTag tag = stack.getTag();

		tag.putInt(Constants.NBT.X_COORD, pos.getX());
		tag.putInt(Constants.NBT.Y_COORD, pos.getY());
		tag.putInt(Constants.NBT.Z_COORD, pos.getZ());
	}

	public BlockPos getStoredPos(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}

		CompoundTag tag = stack.getTag();

		return new BlockPos(tag.getInt(Constants.NBT.X_COORD), tag.getInt(Constants.NBT.Y_COORD), tag.getInt(Constants.NBT.Z_COORD));
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (entityIn instanceof Player && getActivated(stack))
		{
			if (entityIn.tickCount % 4 == 0)
			{
				BlockPos pos = getStoredPos(stack);

				if (!addRuneToRitual(stack, worldIn, pos, (Player) entityIn))
				{
					setActivatedState(stack, false);
				} else
				{
					if (worldIn.isClientSide)
					{
						spawnParticles(worldIn, pos, 30);
					}
				}
			}
		}
	}

//	@Override
//	public String getHighlightTip(ItemStack stack, String displayName)
//	{
//		if (Strings.isNullOrEmpty(getCurrentRitual(stack)))
//			return displayName;
//
//		Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(getCurrentRitual(stack));
//		if (ritual == null)
//			return displayName;
//
//		return displayName + ": " + TextHelper.localize(ritual.getTranslationKey());
//	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
		if (context.getPlayer().isShiftKeyDown())
		{
			if (context.getLevel().isClientSide)
			{
				trySetDisplayedRitual(stack, context.getLevel(), context.getClickedPos());
			}

			return InteractionResult.SUCCESS;
		} else if (addRuneToRitual(stack, context.getLevel(), context.getClickedPos(), context.getPlayer()))
		{
			setStoredPos(stack, context.getClickedPos());
			setActivatedState(stack, true);

			if (context.getLevel().isClientSide)
			{
				spawnParticles(context.getLevel(), context.getClickedPos().relative(context.getClickedFace()), 15);
			}

			return InteractionResult.SUCCESS;
			// TODO: Have the diviner automagically build the ritual
		}

		return InteractionResult.PASS;
	}

	/**
	 * Adds a single rune to the ritual.
	 *
	 * @param stack  - The Ritual Diviner stack
	 * @param world  - The World
	 * @param pos    - Block Position of the MRS.
	 * @param player - The Player attempting to place the ritual
	 * @return - True if a rune was successfully added
	 */
	public boolean addRuneToRitual(ItemStack stack, Level world, BlockPos pos, Player player)
	{
		BlockEntity tile = world.getBlockEntity(pos);

		if (tile instanceof TileMasterRitualStone)
		{
			Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(this.getCurrentRitual(stack));
			if (ritual != null)
			{
				Direction direction = getDirection(stack);
				List<RitualComponent> components = Lists.newArrayList();
				ritual.gatherComponents(components::add);
				for (RitualComponent component : components)
				{
					if (!canPlaceRitualStone(component.getRuneType(), stack))
					{
						return false;
					}
					BlockPos offset = component.getOffset(direction);
					BlockPos newPos = pos.offset(offset);
					BlockState state = world.getBlockState(newPos);
					Block block = state.getBlock();
					if (RitualHelper.isRune(world, newPos))
					{
						if (RitualHelper.isRuneType(world, newPos, component.getRuneType()))
						{
							if (world.isClientSide)
							{
								undisplayHologram();
							}
						} else
						{
							// Replace existing ritual stone
							RitualHelper.setRuneType(world, newPos, component.getRuneType());
							return true;
						}
					} else
					{
						BlockPlaceContext ctx = new BlockPlaceContext(world, null, InteractionHand.MAIN_HAND, ItemStack.EMPTY, BlockHitResult.miss(new Vec3(0, 0, 0), Direction.UP, newPos));

						if (state.canBeReplaced(ctx))// || block.isReplaceable(world, newPos))
						{
							if (!consumeStone(stack, world, player))
							{
								return false;
							}
							((BlockRitualStone) BloodMagicBlocks.BLANK_RITUAL_STONE.get()).setRuneType(world, newPos, component.getRuneType());
							return true;
						} else
						{
							notifyBlockedBuild(player, newPos);
							return false;
							// TODO: Possibly replace the block with a ritual stone
						}
					}
				}
			}
		}

		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public void trySetDisplayedRitual(ItemStack itemStack, Level world, BlockPos pos)
	{
		BlockEntity tile = world.getBlockEntity(pos);

		if (tile instanceof TileMasterRitualStone)
		{
			Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(this.getCurrentRitual(itemStack));
			TileMasterRitualStone masterRitualStone = (TileMasterRitualStone) tile;

			if (ritual != null)
			{
				Direction direction = getDirection(itemStack);
				ClientHandler.setRitualHolo(masterRitualStone, ritual, direction, true);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void undisplayHologram()
	{
		ClientHandler.setRitualHoloToNull();
	}

	// TODO: Make this work for any IRitualStone
	public boolean consumeStone(ItemStack stack, Level world, Player player)
	{
		if (player.isCreative())
		{
			return true;
		}

		NonNullList<ItemStack> inventory = player.getInventory().items;
		for (ItemStack newStack : inventory)
		{
			if (newStack.isEmpty())
			{

				continue;
			}
			Item item = newStack.getItem();
			if (item instanceof BlockItem)
			{
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof BlockRitualStone)
				{
					newStack.shrink(1);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		if (!stack.hasTag())
			return;

		Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(this.getCurrentRitual(stack));
		if (ritual != null)
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.diviner.currentRitual", Component.translatable(ritual.getTranslationKey())).withStyle(ChatFormatting.GRAY));

			boolean sneaking = Screen.hasShiftDown();
//			boolean extraInfo = sneaking && Keyboard.isKeyDown(Keyboard.KEY_M);
			boolean extraInfo = sneaking && Screen.hasAltDown();

			if (extraInfo)
			{
				tooltip.add(Component.literal(""));

				for (EnumDemonWillType type : EnumDemonWillType.values())
				{
					if (TextHelper.canTranslate(ritual.getTranslationKey() + "." + type.name().toLowerCase(Locale.ROOT) + ".info"))
					{
						tooltip.add(Component.translatable(ritual.getTranslationKey() + "." + type.name().toLowerCase(Locale.ROOT) + ".info"));
					}
				}
			} else if (sneaking)
			{
				tooltip.add(Component.translatable(tooltipBase + "currentDirection", Utils.toFancyCasing(getDirection(stack).name())).withStyle(ChatFormatting.GRAY));
				tooltip.add(Component.literal(""));

				Tuple<Integer, Map<EnumRuneType, Integer>> runeCount = RitualHelper.countRunes(ritual);
				int totalRunes = runeCount.getA();
				Map<EnumRuneType, Integer> runeMap = runeCount.getB();
				for (EnumRuneType type : EnumRuneType.values())
				{
					int count = runeMap.getOrDefault(type, 0);
					if (count > 0)
					{
						tooltip.add(Component.translatable(tooltipBase + type.translationKey, count).withStyle(type.colorCode));
					}
				}
				tooltip.add(Component.literal(""));
				tooltip.add(Component.translatable(tooltipBase + "totalRune", totalRunes).withStyle(ChatFormatting.GRAY));
			} else
			{
				tooltip.add(Component.literal(""));
				if (TextHelper.canTranslate(ritual.getTranslationKey() + ".info"))
				{
					tooltip.add(Component.translatable(ritual.getTranslationKey() + ".info").withStyle(ChatFormatting.GRAY));
					tooltip.add(Component.literal(""));
				}

				tooltip.add(Component.translatable(tooltipBase + "extraInfo").withStyle(ChatFormatting.BLUE));
				tooltip.add(Component.translatable(tooltipBase + "extraExtraInfo").withStyle(ChatFormatting.BLUE));
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		setActivatedState(stack, false);

		HitResult ray = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);

		if (ray != null && ray.getType() == HitResult.Type.BLOCK)
		{
			return new InteractionResultHolder<>(InteractionResult.PASS, stack);
		}

		if (player.isShiftKeyDown())
		{
			if (!world.isClientSide)
			{
				cycleRitual(stack, player, false);
			}

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
		} else
		{
			if (!world.isClientSide)
			{
				cycleDirection(stack, player);
			}
		}

		return new InteractionResultHolder<>(InteractionResult.PASS, stack);
	}

	@Override
	public void onUseTick(Level worldIn, LivingEntity entityLiving, ItemStack stack, int count)
	{
		setActivatedState(stack, false);
		if (!entityLiving.level().isClientSide && entityLiving instanceof Player)
		{
			Player player = (Player) entityLiving;

			HitResult ray = getPlayerPOVHitResult(player.level(), player, ClipContext.Fluid.NONE);

			if (ray != null && ray.getType() == HitResult.Type.BLOCK)
			{
				return;
//				return false;
			}

			if (!player.swinging)
			{
				if (player.isShiftKeyDown())
				{
					cycleRitual(stack, player, true);
				} else
				{
					cycleDirection(stack, player);
				}
			}
		}

//		return false;
	}

	public void cycleDirection(ItemStack stack, Player player)
	{
		Direction direction = getDirection(stack);
		Direction newDirection;
		switch (direction)
		{
		case NORTH:
			newDirection = Direction.EAST;
			break;
		case EAST:
			newDirection = Direction.SOUTH;
			break;
		case SOUTH:
			newDirection = Direction.WEST;
			break;
		case WEST:
			newDirection = Direction.NORTH;
			break;
		default:
			newDirection = Direction.NORTH;
		}

		setDirection(stack, newDirection);
		notifyDirectionChange(newDirection, player);
	}

	public void notifyDirectionChange(Direction direction, Player player)
	{
		player.displayClientMessage(Component.translatable(tooltipBase + "currentDirection", Utils.toFancyCasing(direction.name())), true);
	}

	public void setDirection(ItemStack stack, Direction direction)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}

		CompoundTag tag = stack.getTag();

		tag.putInt(Constants.NBT.DIRECTION, direction.get3DDataValue());
	}

	public Direction getDirection(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
			return Direction.NORTH;
		}

		CompoundTag tag = stack.getTag();

		int dir = tag.getInt(Constants.NBT.DIRECTION);
		if (dir == 0)
		{
			return Direction.NORTH;
		}

		return Direction.values()[tag.getInt(Constants.NBT.DIRECTION)];
	}

	/**
	 * Cycles the ritual forward or backward
	 */
	public void cycleRitual(ItemStack stack, Player player, boolean reverse)
	{
		String key = getCurrentRitual(stack);
		List<Ritual> rituals = BloodMagic.RITUAL_MANAGER.getSortedRituals();
		if (reverse)
			Collections.reverse(rituals = Lists.newArrayList(rituals));

		String firstId = "";
		boolean foundId = false;
		boolean foundFirst = false;

		for (Ritual ritual : rituals)
		{
			String id = BloodMagic.RITUAL_MANAGER.getId(ritual);

			if (!BloodMagic.RITUAL_MANAGER.enabled(id, false) || !canDivinerPerformRitual(stack, ritual))
			{
				continue;
			}

			if (!foundFirst)
			{
				firstId = id;
				foundFirst = true;
			}

			if (foundId)
			{
				setCurrentRitual(stack, id);
				notifyRitualChange(id, player);
				return;
			} else if (id.equals(key))
			{
				foundId = true;
			}
		}

		if (foundFirst)
		{
			setCurrentRitual(stack, firstId);
			notifyRitualChange(firstId, player);
		}
	}

	public boolean canDivinerPerformRitual(ItemStack stack, Ritual ritual)
	{
		if (ritual == null)
		{
			return false;
		}

		List<RitualComponent> components = Lists.newArrayList();
		ritual.gatherComponents(components::add);
		for (RitualComponent component : components)
		{
			if (!canPlaceRitualStone(component.getRuneType(), stack))
			{
				return false;
			}
		}

		return true;
	}

	public void notifyRitualChange(String key, Player player)
	{
		Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(key);
		if (ritual != null)
		{
			player.displayClientMessage(Component.translatable(ritual.getTranslationKey()), true);
		}
	}

	public void setCurrentRitual(ItemStack stack, String key)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}

		CompoundTag tag = stack.getTag();

		tag.putString("current_ritual", key);
	}

	public String getCurrentRitual(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
		}

		CompoundTag tag = stack.getTag();
		return tag.getString("current_ritual");
	}

	public boolean canPlaceRitualStone(EnumRuneType rune, ItemStack stack)
	{
		int meta = type;
		switch (rune)
		{
		case BLANK:
		case AIR:
		case EARTH:
		case FIRE:
		case WATER:
			return true;
		case DUSK:
			return meta >= 1;
		case DAWN:
			return meta >= 2;
		}

		return false;
	}

	public static void spawnParticles(Level worldIn, BlockPos pos, int amount)
	{
		BlockState state = worldIn.getBlockState(pos);
		Block block = worldIn.getBlockState(pos).getBlock();

		if (state.isAir())
		{
			for (int i = 0; i < amount; ++i)
			{
				double d0 = worldIn.random.nextGaussian() * 0.02D;
				double d1 = worldIn.random.nextGaussian() * 0.02D;
				double d2 = worldIn.random.nextGaussian() * 0.02D;
				worldIn.addParticle(ParticleTypes.HAPPY_VILLAGER, (double) ((float) pos.getX() + worldIn.random.nextFloat()), (double) pos.getY() + (double) worldIn.random.nextFloat(), (double) ((float) pos.getZ() + worldIn.random.nextFloat()), d0, d1, d2);
			}
		} else
		{
			for (int i1 = 0; i1 < amount; ++i1)
			{
				double d0 = worldIn.random.nextGaussian() * 0.02D;
				double d1 = worldIn.random.nextGaussian() * 0.02D;
				double d2 = worldIn.random.nextGaussian() * 0.02D;
				worldIn.addParticle(ParticleTypes.HAPPY_VILLAGER, (double) ((float) pos.getX() + worldIn.random.nextFloat() * 3.0f - 1), (double) pos.getY() + (double) worldIn.random.nextFloat() * 3.0f - 1, (double) ((float) pos.getZ() + worldIn.random.nextFloat() * 3.0f - 1), d0, d1, d2);
			}
		}
	}

	public void notifyBlockedBuild(Player player, BlockPos pos)
	{
		player.displayClientMessage(Component.translatable("chat.bloodmagic.diviner.blockedBuild", pos.getX(), pos.getY(), pos.getZ()), true);
	}
}
