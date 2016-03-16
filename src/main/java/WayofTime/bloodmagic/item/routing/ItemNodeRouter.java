package WayofTime.bloodmagic.item.routing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import WayofTime.bloodmagic.api.iface.INodeRenderer;
import WayofTime.bloodmagic.client.IVariantProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.routing.IMasterRoutingNode;
import WayofTime.bloodmagic.routing.IRoutingNode;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.helper.TextHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ItemNodeRouter extends Item implements INodeRenderer, IVariantProvider
{
    public ItemNodeRouter()
    {
        setUnlocalizedName(Constants.Mod.MODID + ".nodeRouter");
        setRegistryName(Constants.BloodMagicItem.NODE_ROUTER.getRegName());
        setMaxStackSize(1);
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        stack = NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();
        BlockPos coords = getBlockPos(stack);

        if (coords != null && tag != null)
        {
            tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.telepositionFocus.coords", coords.getX(), coords.getY(), coords.getZ()));
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }

        TileEntity tileHit = world.getTileEntity(pos);

        if (!(tileHit instanceof IRoutingNode))
        {
            // TODO: Remove contained position?
            BlockPos containedPos = getBlockPos(stack);
            if (!containedPos.equals(BlockPos.ORIGIN))
            {
                this.setBlockPos(stack, BlockPos.ORIGIN);
                ChatUtil.sendChat(player, "Removing contained location");
                return false;
            }
            return false;
        }
        IRoutingNode node = (IRoutingNode) tileHit;
        BlockPos containedPos = getBlockPos(stack);
        if (containedPos.equals(BlockPos.ORIGIN))
        {
            this.setBlockPos(stack, pos);
            ChatUtil.sendChat(player, "Setting node location");
            return true;
        } else
        {
            TileEntity pastTile = world.getTileEntity(containedPos);
            if (pastTile instanceof IRoutingNode)
            {
                IRoutingNode pastNode = (IRoutingNode) pastTile;
                if (pastNode instanceof IMasterRoutingNode)
                {
                    IMasterRoutingNode master = (IMasterRoutingNode) pastNode;

                    if (!node.isMaster(master))
                    {
                        if (node.getMasterPos().equals(BlockPos.ORIGIN))
                        {
                            node.connectMasterToRemainingNode(world, new LinkedList<BlockPos>(), master);
                            master.addConnection(pos, containedPos);
                            master.addNodeToList(node);
                            node.addConnection(containedPos);
                            ChatUtil.sendChat(player, "Linked node to master!");
                            this.setBlockPos(stack, BlockPos.ORIGIN);
                            return true;
                        }
                    } else
                    {
                        master.addConnection(pos, containedPos);
                        node.addConnection(containedPos);
                        ChatUtil.sendChat(player, "Linked node to master!");
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return true;
                    }

                } else if (node instanceof IMasterRoutingNode)
                {
                    IMasterRoutingNode master = (IMasterRoutingNode) node;

                    if (!pastNode.isMaster(master))
                    {
                        if (pastNode.getMasterPos().equals(BlockPos.ORIGIN))
                        {
                            pastNode.connectMasterToRemainingNode(world, new LinkedList<BlockPos>(), master);
                            master.addConnection(pos, containedPos);
                            pastNode.addConnection(pos);
                            master.addNodeToList(pastNode);
                            ChatUtil.sendChat(player, "Linked node to master!");
                            this.setBlockPos(stack, BlockPos.ORIGIN);
                            return true;
                        }
                    } else
                    {
                        master.addConnection(pos, containedPos);
                        pastNode.addConnection(pos);
                        ChatUtil.sendChat(player, "Linked node to master!");
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return true;
                    }
                } else
                {
                    //Both nodes are not master nodes, so normal linking
                    if (pastNode.getMasterPos().equals(node.getMasterPos()))
                    {
                        if (!pastNode.getMasterPos().equals(BlockPos.ORIGIN))
                        {
                            TileEntity testTile = world.getTileEntity(pastNode.getMasterPos());
                            if (testTile instanceof IMasterRoutingNode)
                            {
                                IMasterRoutingNode master = (IMasterRoutingNode) testTile;
                                master.addConnection(pos, containedPos);
                            }
                        }
                        pastNode.addConnection(pos);
                        node.addConnection(containedPos);
                        ChatUtil.sendChat(player, "Linked nodes together.");
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return true;
                    } else if (pastNode.getMasterPos().equals(BlockPos.ORIGIN)) //pastNode is not connected to a master, but node is
                    {
                        TileEntity tile = world.getTileEntity(node.getMasterPos());
                        if (tile instanceof IMasterRoutingNode)
                        {
                            IMasterRoutingNode master = (IMasterRoutingNode) tile;
                            master.addConnection(pos, containedPos);
                            master.addNodeToList(pastNode);
                            pastNode.connectMasterToRemainingNode(world, new LinkedList<BlockPos>(), master);
                        }
                        pastNode.addConnection(pos);
                        node.addConnection(containedPos);
                        ChatUtil.sendChat(player, "Linked nodes together.");
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return true;
                    } else if (node.getMasterPos().equals(BlockPos.ORIGIN)) //node is not connected to a master, but pastNode is
                    {
                        TileEntity tile = world.getTileEntity(pastNode.getMasterPos());
                        if (tile instanceof IMasterRoutingNode)
                        {
                            IMasterRoutingNode master = (IMasterRoutingNode) tile;
                            master.addConnection(pos, containedPos);
                            master.addNodeToList(node);
                            node.connectMasterToRemainingNode(world, new LinkedList<BlockPos>(), master);
                        }
                        pastNode.addConnection(pos);
                        node.addConnection(containedPos);
                        ChatUtil.sendChat(player, "Linked nodes together.");
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return true;
                    } else
                    {
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "type=normal"));
        return ret;
    }

    public BlockPos getBlockPos(ItemStack stack)
    {
        stack = NBTHelper.checkNBT(stack);
        return new BlockPos(stack.getTagCompound().getInteger(Constants.NBT.X_COORD), stack.getTagCompound().getInteger(Constants.NBT.Y_COORD), stack.getTagCompound().getInteger(Constants.NBT.Z_COORD));
    }

    public ItemStack setBlockPos(ItemStack stack, BlockPos pos)
    {
        NBTHelper.checkNBT(stack);
        NBTTagCompound itemTag = stack.getTagCompound();
        itemTag.setInteger(Constants.NBT.X_COORD, pos.getX());
        itemTag.setInteger(Constants.NBT.Y_COORD, pos.getY());
        itemTag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
        return stack;
    }
}
