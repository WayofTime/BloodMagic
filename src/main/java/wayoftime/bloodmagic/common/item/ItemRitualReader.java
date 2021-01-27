package wayoftime.bloodmagic.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRitualReaderState;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.tile.TileMasterRitualStone;
import wayoftime.bloodmagic.util.ChatUtil;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.handler.event.ClientHandler;
import wayoftime.bloodmagic.util.helper.NBTHelper;
import wayoftime.bloodmagic.util.helper.TextHelper;
import wayoftime.bloodmagic.will.DemonWillHolder;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.api.compat.IDiscreteDemonWill;

public class ItemRitualReader extends Item
{
	public static final String tooltipBase = "tooltip.bloodmagic.ritualReader.";

	public ItemRitualReader()
	{
		super(new Item.Properties().maxStackSize(1).group(BloodMagic.TAB));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if (!stack.hasTag())
			return;

		EnumRitualReaderState state = this.getState(stack);
		tooltip.add(new TranslationTextComponent(tooltipBase + "currentState", TextHelper.localizeEffect(tooltipBase + state.toString().toLowerCase())).mergeStyle(TextFormatting.GRAY));

		tooltip.add(new StringTextComponent(""));

		boolean sneaking = Screen.hasShiftDown();

		if (sneaking)
		{
			tooltip.add(new TranslationTextComponent(tooltipBase + "desc." + state.toString().toLowerCase()).mergeStyle(TextFormatting.GRAY));
		} else
		{
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.extraInfo").mergeStyle(TextFormatting.GRAY));
		}

		super.addInformation(stack, world, tooltip, flag);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		RayTraceResult ray = Item.rayTrace(world, player, FluidMode.NONE);
		if (ray != null && ray.getType() == RayTraceResult.Type.BLOCK)
		{
			return new ActionResult<>(ActionResultType.PASS, stack);
		}

		if (player.isSneaking())
		{
			if (!world.isRemote)
			{
				cycleReader(stack, player);
			}

			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}

		return new ActionResult<>(ActionResultType.PASS, stack);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World world = context.getWorld();
		BlockPos pos = context.getPos();

		PlayerEntity player = context.getPlayer();
		ItemStack stack = context.getItem();
		Hand hand = context.getHand();
		Direction direction = context.getFace();

//		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote)
		{
			EnumRitualReaderState state = this.getState(stack);
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof IMasterRitualStone)
			{
				IMasterRitualStone master = (IMasterRitualStone) tile;
				if (master.getCurrentRitual() == null)
					super.onItemUse(context);
				this.setMasterBlockPos(stack, pos);
				this.setBlockPos(stack, BlockPos.ZERO);

				switch (state)
				{
				case INFORMATION:
					master.provideInformationOfRitualToPlayer(player);

					break;
				case SET_AREA:
					if (player.isSneaking() && player.getHeldItem(Hand.OFF_HAND).getItem() instanceof ItemBloodOrb)
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

					if (range == null || range.isEmpty() || player.isSneaking())
					{
						String newRange = master.getNextBlockRange(range);
						range = newRange;
						this.setCurrentBlockRange(stack, newRange);
					}

					master.provideInformationOfRangeToPlayer(player, range);

					break;
				case SET_WILL_TYPES:
					List<EnumDemonWillType> typeList = new ArrayList<>();
					NonNullList<ItemStack> inv = player.inventory.mainInventory;
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

				return ActionResultType.FAIL;
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
							player.sendStatusMessage(new TranslationTextComponent("ritual.bloodmagic.blockRange.firstBlock"), true);
						} else
						{
							tile = world.getTileEntity(masterPos);
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
								DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(master.getWorldObj(), master.getBlockPos());

								int maxHorizontalRange = ritual.getMaxHorizontalRadiusForRange(range, willConfig, holder);
								int maxVerticalRange = ritual.getMaxVerticalRadiusForRange(range, willConfig, holder);
								int maxVolume = ritual.getMaxVolumeForRange(range, willConfig, holder);

								switch (master.setBlockRangeByBounds(player, range, containedPos, pos2))
								{
								case SUCCESS:
									player.sendStatusMessage(new TranslationTextComponent("ritual.bloodmagic.blockRange.success"), true);
									break;
								case NOT_WITHIN_BOUNDARIES:
									player.sendStatusMessage(new TranslationTextComponent("ritual.bloodmagic.blockRange.tooFar", maxVerticalRange, maxHorizontalRange), false);
									break;
								case VOLUME_TOO_LARGE:
									player.sendStatusMessage(new TranslationTextComponent("ritual.bloodmagic.blockRange.tooBig", maxVolume), false);
									break;
								default:
									player.sendStatusMessage(new TranslationTextComponent("ritual.bloodmagic.blockRange.noRange"), false);
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
				TileEntity tile = world.getTileEntity(pos);
				if (tile instanceof TileMasterRitualStone)
				{

					System.out.println("Setting range holo... I think");
					ClientHandler.setRitualRangeHolo((TileMasterRitualStone) tile, true);

				}
			}

		}

		return super.onItemUse(context);
	}

	public BlockPos getBlockPos(ItemStack stack)
	{
		stack = NBTHelper.checkNBT(stack);
		return new BlockPos(stack.getTag().getInt(Constants.NBT.X_COORD), stack.getTag().getInt(Constants.NBT.Y_COORD), stack.getTag().getInt(Constants.NBT.Z_COORD));
	}

	public ItemStack setBlockPos(ItemStack stack, BlockPos pos)
	{
		stack = NBTHelper.checkNBT(stack);
		CompoundNBT itemTag = stack.getTag();
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
		CompoundNBT itemTag = stack.getTag();
		itemTag.putInt(Constants.NBT.X_COORD + "master", pos.getX());
		itemTag.putInt(Constants.NBT.Y_COORD + "master", pos.getY());
		itemTag.putInt(Constants.NBT.Z_COORD + "master", pos.getZ());
		return stack;
	}

	public String getCurrentBlockRange(ItemStack stack)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		return tag.getString("range");
	}

	public void setCurrentBlockRange(ItemStack stack, String range)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putString("range", range);
	}

	public void cycleReader(ItemStack stack, PlayerEntity player)
	{
		EnumRitualReaderState prevState = getState(stack);
		int val = prevState.ordinal();
		int nextVal = val + 1 >= EnumRitualReaderState.values().length ? 0 : val + 1;
		EnumRitualReaderState nextState = EnumRitualReaderState.values()[nextVal];

		setState(stack, nextState);
		notifyPlayerOfStateChange(nextState, player);
	}

	public void notifyPlayerOfStateChange(EnumRitualReaderState state, PlayerEntity player)
	{
		ChatUtil.sendNoSpam(player, new TranslationTextComponent(tooltipBase + "currentState", new TranslationTextComponent(tooltipBase + state.toString().toLowerCase())));
	}

	public void setState(ItemStack stack, EnumRitualReaderState state)
	{
		NBTHelper.checkNBT(stack);

		CompoundNBT tag = stack.getTag();

		tag.putInt(Constants.NBT.RITUAL_READER, state.ordinal());
	}

	public EnumRitualReaderState getState(ItemStack stack)
	{
		if (!stack.hasTag())
		{
			stack.setTag(new CompoundNBT());
			return EnumRitualReaderState.INFORMATION;
		}

		CompoundNBT tag = stack.getTag();

		return EnumRitualReaderState.values()[tag.getInt(Constants.NBT.RITUAL_READER)];
	}
}