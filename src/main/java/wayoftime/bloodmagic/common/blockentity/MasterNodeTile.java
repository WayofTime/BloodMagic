package wayoftime.bloodmagic.common.blockentity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.BMBlocks;
import wayoftime.bloodmagic.common.item.BMItems;
import wayoftime.bloodmagic.common.menu.NodeMasterMenu;
import wayoftime.bloodmagic.common.routing.IRoutingFilter;

import java.util.*;

public class MasterNodeTile extends RoutingNodeTile implements MenuProvider {

    public MasterNodeTile(BlockPos pos, BlockState blockState) {
        super(BMTiles.MASTER_ROUTING_NODE.get(), pos, blockState);
    }

    public ItemStackHandler upgradeInv = new ItemStackHandler(2) {
        @Override
        public int getSlotLimit(int slot) {
            return switch (slot) {
                case 0 -> BloodMagic.SERVER_CONFIG.MAX_AMOUNT_UPGRADES.get();
                case 1 -> BloodMagic.SERVER_CONFIG.MAX_SPEED_UPGRADES.get();
                default -> 0;
            };
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                // TODO replace with the actual upgrades after adding them
                case 0 -> stack.is(BMItems.NODE_AMOUNT_UPGRADE.get());
                case 1 -> stack.is(BMItems.NODE_SPEED_UPGRADE.get());
                default -> false;
            };
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };

    public int ticks = 0;
    private Set<BlockPos> outputNodes;
    private Set<BlockPos> inputNodes;
    public static void tick(Level level, BlockPos pos, BlockState state, MasterNodeTile node) {
        if (level.isClientSide) {
            return;
        }

        if (node.inputNodes == null || node.outputNodes == null) {

        }

        if (node.ticks % Math.min(1, 20 - node.upgradeInv.getStackInSlot(1).getCount()) != 0) {
            return;
        }

    }

    public void markNodeDirty(BlockPos nodePos) {

    }

    @Override
    public boolean addToNetwork(BlockPos parent) {
        return false;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ticks = tag.getInt("ticks");
        upgradeInv.deserializeNBT(registries, tag.getCompound("upgrades"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("ticks", ticks % 20);
        tag.put("upgrades", upgradeInv.serializeNBT(registries));
    }

    // TODO fix these two
    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.bloodmagic.node.master");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new NodeMasterMenu(containerId, playerInventory, this.upgradeInv);
    }
}
