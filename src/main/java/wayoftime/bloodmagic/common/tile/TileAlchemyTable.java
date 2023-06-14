package wayoftime.bloodmagic.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.ArrayUtils;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.event.BloodMagicCraftedEvent;
import wayoftime.bloodmagic.common.container.tile.ContainerAlchemyTable;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.common.item.IAlchemyItem;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.common.item.IBloodOrb;
import wayoftime.bloodmagic.common.item.potion.ItemAlchemyFlask;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.network.AlchemyTableFlagPacket;
import wayoftime.bloodmagic.recipe.EffectHolder;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.recipe.flask.RecipePotionFlaskBase;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

public class TileAlchemyTable extends TileInventory implements WorldlyContainer, MenuProvider
{
	public static final int orbSlot = 6;
	public static final int outputSlot = 7;

	public Direction direction = Direction.NORTH;
	public boolean isSlave = false;
	public int burnTime = 0;
	public int ticksRequired = 1;
	public boolean orbFlag = false;
	public boolean lpFlag = false;

	public BlockPos connectedPos = BlockPos.ZERO;
	public boolean[] blockedSlots = new boolean[] { false, false, false, false, false, false };
	public boolean[] allowedDirectionsSlot0 = new boolean[] { false, false, true, true, true, true };
	public boolean[] allowedDirectionsSlot1 = new boolean[] { false, false, true, true, true, true };
	public boolean[] allowedDirectionsSlot2 = new boolean[] { false, false, true, true, true, true };
	public boolean[] allowedDirectionsSlot3 = new boolean[] { false, false, true, true, true, true };
	public boolean[] allowedDirectionsSlot4 = new boolean[] { false, false, true, true, true, true };
	public boolean[] allowedDirectionsSlot5 = new boolean[] { false, false, true, true, true, true };
	public boolean[] allowedDirectionsOrb = new boolean[] { false, true, false, false, false, false };
	public boolean[] allowedDirectionsOutput = new boolean[] { true, false, false, false, false, false };

	public int activeSlot = -1;

