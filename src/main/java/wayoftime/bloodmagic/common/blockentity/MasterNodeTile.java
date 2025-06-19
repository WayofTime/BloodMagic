package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import wayoftime.bloodmagic.common.routing.IFluidFilter;
import wayoftime.bloodmagic.common.routing.IItemFilter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class MasterNodeTile extends RoutingNodeTile implements MenuProvider {

    public MasterNodeTile(BlockPos pos, BlockState blockState) {
        super(BMTiles.MASTER_NODE.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MasterNodeTile node) {

    }

    private Map<BlockPos, EnumMap<Direction, IItemFilter>> inputItemFilterMap = new HashMap<>();
    private Map<BlockPos, EnumMap<Direction, IItemFilter>> outputItemFilterMap = new HashMap<>();
    public void addItemInputFilterInfo(BlockPos pos, EnumMap<Direction, IItemFilter> filters) {
        inputItemFilterMap.put(pos, filters);
        setChanged();
    }

    public void addItemOutputFilterInfo(BlockPos pos, EnumMap<Direction, IItemFilter> filters) {
        outputItemFilterMap.put(pos, filters);
        setChanged();
    }

    private Map<BlockPos, EnumMap<Direction, IFluidFilter>> inputFluidFilterMap = new HashMap<>();
    private Map<BlockPos, EnumMap<Direction, IFluidFilter>> outputFluidFilterMap = new HashMap<>();
    public void addFluidInputFilterInfo(BlockPos pos, EnumMap<Direction, IFluidFilter> filters) {
        inputFluidFilterMap.put(pos, filters);
        setChanged();
    }

    public void addFluidOutputFilterInfo(BlockPos pos, EnumMap<Direction, IFluidFilter> filters) {
        outputFluidFilterMap.put(pos, filters);
        setChanged();
    }

    private Map<BlockPos, EnumMap<Direction, Byte>> priorityMap = new HashMap<>();
    public void addPriorityInfo(BlockPos pos, EnumMap<Direction, Byte> priority) {
        priorityMap.put(pos, priority);
    }

    public void removeNodeInfo(BlockPos pos) {
        inputItemFilterMap.remove(pos);
        outputItemFilterMap.remove(pos);
        inputFluidFilterMap.remove(pos);
        outputFluidFilterMap.remove(pos);
    }

    @Override
    public Component getDisplayName() {
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    }
}
