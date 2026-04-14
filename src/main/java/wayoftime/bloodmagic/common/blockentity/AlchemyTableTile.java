package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.api.datacomponent.Binding;
import wayoftime.bloodmagic.api.helper.SoulNetworkHelper;
import wayoftime.bloodmagic.api.soulnetwork.SoulTicket;
import wayoftime.bloodmagic.common.block.AlchemyTableBlock;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.datacomponent.SoulNetwork;
import wayoftime.bloodmagic.common.datamap.BMDataMaps;
import wayoftime.bloodmagic.common.datamap.BloodOrb;
import wayoftime.bloodmagic.common.menu.AlchemyTableMenu;
import wayoftime.bloodmagic.common.recipe.BMRecipes;
import wayoftime.bloodmagic.common.recipe.alchemy_table.AlchemyTableInput;
import wayoftime.bloodmagic.common.recipe.alchemy_table.AlchemyTableRecipe;
import wayoftime.bloodmagic.util.TablePart;

public class AlchemyTableTile extends BaseTile implements MenuProvider {

    public Direction FACING;
    public TablePart PART;

    private int stackLimit = 0;
    private int errorFlag = 0;
    private int progress = 0;
    public final SimpleContainerData data = new SimpleContainerData(DATA_COUNT) {
        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> stackLimit = value;
                case 1 -> errorFlag = value;
                case 2 -> progress = value;
            }
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), AlchemyTableBlock.UPDATE_ALL);
        }

        @Override
        public int get(int index) {
            return switch(index) {
                case 0 -> stackLimit;
                case 1 -> errorFlag;
                case 2 -> progress;

                default -> 0;
            };
        }
    };

    public static final int INPUT_COUNT = 6;

    public static final int INPUT_SLOT = 0;
    public static final int ORB_SLOT = INPUT_SLOT + INPUT_COUNT;
    public static final int OUTPUT_SLOT = ORB_SLOT + 1;

    public static final int STACK_LIMIT = 0;
    public static final int ERROR_FLAG = STACK_LIMIT + 1;
    public static final int PROGRESS = ERROR_FLAG + 1;

    public static final int SLOT_COUNT = OUTPUT_SLOT + 1;
    public static final int DATA_COUNT = PROGRESS + 1;

    public static final int ERR_ORB = 1;
    public static final int ERR_ESSENCE = 2;

    public ItemStackHandler inv;
    public int work = 0;

    public AlchemyTableTile(BlockPos pos, BlockState state) {
        super(BMTiles.ALCHEMY_TABLE_TYPE.get(), pos, state);
        FACING = state.getValue(AlchemyTableBlock.FACING);
        PART = state.getValue(AlchemyTableBlock.PART);
        if (PART == TablePart.RIGHT) {
            return;
        }

        inv = new ItemStackHandler(SLOT_COUNT) {
            @Override
            public int getSlotLimit(int slot) {
                if (slot < ORB_SLOT) {
                    return data.get(STACK_LIMIT) == 0 ? 64 : 1;
                }

                return super.getSlotLimit(slot);
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == ORB_SLOT) {
                    return stack.getItemHolder().getData(BMDataMaps.BLOOD_ORB_STATS) != null;
                }

                return slot != OUTPUT_SLOT;
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), AlchemyTableBlock.UPDATE_ALL);
            }
        };
    }

    private RecipeManager.CachedCheck<AlchemyTableInput, AlchemyTableRecipe> recipeCheck = RecipeManager.createCheck(BMRecipes.ALCHEMY_TABLE_TYPE.get());
    public static void tick(Level level, BlockPos pos, BlockState state, AlchemyTableTile table) {
        if (table.PART == TablePart.RIGHT) {
            return;
        }
        AlchemyTableInput input = table.getInput();
        RecipeHolder<AlchemyTableRecipe> recipe = table.recipeCheck.getRecipeFor(input, level).orElse(null);
        if (recipe == null) {
            table.work = 0;
            return;
        }
        ItemStack orbStack = table.inv.getStackInSlot(ORB_SLOT);
        Binding binding = orbStack.getOrDefault(BMDataComponents.BINDING, Binding.EMPTY);
        BloodOrb orb = orbStack.getItemHolder().getData(BMDataMaps.BLOOD_ORB_STATS);
        if (orb.tier() < recipe.value().tier()) { // cannot be null, if it is it cant be placed in orb slot in the first place
            table.data.set(ERROR_FLAG, ERR_ORB);
            return;
        }
        if (binding.isEmpty()) {
            table.data.set(ERROR_FLAG, ERR_ORB);
            return;
        }
        SoulNetwork network = SoulNetworkHelper.getSoulNetwork(binding);
        if (network.getCurrentEssence() < recipe.value().essence()) {
            table.data.set(ERROR_FLAG, ERR_ESSENCE);
            return;
        }

        ItemStack output = recipe.value().assemble(input, level.registryAccess());
        ItemStack currentOutput = table.inv.getStackInSlot(OUTPUT_SLOT);
        if (!currentOutput.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(output, currentOutput)) {
                table.work = 0;
                return;
            }
        }
        if (++table.work >= recipe.value().duration()) {
            if (currentOutput.isEmpty()) {
                table.inv.setStackInSlot(OUTPUT_SLOT, output);
            } else {
                table.inv.getStackInSlot(OUTPUT_SLOT).grow(output.getCount());
            }
            table.work = 0;
            network.syphon(SoulTicket.block(level, pos, recipe.value().essence()));
            for (int i = 0; i < INPUT_COUNT; i++) {
                ItemStack inputStack = table.inv.getStackInSlot(i);
                if (inputStack.hasCraftingRemainingItem()) {
                    table.inv.setStackInSlot(i, inputStack.getCraftingRemainingItem());
                } else {
                    inputStack.shrink(1);
                }
                if (inputStack.isEmpty()) {
                    table.inv.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
            table.setChanged();
        }

        table.data.set(PROGRESS, (int) Math.clamp((double) table.work / (double) recipe.value().duration() * 90, 0, 90));
    }

    public AlchemyTableInput getInput() {
        NonNullList<ItemStack> inputs = NonNullList.create();
        for (int i = 0; i < ORB_SLOT; i++) {
            inputs.add(inv.getStackInSlot(i));
        }

        return new AlchemyTableInput(inputs);
    }

    public IItemHandler getItemHandler(Direction direction) {
        if (PART == TablePart.RIGHT) {
            return ((AlchemyTableTile) level.getBlockEntity(getBlockPos().relative(FACING.getCounterClockWise())))
                    .getItemHandler(direction);
        }

        if (direction == null) {
            return inv;
        }

        return switch (direction) {
            case UP -> new RangedWrapper(inv, ORB_SLOT, ORB_SLOT + 1);
            case DOWN -> new RangedWrapper(inv, OUTPUT_SLOT, OUTPUT_SLOT + 1);
            default -> new RangedWrapper(inv, INPUT_SLOT, INPUT_SLOT + INPUT_COUNT);
        };
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        PART = TablePart.valueOf(tag.getString("part").toUpperCase());
        FACING = Direction.valueOf(tag.getString("facing").toUpperCase());
        if (PART == TablePart.RIGHT) {
            return;
        }
        inv.deserializeNBT(registries, tag.getCompound("inv"));
        stackLimit = tag.getInt("stack_limit");
        work = tag.getInt("work");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("part", PART.getSerializedName());
        tag.putString("facing", FACING.getSerializedName());
        if (PART == TablePart.RIGHT) {
            return;
        }
        tag.put("inv", inv.serializeNBT(registries));
        tag.putInt("stack_limit", stackLimit);
        tag.putInt("work", work);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(BMBlocks.ALCHEMY_TABLE.block().get().getDescriptionId());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AlchemyTableMenu(containerId, playerInventory, inv, data);
    }
}
