package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.RoutingNodeBlock;
import wayoftime.bloodmagic.common.capability.BMCaps;
import wayoftime.bloodmagic.common.routing.IRoutingFilter;
import wayoftime.bloodmagic.common.routing.NodeContext;

import java.util.*;
import java.util.function.BiConsumer;

public class FilterNodeTile extends RoutingNodeTile implements MenuProvider {

    public static final int MAX_PRIO = 10;
    private final boolean isOutput;
    public FilterNodeTile(BlockPos pos, BlockState blockState, boolean isOutput) {
        super(BMTiles.FILTER_ROUTING_NODE.get(), pos, blockState);
        this.isOutput = isOutput;
    }

    public FilterNodeTile(BlockPos pos, BlockState state) {
        this(pos, state, false);
    }

    private Map<Direction, BlockCapabilityCache<IItemHandler, Direction>> itemCapCache = new HashMap<>();
    private Map<Direction, BlockCapabilityCache<IFluidHandler, Direction>> fluidCapCache = new HashMap<>();

    public final SimpleContainerData priorities = new SimpleContainerData(6);

    public final ItemStackHandler filterInv = new ItemStackHandler(6) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getCapability(BMCaps.ROUTING_FILTER_PROVIDER, NodeContext.EMPTY) != null;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };


    public void propagateNetwork(BiConsumer<BlockPos, Optional<Boolean>> collector, boolean requireEnabled) {
        if (requireEnabled && !getBlockState().getValue(RoutingNodeBlock.ENABLED)) {
            return;
        }

        collector.accept(getBlockPos(), Optional.of(isOutput));

        for (BlockPos pos : children) {
            if (level.getBlockEntity(pos) instanceof RoutingNodeTile node) {
                node.propagateNetwork(collector, requireEnabled);
            }
        }
    }

    // TODO not return null for both below
    @Override
    public Component getDisplayName() {
        return null;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return null;
    }
}
