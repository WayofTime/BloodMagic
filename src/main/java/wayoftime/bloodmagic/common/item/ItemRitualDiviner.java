package wayoftime.bloodmagic.common.item;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BlockRitualStone;
import wayoftime.bloodmagic.common.block.BloodMagicBlocks;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.tile.TileMasterRitualStone;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;
import wayoftime.bloodmagic.util.helper.RitualHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;

public class ItemRitualDiviner extends Item
{
	final int type;
	public static final String tooltipBase = "tooltip.bloodmagic.diviner.";
	public static String[] names = { "normal", "dusk", "dawn" };

	public ItemRitualDiviner(int type)
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
		this.type = type;
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
	public ActionResultType onItemUse(ItemUseContext context)
	{
		ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
		if (context.getPlayer().isSneaking())
		{
			if (context.getWorld().isRemote)
			{
				trySetDisplayedRitual(stack, context.getWorld(), context.getPos());
			}

			return ActionResultType.SUCCESS;
		} else if (addRuneToRitual(stack, context.getWorld(), context.getPos(), context.getPlayer()))
		{
			if (context.getWorld().isRemote)
			{
				spawnParticles(context.getWorld(), context.getPos().offset(context.getFace()), 15);
			}

			return ActionResultType.SUCCESS;
			// TODO: Have the diviner automagically build the ritual
		}

		return ActionResultType.PASS;
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
	public boolean addRuneToRitual(ItemStack stack, World world, BlockPos pos, PlayerEntity player)
	{
		TileEntity tile = world.getTileEntity(pos);

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
					BlockPos newPos = pos.add(offset);
					BlockState state = world.getBlockState(newPos);
					Block block = state.getBlock();
					if (RitualHelper.isRune(world, newPos))
					{
						if (RitualHelper.isRuneType(world, newPos, component.getRuneType()))
						{
							if (world.isRemote)
							{
								undisplayHologram();
							}
						} else
						{
							// Replace existing ritual stone
							RitualHelper.setRuneType(world, newPos, component.getRuneType());
							return true;
						}
					} else if (block.isAir(state, world, newPos))// || block.isReplaceable(world, newPos))
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

		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public void trySetDisplayedRitual(ItemStack itemStack, World world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);

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
	public boolean consumeStone(ItemStack stack, World world, PlayerEntity player)
	{
		if (player.isCreative())
		{
			return true;
		}

		NonNullList<ItemStack> inventory = player.inventory.mainInventory;
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
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if (!stack.hasTag())
			return;

		Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(this.getCurrentRitual(stack));
		if (ritual != null)
		{
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.diviner.currentRitual", new TranslationTextComponent(ritual.getTranslationKey())));

			boolean sneaking = Screen.hasShiftDown();
//			boolean extraInfo = sneaking && Keyboard.isKeyDown(Keyboard.KEY_M);
			boolean extraInfo = sneaking && Screen.hasAltDown();

			if (extraInfo)
			{
				tooltip.add(new StringTextComponent(""));

				for (EnumDemonWillType type : EnumDemonWillType.values())
				{
					if (TextHelper.canTranslate(ritual.getTranslationKey() + "." + type.name().toLowerCase() + ".info"))
					{
						tooltip.add(new TranslationTextComponent(ritual.getTranslationKey() + "." + type.name().toLowerCase() + ".info"));
					}
				}
			} else if (sneaking)
			{
				tooltip.add(new TranslationTextComponent(tooltipBase + "currentDirection", Utils.toFancyCasing(getDirection(stack).name())));
				tooltip.add(new StringTextComponent(""));
				List<RitualComponent> components = Lists.newArrayList();
				ritual.gatherComponents(components::add);

				int blankRunes = 0;
				int airRunes = 0;
				int waterRunes = 0;
				int fireRunes = 0;
				int earthRunes = 0;
				int duskRunes = 0;
				int dawnRunes = 0;
				int totalRunes = components.size();

				for (RitualComponent component : components)
				{
					switch (component.getRuneType())
					{
					case BLANK:
						blankRunes++;
						break;
					case AIR:
						airRunes++;
						break;
					case EARTH:
						earthRunes++;
						break;
					case FIRE:
						fireRunes++;
						break;
					case WATER:
						waterRunes++;
						break;
					case DUSK:
						duskRunes++;
						break;
					case DAWN:
						dawnRunes++;
						break;
					}
				}

				if (blankRunes > 0)
					tooltip.add(new TranslationTextComponent(tooltipBase + "blankRune", blankRunes).mergeStyle(EnumRuneType.BLANK.colorCode));
				if (waterRunes > 0)
					tooltip.add(new TranslationTextComponent(tooltipBase + "waterRune", waterRunes).mergeStyle(EnumRuneType.WATER.colorCode));
				if (airRunes > 0)
					tooltip.add(new TranslationTextComponent(tooltipBase + "airRune", airRunes).mergeStyle(EnumRuneType.AIR.colorCode));
				if (fireRunes > 0)
					tooltip.add(new TranslationTextComponent(tooltipBase + "fireRune", fireRunes).mergeStyle(EnumRuneType.FIRE.colorCode));
				if (earthRunes > 0)
					tooltip.add(new TranslationTextComponent(tooltipBase + "earthRune", earthRunes).mergeStyle(EnumRuneType.EARTH.colorCode));
				if (duskRunes > 0)
					tooltip.add(new TranslationTextComponent(tooltipBase + "duskRune", duskRunes).mergeStyle(EnumRuneType.DUSK.colorCode));
				if (dawnRunes > 0)
					tooltip.add(new TranslationTextComponent(tooltipBase + "dawnRune", dawnRunes).mergeStyle(EnumRuneType.DAWN.colorCode));

				tooltip.add(new StringTextComponent(""));
				tooltip.add(new TranslationTextComponent(tooltipBase + "totalRune", totalRunes));
			} else
			{
				tooltip.add(new StringTextComponent(""));
				if (TextHelper.canTranslate(ritual.getTranslationKey() + ".info"))
				{
					tooltip.add(new TranslationTextComponent(ritual.getTranslationKey() + ".info"));
					tooltip.add(new StringTextComponent(""));
				}

				tooltip.add(new TranslationTextComponent(tooltipBase + "extraInfo").mergeStyle(TextFormatting.BLUE));
				tooltip.add(new TranslationTextComponent(tooltipBase + "extraExtraInfo").mergeStyle(TextFormatting.BLUE));
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		RayTraceResult ray = rayTrace(world, player, RayTraceContext.FluidMode.NONE);

		if (ray != null && ray.getType() == RayTraceResult.Type.BLOCK)
		{
			return new ActionResult<>(ActionResultType.PASS, stack);
		}

		if (player.isSneaking())
		{
			if (!world.isRemote)
			{
				cycleRitual(stack, player, false);
			}

			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		} else
		{
			if (!world.isRemote)
			{
				cycleDirection(stack, player);
			}
		}

		return new ActionResult<>(ActionResultType.PASS, stack);
	}

	@Override
	public void onUse(World worldIn, LivingEntity entityLiving, ItemStack stack, int count)
	{
		if (!entityLiving.world.isRemote && entityLiving instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) entityLiving;

			RayTraceResult ray = rayTrace(player.world, player, RayTraceContext.FluidMode.NONE);

			if (ray != null && ray.getType() == RayTraceResult.Type.BLOCK)
			{
				return;
//				return false;
			}

			if (!player.isSwingInProgress)
			{
				if (player.isSneaking())
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

	public void cycleDirection(ItemStack stack, PlayerEntity player)
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

	public void notifyDirectionChange(Direction direction, PlayerEntity player)
	{
		player.sendStatusMessage(new TranslationTextComponent(tooltipBase + "currentDirection", Utils.toFancyCasing(direction.name())), true);
	}

	public void setDirection(ItemStack stack, Direction direction)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
		}

		CompoundNBT tag = stack.getTag();

		tag.putInt(Constants.NBT.DIRECTION, direction.getIndex());
	}

	public Direction getDirection(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
			return Direction.NORTH;
		}

		CompoundNBT tag = stack.getTag();

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
	public void cycleRitual(ItemStack stack, PlayerEntity player, boolean reverse)
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

	public void notifyRitualChange(String key, PlayerEntity player)
	{
		Ritual ritual = BloodMagic.RITUAL_MANAGER.getRitual(key);
		if (ritual != null)
		{
			player.sendStatusMessage(new TranslationTextComponent(ritual.getTranslationKey()), true);
		}
	}

	public void setCurrentRitual(ItemStack stack, String key)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
		}

		CompoundNBT tag = stack.getTag();

		tag.putString("current_ritual", key);
	}

	public String getCurrentRitual(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
		}

		CompoundNBT tag = stack.getTag();
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

	public static void spawnParticles(World worldIn, BlockPos pos, int amount)
	{
		BlockState state = worldIn.getBlockState(pos);
		Block block = worldIn.getBlockState(pos).getBlock();

		if (block.isAir(state, worldIn, pos))
		{
			for (int i = 0; i < amount; ++i)
			{
				double d0 = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				worldIn.addParticle(ParticleTypes.HAPPY_VILLAGER, (double) ((float) pos.getX() + random.nextFloat()), (double) pos.getY() + (double) random.nextFloat(), (double) ((float) pos.getZ() + random.nextFloat()), d0, d1, d2);
			}
		} else
		{
			for (int i1 = 0; i1 < amount; ++i1)
			{
				double d0 = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				worldIn.addParticle(ParticleTypes.HAPPY_VILLAGER, (double) ((float) pos.getX() + random.nextFloat()), (double) pos.getY() + (double) random.nextFloat() * 1.0f, (double) ((float) pos.getZ() + random.nextFloat()), d0, d1, d2);
			}
		}
	}

	public void notifyBlockedBuild(PlayerEntity player, BlockPos pos)
	{
		player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.diviner.blockedBuild", pos.getX(), pos.getY(), pos.getZ()), true);
	}
}
