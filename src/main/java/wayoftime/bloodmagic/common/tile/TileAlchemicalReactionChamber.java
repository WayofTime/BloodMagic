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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.block.BlockAlchemicalReactionChamber;
import wayoftime.bloodmagic.common.container.tile.ContainerAlchemicalReactionChamber;
import wayoftime.bloodmagic.common.item.arc.IARCTool;
import wayoftime.bloodmagic.common.item.inventory.InventoryWrapper;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.network.ARCTanksPacket;
import wayoftime.bloodmagic.recipe.RecipeARC;
import wayoftime.bloodmagic.recipe.helper.FluidStackIngredient;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.MultiSlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TileAlchemicalReactionChamber extends TileInventory implements MenuProvider, WorldlyContainer, IFluidHandler
{
	public static final int ARC_TOOL_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int NUM_OUTPUTS = 5;
	public static final int INPUT_SLOT = 6;
	public static final int INPUT_BUCKET_SLOT = 7;
	public static final int OUTPUT_BUCKET_SLOT = 8;

	public FluidTank inputTank = new FluidTank(FluidType.BUCKET_VOLUME * 20);
	public FluidTank outputTank = new FluidTank(FluidType.BUCKET_VOLUME * 20);

	public double currentProgress = 0;
	public static final double DEFAULT_SPEED = 0.005;

	private LazyOptional fluidOptional;

	public TileAlchemicalReactionChamber(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, 9, "alchemicalreactionchamber", pos, state);
		this.initializeFluidCapabilities();
	}

	public TileAlchemicalReactionChamber(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.ARC_TYPE.get(), pos, state);
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		super.deserialize(tag);

		currentProgress = tag.getDouble(Constants.NBT.ARC_PROGRESS);

		CompoundTag inputTankTag = tag.getCompound("inputtank");
		inputTank.readFromNBT(inputTankTag);

		CompoundTag outputTankTag = tag.getCompound("outputtank");
		outputTank.readFromNBT(outputTankTag);
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		super.serialize(tag);

		tag.putDouble(Constants.NBT.ARC_PROGRESS, currentProgress);

		CompoundTag inputTankTag = new CompoundTag();
		inputTank.writeToNBT(inputTankTag);
		tag.put("inputtank", inputTankTag);

		CompoundTag outputTankTag = new CompoundTag();
		outputTank.writeToNBT(outputTankTag);
		tag.put("outputtank", outputTankTag);

		return tag;
	}

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

		ItemStack[] outputInventory = new ItemStack[] { getItem(1), getItem(2), getItem(3), getItem(4), getItem(5) };

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
		EnumDemonWillType type = EnumDemonWillType.DEFAULT;
		if (toolStack.getItem() instanceof IARCTool)
		{
			craftingMultiplier = ((IARCTool) toolStack.getItem()).getCraftingSpeedMultiplier(toolStack);
			type = ((IARCTool) toolStack.getItem()).getDominantWillType(toolStack);
		}

		RecipeARC recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getARC(level, inputStack, toolStack, inputTank.getFluid());
		if (canCraft(recipe, inputStack, toolStack, outputSlotHandler))
		{
			// We have enough fluid (if applicable) and the theoretical outputs can fit.
			setIsCrafting(level, worldPosition, getBlockState(), true);
			setDisplayedType(level, worldPosition, getBlockState(), type);
			currentProgress += craftingMultiplier * DEFAULT_SPEED;
			if (currentProgress >= 1)
			{
				if (!level.isClientSide)
				{
					outputChanged = true;
					double bonusChance = 1;
					if (toolStack.getItem() instanceof IARCTool)
					{
						bonusChance = ((IARCTool) toolStack.getItem()).getAdditionalOutputChanceMultiplier(toolStack);
					}

					craftItem(recipe, inputStack, toolStack, outputSlotHandler, bonusChance);
				}

				currentProgress = 0;
			}
		} else
		{
			if (toolStack.is(BloodMagicTags.ARC_TOOL_FURNACE))
			{
				InventoryWrapper invWrapper = new InventoryWrapper(1);
				invWrapper.setItem(0, inputStack.copy());
//				ItemStack[] outputInventory = new ItemStack[]
//				{ input };

//				MultiSlotItemHandler outputSlotHandler = new MultiSlotItemHandler(outputInventory, 64);
				Optional<SmeltingRecipe> furnaceRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, invWrapper, level);
				if (furnaceRecipe.isPresent())
				{
					ItemStack outputStack = furnaceRecipe.get().assemble(invWrapper, level.registryAccess());
					if (canCraftFurnace(outputStack, outputSlotHandler))
					{
						setIsCrafting(level, worldPosition, getBlockState(), true);
						setDisplayedType(level, worldPosition, getBlockState(), type);
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
					setIsCrafting(level, getBlockPos(), getBlockState(), false);
					setDisplayedType(level, worldPosition, getBlockState(), type);
				}
			} else
			{
				currentProgress = 0;
				setIsCrafting(level, getBlockPos(), getBlockState(), false);
				setDisplayedType(level, worldPosition, getBlockState(), type);
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

	public void setIsCrafting(Level world, BlockPos pos, BlockState state, boolean isCrafting)
	{
		boolean isCurrentlyCrafting = state.getValue(BlockAlchemicalReactionChamber.LIT);
		if (isCurrentlyCrafting != isCrafting)
		{
			world.setBlock(pos, state.setValue(BlockAlchemicalReactionChamber.LIT, isCrafting), 2);
		}
	}

	public void setDisplayedType(Level world, BlockPos pos, BlockState state, EnumDemonWillType type)
	{
		EnumDemonWillType currentType = state.getValue(BlockAlchemicalReactionChamber.TYPE);
		if (type != currentType)
		{
			world.setBlock(pos, state.setValue(BlockAlchemicalReactionChamber.TYPE, type), 2);
		}
	}

	private boolean canCraft(RecipeARC recipe, ItemStack inputStack, ItemStack toolStack, MultiSlotItemHandler outputSlotHandler)
	{
		if (recipe == null)
			return false;

		if (inputStack.getCount() < recipe.getRequiredInputCount())
		{
			return false;
		}

		if (toolStack.isDamageableItem() && toolStack.getDamageValue() >= toolStack.getMaxDamage())
		{
			return false;
		}

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

	private void craftItem(RecipeARC recipe, ItemStack inputStack, ItemStack toolStack, MultiSlotItemHandler outputSlotHandler, double modifier)
	{
		if (this.canCraft(recipe, inputStack, toolStack, outputSlotHandler))
		{
			if (recipe.getFluidIngredient() != null)
			{
				FluidStack inputFluidStack = recipe.getFluidIngredient().getMatchingInstance(inputTank.getFluid());
				inputTank.drain(inputFluidStack, FluidAction.EXECUTE);
			}

			outputSlotHandler.canTransferAllItemsToSlots(recipe.getAllOutputs(level.random, inputStack, toolStack, modifier), false);
			outputTank.fill(recipe.getFluidOutput().copy(), FluidAction.EXECUTE);
			consumeInventory(recipe.getRequiredInputCount(), recipe.getConsumeIngredient(), recipe.breakTool());
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
		consumeInventory(1, false, false);
	}

	public void consumeInventory(int inputCount, boolean consumeInput, boolean breakTool)
	{
		ItemStack inputStack = getItem(INPUT_SLOT);
		if (!inputStack.isEmpty())
		{
			if (!consumeInput && inputStack.getItem().hasCraftingRemainingItem(inputStack))
			{
				setItem(INPUT_SLOT, inputStack.getItem().getCraftingRemainingItem(inputStack));
			} else
			{
				inputStack.shrink(inputCount);
				if (inputStack.isEmpty())
				{
					setItem(INPUT_SLOT, ItemStack.EMPTY);
				}
			}
		}

		ItemStack toolStack = getItem(ARC_TOOL_SLOT);
		if (!toolStack.isEmpty())
		{
			if (toolStack.getItem().isDamageable(toolStack))
			{
				int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, toolStack);
				if (toolStack.isDamageableItem() && (unbreakingLevel == 0 || level.random.nextInt(unbreakingLevel + 1) == 0))
				{
					toolStack.setDamageValue(toolStack.getDamageValue() + 1);
					if (toolStack.getDamageValue() >= toolStack.getMaxDamage() && breakTool)
					{
						setItem(ARC_TOOL_SLOT, ItemStack.EMPTY);
					}
				}
			} else if (toolStack.getItem().hasCraftingRemainingItem(toolStack))
			{
				setItem(ARC_TOOL_SLOT, toolStack.getItem().getCraftingRemainingItem(inputStack));
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
	public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_)
	{
		assert level != null;
		return new ContainerAlchemicalReactionChamber(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public Component getDisplayName()
	{
		return Component.literal("Alchemical Reaction Chamber");
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
			return itemStack.is(BloodMagicTags.ARC_TOOL);
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
		if (capability == ForgeCapabilities.FLUID_HANDLER)
		{
			return fluidOptional.cast();
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public void invalidateCaps()
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