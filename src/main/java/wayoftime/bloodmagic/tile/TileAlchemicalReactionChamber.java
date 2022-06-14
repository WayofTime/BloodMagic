package wayoftime.bloodmagic.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.item.arc.IARCTool;
import wayoftime.bloodmagic.common.item.inventory.InventoryWrapper;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.network.ARCTanksPacket;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.recipe.helper.FluidStackIngredient;
import wayoftime.bloodmagic.tile.container.ContainerAlchemicalReactionChamber;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.MultiSlotItemHandler;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TileAlchemicalReactionChamber extends TileInventory implements ITickableTileEntity, INamedContainerProvider, ISidedInventory, IFluidHandler
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

	public double currentProgress = 0;
	public static final double DEFAULT_SPEED = 0.005;

	private LazyOptional fluidOptional;

	public TileAlchemicalReactionChamber(TileEntityType<?> type)
	{
		super(type, 9, "alchemicalreactionchamber");
		this.initializeFluidCapabilities();
	}

	public TileAlchemicalReactionChamber()
	{
		this(TYPE);
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		super.deserialize(tag);

		currentProgress = tag.getDouble(Constants.NBT.ARC_PROGRESS);

		CompoundNBT inputTankTag = tag.getCompound("inputtank");
		inputTank.readFromNBT(inputTankTag);

		CompoundNBT outputTankTag = tag.getCompound("outputtank");
		outputTank.readFromNBT(outputTankTag);
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		super.serialize(tag);

		tag.putDouble(Constants.NBT.ARC_PROGRESS, currentProgress);

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
//		if (world.isRemote)
//		{
//			return;
//		}

//		if (world.getGameTime() % 20 == 0)
//		{
//			outputTank.fill(new FluidStack(Fluids.WATER, 100), FluidAction.EXECUTE);
//		}

		boolean outputChanged = false;

		ItemStack fullBucketStack = this.getItem(INPUT_BUCKET_SLOT);
		ItemStack emptyBucketStack = this.getItem(OUTPUT_BUCKET_SLOT);

		ItemStack[] outputInventory = new ItemStack[] { getItem(1), getItem(2), getItem(3),
				getItem(4), getItem(5) };

		MultiSlotItemHandler outputSlotHandler = new MultiSlotItemHandler(outputInventory, 64);

		if (!level.isClientSide)
		{
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
							outputChanged = true;
							inputTank.fill(transferedStack, FluidAction.EXECUTE);
							outputSlotHandler.canTransferAllItemsToSlots(arrayList, false);
							if (fullBucketStack.getCount() > 1)
							{
								fullBucketStack.setCount(fullBucketStack.getCount() - 1);
							} else
							{
								setItem(INPUT_BUCKET_SLOT, ItemStack.EMPTY);
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
							outputChanged = true;
							outputTank.drain(transferedStack, FluidAction.EXECUTE);
							outputSlotHandler.canTransferAllItemsToSlots(arrayList, false);
							if (emptyBucketStack.getCount() > 1)
							{
								emptyBucketStack.setCount(emptyBucketStack.getCount() - 1);
							} else
							{
								setItem(OUTPUT_BUCKET_SLOT, ItemStack.EMPTY);
							}
						}
					}
				}
			}
		}

		ItemStack inputStack = this.getItem(INPUT_SLOT);
		ItemStack toolStack = this.getItem(ARC_TOOL_SLOT);

		double craftingMultiplier = 1;
		if (toolStack.getItem() instanceof IARCTool)
		{
			craftingMultiplier = ((IARCTool) toolStack.getItem()).getCraftingSpeedMultiplier(toolStack);
		}

		RecipeARC recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getARC(level, inputStack, toolStack, inputTank.getFluid());
		if (canCraft(recipe, outputSlotHandler))
		{
			// We have enough fluid (if applicable) and the theoretical outputs can fit.
			currentProgress += craftingMultiplier * DEFAULT_SPEED;
			if (currentProgress >= 1)
			{
				if (!level.isClientSide)
				{
					outputChanged = true;
					craftItem(recipe, outputSlotHandler);
				}

				currentProgress = 0;
			}
		} else
		{
			if (toolStack.getItem().is(BloodMagicTags.ARC_TOOL_FURNACE))
			{
				InventoryWrapper invWrapper = new InventoryWrapper(1);
				invWrapper.setItem(0, inputStack.copy());
//				ItemStack[] outputInventory = new ItemStack[]
//				{ input };

//				MultiSlotItemHandler outputSlotHandler = new MultiSlotItemHandler(outputInventory, 64);
				Optional<FurnaceRecipe> furnaceRecipe = level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, invWrapper, level);
				if (furnaceRecipe.isPresent())
				{
					ItemStack outputStack = furnaceRecipe.get().assemble(invWrapper);
					if (canCraftFurnace(outputStack, outputSlotHandler))
					{
						currentProgress += craftingMultiplier * DEFAULT_SPEED;
						if (currentProgress >= 1)
						{
							if (!level.isClientSide)
							{
								craftFurnace(outputStack, outputSlotHandler);
								outputChanged = true;
							}

							currentProgress = 0;
						}
					}
				} else
				{
					currentProgress = 0;
				}
			} else
			{
				currentProgress = 0;
			}
		}

		if (outputChanged && !level.isClientSide)
		{
			for (int i = 0; i < NUM_OUTPUTS; i++)
			{
				this.setItem(OUTPUT_SLOT + i, outputSlotHandler.getStackInSlot(i));
			}
		}

