package wayoftime.bloodmagic.common.blockentity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.menu.ARCMenu;
import wayoftime.bloodmagic.common.block.ARCBlock;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.EnumWillType;
import wayoftime.bloodmagic.common.recipe.BMRecipes;
import wayoftime.bloodmagic.common.recipe.arc.ARCRecipe;
import wayoftime.bloodmagic.common.recipe.arc.ARCRecipeInput;
import wayoftime.bloodmagic.common.tag.BMTags;
import wayoftime.bloodmagic.util.ARCOutputHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ARCTile extends BaseTile implements MenuProvider {

    public static final int TOOL_SLOT = 0;
    public static final int INPUT_SLOT = 1;
    public static final int INPUT_BUCKET_SLOT = 2;
    public static final int OUTPUT_BUCKET_SLOT = 3;
    public static final int OUTPUT_SLOT = 4;

    public static final int NUM_OUTPUTS = 5;

    private double progress = 0;
    public static final double DEFAULT_SPEED = 0.005;

    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickSmelting;
    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickBlasting;
    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickSmoking;
    private final RecipeManager.CachedCheck<ARCRecipeInput, ARCRecipe> quickARC;

    public ARCTile(BlockPos pos, BlockState blockState) {
        super(BMTiles.ARC_TYPE.get(), pos, blockState);
        quickSmelting = createCookingLookup(RecipeType.SMELTING);
        quickBlasting = createCookingLookup(RecipeType.BLASTING);
        quickSmoking = createCookingLookup(RecipeType.SMOKING);
        quickARC = RecipeManager.createCheck(BMRecipes.ARC_TYPE.get());
    }

    private RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> createCookingLookup(RecipeType<? extends AbstractCookingRecipe> recipeType) {
        return RecipeManager.createCheck((RecipeType<AbstractCookingRecipe>) recipeType);
    }

    public final ItemStackHandler arcInv = new ItemStackHandler(OUTPUT_SLOT + NUM_OUTPUTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case TOOL_SLOT -> stack.is(BMTags.Items.ARC_TOOL);
                case INPUT_BUCKET_SLOT, OUTPUT_BUCKET_SLOT -> FluidUtil.getFluidHandler(stack).isPresent();
                case INPUT_SLOT -> true;
                default -> false;
            };
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot == INPUT_BUCKET_SLOT || slot == OUTPUT_BUCKET_SLOT) {
                return 1;
            }
            return super.getSlotLimit(slot);
        }
    };

    public int getProgressForGui() {
        return (int) (progress * 38);
    }

    public static IItemHandler getItemHandler(ARCTile tile, @Nullable Direction side) {
        if (side == null) {
            return tile.arcInv;
        }
        switch (side) {
            case UP:
                return new RangedWrapper(tile.arcInv, TOOL_SLOT, TOOL_SLOT + 1);
            case DOWN:
                new RangedWrapper(tile.arcInv, OUTPUT_SLOT, OUTPUT_SLOT + NUM_OUTPUTS);
            default:
                return new RangedWrapper(tile.arcInv, INPUT_SLOT, OUTPUT_BUCKET_SLOT+1);
        }
    }

    public final FluidTank inputTank = new FluidTank(20 * FluidType.BUCKET_VOLUME) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    public final FluidTank outputTank = new FluidTank(20 * FluidType.BUCKET_VOLUME) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    @Override
    public Component getDisplayName() {
        return Component.literal("Alchemical Reaction Chamber");
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        CompoundTag inv = tag.getCompound("arcinv");
        arcInv.deserializeNBT(registries, inv);
        inputTank.readFromNBT(registries, tag.getCompound("inputtank"));
        outputTank.readFromNBT(registries, tag.getCompound("outputtank"));
        progress = tag.getDouble("arcprogress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag inv = arcInv.serializeNBT(registries);
        tag.put("arcinv", inv);
        CompoundTag input = new CompoundTag();
        CompoundTag output = new CompoundTag();
        inputTank.writeToNBT(registries, input);
        outputTank.writeToNBT(registries, output);
        tag.put("inputtank", input);
        tag.put("outputtank", output);
        tag.putDouble("arcprogress", progress);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ARCMenu(containerId, playerInventory, this);
    }

    public IFluidHandler getFluidHandler(Direction direction) {
        if (direction == Direction.DOWN) {
            return this.outputTank;
        }
        return this.inputTank;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, ARCTile arcTile) {
        if (level.isClientSide) {
            return;
        }

        ItemStack[] outputItems = {
                arcTile.arcInv.getStackInSlot(OUTPUT_SLOT),
                arcTile.arcInv.getStackInSlot(OUTPUT_SLOT + 1),
                arcTile.arcInv.getStackInSlot(OUTPUT_SLOT + 2),
                arcTile.arcInv.getStackInSlot(OUTPUT_SLOT + 3),
                arcTile.arcInv.getStackInSlot(OUTPUT_SLOT + 4)
        };
        ARCOutputHandler itemOutputHandler = new ARCOutputHandler(outputItems, 64);
        boolean outputChanged = arcTile.handleSlots(itemOutputHandler);
        arcTile.updateType();
        ItemStack toolStack = arcTile.arcInv.getStackInSlot(TOOL_SLOT);
        ItemStack inputStack = arcTile.arcInv.getStackInSlot(INPUT_SLOT);
        boolean didProgress = false;
        if (toolStack.is(BMTags.Items.ARC_TOOL)) {
            if (toolStack.is(BMTags.Items.ARC_FURNACE)) {
                Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> recipe = Optional.empty();
                SingleRecipeInput input = new SingleRecipeInput(inputStack);
                if (toolStack.is(BMTags.Items.ARC_SMELTING)) {
                     recipe = arcTile.quickSmelting.getRecipeFor(input, level);
                } else if (toolStack.is(BMTags.Items.ARC_BLASTING)) {
                    recipe = arcTile.quickBlasting.getRecipeFor(input, level);
                } else if (toolStack.is(BMTags.Items.ARC_SMOKING)) {
                    recipe = arcTile.quickSmoking.getRecipeFor(input, level);
                }
                if (arcTile.canCraftFurnace(recipe, itemOutputHandler)) {
                    arcTile.progress += DEFAULT_SPEED * ((double) recipe.get().value().getCookingTime() / 200D) * toolStack.getOrDefault(BMDataComponents.ARC_SPEED, 1D);
                    didProgress = true;
                    if (arcTile.progress >= 1) {
                        arcTile.craftFurnace(recipe.get().value(), input, itemOutputHandler);
                        outputChanged = true;
                    }
                }
            } else {
                ARCRecipeInput input = new ARCRecipeInput(toolStack, inputStack, arcTile.inputTank.getFluidInTank(0));
                Optional<RecipeHolder<ARCRecipe>> recipe = arcTile.quickARC.getRecipeFor(input, level);
                if (arcTile.canCraft(recipe, itemOutputHandler)) {
                    arcTile.progress += DEFAULT_SPEED * toolStack.getOrDefault(BMDataComponents.ARC_SPEED, 1D);
                    didProgress = true;
                    if (arcTile.progress >= 1) {
                        arcTile.craft(recipe.get().value(), input, itemOutputHandler);
                        outputChanged = true;
                    }
                }
            }
        }

        arcTile.setLit(didProgress);
        if (!didProgress) {
            arcTile.progress = 0;
        }

        if (outputChanged) {
            for (int i = 0; i < NUM_OUTPUTS; i++) {
                arcTile.arcInv.setStackInSlot(OUTPUT_SLOT + i, itemOutputHandler.getStackInSlot(i));
            }
        }
    }


    private boolean canCraftFurnace(Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> recipe, ARCOutputHandler outputHandler) {
        if (recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().value().getResultItem(level.registryAccess());
        return outputHandler.canTransferAllItemsToSlots(List.of(result), true);
    }

    private void craftFurnace(AbstractCookingRecipe value, SingleRecipeInput input, ARCOutputHandler outputHandler) {
        ItemStack output = value.assemble(input, level.registryAccess());
        handleInventory(List.of(output), outputHandler);
    }

    private boolean canCraft(Optional<RecipeHolder<ARCRecipe>> recipe, ARCOutputHandler outputHandler) {
        if (recipe.isEmpty()) {
            return false;
        }
        ARCRecipe arcRecipe = recipe.get().value();
        List<Pair<ItemStack, Double>> chanceOutputs = arcRecipe.getAllListedOutputs();
        List<ItemStack> outputs = chanceOutputs.stream().map(Pair::getFirst).toList();
        if (!outputHandler.canTransferAllItemsToSlots(outputs, true)) {
            return false;
        }
        if (arcRecipe.getOutputFluid().isPresent()) {
            int filled = outputTank.fill(arcRecipe.getOutputFluid().get(), FluidAction.SIMULATE);
            if (!(filled == arcRecipe.getOutputFluid().get().getAmount())) {
                return false;
            }
        }
        return true;
    }

    private void craft(ARCRecipe value, ARCRecipeInput input, ARCOutputHandler outputHandler) {
        value.assemble(input, level.registryAccess());
        List<ItemStack> outputs = value.getActualOutputs();
        int filled = outputTank.fill(value.getActualOutputFluid(), FluidAction.EXECUTE);
        BloodMagic.LOGGER.info("filled {}mB into output tank", filled);
        handleInventory(outputs, outputHandler);
    }

    private void handleInventory(List<ItemStack> toOutput, ARCOutputHandler outputHandler) {
        if (!outputHandler.canTransferAllItemsToSlots(toOutput, false)) {
            BloodMagic.LOGGER.info("couldnt stash all {}", toOutput);
        }
        arcInv.getStackInSlot(INPUT_SLOT).shrink(1);
        progress = 0;

        ItemStack toolStack = arcInv.getStackInSlot(TOOL_SLOT);
        if (!toolStack.has(DataComponents.UNBREAKABLE)) {
            if (toolStack.hasCraftingRemainingItem()) {
                arcInv.setStackInSlot(TOOL_SLOT, toolStack.getCraftingRemainingItem());
            } else if (toolStack.has(DataComponents.MAX_DAMAGE)){
                int lost = EnchantmentHelper.processDurabilityChange((ServerLevel) level, toolStack, 1); // this *should* apply enchantments like unbreaking
                toolStack.set(DataComponents.DAMAGE, toolStack.getOrDefault(DataComponents.DAMAGE, 0) + lost);
            } else {
                toolStack.shrink(1);
            }
        }
    }

    public void setLit(boolean lit) {
        if (!getBlockState().getValue(ARCBlock.LIT)) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState().setValue(ARCBlock.LIT, lit), Block.UPDATE_ALL);
        }
    }

    public void updateType() {
        EnumWillType type = arcInv.getStackInSlot(TOOL_SLOT).getOrDefault(BMDataComponents.DEMON_WILL_TYPE, EnumWillType.DEFAULT);
        if (getBlockState().getValue(ARCBlock.TYPE) != type) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState().setValue(ARCBlock.TYPE, type), Block.UPDATE_ALL);
        }
    }

    public boolean handleSlots(ARCOutputHandler itemOutputHandler) {
        IFluidHandlerItem testInputHandler = FluidUtil.getFluidHandler(arcInv.getStackInSlot(INPUT_BUCKET_SLOT).copy()).orElse(null);
        IFluidHandlerItem testOutputHandler = FluidUtil.getFluidHandler(arcInv.getStackInSlot(OUTPUT_BUCKET_SLOT).copy()).orElse(null);

        boolean outputChanged = false;
        if (testInputHandler != null) {
            FluidStack transferredStack = FluidUtil.tryFluidTransfer(inputTank, testInputHandler, Integer.MAX_VALUE, false);
            if (!transferredStack.isEmpty()) {
                testInputHandler.drain(transferredStack, FluidAction.EXECUTE);
                List<ItemStack> arraylist = new ArrayList<>();
                arraylist.add(testInputHandler.getContainer());
                if (itemOutputHandler.canTransferAllItemsToSlots(arraylist, true)) {
                    outputChanged = true;
                    inputTank.fill(transferredStack, FluidAction.EXECUTE);
                    itemOutputHandler.canTransferAllItemsToSlots(arraylist, false);
                    arcInv.setStackInSlot(INPUT_BUCKET_SLOT, ItemStack.EMPTY);
                }
            } else {
                transferredStack = FluidUtil.tryFluidTransfer(testInputHandler, inputTank, inputTank.getFluidAmount(), false);
                if (!transferredStack.isEmpty()) {
                    testInputHandler.fill(transferredStack, FluidAction.EXECUTE);
                    List<ItemStack> arrayList = new ArrayList<>();
                    arrayList.add(testInputHandler.getContainer());
                    if (itemOutputHandler.canTransferAllItemsToSlots(arrayList, true)) {
                        outputChanged = true;
                        inputTank.drain(transferredStack, FluidAction.EXECUTE);
                        itemOutputHandler.canTransferAllItemsToSlots(arrayList, false);
                        arcInv.setStackInSlot(INPUT_BUCKET_SLOT, ItemStack.EMPTY);
                    }
                }
            }
        }

        if (testOutputHandler != null) {
            /* probably dont insert into output tank
            FluidStack transferredStack = FluidUtil.tryFluidTransfer(outputTank, testOutputHandler, outputTank.getCapacity() - outputTank.getFluidAmount(), false);
            if (!transferredStack.isEmpty()) {
                testOutputHandler.drain(transferredStack, FluidAction.EXECUTE);
                List<ItemStack> arraylist = new ArrayList<>();
                arraylist.add(testOutputHandler.getContainer());
                if (itemOutputHandler.canTransferAllItemsToSlots(arraylist, true)) {
                    outputChanged = true;
                    outputTank.fill(transferredStack, FluidAction.EXECUTE);
                    itemOutputHandler.canTransferAllItemsToSlots(arraylist, false);
                    itemHandler.setStackInSlot(OUTPUT_BUCKET_SLOT, ItemStack.EMPTY);
                }
            } else {

             */
            FluidStack transferredStack = FluidUtil.tryFluidTransfer(testOutputHandler, outputTank, outputTank.getFluidAmount(), false);
            if (!transferredStack.isEmpty()) {
                testOutputHandler.fill(transferredStack, FluidAction.EXECUTE);
                List<ItemStack> arrayList = new ArrayList<>();
                arrayList.add(testOutputHandler.getContainer());
                if (itemOutputHandler.canTransferAllItemsToSlots(arrayList, true)) {
                    outputChanged = true;
                    outputTank.drain(transferredStack, FluidAction.EXECUTE);
                    itemOutputHandler.canTransferAllItemsToSlots(arrayList, false);
                    arcInv.setStackInSlot(OUTPUT_BUCKET_SLOT, ItemStack.EMPTY);
                }
            }
            //}
        }

        ItemStack toolStack = arcInv.getStackInSlot(TOOL_SLOT).copy();
        if (toolStack.getDamageValue() >= toolStack.getMaxDamage()) {
            List<ItemStack> arrayList = new ArrayList<>();
            toolStack.setDamageValue(toolStack.getMaxDamage());
            arrayList.add(toolStack);
            if (itemOutputHandler.canTransferAllItemsToSlots(arrayList, true)) {
                outputChanged = true;
                itemOutputHandler.canTransferAllItemsToSlots(arrayList, false);
                arcInv.setStackInSlot(TOOL_SLOT, ItemStack.EMPTY);
                updateType();
            }
        }

        return outputChanged;
    }
}
