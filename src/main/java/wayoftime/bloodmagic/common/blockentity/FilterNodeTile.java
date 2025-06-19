package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.datacomponent.BMDataComponents;
import wayoftime.bloodmagic.common.routing.IFluidFilter;
import wayoftime.bloodmagic.common.routing.IItemFilter;

import java.util.EnumMap;

public class FilterNodeTile extends RoutingNodeTile implements MenuProvider {

    private final boolean isOutput;
    public FilterNodeTile(BlockPos pos, BlockState blockState, boolean isOutput) {
        super(BMTiles.FILTER_NODE.get(), pos, blockState);
        this.isOutput = isOutput;
    }

    public final ItemStackHandler filterInv = new ItemStackHandler(6) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.has(BMDataComponents.filter_data);
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            sendFilterInfo();
        }
    };

    @Override
    public void masterCheckinRequest() {
        super.masterCheckinRequest();
        sendFilterInfo();
    }

    @Override
    public void setActive(boolean newState) {
        if (isActive != newState) {
            BlockEntity be = level.getBlockEntity(masterPos);
            if (be instanceof MasterNodeTile master) {
                if (newState) {
                    sendFilterInfo();
                } else {
                    master.removeNodeInfo(getBlockPos());
                }
            }
        }
        super.setActive(newState);
    }

    @Override
    public void setMasterPos(BlockPos masterPos) {
        super.setMasterPos(masterPos);
        sendFilterInfo();
    }

    protected void sendFilterInfo() {
        BlockEntity be = level.getBlockEntity(masterPos);
        if (be instanceof MasterNodeTile master) {
            master.addPriorityInfo(getBlockPos(), getPriorityMap());
            if (isOutput) {
                master.addItemOutputFilterInfo(getBlockPos(), getItemFilters());
                master.addFluidOutputFilterInfo(getBlockPos(), getFluidFilters());
            } else {
                master.addItemInputFilterInfo(getBlockPos(), getItemFilters());
                master.addFluidInputFilterInfo(getBlockPos(), getFluidFilters());
            }
        }
    }

    protected EnumMap<Direction, Byte> getPriorityMap() {

    }

    protected EnumMap<Direction, IItemFilter> getItemFilters() {}

    protected EnumMap<Direction, IFluidFilter> getFluidFilters() {}

    @Override
    public Component getDisplayName() {
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    }
}
