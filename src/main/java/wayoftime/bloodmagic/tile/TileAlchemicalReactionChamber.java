package wayoftime.bloodmagic.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
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
import wayoftime.bloodmagic.api.event.recipes.FluidStackIngredient;
import wayoftime.bloodmagic.api.impl.BloodMagicAPI;
import wayoftime.bloodmagic.api.impl.recipe.RecipeARC;
import wayoftime.bloodmagic.common.item.IARCTool;
import wayoftime.bloodmagic.common.item.inventory.InventoryWrapper;
import wayoftime.bloodmagic.common.tags.BloodMagicTags;
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

	public double currentProgress = 0;
	public static final double DEFAULT_SPEED = 0.005;

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

		ItemStack fullBucketStack = this.getStackInSlot(INPUT_BUCKET_SLOT);
		ItemStack emptyBucketStack = this.getStackInSlot(OUTPUT_BUCKET_SLOT);

		ItemStack[] outputInventory = new ItemStack[]
		{ getStackInSlot(1), getStackInSlot(2), getStackInSlot(3), getStackInSlot(4), getStackInSlot(5) };

		MultiSlotItemHandler outputSlotHandler = new MultiSlotItemHandler(outputInventory, 64);

		if (!world.isRemote)
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
							outputChanged = true;
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
		}

		ItemStack inputStack = this.getStackInSlot(INPUT_SLOT);
		ItemStack toolStack = this.getStackInSlot(ARC_TOOL_SLOT);

		double craftingMultiplier = 1;
		if (toolStack.getItem() instanceof IARCTool)
		{
			craftingMultiplier = ((IARCTool) toolStack.getItem()).getCraftingSpeedMultiplier(toolStack);
		}

		RecipeARC recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getARC(world, inputStack, toolStack, inputTank.getFluid());
		if (canCraft(recipe, outputSlotHandler))
		{
			// We have enough fluid (if applicable) and the theoretical outputs can fit.
			currentProgress += craftingMultiplier * DEFAULT_SPEED;
			if (currentProgress >= 1)
			{
				if (!world.isRemote)
				{
					outputChanged = true;
					craftItem(recipe, outputSlotHandler);
				}

				currentProgress = 0;
			}
		} else
		{
			if (toolStack.getItem().isIn(BloodMagicTags.ARC_TOOL_FURNACE))
			{
				InventoryWrapper invWrapper = new InventoryWrapper(1);
				invWrapper.setInventorySlotContents(0, inputStack.copy());
//				ItemStack[] outputInventory = new ItemStack[]
//				{ input };

//				MultiSlotItemHandler outputSlotHandler = new MultiSlotItemHandler(outputInventory, 64);
				Optional<FurnaceRecipe> furnaceRecipe = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, invWrapper, world);
				if (furnaceRecipe.isPresent())
				{
					ItemStack outputStack = furnaceRecipe.get().getCraftingResult(invWrapper);
					if (canCraftFurnace(outputStack, outputSlotHandler))
					{
						currentProgress += craftingMultiplier * DEFAULT_SPEED;
						if (currentProgress >= 1)
						{
							if (!world.isRemote)
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

		if (outputChanged && !world.isRemote)
		{
			for (int i = 0; i < NUM_OUTPUTS; i++)
			{
				this.setInventorySlotContents(OUTPUT_SLOT + i, outputSlotHandler.getStackInSlot(i));
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

			outputSlotHandler.canTransferAllItemsToSlots(recipe.getAllOutputs(world.rand), false);
			outputTank.fill(recipe.getFluidOutput().copy(), FluidAction.EXECUTE);
			consumeInventory();
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
		consumeInventory();
	}

	public void consumeInventory()
	{
		ItemStack inputStack = getStackInSlot(INPUT_SLOT);
		if (!inputStack.isEmpty())
		{
			if (inputStack.getItem().hasContainerItem(inputStack))
			{
				setInventorySlotContents(INPUT_SLOT, inputStack.getItem().getContainerItem(inputStack));
			} else
			{
				inputStack.shrink(1);
				if (inputStack.isEmpty())
				{
					setInventorySlotContents(INPUT_SLOT, ItemStack.EMPTY);
				}
			}
		}

		ItemStack toolStack = getStackInSlot(ARC_TOOL_SLOT);
		if (!toolStack.isEmpty())
		{
			if (toolStack.isDamageable())
			{
				toolStack.setDamage(toolStack.getDamage() + 1);
				if (toolStack.getDamage() >= toolStack.getMaxDamage())
				{
					setInventorySlotContents(ARC_TOOL_SLOT, ItemStack.EMPTY);
				}
			} else if (toolStack.getItem().hasContainerItem(toolStack))
			{
				setInventorySlotContents(ARC_TOOL_SLOT, toolStack.getItem().getContainerItem(inputStack));
			} else
			{
				toolStack.shrink(1);
				if (toolStack.isEmpty())
				{
					setInventorySlotContents(ARC_TOOL_SLOT, ItemStack.EMPTY);
				}
			}
		}
	}

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
		return currentProgress;
	}
}