package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.Binding;
import wayoftime.bloodmagic.common.datacomponent.SoulNetwork;
import wayoftime.bloodmagic.common.item.BloodOrbItem;
import wayoftime.bloodmagic.common.recipe.BMRecipes;
import wayoftime.bloodmagic.common.recipe.alchemytable.AlchemyTableInput;
import wayoftime.bloodmagic.common.recipe.alchemytable.AlchemyTableRecipe;
import wayoftime.bloodmagic.util.SoulTicket;
import wayoftime.bloodmagic.util.helper.SoulNetworkHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlchemyTableTile extends BaseTile implements MenuProvider {
    public static final int ORB_SLOT = 6;
    public static final int OUTPUT_SLOT = 7;

    public Direction direction = Direction.NORTH;
    public int burnTime = 0;
    public int ticksRequired = 1;

    private AlchemyTableRecipe cachedRecipe = null;

    public final ItemStackHandler inv = new ItemStackHandler(8) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == OUTPUT_SLOT) return false;
            if (slot == ORB_SLOT) return stack.getItem() instanceof BloodOrbItem;
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
            // Invalidate cached recipe on input change
            if (slot != OUTPUT_SLOT) {
                cachedRecipe = null;
            }
        }
    };

    public AlchemyTableTile(BlockPos pos, BlockState state) {
        super(BMTiles.ALCHEMY_TABLE_TYPE.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        direction = Direction.from3DDataValue(tag.getInt("direction"));
        burnTime = tag.getInt("burnTime");
        ticksRequired = tag.getInt("ticksRequired");
        inv.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("direction", direction.get3DDataValue());
        tag.putInt("burnTime", burnTime);
        tag.putInt("ticksRequired", ticksRequired);
        tag.put("inventory", inv.serializeNBT(registries));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AlchemyTableTile tile) {
        tile.tick();
    }

    public void tick() {
        if (level == null || level.isClientSide) return;

        // Check if we can craft
        Optional<AlchemyTableRecipe> recipeOpt = getRecipe();
        if (recipeOpt.isEmpty()) {
            burnTime = 0;
            return;
        }

        AlchemyTableRecipe recipe = recipeOpt.get();
        ticksRequired = recipe.getTicks();

        // Check orb tier
        ItemStack orbStack = inv.getStackInSlot(ORB_SLOT);
        int orbTier = getOrbTier(orbStack);
        if (orbTier < recipe.getMinimumTier()) {
            burnTime = 0;
            return;
        }

        // Check if output slot can accept result
        ItemStack output = recipe.getOutput();
        ItemStack currentOutput = inv.getStackInSlot(OUTPUT_SLOT);
        if (!currentOutput.isEmpty() && (!ItemStack.isSameItemSameComponents(currentOutput, output) || currentOutput.getCount() + output.getCount() > currentOutput.getMaxStackSize())) {
            burnTime = 0;
            return;
        }

        // Syphon LP per tick
        int syphonPerTick = recipe.getSyphon() / Math.max(1, recipe.getTicks());
        if (syphonPerTick > 0 && orbStack.getItem() instanceof BloodOrbItem) {
            Binding binding = orbStack.getOrDefault(BMDataComponents.BINDING, Binding.EMPTY);
            if (!binding.isEmpty()) {
                SoulNetwork network = SoulNetworkHelper.getSoulNetwork(binding);
                if (network != null) {
                    int syphoned = network.syphon(SoulTicket.item(orbStack, level, worldPosition, syphonPerTick));
                    if (syphoned < syphonPerTick) {
                        // Not enough LP
                        return;
                    }
                }
            }
        }

        burnTime++;

        if (burnTime >= ticksRequired) {
            // Craft!
            craftItem(recipe);
            burnTime = 0;
        }

        setChanged();
    }

    private void craftItem(AlchemyTableRecipe recipe) {
        // Consume ingredients
        List<Ingredient> ingredients = new ArrayList<>(recipe.getInput());
        for (int i = 0; i < 6; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            for (int j = 0; j < ingredients.size(); j++) {
                if (ingredients.get(j).test(stack)) {
                    // Handle container items (like buckets)
                    ItemStack container = stack.getCraftingRemainingItem();
                    stack.shrink(1);
                    if (stack.isEmpty() && !container.isEmpty()) {
                        inv.setStackInSlot(i, container);
                    }
                    ingredients.remove(j);
                    break;
                }
            }
        }

        // Add output
        ItemStack output = recipe.getOutput().copy();
        ItemStack currentOutput = inv.getStackInSlot(OUTPUT_SLOT);
        if (currentOutput.isEmpty()) {
            inv.setStackInSlot(OUTPUT_SLOT, output);
        } else {
            currentOutput.grow(output.getCount());
        }

        cachedRecipe = null;
    }

    private Optional<AlchemyTableRecipe> getRecipe() {
        if (cachedRecipe != null) {
            AlchemyTableInput input = createInput();
            if (cachedRecipe.matches(input, level)) {
                return Optional.of(cachedRecipe);
            }
        }

        AlchemyTableInput input = createInput();
        Optional<AlchemyTableRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(BMRecipes.ALCHEMY_TABLE_TYPE.get(), input, level)
                .map(holder -> holder.value());

        recipe.ifPresent(r -> cachedRecipe = r);
        return recipe;
    }

    private AlchemyTableInput createInput() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }
        return new AlchemyTableInput(items, getOrbTier(inv.getStackInSlot(ORB_SLOT)));
    }

    private int getOrbTier(ItemStack orbStack) {
        if (orbStack.getItem() instanceof BloodOrbItem orbItem) {
            return orbItem.getOrbTier(orbStack);
        }
        return 0;
    }

    public void dropItems() {
        if (level != null && !level.isClientSide) {
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
                }
            }
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.bloodmagic.alchemytable");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        // TODO: Implement AlchemyTableMenu when GUI is added
        return null;
    }
}
