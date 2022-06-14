package wayoftime.bloodmagic.common.item.routing;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.routing.IMasterRoutingNode;
import wayoftime.bloodmagic.common.routing.INodeRenderer;
import wayoftime.bloodmagic.common.routing.IRoutingNode;
import wayoftime.bloodmagic.util.Constants;
import wayoftime.bloodmagic.util.helper.NBTHelper;

public class ItemNodeRouter extends Item implements INodeRenderer
{
	public ItemNodeRouter()
	{
		super(new Item.Properties().stacksTo(1).tab(BloodMagic.TAB));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if (!stack.hasTag())
			return;
		CompoundNBT tag = stack.getTag();
		BlockPos coords = getBlockPos(stack);

		if (coords != null && tag != null)
		{
			tooltip.add(new TranslationTextComponent("tooltip.bloodmagic.telepositionfocus.coords", coords.getX(), coords.getY(), coords.getZ()));
		}
	}

	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
		Hand hand = context.getHand();
		PlayerEntity player = context.getPlayer();
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();

		ItemStack stack = player.getItemInHand(hand);
		if (world.isClientSide)
		{
			return ActionResultType.PASS;
		}

		TileEntity tileHit = world.getBlockEntity(pos);

		if (!(tileHit instanceof IRoutingNode))
		{
			// TODO: Remove contained position?
			BlockPos containedPos = getBlockPos(stack);
			if (!containedPos.equals(BlockPos.ZERO))
			{
				this.setBlockPos(stack, BlockPos.ZERO);
				player.displayClientMessage(new TranslationTextComponent("chat.bloodmagic.routing.remove"), true);
				return ActionResultType.FAIL;
			}
			return ActionResultType.FAIL;
		}
		IRoutingNode node = (IRoutingNode) tileHit;
		BlockPos containedPos = getBlockPos(stack);
		if (containedPos.equals(BlockPos.ZERO))
		{
			this.setBlockPos(stack, pos);
			player.displayClientMessage(new TranslationTextComponent("chat.bloodmagic.routing.set"), true);
			return ActionResultType.SUCCESS;
		} else
		{
			if (containedPos.distSqr(pos) > 16 * 16)
			{
				player.displayClientMessage(new TranslationTextComponent("chat.bloodmagic.routing.distance"), true);
				return ActionResultType.SUCCESS;
			} else if (containedPos.equals(pos))
			{
				player.displayClientMessage(new TranslationTextComponent("chat.bloodmagic.routing.same"), true);
				return ActionResultType.SUCCESS;
			}
			TileEntity pastTile = world.getBlockEntity(containedPos);
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
							player.displayClientMessage(new TranslationTextComponent("chat.bloodmagic.routing.link.master"), true);
							this.setBlockPos(stack, BlockPos.ZERO);
							return ActionResultType.SUCCESS;
						}
					} else
					{
						master.addConnection(pos, containedPos);
						node.addConnection(containedPos);
						player.displayClientMessage(new TranslationTextComponent("chat.bloodmagic.routing.link.master"), true);
						this.setBlockPos(stack, BlockPos.ZERO);
						return ActionResultType.SUCCESS;
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
							player.displayClientMessage(new TranslationTextComponent("chat.bloodmagic.routing.link.master"), true);
							this.setBlockPos(stack, BlockPos.ZERO);
							return ActionResultType.SUCCESS;
						}
					} else
					{
						master.addConnection(pos, containedPos);
						pastNode.addConnection(pos);
						player.displayClientMessage(new TranslationTextComponent("chat.bloodmagic.routing.link.master"), true);
						this.setBlockPos(stack, BlockPos.ZERO);
						return ActionResultType.SUCCESS;
					}
				} else
				{
					// Both nodes are not master nodes, so normal linking
					if (pastNode.getMasterPos().equals(node.getMasterPos()))
					{
						if (!pastNode.getMasterPos().equals(BlockPos.ZERO))
						{
							TileEntity testTile = world.getBlockEntity(pastNode.getMasterPos());
							if (testTile instanceof IMasterRoutingNode)
							{
								IMasterRoutingNode master = (IMasterRoutingNode) testTile;
								master.addConnection(pos, containedPos);
							}
						}
						pastNode.addConnection(pos);
						node.addConnection(containedPos);
						player.displayClientMessage(new TranslationTextComponent("chat.bloodmagic.routing.link"), true);
						this.setBlockPos(stack, BlockPos.ZERO);
						return ActionResultType.SUCCESS;
					} else if (pastNode.getMasterPos().equals(BlockPos.ZERO)) // pastNode is not connected to a
																				// master, but node is
					{
						TileEntity tile = world.getBlockEntity(node.getMasterPos());
						if (tile instanceof IMasterRoutingNode)
						{
							IMasterRoutingNode master = (IMasterRoutingNode) tile;
							master.addConnection(pos, containedPos);
							master.addNodeToList(pastNode);
							pastNode.connectMasterToRemainingNode(world, new LinkedList<>(), master);
						}
						pastNode.addConnection(pos);
						node.addConnection(containedPos);
						player.displayClientMessage(new TranslationTextComponent("chat.bloodmagic.routing.link"), true);
						this.setBlockPos(stack, BlockPos.ZERO);
						return ActionResultType.SUCCESS;
					} else if (node.getMasterPos().equals(BlockPos.ZERO)) // node is not connected to a master, but
																			// pastNode is
					{
						TileEntity tile = world.getBlockEntity(pastNode.getMasterPos());
						if (tile instanceof IMasterRoutingNode)
						{
							IMasterRoutingNode master = (IMasterRoutingNode) tile;
							master.addConnection(pos, containedPos);
							master.addNodeToList(node);
							node.connectMasterToRemainingNode(world, new LinkedList<>(), master);
						}
						pastNode.addConnection(pos);
						node.addConnection(containedPos);
						player.displayClientMessage(new TranslationTextComponent("chat.bloodmagic.routing.link"), true);
						this.setBlockPos(stack, BlockPos.ZERO);
						return ActionResultType.SUCCESS;
					} else
					{
						this.setBlockPos(stack, BlockPos.ZERO);
						return ActionResultType.SUCCESS;
					}
				}
			}
		}

		return ActionResultType.FAIL;
	}

	public BlockPos getBlockPos(ItemStack stack)
	{
		stack = NBTHelper.checkNBT(stack);
		return new BlockPos(stack.getTag().getInt(Constants.NBT.X_COORD), stack.getTag().getInt(Constants.NBT.Y_COORD), stack.getTag().getInt(Constants.NBT.Z_COORD));
	}

	public ItemStack setBlockPos(ItemStack stack, BlockPos pos)
	{
		NBTHelper.checkNBT(stack);
		CompoundNBT itemTag = stack.getTag();
		itemTag.putInt(Constants.NBT.X_COORD, pos.getX());
		itemTag.putInt(Constants.NBT.Y_COORD, pos.getY());
		itemTag.putInt(Constants.NBT.Z_COORD, pos.getZ());
		return stack;
	}
}
