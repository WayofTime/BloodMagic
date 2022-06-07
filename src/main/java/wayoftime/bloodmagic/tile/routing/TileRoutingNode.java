package wayoftime.bloodmagic.tile.routing;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.registries.ObjectHolder;
import wayoftime.bloodmagic.common.routing.IItemRoutingNode;
import wayoftime.bloodmagic.common.routing.IMasterRoutingNode;
import wayoftime.bloodmagic.common.routing.IRoutingNode;
import wayoftime.bloodmagic.tile.TileInventory;
import wayoftime.bloodmagic.util.Constants;

public class TileRoutingNode extends TileInventory implements IRoutingNode, IItemRoutingNode, ITickableTileEntity
{
	@ObjectHolder("bloodmagic:itemroutingnode")
	public static TileEntityType<TileRoutingNode> TYPE;

	private int currentInput;
	private BlockPos masterPos = BlockPos.ZERO;
	private List<BlockPos> connectionList = new LinkedList<>();
	private AxisAlignedBB boundingBox;

	public TileRoutingNode(TileEntityType<?> type, int size, String name)
	{
		super(type, size, name);
	}

	public TileRoutingNode()
	{
		this(TYPE, 1, "routingnode");
	}

	@Override
	public void tick()
	{
		if (!getWorld().isRemote)
		{
			currentInput = getWorld().getRedstonePowerFromNeighbors(pos);
//            currentInput = getWorld().getStrongPower(pos);
		}
	}

	@Override
	public CompoundNBT serialize(CompoundNBT tag)
	{
		super.serialize(tag);
		CompoundNBT masterTag = new CompoundNBT();
		masterTag.putInt(Constants.NBT.X_COORD, masterPos.getX());
		masterTag.putInt(Constants.NBT.Y_COORD, masterPos.getY());
		masterTag.putInt(Constants.NBT.Z_COORD, masterPos.getZ());
		tag.put(Constants.NBT.ROUTING_MASTER, masterTag);

		ListNBT tags = new ListNBT();
		for (BlockPos pos : connectionList)
		{
			CompoundNBT posTag = new CompoundNBT();
			posTag.putInt(Constants.NBT.X_COORD, pos.getX());
			posTag.putInt(Constants.NBT.Y_COORD, pos.getY());
			posTag.putInt(Constants.NBT.Z_COORD, pos.getZ());
			tags.add(posTag);
		}
		tag.put(Constants.NBT.ROUTING_CONNECTION, tags);
		return tag;
	}

	@Override
	public void deserialize(CompoundNBT tag)
	{
		super.deserialize(tag);
		connectionList.clear();
		CompoundNBT masterTag = tag.getCompound(Constants.NBT.ROUTING_MASTER);
		masterPos = new BlockPos(masterTag.getInt(Constants.NBT.X_COORD), masterTag.getInt(Constants.NBT.Y_COORD), masterTag.getInt(Constants.NBT.Z_COORD));

		ListNBT tags = tag.getList(Constants.NBT.ROUTING_CONNECTION, 10);
		for (int i = 0; i < tags.size(); i++)
		{
			CompoundNBT blockTag = tags.getCompound(i);
			BlockPos newPos = new BlockPos(blockTag.getInt(Constants.NBT.X_COORD), blockTag.getInt(Constants.NBT.Y_COORD), blockTag.getInt(Constants.NBT.Z_COORD));
			connectionList.add(newPos);
		}
	}

	@Override
	public void removeAllConnections()
	{
		TileEntity testTile = getWorld().getTileEntity(getMasterPos());
		if (testTile instanceof IMasterRoutingNode)
		{
			((IMasterRoutingNode) testTile).removeConnection(pos); // Remove this node from the master
		}
		for (BlockPos testPos : connectionList)
		{
			TileEntity tile = getWorld().getTileEntity(testPos);
			if (tile instanceof IRoutingNode)
			{
				((IRoutingNode) tile).removeConnection(pos);
				getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(testPos), getWorld().getBlockState(testPos), 3);
			}
		}

		connectionList.clear();
	}

	@Override
	public void connectMasterToRemainingNode(World world, List<BlockPos> alreadyChecked, IMasterRoutingNode master)
	{
		this.masterPos = master.getBlockPos();
		List<BlockPos> connectedList = this.getConnected();
		for (BlockPos testPos : connectedList)
		{
			if (alreadyChecked.contains(testPos))
			{
				continue;
			}
			alreadyChecked.add(testPos);
			TileEntity tile = world.getTileEntity(testPos);
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

		master.addConnections(this.getBlockPos(), connectedList);
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
			TileEntity tile = world.getTileEntity(testPos);
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

//			alreadyChecked.addAll(checkResult.getMiddle());
//			nodeList.addAll(checkResult.getRight());
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
			TileEntity testTile = world.getTileEntity(masterPos);
			IMasterRoutingNode masterNode = null;
			if (testTile instanceof IMasterRoutingNode)
			{
				masterNode = (IMasterRoutingNode) testTile;
				masterNode.removeConnection(getBlockPos());
			}
			for (IRoutingNode node : recheckResult.getRight())
			{
				BlockPos masterPos = node.getMasterPos();
				node.removeConnection(masterPos);
				if (masterNode != null)
					masterNode.removeConnection(node.getBlockPos());
			}

			return recheckResult.getMiddle();
		}

		return recheckResult.getMiddle();
	}

	@Override
	public BlockPos getBlockPos()
	{
		return this.getPos();
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
		BlockPos checkPos = master.getBlockPos();
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
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			connectionList.add(pos1);
		}
	}

	@Override
	public void removeConnection(BlockPos pos1)
	{
		if (connectionList.contains(pos1))
		{
			connectionList.remove(pos1);
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
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

	@Override
	@OnlyIn(Dist.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 10000;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		if (boundingBox == null)
		{
			boundingBox = super.getRenderBoundingBox().grow(5);
		}
		return boundingBox;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return LazyOptional.empty();
		}

		return super.getCapability(capability, facing);
	}
}