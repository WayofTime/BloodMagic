package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import wayoftime.bloodmagic.common.alchemyarray.AlchemyArrayEffect;
import wayoftime.bloodmagic.common.alchemyarray.AlchemyArrayEffectCrafting;
import wayoftime.bloodmagic.common.recipe.BMRecipes;
import wayoftime.bloodmagic.common.recipe.DoubleIngredientInput;
import wayoftime.bloodmagic.common.recipe.alchemyarray.AlchemyArrayRecipe;

public class AlchemyArrayTile extends BaseTile {
    public boolean isActive = false;
    public int activeCounter = 0;
    public Direction rotation = Direction.NORTH;
    public int rotateCooldown = 0;

    public AlchemyArrayEffect arrayEffect;
    private boolean doDropIngredients = true;

    public final ItemStackHandler inv = new ItemStackHandler(2) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return slot == 0 || slot == 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    public AlchemyArrayTile(BlockPos pos, BlockState state) {
        super(BMTiles.ALCHEMY_ARRAY_TYPE.get(), pos, state);
    }

    public void onEntityCollidedWithBlock(BlockState state, Entity entity) {
        if (arrayEffect != null) {
            arrayEffect.onEntityCollidedWithBlock(this, getLevel(), worldPosition, state, entity);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.isActive = tag.getBoolean("isActive");
        this.activeCounter = tag.getInt("activeCounter");
        if (!tag.contains("doDropIngredients")) {
            this.doDropIngredients = true;
        } else {
            this.doDropIngredients = tag.getBoolean("doDropIngredients");
        }
        this.rotation = Direction.from2DDataValue(tag.getInt("direction"));
        inv.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    public void doDropIngredients(boolean drop) {
        this.doDropIngredients = drop;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isActive", isActive);
        tag.putInt("activeCounter", activeCounter);
        tag.putBoolean("doDropIngredients", doDropIngredients);
        tag.putInt("direction", rotation.get2DDataValue());
        tag.put("inventory", inv.serializeNBT(registries));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AlchemyArrayTile tile) {
        tile.tick();
    }

    public void tick() {
        if (isActive && attemptCraft()) {
            activeCounter++;
        } else {
            isActive = false;
            doDropIngredients = true;
            activeCounter = 0;
            arrayEffect = null;
        }
        if (rotateCooldown > 0)
            rotateCooldown--;
    }

    public boolean attemptCraft() {
        if (arrayEffect != null) {
            isActive = true;
        } else {
            AlchemyArrayEffect effect = getEffect();
            if (effect == null) {
                return false;
            } else {
                arrayEffect = effect;
            }
        }

        if (arrayEffect != null) {
            isActive = true;
            if (arrayEffect.update(this, this.activeCounter)) {
                inv.extractItem(0, 1, false);
                inv.extractItem(1, 1, false);
                this.getLevel().setBlockAndUpdate(getBlockPos(), Blocks.AIR.defaultBlockState());
            }

            return true;
        }
        return false;
    }

    private AlchemyArrayEffect getEffect() {
        if (level == null) return null;

        ItemStack base = inv.getStackInSlot(0);
        ItemStack added = inv.getStackInSlot(1);

        if (base.isEmpty() || added.isEmpty()) return null;

        DoubleIngredientInput input = new DoubleIngredientInput(base, added);

        return level.getRecipeManager()
                .getRecipeFor(BMRecipes.ALCHEMY_ARRAY_TYPE.get(), input, level)
                .map(holder -> {
                    AlchemyArrayRecipe recipe = holder.value();
                    if (!recipe.getOutput().isEmpty()) {
                        return new AlchemyArrayEffectCrafting(recipe.getOutput());
                    }
                    return null;
                })
                .orElse(null);
    }

    public Direction getRotation() {
        return rotation;
    }

    public void setRotation(Direction rotation) {
        this.rotation = rotation;
    }

    public ItemStack getItem(int slot) {
        return inv.getStackInSlot(slot);
    }

    public void dropItems() {
        if (doDropIngredients && level != null && !level.isClientSide) {
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
                }
            }
        }
    }
}
