package wayoftime.bloodmagic.common.tile.routing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import wayoftime.bloodmagic.api.compat.EnumDemonWillType;
import wayoftime.bloodmagic.common.container.tile.ContainerMasterRoutingNode;
import wayoftime.bloodmagic.common.item.routing.IRouterUpgrade;
import wayoftime.bloodmagic.common.routing.IFluidFilter;
import wayoftime.bloodmagic.common.routing.IInputFluidRoutingNode;
import wayoftime.bloodmagic.common.routing.IInputItemRoutingNode;
import wayoftime.bloodmagic.common.routing.IItemFilter;
import wayoftime.bloodmagic.common.routing.IMasterRoutingNode;
import wayoftime.bloodmagic.common.routing.IOutputFluidRoutingNode;
import wayoftime.bloodmagic.common.routing.IOutputItemRoutingNode;
import wayoftime.bloodmagic.common.routing.IRoutingNode;
import wayoftime.bloodmagic.common.routing.NodeHelper;
import wayoftime.bloodmagic.common.tile.BloodMagicTileEntities;
import wayoftime.bloodmagic.common.tile.TileInventory;
import wayoftime.bloodmagic.demonaura.WorldDemonWillHandler;
import wayoftime.bloodmagic.util.Constants;

public class TileMasterRoutingNode extends TileInventory implements IMasterRoutingNode, MenuProvider
{
	public static final int tickRate = 20;
	private int currentInput;
	// A list of connections
	private TreeMap<BlockPos, List<BlockPos>> connectionMap = new TreeMap<>();
	private List<BlockPos> generalNodeList = new LinkedList<>();
	private List<BlockPos> outputNodeList = new LinkedList<>();
	private List<BlockPos> inputNodeList = new LinkedList<>();
	private static final int TREE_OFFSET = 10;

	public static final int SLOT = 0;

