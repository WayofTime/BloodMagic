package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.event.BloodMagicCraftedEvent;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.recipe.BMRecipes;
import wayoftime.bloodmagic.common.recipe.forge.ForgeInput;
import wayoftime.bloodmagic.common.recipe.forge.ForgeRecipe;
import wayoftime.bloodmagic.api.BMTags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HellfireForgeTile extends BaseTile {
    public ItemStackHandler inv = new ItemStackHandler(6) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == OUTPUT_SLOT) {
                return false;
            }

            if (slot == GEM_SLOT && !stack.has(BMDataComponents.DEMON_WILL_AMOUNT)) {
                return false;
            }

            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            BloodMagic.LOGGER.info("called for {}", slot);
            setChanged();
        }
    };

    @Override
    public void setChanged() {
        super.setChanged();
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    // one day mojang is going to change Direction. But not today
    public static final int SOUTH = Direction.SOUTH.get2DDataValue(); // 0
    public static final int WEST = Direction.WEST.get2DDataValue(); // 1
    public static final int NORTH = Direction.NORTH.get2DDataValue(); // 2
    public static final int EAST = Direction.EAST.get2DDataValue(); // 3

    public static final int GEM_SLOT = 4;
    public static final int OUTPUT_SLOT = 5;

    public static final int MAX_PROGRESS = 200;
    protected int progress = 0;

    public HellfireForgeTile(BlockPos pos, BlockState blockState) {
        super(BMTiles.HELLFIRE_FORGE_TYPE.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, HellfireForgeTile hellfireForgeTile) {
        ForgeInput input = hellfireForgeTile.getInput();
        Optional<RecipeHolder<ForgeRecipe>> recipeOptional = level.getRecipeManager().getRecipeFor(BMRecipes.SOUL_FORGE_TYPE.get(), input, level);
        if (recipeOptional.isEmpty()) {
            return;
        }
        ForgeRecipe recipe = recipeOptional.get().value();
        ItemStack output = recipe.assemble(input, level.registryAccess());
        if (output.isEmpty()) {
            BloodMagic.LOGGER.info("input matched but no result");
            return;
        }
        ItemStack currentOutput = hellfireForgeTile.inv.getStackInSlot(OUTPUT_SLOT);
        if (!currentOutput.isEmpty() && !ItemStack.isSameItemSameComponents(currentOutput, output)) {
            BloodMagic.LOGGER.info("outputs dont stack!");
            return;
        }

        hellfireForgeTile.progress++;
        if (!(hellfireForgeTile.progress >= MAX_PROGRESS)) {
            ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 1, 0.1, 0, 0.1, 0);
            return;
        }
        BloodMagicCraftedEvent.Forge event = new BloodMagicCraftedEvent.Forge(output, input.asArray());
        NeoForge.EVENT_BUS.post(event);

        ItemStack gemStack = hellfireForgeTile.inv.getStackInSlot(GEM_SLOT);
        if (!gemStack.isEmpty()) {
            double will = gemStack.getOrDefault(BMDataComponents.DEMON_WILL_AMOUNT, 0D);
            will -= recipe.usedWill;
            if (will == 0 && gemStack.is(BMItems.RAW_WILL)) {
                hellfireForgeTile.inv.setStackInSlot(GEM_SLOT, ItemStack.EMPTY);
            } else {
                gemStack.set(BMDataComponents.DEMON_WILL_AMOUNT, will);
            }
        }

        for (int i = SOUTH; i < GEM_SLOT; i++) {
            ItemStack item = hellfireForgeTile.inv.getStackInSlot(i);
            if (item.hasCraftingRemainingItem()) {
                hellfireForgeTile.inv.setStackInSlot(i, item.getCraftingRemainingItem());
                continue;
            }
            item.shrink(1);
            if (item.isEmpty()) {
                hellfireForgeTile.inv.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
        hellfireForgeTile.inv.setStackInSlot(OUTPUT_SLOT, event.getOutput());

        hellfireForgeTile.setChanged();
    }

    public ForgeInput getInput() {
        ItemStack gemStack = inv.getStackInSlot(GEM_SLOT);
        List<ItemStack> stacks = new ArrayList<>();
        int gemIndex = GEM_SLOT;
        for (int i = SOUTH; i < GEM_SLOT; i++) {
            ItemStack testStack = inv.getStackInSlot(i);
            stacks.add(testStack);
            if (testStack.is(BMTags.Items.SOUL_GEM)) {
                gemStack = testStack;
                gemIndex = i;
            }
        }
        return new ForgeInput(stacks, gemStack, gemIndex);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inv.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag inventory = inv.serializeNBT(registries);
        tag.put("inventory", inventory);
    }

    public @Nullable IItemHandler getInventory(Direction side) {
        if (side == null) {
            return inv;
        }

        return switch (side) {
            case UP -> new RangedWrapper(inv, GEM_SLOT, GEM_SLOT + 1);
            case DOWN -> new RangedWrapper(inv, OUTPUT_SLOT, OUTPUT_SLOT + 1);
            default -> new RangedWrapper(inv, side.get2DDataValue(), side.get2DDataValue() + 1);
        };
    }
}
