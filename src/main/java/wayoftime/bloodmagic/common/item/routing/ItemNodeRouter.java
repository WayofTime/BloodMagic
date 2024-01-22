package wayoftime.bloodmagic.common.item.routing;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.routing.IMasterRoutingNode;
import wayoftime.bloodmagic.common.routing.INodeRenderer;
import wayoftime.bloodmagic.common.routing.IRoutingNode;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;

import java.util.LinkedList;
import java.util.List;

public class ItemNodeRouter extends Item implements INodeRenderer
{
	public ItemNodeRouter()
	{
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag)
	{
		if (!stack.hasTag())
			return;
		CompoundTag tag = stack.getTag();
		BlockPos coords = getBlockPos(stack);

		if (coords != null && tag != null)
		{
			tooltip.add(Component.translatable("tooltip.bloodmagic.telepositionfocus.coords", coords.getX(), coords.getY(), coords.getZ()));
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		InteractionHand hand = context.getHand();
		Player player = context.getPlayer();
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();

		ItemStack stack = player.getItemInHand(hand);
		if (world.isClientSide)
		{
			return InteractionResult.PASS;
		}

		BlockEntity tileHit = world.getBlockEntity(pos);

		if (!(tileHit instanceof IRoutingNode))
		{
			// TODO: Remove contained position?
			BlockPos containedPos = getBlockPos(stack);
			if (!containedPos.equals(BlockPos.ZERO))
			{
				this.setBlockPos(stack, BlockPos.ZERO);
				player.displayClientMessage(Component.translatable("chat.bloodmagic.routing.remove"), true);
				return InteractionResult.FAIL;
			}
			return InteractionResult.FAIL;
		}
		IRoutingNode node = (IRoutingNode) tileHit;
		BlockPos containedPos = getBlockPos(stack);
		if (containedPos.equals(BlockPos.ZERO))
		{
			this.setBlockPos(stack, pos);
			player.displayClientMessage(Component.translatable("chat.bloodmagic.routing.set"), true);
			return InteractionResult.SUCCESS;
		} else
		{
			if (containedPos.distSqr(pos) > 16 * 16)
			{
				player.displayClientMessage(Component.translatable("chat.bloodmagic.routing.distance"), true);
				return InteractionResult.SUCCESS;
			} else if (containedPos.equals(pos))
			{
				player.displayClientMessage(Component.translatable("chat.bloodmagic.routing.same"), true);
				return InteractionResult.SUCCESS;
			}
			BlockEntity pastTile = world.getBlockEntity(containedPos);
			if (pastTile instanceof IRoutingNode)
			{
				IRoutingNode pastNode = (IRoutingNode) pastTile;
				if (pastNode instanceof IMasterRoutingNode)
				{
					IMasterRoutingNode master = (IMasterRoutingNode) pastNode;

					if (!node.isMaster(master))
					{
						if (node.getMasterPos().equals(BlockPos.ZERO)) // If the node is not the master and it is
																		// receptive
						{
							node.connectMasterToRemainingNode(world, new LinkedList<>(), master);
							master.addConnection(pos, containedPos);
							master.addNodeToList(node);
							node.addConnection(containedPos);
							player.displayClientMessage(Component.translatable("chat.bloodmagic.routing.link.master"), true);
							this.setBlockPos(stack, BlockPos.ZERO);
							return InteractionResult.SUCCESS;
						}
					} else
					{
						master.addConnection(pos, containedPos);
						node.addConnection(containedPos);
						player.displayClientMessage(Component.translatable("chat.bloodmagic.routing.link.master"), true);
						this.setBlockPos(stack, BlockPos.ZERO);
						return InteractionResult.SUCCESS;
					}

				} else if (node instanceof IMasterRoutingNode)
				{
					IMasterRoutingNode master = (IMasterRoutingNode) node;

					if (!pastNode.isMaster(master))
					{
						if (pastNode.getMasterPos().equals(BlockPos.ZERO)) // TODO: This is where the issue is
						{
							pastNode.connectMasterToRemainingNode(world, new LinkedList<>(), master);
							master.addConnection(pos, containedPos);
							pastNode.addConnection(pos);
							master.addNodeToList(pastNode);
							player.displayClientMessage(Component.translatable("chat.bloodmagic.routing.link.master"), true);
							this.setBlockPos(stack, BlockPos.ZERO);
							return InteractionResult.SUCCESS;
						}
					} else
					{
						master.addConnection(pos, containedPos);
						pastNode.addConnection(pos);
						player.displayClientMessage(Component.translatable("chat.bloodmagic.routing.link.master"), true);
						this.setBlockPos(stack, BlockPos.ZERO);
						return InteractionResult.SUCCESS;
					}
				} else
				{
					// Both nodes are not master nodes, so normal linking
					if (pastNode.getMasterPos().equals(node.getMasterPos()))
					{
						if (!pastNode.getMasterPos().equals(BlockPos.ZERO))
						{
							BlockEntity testTile = world.getBlockEntity(pastNode.getMasterPos());
							if (testTile instanceof IMasterRoutingNode)
							{
								IMasterRoutingNode master = (IMasterRoutingNode) testTile;
								master.addConnection(pos, containedPos);
							}
						}
						pastNode.addConnection(pos);
						node.addConnection(containedPos);
						player.displayClientMessage(Component.translatable("chat.bloodmagic.routing.link"), true);
						this.setBlockPos(stack, BlockPos.ZERO);
						return InteractionResult.SUCCESS;
					} else if (pastNode.getMasterPos().equals(BlockPos.ZERO)) // pastNode is not connected to a
																				// master, but node is
					{
						BlockEntity tile = world.getBlockEntity(node.getMasterPos());
						if (tile instanceof IMasterRoutingNode)
						{
							IMasterRoutingNode master = (IMasterRoutingNode) tile;
							master.addConnection(pos, containedPos);
							master.addNodeToList(pastNode);
							pastNode.connectMasterToRemainingNode(world, new LinkedList<>(), master);
						}
						pastNode.addConnection(pos);
						node.addConnection(containedPos);
						player.displayClientMessage(Component.translatable("chat.bloodmagic.routing.link"), true);
						this.setBlockPos(stack, BlockPos.ZERO);
						return InteractionResult.SUCCESS;
					} else if (node.getMasterPos().equals(BlockPos.ZERO)) // node is not connected to a master, but
																			// pastNode is
					{
						BlockEntity tile = world.getBlockEntity(pastNode.getMasterPos());
						if (tile instanceof IMasterRoutingNode)
						{
							IMasterRoutingNode master = (IMasterRoutingNode) tile;
							master.addConnection(pos, containedPos);
							master.addNodeToList(node);
							node.connectMasterToRemainingNode(world, new LinkedList<>(), master);
						}
						pastNode.addConnection(pos);
						node.addConnection(containedPos);
						player.displayClientMessage(Component.translatable("chat.bloodmagic.routing.link"), true);
						this.setBlockPos(stack, BlockPos.ZERO);
						return InteractionResult.SUCCESS;
					} else
					{
						this.setBlockPos(stack, BlockPos.ZERO);
						return InteractionResult.SUCCESS;
					}
				}
			}
		}

		return InteractionResult.FAIL;
	}

	public BlockPos getBlockPos(ItemStack stack)
	{
		stack = NBTHelper.checkNBT(stack);
		return new BlockPos(stack.getTag().getInt(Constants.NBT.X_COORD), stack.getTag().getInt(Constants.NBT.Y_COORD), stack.getTag().getInt(Constants.NBT.Z_COORD));
	}

	public ItemStack setBlockPos(ItemStack stack, BlockPos pos)
	{
		NBTHelper.checkNBT(stack);
		CompoundTag itemTag = stack.getTag();
		itemTag.putInt(Constants.NBT.X_COORD, pos.getX());
		itemTag.putInt(Constants.NBT.Y_COORD, pos.getY());
		itemTag.putInt(Constants.NBT.Z_COORD, pos.getZ());
		return stack;
	}
}