	public TileMasterRoutingNode(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, 1, "masterroutingnode", pos, state);
	}

	public TileMasterRoutingNode(BlockPos pos, BlockState state)
	{
		this(BloodMagicTileEntities.MASTER_ROUTING_NODE_TYPE.get(), pos, state);
	}

	public void tick()
	{

		// TODO: Want to cache the filters and detect if a filter changes. Could have a
		// changed inventory cause the Master to recheck?
		if (!getLevel().isClientSide)
		{
//          currentInput = getWorld().isBlockIndirectlyGettingPowered(pos);
			currentInput = getLevel().getDirectSignalTo(worldPosition);

//          System.out.println(currentInput);
		}

		if (getLevel().isClientSide || getLevel().getGameTime() % tickRate != 0) // Temporary tick rate solver
		{
			return;
		}

//		System.out.println("Size of input list: " + inputNodeList.size());
		Map<Integer, List<IItemFilter>> outputMap = new TreeMap<>();
		Map<Integer, List<IFluidFilter>> outputFluidMap = new TreeMap<>();

		for (BlockPos outputPos : outputNodeList)
		{
			BlockEntity outputTile = getLevel().getBlockEntity(outputPos);
			if (this.isConnected(new LinkedList<>(), outputPos))
			{
				if (outputTile instanceof IOutputItemRoutingNode)
				{
					IOutputItemRoutingNode outputNode = (IOutputItemRoutingNode) outputTile;

					for (Direction facing : Direction.values())
					{
						if (!outputNode.isInventoryConnectedToSide(facing) || !outputNode.isOutput(facing))
						{
							continue;
						}

						IItemFilter filter = outputNode.getOutputFilterForSide(facing);
						if (filter != null)
						{
							int priority = outputNode.getPriority(facing);
							if (outputMap.containsKey(TREE_OFFSET - priority))
							{
								outputMap.get(TREE_OFFSET - priority).add(filter);
							} else
							{
								List<IItemFilter> filterList = new LinkedList<>();
								filterList.add(filter);
								outputMap.put(TREE_OFFSET - priority, filterList);
							}
						}
					}
				}

				if (outputTile instanceof IOutputFluidRoutingNode)
				{
					IOutputFluidRoutingNode outputNode = (IOutputFluidRoutingNode) outputTile;

					for (Direction facing : Direction.values())
					{
						if (!outputNode.isTankConnectedToSide(facing) || !outputNode.isFluidOutput(facing))
						{
							continue;
						}

						IFluidFilter filter = outputNode.getOutputFluidFilterForSide(facing);
						if (filter != null)
						{
							int priority = outputNode.getPriority(facing);
							if (outputFluidMap.containsKey(priority))
							{
								outputFluidMap.get(priority).add(filter);
							} else
							{
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

		for (BlockPos inputPos : inputNodeList)
		{
			BlockEntity inputTile = getLevel().getBlockEntity(inputPos);
			if (this.isConnected(new LinkedList<>(), inputPos))
			{
				if (inputTile instanceof IInputItemRoutingNode)
				{
					IInputItemRoutingNode inputNode = (IInputItemRoutingNode) inputTile;

					for (Direction facing : Direction.values())
					{
						if (!inputNode.isInventoryConnectedToSide(facing) || !inputNode.isInput(facing))
						{
							continue;
						}

						IItemFilter filter = inputNode.getInputFilterForSide(facing);
						if (filter != null)
						{
							int priority = inputNode.getPriority(facing);
							if (inputMap.containsKey(TREE_OFFSET - priority))
							{
								inputMap.get(TREE_OFFSET - priority).add(filter);
							} else
							{
								List<IItemFilter> filterList = new LinkedList<>();
								filterList.add(filter);
								inputMap.put(TREE_OFFSET - priority, filterList);
							}
						}
					}
				}

				if (inputTile instanceof IInputFluidRoutingNode)
				{
					IInputFluidRoutingNode inputNode = (IInputFluidRoutingNode) inputTile;

					for (Direction facing : Direction.values())
					{
						if (!inputNode.isTankConnectedToSide(facing) || !inputNode.isFluidInput(facing))
						{
							continue;
						}

						IFluidFilter filter = inputNode.getInputFluidFilterForSide(facing);
						if (filter != null)
						{
							int priority = inputNode.getPriority(facing);
							if (inputFluidMap.containsKey(priority))
							{
								inputFluidMap.get(priority).add(filter);
							} else
							{
								List<IFluidFilter> filterList = new LinkedList<>();
								filterList.add(filter);
								inputFluidMap.put(priority, filterList);
							}
						}
					}
				}
			}
		}

		int maxTransfer = this.getMaxTransferForDemonWill(WorldDemonWillHandler.getCurrentWill(getLevel(), worldPosition, EnumDemonWillType.DEFAULT));
		int maxFluidTransfer = 1000 * (maxTransfer / 16); // 16 is without and per upgrade

		for (Entry<Integer, List<IItemFilter>> outputEntry : outputMap.entrySet())
		{
			List<IItemFilter> outputList = outputEntry.getValue();
			for (IItemFilter outputFilter : outputList)
			{
				for (Entry<Integer, List<IItemFilter>> inputEntry : inputMap.entrySet())
				{
					List<IItemFilter> inputList = inputEntry.getValue();
					for (IItemFilter inputFilter : inputList)
					{
						int amountTransfered = inputFilter.transferThroughInputFilter(outputFilter, maxTransfer);
						maxTransfer -= amountTransfered;

						if (maxTransfer <= 0)
						{
							return;
						}
					}
				}
			}
		}

		for (Entry<Integer, List<IFluidFilter>> outputEntry : outputFluidMap.entrySet())
		{
			List<IFluidFilter> outputList = outputEntry.getValue();
			for (IFluidFilter outputFilter : outputList)
			{
				for (Entry<Integer, List<IFluidFilter>> inputEntry : inputFluidMap.entrySet())
				{
					List<IFluidFilter> inputList = inputEntry.getValue();
					for (IFluidFilter inputFilter : inputList)
					{
						maxFluidTransfer -= inputFilter.transferThroughInputFilter(outputFilter, maxFluidTransfer);
						if (maxFluidTransfer <= 0)
						{
							return;
						}
					}
				}
			}
		}
	}

	public int getMaxTransferForDemonWill(double will)
	{
		int rate = 16;
		ItemStack upgradeStack = getItem(SLOT);
		if (!upgradeStack.isEmpty() && upgradeStack.getItem() instanceof IRouterUpgrade)
		{
			rate += ((IRouterUpgrade) upgradeStack.getItem()).getMaxTransferIncrease(upgradeStack);
		}

		return rate;
	}

	@Override
	public CompoundTag serialize(CompoundTag tag)
	{
		super.serialize(tag);
		ListTag tags = new ListTag();
		for (BlockPos pos : generalNodeList)
		{
			CompoundTag posTag = new CompoundTag();
			posTag.putInt(Constants.NBT.X_COORD, pos.getX());
			posTag.putInt(Constants.NBT.Y_COORD, pos.getY());
			posTag.putInt(Constants.NBT.Z_COORD, pos.getZ());
			tags.add(posTag);
		}
		tag.put(Constants.NBT.ROUTING_MASTER_GENERAL, tags);

		tags = new ListTag();
		for (BlockPos pos : inputNodeList)
		{
			CompoundTag posTag = new CompoundTag();
			posTag.putInt(Constants.NBT.X_COORD, pos.getX());
			posTag.putInt(Constants.NBT.Y_COORD, pos.getY());
			posTag.putInt(Constants.NBT.Z_COORD, pos.getZ());
			tags.add(posTag);
		}
		tag.put(Constants.NBT.ROUTING_MASTER_INPUT, tags);

		tags = new ListTag();
		for (BlockPos pos : outputNodeList)
		{
			CompoundTag posTag = new CompoundTag();
			posTag.putInt(Constants.NBT.X_COORD, pos.getX());
			posTag.putInt(Constants.NBT.Y_COORD, pos.getY());
			posTag.putInt(Constants.NBT.Z_COORD, pos.getZ());
			tags.add(posTag);
		}
		tag.put(Constants.NBT.ROUTING_MASTER_OUTPUT, tags);
		return tag;
	}

	@Override
	public void deserialize(CompoundTag tag)
	{
		super.deserialize(tag);

		ListTag tags = tag.getList(Constants.NBT.ROUTING_MASTER_GENERAL, 10);
		for (int i = 0; i < tags.size(); i++)
		{
			CompoundTag blockTag = tags.getCompound(i);
			BlockPos newPos = new BlockPos(blockTag.getInt(Constants.NBT.X_COORD), blockTag.getInt(Constants.NBT.Y_COORD), blockTag.getInt(Constants.NBT.Z_COORD));
			generalNodeList.add(newPos);
		}

		tags = tag.getList(Constants.NBT.ROUTING_MASTER_INPUT, 10);
		for (int i = 0; i < tags.size(); i++)
		{
			CompoundTag blockTag = tags.getCompound(i);
			BlockPos newPos = new BlockPos(blockTag.getInt(Constants.NBT.X_COORD), blockTag.getInt(Constants.NBT.Y_COORD), blockTag.getInt(Constants.NBT.Z_COORD));
			inputNodeList.add(newPos);
		}

		tags = tag.getList(Constants.NBT.ROUTING_MASTER_OUTPUT, 10);
		for (int i = 0; i < tags.size(); i++)
		{
			CompoundTag blockTag = tags.getCompound(i);
			BlockPos newPos = new BlockPos(blockTag.getInt(Constants.NBT.X_COORD), blockTag.getInt(Constants.NBT.Y_COORD), blockTag.getInt(Constants.NBT.Z_COORD));
			outputNodeList.add(newPos);
		}
	}

	@Override
	public boolean isConnected(List<BlockPos> path, BlockPos nodePos)
	{
		// TODO: Figure out how to make it so the path is obtained
//        if (!connectionMap.containsKey(nodePos))
//        {
//            return false;
//        }
		BlockEntity tile = getLevel().getBlockEntity(nodePos);
		if (!(tile instanceof IRoutingNode))
		{
//            connectionMap.remove(nodePos);
			return false;
		}

		IRoutingNode node = (IRoutingNode) tile;
		List<BlockPos> connectionList = node.getConnected();
//        List<BlockPos> testPath = path.subList(0, path.size());
		path.add(nodePos);
		for (BlockPos testPos : connectionList)
		{
			if (path.contains(testPos))
			{
				continue;
			}

			if (testPos.equals(this.getBlockPos()) && node.isConnectionEnabled(testPos))
			{
//                path.clear();
//                path.addAll(testPath);
				return true;
			} else if (NodeHelper.isNodeConnectionEnabled(getLevel(), node, testPos))
			{
				if (isConnected(path, testPos))
				{
//                    path.clear();
//                    path.addAll(testPath);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean isConnectionEnabled(BlockPos testPos)
	{
		return currentInput <= 0;
	}

	@Override
	public void addNodeToList(IRoutingNode node)
	{
		BlockPos newPos = node.getCurrentBlockPos();
		if (!generalNodeList.contains(newPos))
		{
			generalNodeList.add(newPos);
		}
		if (node instanceof IInputItemRoutingNode && node instanceof IInputFluidRoutingNode && !inputNodeList.contains(newPos))
		{
			inputNodeList.add(newPos);
		}
		if (node instanceof IOutputItemRoutingNode && node instanceof IOutputFluidRoutingNode && !outputNodeList.contains(newPos))
		{
			outputNodeList.add(newPos);
		}
	}

	@Override
	public void addConnections(BlockPos pos, List<BlockPos> connectionList)
	{
		for (BlockPos testPos : connectionList)
		{
			addConnection(pos, testPos);
		}
	}

	@Override
	public void addConnection(BlockPos pos1, BlockPos pos2)
	{
		if (connectionMap.containsKey(pos1) && !connectionMap.get(pos1).contains(pos2))
		{
			connectionMap.get(pos1).add(pos2);
		} else
		{
			List<BlockPos> list = new LinkedList<>();
			list.add(pos2);
			connectionMap.put(pos1, list);
		}

		if (connectionMap.containsKey(pos2) && !connectionMap.get(pos2).contains(pos1))
		{
			connectionMap.get(pos2).add(pos1);
		} else
		{
			List<BlockPos> list = new LinkedList<>();
			list.add(pos1);
			connectionMap.put(pos2, list);
		}
	}

	@Override
	public void removeConnection(BlockPos pos1, BlockPos pos2)
	{
		if (connectionMap.containsKey(pos1))
		{
			List<BlockPos> posList = connectionMap.get(pos1);
			posList.remove(pos2);
			if (posList.isEmpty())
			{
				connectionMap.remove(pos1);
			}
		}

		if (connectionMap.containsKey(pos2))
		{
			List<BlockPos> posList = connectionMap.get(pos2);
			posList.remove(pos1);
			if (posList.isEmpty())
			{
				connectionMap.remove(pos2);
			}
		}
	}

	@Override
	public void connectMasterToRemainingNode(Level world, List<BlockPos> alreadyChecked, IMasterRoutingNode master)
	{
		return;
	}

	@Override
	public BlockPos getCurrentBlockPos()
	{
		return this.getBlockPos();
	}

	@Override
	public List<BlockPos> getConnected()
	{
		return new LinkedList<>();
	}

	@Override
	public BlockPos getMasterPos()
	{
		return this.getBlockPos();
	}

	@Override
	public boolean isMaster(IMasterRoutingNode master)
	{
		return false;
	}

	@Override
	public void addConnection(BlockPos pos1)
	{
		// Empty
	}

	@Override
	public void removeConnection(BlockPos pos1)
	{
		generalNodeList.remove(pos1);
		inputNodeList.remove(pos1);
		outputNodeList.remove(pos1);
	}

	@Override
	public void removeAllConnections()
	{
		List<BlockPos> list = generalNodeList.subList(0, generalNodeList.size());
		Iterator<BlockPos> itr = list.iterator();
		while (itr.hasNext())
		{
			BlockPos testPos = itr.next();
			BlockEntity tile = getLevel().getBlockEntity(testPos);
			if (tile instanceof IRoutingNode)
			{
				((IRoutingNode) tile).removeConnection(worldPosition);
				getLevel().sendBlockUpdated(getBlockPos(), getLevel().getBlockState(testPos), getLevel().getBlockState(testPos), 3);
			}

			itr.remove();
			inputNodeList.remove(testPos);
			outputNodeList.remove(testPos);
		}
	}

	@Override
	public Triple<Boolean, List<BlockPos>, List<IRoutingNode>> recheckConnectionToMaster(List<BlockPos> alreadyChecked, List<IRoutingNode> nodeList)
	{
		return Triple.of(true, alreadyChecked, nodeList);
	}

	@Override
	public List<BlockPos> checkAndPurgeConnectionToMaster(BlockPos ignorePos)
	{
		// This is a Master node... Of course we're connected...
		return new LinkedList<BlockPos>();
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

	@Override
	public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_)
	{
		assert level != null;
		return new ContainerMasterRoutingNode(this, p_createMenu_1_, p_createMenu_2_);
	}

	@Override
	public Component getDisplayName()
	{
		return new TextComponent("Master Routing Node");
	}
}