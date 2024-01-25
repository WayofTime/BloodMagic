package wayoftime.bloodmagic.common.tile.routing;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import wayoftime.bloodmagic.common.routing.IItemRoutingNode;
import wayoftime.bloodmagic.common.routing.IMasterRoutingNode;
import wayoftime.bloodmagic.common.routing.IRoutingNode;
import wayoftime.bloodmagic.common.tile.BloodMagicTileEntities;
import wayoftime.bloodmagic.common.tile.TileInventory;
import wayoftime.bloodmagic.util.Constants;

public class TileRoutingNode extends TileInventory implements IRoutingNode, IItemRoutingNode
{
	private int currentInput;
	private BlockPos masterPos = BlockPos.ZERO;
	private List<BlockPos> connectionList = new LinkedList<>();
	private AABB boundingBox;

	public TileRoutingNode(BlockEntityType<?> type, int size, String name, BlockPos pos, BlockState state)
	{
		super(type, size, name, pos, state);
	}

	public TileRoutingNode(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.ROUTING_NODE_TYPE.get(), 1, "routingnode", pos, state);
	}

	public void tick()
	{
		if (!getLevel().isClientSide)
		{
			currentInput = getLevel().getBestNeighborSignal(worldPosition);
			// currentInput = getWorld().getStrongPower(pos);
		}
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		super.serialize(tag);
		CompoundTag masterTag = new CompoundTag();
		masterTag.putInt(Constants.NBT.X_COORD, masterPos.getX());
		masterTag.putInt(Constants.NBT.Y_COORD, masterPos.getY());
		masterTag.putInt(Constants.NBT.Z_COORD, masterPos.getZ());
		tag.put(Constants.NBT.ROUTING_MASTER, masterTag);

		ListTag tags = new ListTag();
		for (BlockPos pos : connectionList)
		{
			CompoundTag posTag = new CompoundTag();
			posTag.putInt(Constants.NBT.X_COORD, pos.getX());
			posTag.putInt(Constants.NBT.Y_COORD, pos.getY());
			posTag.putInt(Constants.NBT.Z_COORD, pos.getZ());
			tags.add(posTag);
		}
		tag.put(Constants.NBT.ROUTING_CONNECTION, tags);
		return tag;
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		super.deserialize(tag);
		connectionList.clear();
		CompoundTag masterTag = tag.getCompound(Constants.NBT.ROUTING_MASTER);
		masterPos = new BlockPos(masterTag.getInt(Constants.NBT.X_COORD), masterTag.getInt(Constants.NBT.Y_COORD), masterTag.getInt(Constants.NBT.Z_COORD));

		ListTag tags = tag.getList(Constants.NBT.ROUTING_CONNECTION, 10);
		for (int i = 0; i < tags.size(); i++)
		{
			CompoundTag blockTag = tags.getCompound(i);
			BlockPos newPos = new BlockPos(blockTag.getInt(Constants.NBT.X_COORD), blockTag.getInt(Constants.NBT.Y_COORD), blockTag.getInt(Constants.NBT.Z_COORD));
			connectionList.add(newPos);
		}
	}

	@Override
	public void removeAllConnections()
	{
		BlockEntity testTile = getLevel().getBlockEntity(getMasterPos());
		if (testTile instanceof IMasterRoutingNode)
		{
			((IMasterRoutingNode) testTile).removeConnection(worldPosition); // Remove this node from the master
		}
		for (BlockPos testPos : connectionList)
		{
			BlockEntity tile = getLevel().getBlockEntity(testPos);
			if (tile instanceof IRoutingNode)
			{
				((IRoutingNode) tile).removeConnection(worldPosition);
				getLevel().sendBlockUpdated(getBlockPos(), getLevel().getBlockState(testPos), getLevel().getBlockState(testPos), 3);
			}
		}

		connectionList.clear();
	}

	@Override
	public void connectMasterToRemainingNode(Level world, List<BlockPos> alreadyChecked, IMasterRoutingNode master)
	{
		this.masterPos = master.getCurrentBlockPos();
		List<BlockPos> connectedList = this.getConnected();
		for (BlockPos testPos : connectedList)
		{
			if (alreadyChecked.contains(testPos))
			{
				continue;
			}
			alreadyChecked.add(testPos);
			BlockEntity tile = world.getBlockEntity(testPos);
			if (!(tile instanceof IRoutingNode))
			{
				continue;
			}
			IRoutingNode node = (IRoutingNode) tile;
			if (node.getMasterPos().equals(BlockPos.ZERO)) // If getMasterPos() returns the origin, the node is not
															// connected to any master.
			{
				master.addNodeToList(node);
				node.connectMasterToRemainingNode(world, alreadyChecked, master);
			}
		}

		master.addConnections(this.getCurrentBlockPos(), connectedList);
	}

	// Returns true if the node is connected to the contained IMasterRoutingNode,
	// and false if no connection. Returns a list of all IRoutingNodes that have
	// been checked, for use in purging the connection to IMasterRoutingNode. It is
	// up to the Node to add themselves to the nodeList.
	@Override
	public Triple<Boolean, List<BlockPos>, List<IRoutingNode>> recheckConnectionToMaster(List<BlockPos> alreadyChecked, List<IRoutingNode> nodeList)
	{
		if (this.masterPos.equals(BlockPos.ZERO))
		{
			// Node already is not connected to a Master, therefore return false;
			return Triple.of(false, alreadyChecked, nodeList);
		}

		List<BlockPos> connectedList = this.getConnected();
		for (BlockPos testPos : connectedList)
		{
			if (alreadyChecked.contains(testPos))
			{
				continue;
			}
			alreadyChecked.add(testPos);
			BlockEntity tile = level.getBlockEntity(testPos);
			if (!(tile instanceof IRoutingNode))
			{
				continue;
			}
			IRoutingNode node = (IRoutingNode) tile;

			if (node instanceof IMasterRoutingNode)
			{
				// Do not need to check any other connections because an IMasterRoutingNode is
				// found.
				return Triple.of(true, alreadyChecked, nodeList);
			}

			Triple<Boolean, List<BlockPos>, List<IRoutingNode>> checkResult = node.recheckConnectionToMaster(alreadyChecked, nodeList);

			if (checkResult.getLeft())
			{
				// Found a Master!
				return checkResult;
			}

			// alreadyChecked.addAll(checkResult.getMiddle());
			// nodeList.addAll(checkResult.getRight());
		}

		nodeList.add(this);
		return Triple.of(false, alreadyChecked, nodeList);
	}

	// Returns true if the node is connected to the contained IMasterRoutingNode,
	// and false if no connection. If false, removes the reference to the original
	// IMasterRoutingNode of all connected nodes.
	@Override
	public List<BlockPos> checkAndPurgeConnectionToMaster(BlockPos ignorePos)
	{
		List<BlockPos> posList = new LinkedList<BlockPos>();
		posList.add(ignorePos);
		Triple<Boolean, List<BlockPos>, List<IRoutingNode>> recheckResult = recheckConnectionToMaster(posList, new LinkedList<IRoutingNode>());
		if (!recheckResult.getLeft())
		{
			BlockEntity testTile = level.getBlockEntity(masterPos);
			IMasterRoutingNode masterNode = null;
			if (testTile instanceof IMasterRoutingNode)
			{
				masterNode = (IMasterRoutingNode) testTile;
				masterNode.removeConnection(getCurrentBlockPos());
			}
			for (IRoutingNode node : recheckResult.getRight())
			{
				BlockPos masterPos = node.getMasterPos();
				node.removeConnection(masterPos);
				if (masterNode != null)
					masterNode.removeConnection(node.getCurrentBlockPos());
			}

			return recheckResult.getMiddle();
		}

		return recheckResult.getMiddle();
	}

	@Override
	public BlockPos getCurrentBlockPos()
	{
		return this.getBlockPos();
	}

	@Override
	public List<BlockPos> getConnected()
	{
		return connectionList;
	}

	@Override
	public BlockPos getMasterPos()
	{
		return masterPos;
	}

	@Override
	public boolean isMaster(IMasterRoutingNode master)
	{
		BlockPos checkPos = master.getCurrentBlockPos();
		return checkPos.equals(getMasterPos());
	}

	@Override
	public boolean isConnectionEnabled(BlockPos testPos)
	{
		return currentInput <= 0;
	}

	@Override
	public void addConnection(BlockPos pos1)
	{
		if (!connectionList.contains(pos1))
		{
			getLevel().sendBlockUpdated(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()), 3);
			connectionList.add(pos1);
		}
	}

	@Override
	public void removeConnection(BlockPos pos1)
	{
		if (connectionList.contains(pos1))
		{
			connectionList.remove(pos1);
			getLevel().sendBlockUpdated(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()), 3);
		}

		if (pos1.equals(masterPos))
		{
			this.masterPos = BlockPos.ZERO;
		}
	}

	@Override
	public boolean isInventoryConnectedToSide(Direction side)
	{
		return false;
	}

	@Override
	public int getPriority(Direction side)
	{
		return 0;
	}

	// @Override
	// @OnlyIn(Dist.CLIENT)
	// public double getViewDistance()
	// {
	// return 10000;
	// }

	@Override
	public AABB getRenderBoundingBox()
	{
		if (boundingBox == null)
		{
			boundingBox = super.getRenderBoundingBox().inflate(5);
		}
		return boundingBox;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null)
		{
			return LazyOptional.empty();
		}

		return super.getCapability(capability, facing);
	}
}