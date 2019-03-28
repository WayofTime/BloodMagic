package WayofTime.bloodmagic.tile.routing;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.routing.IItemRoutingNode;
import WayofTime.bloodmagic.routing.IMasterRoutingNode;
import WayofTime.bloodmagic.routing.IRoutingNode;
import WayofTime.bloodmagic.tile.TileInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

public class TileRoutingNode extends TileInventory implements IRoutingNode, IItemRoutingNode, ITickable {
    private int currentInput;
    private BlockPos masterPos = BlockPos.ORIGIN;
    private List<BlockPos> connectionList = new LinkedList<>();

    public TileRoutingNode(int size, String name) {
        super(size, name);
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            currentInput = getWorld().getRedstonePowerFromNeighbors(pos);
//            currentInput = getWorld().getStrongPower(pos);
        }
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag) {
        super.serialize(tag);
        NBTTagCompound masterTag = new NBTTagCompound();
        masterTag.setInteger(Constants.NBT.X_COORD, masterPos.getX());
        masterTag.setInteger(Constants.NBT.Y_COORD, masterPos.getY());
        masterTag.setInteger(Constants.NBT.Z_COORD, masterPos.getZ());
        tag.setTag(Constants.NBT.ROUTING_MASTER, masterTag);

        NBTTagList tags = new NBTTagList();
        for (BlockPos pos : connectionList) {
            NBTTagCompound posTag = new NBTTagCompound();
            posTag.setInteger(Constants.NBT.X_COORD, pos.getX());
            posTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
            posTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
            tags.appendTag(posTag);
        }
        tag.setTag(Constants.NBT.ROUTING_CONNECTION, tags);
        return tag;
    }

    @Override
    public void deserialize(NBTTagCompound tag) {
        super.deserialize(tag);
        connectionList.clear();
        NBTTagCompound masterTag = tag.getCompoundTag(Constants.NBT.ROUTING_MASTER);
        masterPos = new BlockPos(masterTag.getInteger(Constants.NBT.X_COORD), masterTag.getInteger(Constants.NBT.Y_COORD), masterTag.getInteger(Constants.NBT.Z_COORD));

        NBTTagList tags = tag.getTagList(Constants.NBT.ROUTING_CONNECTION, 10);
        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound blockTag = tags.getCompoundTagAt(i);
            BlockPos newPos = new BlockPos(blockTag.getInteger(Constants.NBT.X_COORD), blockTag.getInteger(Constants.NBT.Y_COORD), blockTag.getInteger(Constants.NBT.Z_COORD));
            connectionList.add(newPos);
        }
    }

    @Override
    public void removeAllConnections() {
        TileEntity testTile = getWorld().getTileEntity(getMasterPos());
        if (testTile instanceof IMasterRoutingNode) {
            ((IMasterRoutingNode) testTile).removeConnection(pos); // Remove this node from the master
        }
        for (BlockPos testPos : connectionList) {
            TileEntity tile = getWorld().getTileEntity(testPos);
            if (tile instanceof IRoutingNode) {
                ((IRoutingNode) tile).removeConnection(pos);
                getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(testPos), getWorld().getBlockState(testPos), 3);
            }
        }

        connectionList.clear();
    }

    @Override
    public void connectMasterToRemainingNode(World world, List<BlockPos> alreadyChecked, IMasterRoutingNode master) {
        this.masterPos = master.getBlockPos();
        List<BlockPos> connectedList = this.getConnected();
        for (BlockPos testPos : connectedList) {
            if (alreadyChecked.contains(testPos)) {
                continue;
            }
            alreadyChecked.add(testPos);
            TileEntity tile = world.getTileEntity(testPos);
            if (!(tile instanceof IRoutingNode)) {
                continue;
            }
            IRoutingNode node = (IRoutingNode) tile;
            if (node.getMasterPos().equals(BlockPos.ORIGIN)) //If getMasterPos() returns the origin, the node is not connected to any master.
            {
                master.addNodeToList(node);
                node.connectMasterToRemainingNode(world, alreadyChecked, master);
            }
        }

        master.addConnections(this.getBlockPos(), connectedList);
    }

    @Override
    public BlockPos getBlockPos() {
        return this.getPos();
    }

    @Override
    public List<BlockPos> getConnected() {
        return connectionList;
    }

    @Override
    public BlockPos getMasterPos() {
        return masterPos;
    }

    @Override
    public boolean isMaster(IMasterRoutingNode master) {
        BlockPos checkPos = master.getBlockPos();
        return checkPos.equals(getMasterPos());
    }

    @Override
    public boolean isConnectionEnabled(BlockPos testPos) {
        return currentInput <= 0;
    }

    @Override
    public void addConnection(BlockPos pos1) {
        if (!connectionList.contains(pos1)) {
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
            connectionList.add(pos1);
        }
    }

    @Override
    public void removeConnection(BlockPos pos1) {
        if (connectionList.contains(pos1)) {
            connectionList.remove(pos1);
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
        }

        if (pos1.equals(masterPos)) {
            this.masterPos = BlockPos.ORIGIN;
        }
    }

    @Override
    public boolean isInventoryConnectedToSide(EnumFacing side) {
        return false;
    }

    @Override
    public int getPriority(EnumFacing side) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 10000;
    }
}