	public TileAlchemyTable(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, 8, "alchemytable", pos, state);
	}

	public TileAlchemyTable(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.ALCHEMY_TABLE_TYPE.get(), pos, state);
	}

	public void setInitialTableParameters(Direction direction, boolean isSlave, BlockPos connectedPos)
	{
		this.isSlave = isSlave;
		this.connectedPos = connectedPos;

		if (!isSlave)
		{
			this.direction = direction;
		}
	}

	public boolean isInvisible()
	{
		return isSlave();
	}

	public boolean isInputSlotAccessible(int slot)
	{
		return !(slot < 6 && slot >= 0) || !blockedSlots[slot];
	}

	public void toggleInputSlotAccessible(int slot)
	{
		if (slot < 6 && slot >= 0)
			blockedSlots[slot] = !blockedSlots[slot];
	}

	public boolean isSlotEnabled(int slot, Direction dir)
	{
		switch (slot)
		{
		case 0:
			return allowedDirectionsSlot0[dir.ordinal()];
		case 1:
			return allowedDirectionsSlot1[dir.ordinal()];
		case 2:
			return allowedDirectionsSlot2[dir.ordinal()];
		case 3:
			return allowedDirectionsSlot3[dir.ordinal()];
		case 4:
			return allowedDirectionsSlot4[dir.ordinal()];
		case 5:
			return allowedDirectionsSlot5[dir.ordinal()];
		case 6:
			return allowedDirectionsOrb[dir.ordinal()];
		case 7:
			return allowedDirectionsOutput[dir.ordinal()];
		}

		return false;
	}

	public void setSlotEnabled(boolean enabled, int slot, Direction dir)
	{
		switch (slot)
		{
		case 0:
			allowedDirectionsSlot0[dir.ordinal()] = enabled;
			break;
		case 1:
			allowedDirectionsSlot1[dir.ordinal()] = enabled;
			break;
		case 2:
			allowedDirectionsSlot2[dir.ordinal()] = enabled;
			break;
		case 3:
			allowedDirectionsSlot3[dir.ordinal()] = enabled;
			break;
		case 4:
			allowedDirectionsSlot4[dir.ordinal()] = enabled;
			break;
		case 5:
			allowedDirectionsSlot5[dir.ordinal()] = enabled;
			break;
		case 6:
			allowedDirectionsOrb[dir.ordinal()] = enabled;
			break;
		case 7:
			allowedDirectionsOutput[dir.ordinal()] = enabled;
			break;
		}
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		super.deserialize(tag);

		isSlave = tag.getBoolean("isSlave");
		direction = Direction.from3DDataValue(tag.getInt(Constants.NBT.DIRECTION));
		connectedPos = new BlockPos(tag.getInt(Constants.NBT.X_COORD), tag.getInt(Constants.NBT.Y_COORD), tag.getInt(Constants.NBT.Z_COORD));

		burnTime = tag.getInt("burnTime");
		ticksRequired = tag.getInt("ticksRequired");

		byte[] array = tag.getByteArray("blockedSlots");
		for (int i = 0; i < array.length; i++) blockedSlots[i] = array[i] != 0;

		for (int i = 0; i <= outputSlot; i++)
		{
			byte[] allowedSlotArray = tag.getByteArray("allowedDirections" + i);
			for (int j = 0; j < Math.min(allowedSlotArray.length, Direction.values().length); j++)
			{
				this.setSlotEnabled(allowedSlotArray[j] == 1 ? true : false, i, Direction.values()[j]);
			}
//			tag.putByteArray("allowedDirections" + i, allowedSlotArray);
		}
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		super.serialize(tag);

		tag.putBoolean("isSlave", isSlave);
		tag.putInt(Constants.NBT.DIRECTION, direction.get3DDataValue());
		tag.putInt(Constants.NBT.X_COORD, connectedPos.getX());
		tag.putInt(Constants.NBT.Y_COORD, connectedPos.getY());
		tag.putInt(Constants.NBT.Z_COORD, connectedPos.getZ());

		tag.putInt("burnTime", burnTime);
		tag.putInt("ticksRequired", ticksRequired);

		byte[] blockedSlotArray = new byte[blockedSlots.length];
		for (int i = 0; i < blockedSlots.length; i++) blockedSlotArray[i] = (byte) (blockedSlots[i] ? 1 : 0);

		tag.putByteArray("blockedSlots", blockedSlotArray);

		for (int i = 0; i <= outputSlot; i++)
		{
			byte[] allowedSlotArray = new byte[Direction.values().length];
			for (int j = 0; j < Direction.values().length; j++)
			{
				allowedSlotArray[j] = (byte) (this.isSlotEnabled(i, Direction.values()[j]) ? 1 : 0);
			}
			tag.putByteArray("allowedDirections" + i, allowedSlotArray);
		}

		return tag;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
	{
		if (facing != null && capability == ForgeCapabilities.ITEM_HANDLER)
		{
			if (this.isSlave())
			{
				BlockEntity tile = getLevel().getBlockEntity(connectedPos);
				if (tile instanceof TileAlchemyTable && !((TileAlchemyTable) tile).isSlave)
				{
					return (LazyOptional<T>) tile.getCapability(capability, facing);
				}
			} else
			{
				return super.getCapability(capability, facing);
			}
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public int[] getSlotsForFace(Direction side)
	{
		List<Integer> integerList = new ArrayList<Integer>();
		for (int i = 0; i <= outputSlot; i++)
		{
			if (this.isSlotEnabled(i, side))
			{
				integerList.add(i);
			}
		}

		int[] intArray = new int[integerList.size()];
		for (int i = 0; i < intArray.length; i++)
		{
			intArray[i] = integerList.get(i);
		}

		return intArray;
//		switch (side)
//		{
//		case DOWN:
//			return new int[] { outputSlot };
//		case UP:
//			return new int[] { orbSlot };
//		default:
//			return new int[] { 0, 1, 2, 3, 4, 5 };
//		}
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction)
	{
		switch (index)
		{
		case outputSlot:
			return false;
		case orbSlot:
			return !stack.isEmpty() && stack.getItem() instanceof IBloodOrb;
		default:
			return this.isSlotEnabled(index, direction);
		}
//		switch (direction)
//		{
//		case DOWN:
//			return index != outputSlot && index != orbSlot;
//		case UP:
//			if (index == orbSlot && !stack.isEmpty() && stack.getItem() instanceof IBloodOrb)
//			{
//				return true;
//			} else
//			{
//				return true;
//			}
//		default:
//			if (this.isSlave)
//			{
//				TileEntity tile = getWorld().getTileEntity(connectedPos);
//				if (tile instanceof TileAlchemyTable && !((TileAlchemyTable) tile).isSlave)
//				{
//					return ((TileAlchemyTable) tile).canInsertItem(index, stack, direction);
//				}
//			}
//			return getAccessibleInputSlots(direction).contains(index);
//		}
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
	{
		switch (direction)
		{
		default:
			return this.isSlotEnabled(index, direction);
		}
	}

	public List<Integer> getAccessibleInputSlots(Direction direction)
	{
		List<Integer> list = new ArrayList<>();

		for (int i = 0; i < 6; i++)
		{
			if (isInputSlotAccessible(i))
			{
				list.add(i);
			}
		}

		return list;
	}

	public void tick()
	{
		if (isSlave())
		{
			return;
		}

		List<ItemStack> inputList = new ArrayList<>();

		ItemStack flaskStack = ItemStack.EMPTY;
		int flaskIndex = 0;
		int j = 0;

		for (int i = 0; i < 6; i++)
		{
			if (!getItem(i).isEmpty())
			{
				ItemStack slotStack = getItem(i);
				inputList.add(slotStack);
				if (slotStack.getItem() instanceof ItemAlchemyFlask && flaskStack.isEmpty())
				{
					flaskStack = slotStack;
					flaskIndex = j;
				}

				j++;
			}
		}

		int tier = getTierOfOrb();

		// Simple recipes
		RecipeAlchemyTable recipeAlchemyTable = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyTable(level, inputList);
		if (recipeAlchemyTable != null && (burnTime > 0 || (!getLevel().isClientSide && tier >= recipeAlchemyTable.getMinimumTier() && getContainedLp() >= recipeAlchemyTable.getSyphon())))
		{
			orbFlag = lpFlag = false;
			if (burnTime == 1)
				notifyUpdate();

			if (canCraft(recipeAlchemyTable.getOutput(inputList)))
			{
				ticksRequired = recipeAlchemyTable.getTicks();
				burnTime++;
				if (burnTime >= ticksRequired)
				{
					if (!getLevel().isClientSide)
					{
						if (recipeAlchemyTable.getSyphon() > 0)
						{
							if (consumeLp(recipeAlchemyTable.getSyphon()) < recipeAlchemyTable.getSyphon())
							{
								// There was not enough LP to craft or there was no orb
								burnTime = 0;
								notifyUpdate();
								return;
							}
						}

						ItemStack[] inputs = new ItemStack[0];
						for (ItemStack stack : inputList) ArrayUtils.add(inputs, stack.copy());

						BloodMagicCraftedEvent.AlchemyTable event = new BloodMagicCraftedEvent.AlchemyTable(recipeAlchemyTable.getOutput(inputList).copy(), inputs);
						MinecraftForge.EVENT_BUS.post(event);

						ItemStack outputSlotStack = getItem(outputSlot);
						if (outputSlotStack.isEmpty())
							setItem(outputSlot, event.getOutput());
						else
							outputSlotStack.grow(event.getOutput().getCount());

						consumeInventory(recipeAlchemyTable);

						burnTime = 0;
						notifyUpdate();
					}
				}
			}
			return;
		}

		if (!flaskStack.isEmpty())
		{
//			System.out.println("Haz flask");
			List<EffectHolder> holderList = ((ItemAlchemyFlask) flaskStack.getItem()).getEffectHoldersOfFlask(flaskStack);
			inputList.remove(flaskIndex);

			RecipePotionFlaskBase recipePotionFlask = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getPotionFlaskRecipe(level, flaskStack, holderList, inputList);
			if (recipePotionFlask != null && (burnTime > 0 || (!getLevel().isClientSide && tier >= recipePotionFlask.getMinimumTier() && getContainedLp() >= recipePotionFlask.getSyphon())))
			{
				orbFlag = lpFlag = false;
				if (burnTime == 1)
					notifyUpdate();

				if (getItem(outputSlot).isEmpty())
				{
					ticksRequired = recipePotionFlask.getTicks();
					burnTime++;

					if (burnTime >= ticksRequired)
					{
						if (!getLevel().isClientSide)
						{
							if (recipePotionFlask.getSyphon() > 0)
							{
								if (consumeLp(recipePotionFlask.getSyphon()) < recipePotionFlask.getSyphon())
								{
									// There was not enough LP to craft or there was no orb
									burnTime = 0;
									notifyUpdate();
									return;
								}
							}

//							ItemStack[] inputs = new ItemStack[0];
//							for (ItemStack stack : inputList) ArrayUtils.add(inputs, stack.copy());
//
//							BloodMagicCraftedEvent.AlchemyTable event = new BloodMagicCraftedEvent.AlchemyTable(recipeAlchemyTable.getOutput(inputList).copy(), inputs);
//							MinecraftForge.EVENT_BUS.post(event);
//
//							ItemStack outputSlotStack = getStackInSlot(outputSlot);
//							if (outputSlotStack.isEmpty())
//								setInventorySlotContents(outputSlot, event.getOutput());
//							else
//								outputSlotStack.grow(event.getOutput().getCount());

							ItemStack outputStack = recipePotionFlask.getOutput(flaskStack, holderList);
							if (outputStack.getItem() instanceof ItemAlchemyFlask)
							{
								((ItemAlchemyFlask) outputStack.getItem()).resyncEffectInstances(outputStack);
							}
							setItem(outputSlot, outputStack);

							// TODO: Need similar for the potion one, but isolate the flask
							consumeInventory(recipePotionFlask);

							burnTime = 0;
							notifyUpdate();
						}
					}
				}

				return;
			}
		}

		{
			burnTime = 0;
			if (!level.isClientSide)
			{
				boolean oldOrbFlag = orbFlag;
				boolean oldLPFlag = lpFlag;

				if (recipeAlchemyTable != null)
				{
					orbFlag = tier < recipeAlchemyTable.getMinimumTier() ? true : false;
					lpFlag = (!orbFlag && (getContainedLp() < recipeAlchemyTable.getSyphon())) ? true : false;
				} else
				{
					orbFlag = lpFlag = false;
				}

				if (orbFlag != oldOrbFlag || lpFlag != oldLPFlag)
				{
					BloodMagic.packetHandler.sendToAllTracking(new AlchemyTableFlagPacket(this), this);
				}
			}
		}
	}

	public double getProgressForGui()
	{
		return ((double) burnTime) / ticksRequired;
	}

	public boolean getOrbFlagForGui()
	{
		return orbFlag;
	}

	public boolean getLPFlagforGui()
	{
		return lpFlag;
	}

	public void setOrbFlagForGui(boolean orbFlag)
	{
		this.orbFlag = orbFlag;
	}

	public void setLPFlagForGui(boolean lpFlag)
	{
		this.lpFlag = lpFlag;
	}

	private boolean canCraft(ItemStack output)
	{
		ItemStack currentOutputStack = getItem(outputSlot);
		if (output.isEmpty())
			return false;
		if (currentOutputStack.isEmpty())
			return true;
		if (!ItemHandlerHelper.canItemStacksStack(output, currentOutputStack))
			return false;
		int result = currentOutputStack.getCount() + output.getCount();
		return result <= getMaxStackSize() && result <= currentOutputStack.getMaxStackSize();
	}

	public int getTierOfOrb()
	{
		ItemStack orbStack = getItem(orbSlot);
		if (!orbStack.isEmpty())
		{
			if (orbStack.getItem() instanceof IBloodOrb)
			{
				BloodOrb orb = ((IBloodOrb) orbStack.getItem()).getOrb(orbStack);
				return orb == null ? 0 : orb.getTier();
			}
		}

		return 0;
	}

	public int getContainedLp()
	{
		ItemStack orbStack = getItem(orbSlot);
		if (!orbStack.isEmpty())
		{
			if (orbStack.getItem() instanceof IBloodOrb)
			{
				Binding binding = ((IBindable) orbStack.getItem()).getBinding(orbStack);
				if (binding == null)
				{
					return 0;
				}

				SoulNetwork network = NetworkHelper.getSoulNetwork(binding);

				return network.getCurrentEssence();
			}
		}

		return 0;
	}

	public void craftItem(List<ItemStack> inputList, RecipeAlchemyTable recipe)
	{
		ItemStack outputStack = recipe.getOutput(inputList);
		if (this.canCraft(outputStack))
		{
			ItemStack currentOutputStack = getItem(outputSlot);

			ItemStack[] inputs = new ItemStack[0];
			for (ItemStack stack : inputList) ArrayUtils.add(inputs, stack.copy());

			BloodMagicCraftedEvent.AlchemyTable event = new BloodMagicCraftedEvent.AlchemyTable(outputStack.copy(), inputs);
			MinecraftForge.EVENT_BUS.post(event);
			outputStack = event.getOutput();

			if (currentOutputStack.isEmpty())
			{
				setItem(outputSlot, outputStack);
			} else if (ItemHandlerHelper.canItemStacksStack(outputStack, currentOutputStack))
			{
				currentOutputStack.grow(outputStack.getCount());
			}

			consumeInventory(recipe);
		}
	}

	public int consumeLp(int requested)
	{
		ItemStack orbStack = getItem(orbSlot);

		if (!orbStack.isEmpty())
		{
			if (orbStack.getItem() instanceof IBloodOrb)
			{
				if (NetworkHelper.syphonFromContainer(orbStack, SoulTicket.item(orbStack, level, worldPosition, requested)))
				{
					return requested;
				}
			}
		}

		return 0;
	}

	public void consumeInventory(RecipeAlchemyTable recipe)
	{
		for (int i = 0; i < 6; i++)
		{
			ItemStack inputStack = getItem(i);
			if (!inputStack.isEmpty())
			{
				if (inputStack.getItem() instanceof IAlchemyItem)
				{
					if (((IAlchemyItem) inputStack.getItem()).isStackChangedOnUse(inputStack))
					{
						setItem(i, ((IAlchemyItem) inputStack.getItem()).onConsumeInput(inputStack));
					}

					continue;
				} else if (inputStack.getItem().hasCraftingRemainingItem(inputStack))
				{
					setItem(i, inputStack.getItem().getCraftingRemainingItem(inputStack));
					continue;
				} else if (inputStack.getMaxDamage() > 0)
				{
//					inputStack.setDamage(inputStack.getDamage() + 1);
//					if (inputStack.getDamage() >= inputStack.getMaxDamage())
//					{
//						
//					}

					if (!inputStack.isDamageableItem())
					{
						continue;
					}
					if (inputStack.hurt(1, level.random, null))
					{
						setItem(i, ItemStack.EMPTY);
					}
					continue;
				}

				inputStack.shrink(1);
				if (inputStack.isEmpty())
				{
					setItem(i, ItemStack.EMPTY);
				}
			}
		}
	}

	public void consumeInventory(RecipePotionFlaskBase recipe)
	{
		for (int i = 0; i < 6; i++)
		{
			ItemStack inputStack = getItem(i);
			if (!inputStack.isEmpty())
			{
				if (inputStack.getItem() instanceof IAlchemyItem)
				{
					if (((IAlchemyItem) inputStack.getItem()).isStackChangedOnUse(inputStack))
					{
						setItem(i, ((IAlchemyItem) inputStack.getItem()).onConsumeInput(inputStack));
					}

					continue;
				} else if (inputStack.getItem().hasCraftingRemainingItem(inputStack))
				{
					setItem(i, inputStack.getItem().getCraftingRemainingItem(inputStack));
					continue;
				}

				inputStack.shrink(1);
				if (inputStack.isEmpty())
				{
					setItem(i, ItemStack.EMPTY);
				}
			}
		}
	}

	public Direction getDirection()
	{
		return direction;
	}

	public boolean isSlave()
	{
		return isSlave;
	}

	public int getBurnTime()
	{
		return burnTime;
	}

	public int getTicksRequired()
	{
		return ticksRequired;
	}

	public BlockPos getConnectedPos()
	{
		return connectedPos;
	}

	public boolean[] getBlockedSlots()
	{
		return blockedSlots;
	}

	public static int getOrbSlot()
	{
		return orbSlot;
	}

	public static int getOutputSlot()
	{
		return outputSlot;
	}

	@Override
	public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_)
	{
		assert level != null;
		return new ContainerAlchemyTable(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public Component getDisplayName()
	{
		return Component.literal("Alchemy Table");
	}
}