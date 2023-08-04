package wayoftime.bloodmagic.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDiscreteDemonWill;
import wayoftime.bloodmagic.common.tile.TileMasterRitualStone;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRitualReaderState;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;
import wayoftime.bloodmagic.will.DemonWillHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemRitualReader extends Item
{
	public static final String tooltipBase = "tooltip.bloodmagic.ritualReader.";

	public ItemRitualReader()
	{
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		if (!stack.hasTag())
			return;

		EnumRitualReaderState state = this.getState(stack);
		tooltip.add(Component.translatable(tooltipBase + "currentState", TextHelper.localizeEffect(tooltipBase + state.toString().toLowerCase(Locale.ROOT))).withStyle(ChatFormatting.GRAY));

		tooltip.add(Component.literal(""));

		boolean sneaking = Screen.hasShiftDown();

		if (sneaking)
		{
			tooltip.add(Component.translatable(tooltipBase + "desc." + state.toString().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY));
		} else
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.extraInfo").withStyle(ChatFormatting.GRAY));
		}

		super.appendHoverText(stack, world, tooltip, flag);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		HitResult ray = Item.getPlayerPOVHitResult(world, player, Fluid.NONE);
		if (ray != null && ray.getType() == HitResult.Type.BLOCK)
		{
			return new InteractionResultHolder<>(InteractionResult.PASS, stack);
		}

		if (player.isShiftKeyDown())
		{
			if (!world.isClientSide)
			{
				cycleReader(stack, player);
			}

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
		}

		return new InteractionResultHolder<>(InteractionResult.PASS, stack);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();

		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		InteractionHand hand = context.getHand();
		Direction direction = context.getClickedFace();

//		ItemStack stack = player.getHeldItem(hand);
		if (!world.isClientSide)
		{
			EnumRitualReaderState state = this.getState(stack);
			BlockEntity tile = world.getBlockEntity(pos);
			if (tile instanceof IMasterRitualStone)
			{
				IMasterRitualStone master = (IMasterRitualStone) tile;
				if (master.getCurrentRitual() == null)
					super.useOn(context);
				this.setMasterBlockPos(stack, pos);
				this.setBlockPos(stack, BlockPos.ZERO);

				switch (state)
				{
				case INFORMATION:
					master.provideInformationOfRitualToPlayer(player);

					break;
				case SET_AREA:
					if (player.isShiftKeyDown() && player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ItemBloodOrb)
					{
						Ritual ritual = master.getCurrentRitual();
						for (String range : ritual.getListOfRanges())
						{
							AreaDescriptor aabb = ritual.getBlockRange(range);
							master.setBlockRange(range, aabb);
						}
						break;
					}

					String range = this.getCurrentBlockRange(stack);

					if (range == null || range.isEmpty() || player.isShiftKeyDown())
					{
						String newRange = master.getNextBlockRange(range);
						range = newRange;
						this.setCurrentBlockRange(stack, newRange);
					}

					master.provideInformationOfRangeToPlayer(player, range);

					break;
				case SET_WILL_TYPES:
					List<EnumDemonWillType> typeList = new ArrayList<>();
					NonNullList<ItemStack> inv = player.getInventory().items;
					for (int i = 0; i < 9; i++)
					{
						ItemStack testStack = inv.get(i);
						if (testStack.isEmpty())
						{
							continue;
						}

						if (testStack.getItem() instanceof IDiscreteDemonWill)
						{
							EnumDemonWillType type = ((IDiscreteDemonWill) testStack.getItem()).getType(testStack);
							if (!typeList.contains(type))
							{
								typeList.add(type);
							}
						}
					}

					master.setActiveWillConfig(player, typeList);
					master.provideInformationOfWillConfigToPlayer(player, typeList);
					break;
				}

				return InteractionResult.FAIL;
			} else
			{
				if (state == EnumRitualReaderState.SET_AREA)
				{
					BlockPos masterPos = this.getMasterBlockPos(stack);
					if (!masterPos.equals(BlockPos.ZERO))
					{
						BlockPos containedPos = getBlockPos(stack);
						if (containedPos.equals(BlockPos.ZERO))
						{
							BlockPos pos1 = pos.subtract(masterPos);
							this.setBlockPos(stack, pos1);
							player.displayClientMessage(Component.translatable("ritual.bloodmagic.blockRange.firstBlock"), true);
						} else
						{
							tile = world.getBlockEntity(masterPos);
							if (tile instanceof IMasterRitualStone)
							{
								IMasterRitualStone master = (IMasterRitualStone) tile;
								BlockPos pos2 = pos.subtract(masterPos);
								String range = this.getCurrentBlockRange(stack);
								if (range == null || range.isEmpty())
								{
									String newRange = master.getNextBlockRange(range);
									range = newRange;
									this.setCurrentBlockRange(stack, newRange);
								}
								Ritual ritual = master.getCurrentRitual();
								List<EnumDemonWillType> willConfig = master.getActiveWillConfig();
								DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(master.getWorldObj(), master.getMasterBlockPos());

								int maxHorizontalRange = ritual.getMaxHorizontalRadiusForRange(range, willConfig, holder);
								int maxVerticalRange = ritual.getMaxVerticalRadiusForRange(range, willConfig, holder);
								int maxVolume = ritual.getMaxVolumeForRange(range, willConfig, holder);

								switch (master.setBlockRangeByBounds(player, range, containedPos, pos2))
								{
								case SUCCESS:
									player.displayClientMessage(Component.translatable("ritual.bloodmagic.blockRange.success"), true);
									break;
								case NOT_WITHIN_BOUNDARIES:
									player.displayClientMessage(Component.translatable("ritual.bloodmagic.blockRange.tooFar", maxVerticalRange, maxHorizontalRange), false);
									break;
								case VOLUME_TOO_LARGE:
									player.displayClientMessage(Component.translatable("ritual.bloodmagic.blockRange.tooBig", maxVolume), false);
									break;
								default:
									player.displayClientMessage(Component.translatable("ritual.bloodmagic.blockRange.noRange"), false);
									break;
								}
							}
							this.setBlockPos(stack, BlockPos.ZERO);
						}
					}
				}
			}
		} else
		{
			EnumRitualReaderState state = this.getState(stack);

			if (state == EnumRitualReaderState.SET_AREA)
			{
				BlockEntity tile = world.getBlockEntity(pos);
				if (tile instanceof TileMasterRitualStone)
				{

//					System.out.println("Setting range holo... I think");
					ClientHandler.setRitualRangeHolo((TileMasterRitualStone) tile, true);

				}
			}

		}

		return super.useOn(context);
	}

	public BlockPos getBlockPos(ItemStack stack)
	{
		stack = NBTHelper.checkNBT(stack);
		return new BlockPos(stack.getTag().getInt(Constants.NBT.X_COORD), stack.getTag().getInt(Constants.NBT.Y_COORD), stack.getTag().getInt(Constants.NBT.Z_COORD));
	}

	public ItemStack setBlockPos(ItemStack stack, BlockPos pos)
	{
		stack = NBTHelper.checkNBT(stack);
		CompoundTag itemTag = stack.getTag();
		itemTag.putInt(Constants.NBT.X_COORD, pos.getX());
		itemTag.putInt(Constants.NBT.Y_COORD, pos.getY());
		itemTag.putInt(Constants.NBT.Z_COORD, pos.getZ());
		return stack;
	}

	public BlockPos getMasterBlockPos(ItemStack stack)
	{
		stack = NBTHelper.checkNBT(stack);
		return new BlockPos(stack.getTag().getInt(Constants.NBT.X_COORD + "master"), stack.getTag().getInt(Constants.NBT.Y_COORD + "master"), stack.getTag().getInt(Constants.NBT.Z_COORD + "master"));
	}

	public ItemStack setMasterBlockPos(ItemStack stack, BlockPos pos)
	{
		stack = NBTHelper.checkNBT(stack);
		CompoundTag itemTag = stack.getTag();
		itemTag.putInt(Constants.NBT.X_COORD + "master", pos.getX());
		itemTag.putInt(Constants.NBT.Y_COORD + "master", pos.getY());
		itemTag.putInt(Constants.NBT.Z_COORD + "master", pos.getZ());
		return stack;
	}

	public String getCurrentBlockRange(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		return tag.getString("range");
	}

	public void setCurrentBlockRange(ItemStack stack, String range)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		tag.putString("range", range);
	}

	public void cycleReader(ItemStack stack, Player player)
	{
		EnumRitualReaderState prevState = getState(stack);
		int val = prevState.ordinal();
		int nextVal = val + 1 >= EnumRitualReaderState.values().length ? 0 : val + 1;
		EnumRitualReaderState nextState = EnumRitualReaderState.values()[nextVal];

		setState(stack, nextState);
		notifyPlayerOfStateChange(nextState, player);
	}

	public void notifyPlayerOfStateChange(EnumRitualReaderState state, Player player)
	{
		ChatUtil.sendNoSpam(player, Component.translatable(tooltipBase + "currentState", Component.translatable(tooltipBase + state.toString().toLowerCase(Locale.ROOT))));
	}

	public void setState(ItemStack stack, EnumRitualReaderState state)
	{
		NBTHelper.checkNBT(stack);

		CompoundTag tag = stack.getTag();

		tag.putInt(Constants.NBT.RITUAL_READER, state.ordinal());
	}

	public EnumRitualReaderState getState(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundTag());
			return EnumRitualReaderState.INFORMATION;
		}

		CompoundTag tag = stack.getTag();

		return EnumRitualReaderState.values()[tag.getInt(Constants.NBT.RITUAL_READER)];
	}
}