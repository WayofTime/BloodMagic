package WayofTime.bloodmagic.tile.routing;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.routing.*;
import WayofTime.bloodmagic.tile.TileInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.Map.Entry;

public class TileMasterRoutingNode extends TileInventory implements IMasterRoutingNode, ITickable {
    public static final int tickRate = 20;
    private int currentInput;
    // A list of connections
    private TreeMap<BlockPos, List<BlockPos>> connectionMap = new TreeMap<>();
    private List<BlockPos> generalNodeList = new LinkedList<>();
    private List<BlockPos> outputNodeList = new LinkedList<>();
    private List<BlockPos> inputNodeList = new LinkedList<>();

    public TileMasterRoutingNode() {
        super(0, "masterRoutingNode");
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {
//            currentInput = getWorld().isBlockIndirectlyGettingPowered(pos);
            currentInput = getWorld().getStrongPower(pos);

//            System.out.println(currentInput);
        }

        if (getWorld().isRemote || getWorld().getTotalWorldTime() % tickRate != 0) //Temporary tick rate solver
        {
            return;
        }

        Map<Integer, List<IItemFilter>> outputMap = new TreeMap<>();
        Map<Integer, List<IFluidFilter>> outputFluidMap = new TreeMap<>();

        for (BlockPos outputPos : outputNodeList) {
            TileEntity outputTile = getWorld().getTileEntity(outputPos);
            if (this.isConnected(new LinkedList<>(), outputPos)) {
                if (outputTile instanceof IOutputItemRoutingNode) {
                    IOutputItemRoutingNode outputNode = (IOutputItemRoutingNode) outputTile;

                    for (EnumFacing facing : EnumFacing.VALUES) {
                        if (!outputNode.isInventoryConnectedToSide(facing) || !outputNode.isOutput(facing)) {
                            continue;
                        }

                        IItemFilter filter = outputNode.getOutputFilterForSide(facing);
                        if (filter != null) {
                            int priority = outputNode.getPriority(facing);
                            if (outputMap.containsKey(priority)) {
                                outputMap.get(priority).add(filter);
                            } else {
                                List<IItemFilter> filterList = new LinkedList<>();
                                filterList.add(filter);
                                outputMap.put(priority, filterList);
                            }
                        }
                    }
                }

                if (outputTile instanceof IOutputFluidRoutingNode) {
                    IOutputFluidRoutingNode outputNode = (IOutputFluidRoutingNode) outputTile;

                    for (EnumFacing facing : EnumFacing.VALUES) {
                        if (!outputNode.isTankConnectedToSide(facing) || !outputNode.isFluidOutput(facing)) {
                            continue;
                        }

                        IFluidFilter filter = outputNode.getOutputFluidFilterForSide(facing);
                        if (filter != null) {
                            int priority = outputNode.getPriority(facing);
                            if (outputFluidMap.containsKey(priority)) {
                                outputFluidMap.get(priority).add(filter);
                            } else {
                                List<IFluidFilter> filterList = new LinkedList<>();
                                filterList.add(filter);
                                outputFluidMap.put(priority, filterList);
                            }
                        }
                    }
                }
            }
        }

        Map<Integer, List<IItemFilter>> inputMap = new TreeMap<>();
        Map<Integer, List<IFluidFilter>> inputFluidMap = new TreeMap<>();

        for (BlockPos inputPos : inputNodeList) {
            TileEntity inputTile = getWorld().getTileEntity(inputPos);
            if (this.isConnected(new LinkedList<>(), inputPos)) {
                if (inputTile instanceof IInputItemRoutingNode) {
                    IInputItemRoutingNode inputNode = (IInputItemRoutingNode) inputTile;

                    for (EnumFacing facing : EnumFacing.VALUES) {
                        if (!inputNode.isInventoryConnectedToSide(facing) || !inputNode.isInput(facing)) {
                            continue;
                        }

                        IItemFilter filter = inputNode.getInputFilterForSide(facing);
                        if (filter != null) {
                            int priority = inputNode.getPriority(facing);
                            if (inputMap.containsKey(priority)) {
                                inputMap.get(priority).add(filter);
                            } else {
                                List<IItemFilter> filterList = new LinkedList<>();
                                filterList.add(filter);
                                inputMap.put(priority, filterList);
                            }
                        }
                    }
                }

                if (inputTile instanceof IInputFluidRoutingNode) {
                    IInputFluidRoutingNode inputNode = (IInputFluidRoutingNode) inputTile;

                    for (EnumFacing facing : EnumFacing.VALUES) {
                        if (!inputNode.isTankConnectedToSide(facing) || !inputNode.isFluidInput(facing)) {
                            continue;
                        }

                        IFluidFilter filter = inputNode.getInputFluidFilterForSide(facing);
                        if (filter != null) {
                            int priority = inputNode.getPriority(facing);
                            if (inputFluidMap.containsKey(priority)) {
                                inputFluidMap.get(priority).add(filter);
                            } else {
                                List<IFluidFilter> filterList = new LinkedList<>();
                                filterList.add(filter);
                                inputFluidMap.put(priority, filterList);
                            }
                        }
                    }
                }
            }
        }

        int maxTransfer = this.getMaxTransferForDemonWill(WorldDemonWillHandler.getCurrentWill(getWorld(), pos, EnumDemonWillType.DEFAULT));
        int maxFluidTransfer = 1000;

        for (Entry<Integer, List<IItemFilter>> outputEntry : outputMap.entrySet()) {
            List<IItemFilter> outputList = outputEntry.getValue();
            for (IItemFilter outputFilter : outputList) {
                for (Entry<Integer, List<IItemFilter>> inputEntry : inputMap.entrySet()) {
                    List<IItemFilter> inputList = inputEntry.getValue();
                    for (IItemFilter inputFilter : inputList) {
                        maxTransfer -= inputFilter.transferThroughInputFilter(outputFilter, maxTransfer);
                        if (maxTransfer <= 0) {
                            return;
                        }
                    }
                }
            }
        }

        for (Entry<Integer, List<IFluidFilter>> outputEntry : outputFluidMap.entrySet()) {
            List<IFluidFilter> outputList = outputEntry.getValue();
            for (IFluidFilter outputFilter : outputList) {
                for (Entry<Integer, List<IFluidFilter>> inputEntry : inputFluidMap.entrySet()) {
                    List<IFluidFilter> inputList = inputEntry.getValue();
                    for (IFluidFilter inputFilter : inputList) {
                        maxFluidTransfer -= inputFilter.transferThroughInputFilter(outputFilter, maxFluidTransfer);
                        if (maxFluidTransfer <= 0) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public int getMaxTransferForDemonWill(double will) {
        return 64;
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag) {
        super.serialize(tag);
        NBTTagList tags = new NBTTagList();
        for (BlockPos pos : generalNodeList) {
            NBTTagCompound posTag = new NBTTagCompound();
            posTag.setInteger(Constants.NBT.X_COORD, pos.getX());
            posTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
            posTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
            tags.appendTag(posTag);
        }
        tag.setTag(Constants.NBT.ROUTING_MASTER_GENERAL, tags);

        tags = new NBTTagList();
        for (BlockPos pos : inputNodeList) {
            NBTTagCompound posTag = new NBTTagCompound();
            posTag.setInteger(Constants.NBT.X_COORD, pos.getX());
            posTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
            posTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
            tags.appendTag(posTag);
        }
        tag.setTag(Constants.NBT.ROUTING_MASTER_INPUT, tags);

        tags = new NBTTagList();
        for (BlockPos pos : outputNodeList) {
            NBTTagCompound posTag = new NBTTagCompound();
            posTag.setInteger(Constants.NBT.X_COORD, pos.getX());
            posTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
            posTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
            tags.appendTag(posTag);
        }
        tag.setTag(Constants.NBT.ROUTING_MASTER_OUTPUT, tags);
        return tag;
    }

    @Override
    public void deserialize(NBTTagCompound tag) {
        super.deserialize(tag);

        NBTTagList tags = tag.getTagList(Constants.NBT.ROUTING_MASTER_GENERAL, 10);
        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound blockTag = tags.getCompoundTagAt(i);
            BlockPos newPos = new BlockPos(blockTag.getInteger(Constants.NBT.X_COORD), blockTag.getInteger(Constants.NBT.Y_COORD), blockTag.getInteger(Constants.NBT.Z_COORD));
            generalNodeList.add(newPos);
        }

        tags = tag.getTagList(Constants.NBT.ROUTING_MASTER_INPUT, 10);
        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound blockTag = tags.getCompoundTagAt(i);
            BlockPos newPos = new BlockPos(blockTag.getInteger(Constants.NBT.X_COORD), blockTag.getInteger(Constants.NBT.Y_COORD), blockTag.getInteger(Constants.NBT.Z_COORD));
            inputNodeList.add(newPos);
        }

        tags = tag.getTagList(Constants.NBT.ROUTING_MASTER_OUTPUT, 10);
        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound blockTag = tags.getCompoundTagAt(i);
            BlockPos newPos = new BlockPos(blockTag.getInteger(Constants.NBT.X_COORD), blockTag.getInteger(Constants.NBT.Y_COORD), blockTag.getInteger(Constants.NBT.Z_COORD));
            outputNodeList.add(newPos);
        }
    }

    @Override
    public boolean isConnected(List<BlockPos> path, BlockPos nodePos) {
        //TODO: Figure out how to make it so the path is obtained
//        if (!connectionMap.containsKey(nodePos))
//        {
//            return false;
//        }
        TileEntity tile = getWorld().getTileEntity(nodePos);
        if (!(tile instanceof IRoutingNode)) {
//            connectionMap.remove(nodePos);
            return false;
        }

        IRoutingNode node = (IRoutingNode) tile;
        List<BlockPos> connectionList = node.getConnected();
//        List<BlockPos> testPath = path.subList(0, path.size());
        path.add(nodePos);
        for (BlockPos testPos : connectionList) {
            if (path.contains(testPos)) {
                continue;
            }

            if (testPos.equals(this.getPos()) && node.isConnectionEnabled(testPos)) {
//                path.clear();
//                path.addAll(testPath);
                return true;
            } else if (NodeHelper.isNodeConnectionEnabled(getWorld(), node, testPos)) {
                if (isConnected(path, testPos)) {
//                    path.clear();
//                    path.addAll(testPath);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isConnectionEnabled(BlockPos testPos) {
        return currentInput <= 0;
    }

    @Override
    public void addNodeToList(IRoutingNode node) {
        BlockPos newPos = node.getBlockPos();
        if (!generalNodeList.contains(newPos)) {
            generalNodeList.add(newPos);
        }
        if (node instanceof IInputItemRoutingNode && !inputNodeList.contains(newPos)) {
            inputNodeList.add(newPos);
        }
        if (node instanceof IOutputItemRoutingNode && !outputNodeList.contains(newPos)) {
            outputNodeList.add(newPos);
        }
    }

    @Override
    public void addConnections(BlockPos pos, List<BlockPos> connectionList) {
        for (BlockPos testPos : connectionList) {
            addConnection(pos, testPos);
        }
    }

    @Override
    public void addConnection(BlockPos pos1, BlockPos pos2) {
        if (connectionMap.containsKey(pos1) && !connectionMap.get(pos1).contains(pos2)) {
            connectionMap.get(pos1).add(pos2);
        } else {
            List<BlockPos> list = new LinkedList<>();
            list.add(pos2);
            connectionMap.put(pos1, list);
        }

        if (connectionMap.containsKey(pos2) && !connectionMap.get(pos2).contains(pos1)) {
            connectionMap.get(pos2).add(pos1);
        } else {
            List<BlockPos> list = new LinkedList<>();
            list.add(pos1);
            connectionMap.put(pos2, list);
        }
    }

    @Override
    public void removeConnection(BlockPos pos1, BlockPos pos2) {
        if (connectionMap.containsKey(pos1)) {
            List<BlockPos> posList = connectionMap.get(pos1);
            posList.remove(pos2);
            if (posList.isEmpty()) {
                connectionMap.remove(pos1);
            }
        }

        if (connectionMap.containsKey(pos2)) {
            List<BlockPos> posList = connectionMap.get(pos2);
            posList.remove(pos1);
            if (posList.isEmpty()) {
                connectionMap.remove(pos2);
            }
        }
    }

    @Override
    public void connectMasterToRemainingNode(World world, List<BlockPos> alreadyChecked, IMasterRoutingNode master) {
        return;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.getPos();
    }

    @Override
    public List<BlockPos> getConnected() {
        return new LinkedList<>();
    }

    @Override
    public BlockPos getMasterPos() {
        return this.getPos();
    }

    @Override
    public boolean isMaster(IMasterRoutingNode master) {
        return false;
    }

    @Override
    public void addConnection(BlockPos pos1) {
        // Empty
    }

    @Override
    public void removeConnection(BlockPos pos1) {
        generalNodeList.remove(pos1);
        inputNodeList.remove(pos1);
        outputNodeList.remove(pos1);
    }

    @Override
    public void removeAllConnections() {
        List<BlockPos> list = generalNodeList.subList(0, generalNodeList.size());
        Iterator<BlockPos> itr = list.iterator();
        while (itr.hasNext()) {
            BlockPos testPos = itr.next();
            TileEntity tile = getWorld().getTileEntity(testPos);
            if (tile instanceof IRoutingNode) {
                ((IRoutingNode) tile).removeConnection(pos);
                getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(testPos), getWorld().getBlockState(testPos), 3);
            }

            itr.remove();
            inputNodeList.remove(testPos);
            outputNodeList.remove(testPos);
        }
    }
}
