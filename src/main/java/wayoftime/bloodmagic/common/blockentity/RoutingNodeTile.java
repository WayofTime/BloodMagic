package wayoftime.bloodmagic.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class RoutingNodeTile extends BaseTile {

    public RoutingNodeTile(BlockEntityType<? extends RoutingNodeTile> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected BlockPos masterPos = BlockPos.ZERO; // TODO 0, 0, 0 is NOT an invalid coordinate (at least not in every DIM) and putting a master node there WILL make it NOT work
    protected BlockPos parentPos = BlockPos.ZERO; // same thing here
    protected Set<BlockPos> children = new HashSet<>();
    protected boolean isActive = true;

    public static <T extends RoutingNodeTile> void tick(Level level, BlockPos pos, BlockState state, T node) {
        // TODO might want to do something to prevent fast clocks from permanently dis- and re-enabling half the network. not that you *should* have one next to your network but it might cause some lag
        int signal = level.getBestNeighborSignal(pos);
        node.setActive(signal == 0);

        if (node.parentPos != BlockPos.ZERO) {
            BlockEntity be = node.level.getBlockEntity(node.parentPos);
            if (!(be instanceof RoutingNodeTile)) {
                node.removeFromNetwork();
            }
        }
    }

    // when master node wants to (re)evaluate the network.
    // general nodes dont need to do anything here except pass it on, filter nodes will have to resend their filter config
    public void masterCheckinRequest() {
        children.forEach(child -> {
            BlockEntity be = this.level.getBlockEntity(child);
            if (be instanceof RoutingNodeTile node) {
                node.masterCheckinRequest();
            } else { // you in da wrong neighbourhood dawg
                children.remove(child);
            }
        });
    }

    public void setActive(boolean newState) {
        if (this.isActive == newState) {
            return; // no change, done
        }
        this.isActive = newState;
        this.setChanged();
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL); // send block update for sync so client knows and can render lines appropriately

        children.forEach(child -> {
            BlockEntity be = this.level.getBlockEntity(child);
            if (be instanceof RoutingNodeTile node) {
                node.setActive(newState);
            }
        });
        // TODO filter nodes should update the master node with their filter info from here
    }

    public boolean isActive() {
        return isActive;
    }

    public void setParent(BlockPos parent) {
        this.parentPos = parent;
        this.setChanged();
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL); // send block update for sync so client knows and can render lines appropriately
    }

    public boolean hasParent() {
        return this.parentPos != BlockPos.ZERO;
    }

    public BlockPos getParentPos() {
        return this.parentPos;
    }

    public void addConnection(BlockPos child) {
        children.add(child);
        this.setChanged();
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL); // send block update for sync so client knows and can render lines appropriately
    }

    public void removeConnection(BlockPos child) {
        children.remove(child);
        BlockEntity be = this.level.getBlockEntity(child);
        if (be instanceof RoutingNodeTile node) {
            node.removeFromNetwork();
        }
        this.setChanged();
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL); // send block update for sync so client knows and can render lines appropriately
    }

    public void removeFromNetwork() {
        this.parentPos = BlockPos.ZERO;
        this.masterPos = BlockPos.ZERO;

        // TODO filtering nodes should remove their info from master here
        children.forEach(child -> {
            BlockEntity be = this.level.getBlockEntity(child);
            if (be instanceof RoutingNodeTile node) {
                node.removeFromNetwork();
            } else { // you in da wrong neighbourhood dawg
                children.remove(child);
            }
        });
    }

    public boolean hasMaster() {
        return this.masterPos != BlockPos.ZERO;
    }

    public BlockPos getMasterPos() {
        return this.masterPos;
    }

    public void setMasterPos(BlockPos masterPos) {
        this.masterPos = masterPos;
        this.setChanged();
        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL); // send block update for sync so client knows and can render lines appropriately
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag nodeInfo = new CompoundTag();
        nodeInfo.putBoolean("active", isActive);
        nodeInfo.put("master", posToTag(masterPos));
        ListTag childTag = new ListTag();
        children.forEach(pos -> childTag.add(posToTag(pos)));
        nodeInfo.put("children", childTag);
        tag.put("node_info", nodeInfo);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        CompoundTag nodeInfo = tag.getCompound("node_info");
        this.isActive = nodeInfo.getBoolean("active");
        masterPos = tagToPos(tag.getCompound("master"));
        ListTag childrenTag = nodeInfo.getList("children", ListTag.TAG_COMPOUND);
        children.clear(); // just to be sure theres no weird overflow happening
        childrenTag.forEach(childTag -> children.add(tagToPos((CompoundTag) childTag))); // cast should be fine since we tell .getList its a list of TC's. I *think*
    }

    protected CompoundTag posToTag(BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        return tag;
    }

    protected BlockPos tagToPos(CompoundTag tag) {
        return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }
}
