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
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.block.RoutingNodeBlock;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RoutingNodeTile extends BaseTile {

    // somebody please think of the children
    public RoutingNodeTile(BlockEntityType<? extends RoutingNodeTile> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public RoutingNodeTile(BlockPos pos, BlockState state) {
        super(BMTiles.ROUTING_NODE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, RoutingNodeTile node) {
        if (!(level.getGameTime() % 20 == 0)) {
            return;
        }

        if (!node.hasMaster() && node.hasParent()) {
            if (level.getBlockEntity(node.parentPos) instanceof RoutingNodeTile parent) {
                if (parent.hasMaster()) {
                    node.masterPos = parent.masterPos;
                }
            }
        }
    }

    protected BlockPos masterPos = BlockPos.ZERO;
    protected BlockPos parentPos = BlockPos.ZERO;
    protected Set<BlockPos> children = new HashSet<>();

    public void propagateNetwork(BiConsumer<BlockPos, Optional<Boolean>> collector, boolean requireEnabled) {
        if (requireEnabled && !getBlockState().getValue(RoutingNodeBlock.ENABLED)) {
            return;
        }

        collector.accept(getBlockPos(), Optional.empty());

        for (BlockPos pos : children) {
            if (level.getBlockEntity(pos) instanceof RoutingNodeTile node) {
                node.propagateNetwork(collector, requireEnabled);
            }
        }
    }

    public void setParent(BlockPos parent) {
        this.parentPos = parent;
    }

    public boolean hasParent() {
        return this.parentPos != BlockPos.ZERO;
    }

    public BlockPos getParentPos() {
        return this.parentPos;
    }

    public boolean addToNetwork(BlockPos parent) {
        if (!(level.getBlockEntity(parent) instanceof RoutingNodeTile parentNode)) {
            return false;
        }
        if (masterPos != BlockPos.ZERO && parentNode.masterPos != BlockPos.ZERO) {
            return false;
        }

        parentPos = parent;
        if (masterPos == BlockPos.ZERO && parentNode.masterPos != BlockPos.ZERO) {
            masterPos = parentNode.masterPos;
        } else if (masterPos != BlockPos.ZERO && parentNode.masterPos == BlockPos.ZERO) {
            parentNode.masterPos = masterPos;
        }

        parentNode.addChild(getBlockPos());
        parentNode.setChanged();
        level.sendBlockUpdated(parentPos, parentNode.getBlockState(), parentNode.getBlockState(), Block.UPDATE_CLIENTS);

        this.setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);

        return true;
    }

    public void addChild(BlockPos child) {
        this.children.add(child);
    }

    public void removeFromNetwork() {
        this.masterPos = BlockPos.ZERO;

        // TODO filtering nodes should remove their info from master here
        children.forEach(child -> {
            if (level.getBlockEntity(child) instanceof RoutingNodeTile node) {
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

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag nodeInfo = new CompoundTag();
        nodeInfo.put("master", posToTag(masterPos));
        nodeInfo.put("parent", posToTag(parentPos));
        ListTag childTag = new ListTag();
        children.forEach(pos -> childTag.add(posToTag(pos)));
        nodeInfo.put("children", childTag);
        tag.put("node_info", nodeInfo);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        CompoundTag nodeInfo = tag.getCompound("node_info");
        masterPos = tagToPos(nodeInfo.getCompound("master"));
        parentPos = tagToPos(nodeInfo.getCompound("parent"));
        ListTag childrenTag = nodeInfo.getList("children", ListTag.TAG_COMPOUND);
        children.clear(); // just to be sure theres no weird overflow happening
        childrenTag.forEach(childTag -> children.add(tagToPos((CompoundTag) childTag))); // cast should be fine since we tell .getList its a list of TC's. I *think*
    }

    protected CompoundTag posToTag(BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        if (pos != BlockPos.ZERO) {
            tag.putInt("x", pos.getX());
            tag.putInt("y", pos.getY());
            tag.putInt("z", pos.getZ());
        }
        return tag;
    }

    protected BlockPos tagToPos(CompoundTag tag) {
        if (tag.contains("x") && tag.contains("y") && tag.contains("z")) {
            return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
        }
        return BlockPos.ZERO;
    }
}