//		FluidUtil.tryEmptyContainer(container, fluidDestination, maxAmount, player, doDrain)
	}

	private boolean canCraft(RecipeARC recipe, MultiSlotItemHandler outputSlotHandler)
	{
		if (recipe == null)
			return false;

		FluidStackIngredient inputFluidIngredient = recipe.getFluidIngredient();
		if (inputFluidIngredient != null && !inputFluidIngredient.test(inputTank.getFluid()))
		{
			return false;
		}

		if (outputSlotHandler.canTransferAllItemsToSlots(recipe.getAllListedOutputs(), true))
		{
			FluidStack outputStack = recipe.getFluidOutput();
			return outputStack.isEmpty() ? true
					: outputTank.fill(outputStack, FluidAction.SIMULATE) >= outputStack.getAmount();
		}

		return false;
	}

	private void craftItem(RecipeARC recipe, MultiSlotItemHandler outputSlotHandler)
	{
		if (this.canCraft(recipe, outputSlotHandler))
		{
			if (recipe.getFluidIngredient() != null)
			{
				FluidStack inputStack = recipe.getFluidIngredient().getMatchingInstance(inputTank.getFluid());
				inputTank.drain(inputStack, FluidAction.EXECUTE);
			}

			outputSlotHandler.canTransferAllItemsToSlots(recipe.getAllOutputs(level.random), false);
			outputTank.fill(recipe.getFluidOutput().copy(), FluidAction.EXECUTE);
			consumeInventory(recipe.getConsumeIngredient());
		}
	}

	private boolean canCraftFurnace(ItemStack outputStack, MultiSlotItemHandler outputSlotHandler)
	{
		List<ItemStack> outputList = new ArrayList<>();
		outputList.add(outputStack);
		return outputSlotHandler.canTransferAllItemsToSlots(outputList, true);
	}

	private void craftFurnace(ItemStack outputStack, MultiSlotItemHandler outputSlotHandler)
	{
		List<ItemStack> outputList = new ArrayList<>();
		outputList.add(outputStack);
		outputSlotHandler.canTransferAllItemsToSlots(outputList, false);
		consumeInventory(false);
	}

	public void consumeInventory(boolean consumeInput)
	{
		ItemStack inputStack = getItem(INPUT_SLOT);
		if (!inputStack.isEmpty())
		{
			if (!consumeInput && inputStack.getItem().hasContainerItem(inputStack))
			{
				setItem(INPUT_SLOT, inputStack.getItem().getContainerItem(inputStack));
			} else
			{
				inputStack.shrink(1);
				if (inputStack.isEmpty())
				{
					setItem(INPUT_SLOT, ItemStack.EMPTY);
				}
			}
		}

		ItemStack toolStack = getItem(ARC_TOOL_SLOT);
		if (!toolStack.isEmpty())
		{
			if (toolStack.isDamageableItem())
			{
				int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, toolStack);
				if (unbreakingLevel == 0 || level.random.nextInt(unbreakingLevel + 1) == 0)
				{
					toolStack.setDamageValue(toolStack.getDamageValue() + 1);
					if (toolStack.getDamageValue() >= toolStack.getMaxDamage())
					{
						setItem(ARC_TOOL_SLOT, ItemStack.EMPTY);
					}
				}
			} else if (toolStack.getItem().hasContainerItem(toolStack))
			{
				setItem(ARC_TOOL_SLOT, toolStack.getItem().getContainerItem(inputStack));
			} else
			{
				toolStack.shrink(1);
				if (toolStack.isEmpty())
				{
					setItem(ARC_TOOL_SLOT, ItemStack.EMPTY);
				}
			}
		}
	}

	@Override
	public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_)
	{
		assert level != null;
		return new ContainerAlchemicalReactionChamber(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("Alchemical Reaction Chamber");
	}

	public double getProgressForGui()
	{
		return currentProgress;
	}

	@Override
	public int[] getSlotsForFace(Direction side)
	{
		switch (side)
		{
		case UP:
			return new int[] { ARC_TOOL_SLOT };
		case DOWN:
			return new int[] { 1, 2, 3, 4, 5 };
		default:
			return new int[] { 6, 7, 8 };
		}
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, Direction direction)
	{
		if (index == INPUT_BUCKET_SLOT || index == OUTPUT_BUCKET_SLOT)
		{
			Optional<FluidStack> fluidStackOptional = FluidUtil.getFluidContained(itemStack);

			return fluidStackOptional.isPresent() && ((index == OUTPUT_BUCKET_SLOT && !fluidStackOptional.get().isEmpty()) || (index == INPUT_BUCKET_SLOT && fluidStackOptional.get().isEmpty()));
		}

		if (index >= OUTPUT_SLOT && index < OUTPUT_SLOT + NUM_OUTPUTS)
		{
			return false;
		}

		if (index == ARC_TOOL_SLOT)
		{
			return itemStack.getItem().is(BloodMagicTags.ARC_TOOL);
		}

		return true;
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
	{
		return index >= OUTPUT_SLOT && index < OUTPUT_SLOT + NUM_OUTPUTS;
	}

	protected void initializeFluidCapabilities()
	{
		this.fluidOptional = LazyOptional.of(() -> this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return fluidOptional.cast();
		}

		return super.getCapability(capability, facing);
	}

	@Override
	protected void invalidateCaps()
	{
		super.invalidateCaps();
		if (fluidOptional != null)
		{
			fluidOptional.invalidate();
			fluidOptional = null;
		}
	}

	@Override
	public int getTanks()
	{
		return 2;
	}

	@Override
	public FluidStack getFluidInTank(int tank)
	{
		switch (tank)
		{
		case 0:
			return inputTank.getFluid();
		default:
			return outputTank.getFluid();
		}
	}

	@Override
	public int getTankCapacity(int tank)
	{
		switch (tank)
		{
		case 0:
			return inputTank.getCapacity();
		default:
			return outputTank.getCapacity();
		}
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack)
	{
		switch (tank)
		{
		case 0:
			return inputTank.isFluidValid(stack);
		default:
			return outputTank.isFluidValid(stack);
		}
	}

	@Override
	public int fill(FluidStack resource, FluidAction action)
	{
		int fillAmount = inputTank.fill(resource, action);
		if (fillAmount > 0 && !level.isClientSide)
		{
			BloodMagic.packetHandler.sendToAllTracking(new ARCTanksPacket(this), this);
//			this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
		}
		return fillAmount;
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action)
	{
		FluidStack drainedStack = outputTank.drain(resource, action);
		if (!drainedStack.isEmpty() && !level.isClientSide)
		{
			BloodMagic.packetHandler.sendToAllTracking(new ARCTanksPacket(this), this);
//			this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
		}
		return drainedStack;
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action)
	{
		FluidStack drainedStack = outputTank.drain(maxDrain, action);
		if (!drainedStack.isEmpty() && !level.isClientSide)
		{
			BloodMagic.packetHandler.sendToAllTracking(new ARCTanksPacket(this), this);
//			this.world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
		}
		return drainedStack;
	}
}