package wayoftime.bloodmagic.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.tile.contailer.ContainerAlchemicalReactionChamber;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.MultiSlotItemHandler;

public class TileAlchemicalReactionChamber extends TileInventory implements ITickableTileEntity, INamedContainerProvider
{
	@ObjectHolder("bloodmagic:alchemicalreactionchamber")
	public static TileEntityType<TileAlchemicalReactionChamber> TYPE;

	public static final int ARC_TOOL_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int NUM_OUTPUTS = 5;
	public static final int INPUT_SLOT = 6;
	public static final int INPUT_BUCKET_SLOT = 7;
	public static final int OUTPUT_BUCKET_SLOT = 8;

	public FluidTank inputTank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 20);
	public FluidTank outputTank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 20);

	public int burnTime = 0;

	public TileAlchemicalReactionChamber(TileEntityType<?> type)
	{
		super(type, 9, "alchemicalreactionchamber");
	}

	public TileAlchemicalReactionChamber()
	{
		this(TYPE);
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		super.deserialize(tag);

		burnTime = tag.getInt(Constants.NBT.SOUL_FORGE_BURN);

		CompoundNBT inputTankTag = tag.getCompound("inputtank");
		inputTank.readFromNBT(inputTankTag);

		CompoundNBT outputTankTag = tag.getCompound("outputtank");
		outputTank.readFromNBT(outputTankTag);
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		super.serialize(tag);

		tag.putInt(Constants.NBT.SOUL_FORGE_BURN, burnTime);

		CompoundNBT inputTankTag = new CompoundNBT();
		inputTank.writeToNBT(inputTankTag);
		tag.put("inputtank", inputTankTag);

		CompoundNBT outputTankTag = new CompoundNBT();
		outputTank.writeToNBT(outputTankTag);
		tag.put("outputtank", outputTankTag);

		return tag;
	}

	@Override
	public void tick()
	{
		if (world.isRemote)
		{
			return;
		}

		if (world.getGameTime() % 20 == 0)
		{
			outputTank.fill(new FluidStack(Fluids.WATER, 100), FluidAction.EXECUTE);
		}

		ItemStack fullBucketStack = this.getStackInSlot(INPUT_BUCKET_SLOT);
		ItemStack emptyBucketStack = this.getStackInSlot(OUTPUT_BUCKET_SLOT);

		ItemStack[] outputInventory = new ItemStack[]
		{ getStackInSlot(1), getStackInSlot(2), getStackInSlot(3), getStackInSlot(4), getStackInSlot(5) };

		MultiSlotItemHandler outputSlotHandler = new MultiSlotItemHandler(outputInventory, 64);

		if (!fullBucketStack.isEmpty() && inputTank.getSpace() >= 1000)
		{
			ItemStack testFullBucketStack = ItemHandlerHelper.copyStackWithSize(fullBucketStack, 1);
			LazyOptional<IFluidHandlerItem> fluidHandlerWrapper = FluidUtil.getFluidHandler(testFullBucketStack);
			if (fluidHandlerWrapper.isPresent())
			{
				IFluidHandlerItem fluidHandler = fluidHandlerWrapper.resolve().get();
				FluidStack transferedStack = FluidUtil.tryFluidTransfer(inputTank, fluidHandler, 1000, false);
				if (!transferedStack.isEmpty())
				{
					fluidHandler.drain(transferedStack, FluidAction.EXECUTE);
					List<ItemStack> arrayList = new ArrayList<>();
					arrayList.add(fluidHandler.getContainer());
					if (outputSlotHandler.canTransferAllItemsToSlots(arrayList, true))
					{
						inputTank.fill(transferedStack, FluidAction.EXECUTE);
						outputSlotHandler.canTransferAllItemsToSlots(arrayList, false);
						if (fullBucketStack.getCount() > 1)
						{
							fullBucketStack.setCount(fullBucketStack.getCount() - 1);
						} else
						{
							setInventorySlotContents(INPUT_BUCKET_SLOT, ItemStack.EMPTY);
						}
					}
				}
			}
		}

		if (!emptyBucketStack.isEmpty() && outputTank.getFluidAmount() >= 1000)
		{
			ItemStack testEmptyBucketStack = ItemHandlerHelper.copyStackWithSize(emptyBucketStack, 1);
			LazyOptional<IFluidHandlerItem> fluidHandlerWrapper = FluidUtil.getFluidHandler(testEmptyBucketStack);
			if (fluidHandlerWrapper.isPresent())
			{
				IFluidHandlerItem fluidHandler = fluidHandlerWrapper.resolve().get();
				FluidStack transferedStack = FluidUtil.tryFluidTransfer(fluidHandler, outputTank, 1000, false);
				if (!transferedStack.isEmpty())
				{
					fluidHandler.fill(transferedStack, FluidAction.EXECUTE);
					List<ItemStack> arrayList = new ArrayList<>();
					arrayList.add(fluidHandler.getContainer());
					if (outputSlotHandler.canTransferAllItemsToSlots(arrayList, true))
					{
						outputTank.drain(transferedStack, FluidAction.EXECUTE);
						outputSlotHandler.canTransferAllItemsToSlots(arrayList, false);
						if (emptyBucketStack.getCount() > 1)
						{
							emptyBucketStack.setCount(emptyBucketStack.getCount() - 1);
						} else
						{
							setInventorySlotContents(OUTPUT_BUCKET_SLOT, ItemStack.EMPTY);
						}
					}
				}
			}
		}

		for (int i = 0; i < NUM_OUTPUTS; i++)
		{
			this.setInventorySlotContents(OUTPUT_SLOT + i, outputSlotHandler.getStackInSlot(i));
		}

//		FluidUtil.tryEmptyContainer(container, fluidDestination, maxAmount, player, doDrain)
	}

//	private boolean canCraft(RecipeTartaricForge recipe)
//	{
//		if (recipe == null)
//			return false;
//
//		ItemStack currentOutputStack = getStackInSlot(outputSlot);
//		if (recipe.getOutput().isEmpty())
//			return false;
//		if (currentOutputStack.isEmpty())
//			return true;
//		if (!currentOutputStack.isItemEqual(recipe.getOutput()))
//			return false;
//		int result = currentOutputStack.getCount() + recipe.getOutput().getCount();
//		return result <= getInventoryStackLimit() && result <= currentOutputStack.getMaxStackSize();
//
//	}
//
//	public void craftItem(RecipeTartaricForge recipe)
//	{
//		if (this.canCraft(recipe))
//		{
//			ItemStack currentOutputStack = getStackInSlot(outputSlot);
//
//			List<ItemStack> inputList = new ArrayList<>();
//			for (int i = 0; i < 4; i++) if (!getStackInSlot(i).isEmpty())
//				inputList.add(getStackInSlot(i).copy());
//
//			BloodMagicCraftedEvent.SoulForge event = new BloodMagicCraftedEvent.SoulForge(recipe.getOutput().copy(), inputList.toArray(new ItemStack[0]));
//			MinecraftForge.EVENT_BUS.post(event);
//
//			if (currentOutputStack.isEmpty())
//			{
//				setInventorySlotContents(outputSlot, event.getOutput());
//			} else if (ItemHandlerHelper.canItemStacksStack(currentOutputStack, event.getOutput()))
//			{
//				currentOutputStack.grow(event.getOutput().getCount());
//			}
//
//			consumeInventory();
//		}
//	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_)
	{
		assert world != null;
		return new ContainerAlchemicalReactionChamber(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("Alchemical Reaction Chamber");
	}

	public double getProgressForGui()
	{
		return 0;
	}
}