package wayoftime.bloodmagic.tile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.api.event.BloodMagicCraftedEvent;
import wayoftime.bloodmagic.common.item.BloodOrb;
import wayoftime.bloodmagic.common.item.IAlchemyItem;
import wayoftime.bloodmagic.common.item.IBindable;
import wayoftime.bloodmagic.common.item.IBloodOrb;
import wayoftime.bloodmagic.core.data.Binding;
import wayoftime.bloodmagic.core.data.SoulNetwork;
import wayoftime.bloodmagic.core.data.SoulTicket;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeAlchemyTable;
import wayoftime.bloodmagic.tile.container.ContainerAlchemyTable;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NetworkHelper;

public class TileAlchemyTable extends TileInventory implements ISidedInventory, ITickableTileEntity, INamedContainerProvider
{
	@ObjectHolder("bloodmagic:alchemytable")
	public static TileEntityType<TileAlchemyTable> TYPE;

	public static final int orbSlot = 6;
	public static final int outputSlot = 7;

	public Direction direction = Direction.NORTH;
	public boolean isSlave = false;
	public int burnTime = 0;
	public int ticksRequired = 1;

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

	public TileAlchemyTable(TileEntityType<?> type)
	{
		super(type, 8, "alchemytable");
	}

	public TileAlchemyTable()
	{
		this(TYPE);
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
	public void deserialize(CompoundNBT tag)
	{
		super.deserialize(tag);

		isSlave = tag.getBoolean("isSlave");
		direction = Direction.byIndex(tag.getInt(Constants.NBT.DIRECTION));
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
	public CompoundNBT serialize(CompoundNBT tag)
	{
		super.serialize(tag);

		tag.putBoolean("isSlave", isSlave);
		tag.putInt(Constants.NBT.DIRECTION, direction.getIndex());
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
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if (this.isSlave())
			{
				TileEntity tile = getWorld().getTileEntity(connectedPos);
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
	public boolean canInsertItem(int index, ItemStack stack, Direction direction)
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
	public boolean canExtractItem(int index, ItemStack stack, Direction direction)
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

	@Override
	public void tick()
	{
		if (isSlave())
		{
			return;
		}

		List<ItemStack> inputList = new ArrayList<>();

		for (int i = 0; i < 6; i++)
		{
			if (!getStackInSlot(i).isEmpty())
			{
				inputList.add(getStackInSlot(i));
			}
		}

		int tier = getTierOfOrb();

		// Simple recipes
		RecipeAlchemyTable recipeAlchemyTable = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAlchemyTable(world, inputList);
		if (recipeAlchemyTable != null && (burnTime > 0 || (!getWorld().isRemote && tier >= recipeAlchemyTable.getMinimumTier() && getContainedLp() >= recipeAlchemyTable.getSyphon())))
		{
			if (burnTime == 1)
				notifyUpdate();

			if (canCraft(recipeAlchemyTable.getOutput(inputList)))
			{
				ticksRequired = recipeAlchemyTable.getTicks();
				burnTime++;
				if (burnTime >= ticksRequired)
				{
					if (!getWorld().isRemote)
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

						ItemStack outputSlotStack = getStackInSlot(outputSlot);
						if (outputSlotStack.isEmpty())
							setInventorySlotContents(outputSlot, event.getOutput());
						else
							outputSlotStack.grow(event.getOutput().getCount());

						consumeInventory(recipeAlchemyTable);

						burnTime = 0;
						notifyUpdate();
					}
				}
			}
		} else
		{
			burnTime = 0;
		}

	}

	public double getProgressForGui()
	{
		return ((double) burnTime) / ticksRequired;
	}

	private boolean canCraft(ItemStack output)
	{
		ItemStack currentOutputStack = getStackInSlot(outputSlot);
		if (output.isEmpty())
			return false;
		if (currentOutputStack.isEmpty())
			return true;
		if (!ItemHandlerHelper.canItemStacksStack(output, currentOutputStack))
			return false;
		int result = currentOutputStack.getCount() + output.getCount();
		return result <= getInventoryStackLimit() && result <= currentOutputStack.getMaxStackSize();
	}

	public int getTierOfOrb()
	{
		ItemStack orbStack = getStackInSlot(orbSlot);
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
		ItemStack orbStack = getStackInSlot(orbSlot);
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
			ItemStack currentOutputStack = getStackInSlot(outputSlot);

			ItemStack[] inputs = new ItemStack[0];
			for (ItemStack stack : inputList) ArrayUtils.add(inputs, stack.copy());

			BloodMagicCraftedEvent.AlchemyTable event = new BloodMagicCraftedEvent.AlchemyTable(outputStack.copy(), inputs);
			MinecraftForge.EVENT_BUS.post(event);
			outputStack = event.getOutput();

			if (currentOutputStack.isEmpty())
			{
				setInventorySlotContents(outputSlot, outputStack);
			} else if (ItemHandlerHelper.canItemStacksStack(outputStack, currentOutputStack))
			{
				currentOutputStack.grow(outputStack.getCount());
			}

			consumeInventory(recipe);
		}
	}

	public int consumeLp(int requested)
	{
		ItemStack orbStack = getStackInSlot(orbSlot);

		if (!orbStack.isEmpty())
		{
			if (orbStack.getItem() instanceof IBloodOrb)
			{
				if (NetworkHelper.syphonFromContainer(orbStack, SoulTicket.item(orbStack, world, pos, requested)))
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
			ItemStack inputStack = getStackInSlot(i);
			if (!inputStack.isEmpty())
			{
				if (inputStack.getItem() instanceof IAlchemyItem)
				{
					if (((IAlchemyItem) inputStack.getItem()).isStackChangedOnUse(inputStack))
					{
						setInventorySlotContents(i, ((IAlchemyItem) inputStack.getItem()).onConsumeInput(inputStack));
					}

					continue;
				} else if (inputStack.getItem().hasContainerItem(inputStack))
				{
					setInventorySlotContents(i, inputStack.getItem().getContainerItem(inputStack));
					continue;
				} else if (inputStack.getMaxDamage() > 0)
				{
//					inputStack.setDamage(inputStack.getDamage() + 1);
//					if (inputStack.getDamage() >= inputStack.getMaxDamage())
//					{
//						
//					}

					if (!inputStack.isDamageable())
					{
						continue;
					}
					if (inputStack.attemptDamageItem(1, world.rand, null))
					{
						setInventorySlotContents(i, ItemStack.EMPTY);
					}
					continue;
				}

				inputStack.shrink(1);
				if (inputStack.isEmpty())
				{
					setInventorySlotContents(i, ItemStack.EMPTY);
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
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_)
	{
		assert world != null;
		return new ContainerAlchemyTable(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("Alchemy Table");
	}
}