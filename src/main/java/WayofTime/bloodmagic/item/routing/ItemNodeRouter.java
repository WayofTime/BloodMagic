package WayofTime.bloodmagic.item.routing;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.iface.INodeRenderer;
import WayofTime.bloodmagic.routing.IMasterRoutingNode;
import WayofTime.bloodmagic.routing.IRoutingNode;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class ItemNodeRouter extends Item implements INodeRenderer, IVariantProvider {
    public ItemNodeRouter() {
        setTranslationKey(BloodMagic.MODID + ".nodeRouter");
        setMaxStackSize(1);
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;
        CompoundNBT tag = stack.getTagCompound();
        BlockPos coords = getBlockPos(stack);

        if (coords != null && tag != null) {
            tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.telepositionFocus.coords", coords.getX(), coords.getY(), coords.getZ()));
        }
    }

    @Override
    public ActionResultType onItemUseFirst(PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            return ActionResultType.PASS;
        }

        TileEntity tileHit = world.getTileEntity(pos);

        if (!(tileHit instanceof IRoutingNode)) {
            // TODO: Remove contained position?
            BlockPos containedPos = getBlockPos(stack);
            if (!containedPos.equals(BlockPos.ORIGIN)) {
                this.setBlockPos(stack, BlockPos.ORIGIN);
                player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.routing.remove"), true);
                return ActionResultType.FAIL;
            }
            return ActionResultType.FAIL;
        }
        IRoutingNode node = (IRoutingNode) tileHit;
        BlockPos containedPos = getBlockPos(stack);
        if (containedPos.equals(BlockPos.ORIGIN)) {
            this.setBlockPos(stack, pos);
            player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.routing.set"), true);
            return ActionResultType.SUCCESS;
        } else {
            TileEntity pastTile = world.getTileEntity(containedPos);
            if (pastTile instanceof IRoutingNode) {
                IRoutingNode pastNode = (IRoutingNode) pastTile;
                if (pastNode instanceof IMasterRoutingNode) {
                    IMasterRoutingNode master = (IMasterRoutingNode) pastNode;

                    if (!node.isMaster(master)) {
                        if (node.getMasterPos().equals(BlockPos.ORIGIN)) //If the node is not the master and it is receptive
                        {
                            node.connectMasterToRemainingNode(world, new LinkedList<>(), master);
                            master.addConnection(pos, containedPos);
                            master.addNodeToList(node);
                            node.addConnection(containedPos);
                            player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.routing.link.master"), true);
                            this.setBlockPos(stack, BlockPos.ORIGIN);
                            return ActionResultType.SUCCESS;
                        }
                    } else {
                        master.addConnection(pos, containedPos);
                        node.addConnection(containedPos);
                        player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.routing.link.master"), true);
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return ActionResultType.SUCCESS;
                    }

                } else if (node instanceof IMasterRoutingNode) {
                    IMasterRoutingNode master = (IMasterRoutingNode) node;

                    if (!pastNode.isMaster(master)) {
                        if (pastNode.getMasterPos().equals(BlockPos.ORIGIN)) //TODO: This is where the issue is
                        {
                            pastNode.connectMasterToRemainingNode(world, new LinkedList<>(), master);
                            master.addConnection(pos, containedPos);
                            pastNode.addConnection(pos);
                            master.addNodeToList(pastNode);
                            player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.routing.link.master"), true);
                            this.setBlockPos(stack, BlockPos.ORIGIN);
                            return ActionResultType.SUCCESS;
                        }
                    } else {
                        master.addConnection(pos, containedPos);
                        pastNode.addConnection(pos);
                        player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.routing.link.master"), true);
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return ActionResultType.SUCCESS;
                    }
                } else {
                    //Both nodes are not master nodes, so normal linking
                    if (pastNode.getMasterPos().equals(node.getMasterPos())) {
                        if (!pastNode.getMasterPos().equals(BlockPos.ORIGIN)) {
                            TileEntity testTile = world.getTileEntity(pastNode.getMasterPos());
                            if (testTile instanceof IMasterRoutingNode) {
                                IMasterRoutingNode master = (IMasterRoutingNode) testTile;
                                master.addConnection(pos, containedPos);
                            }
                        }
                        pastNode.addConnection(pos);
                        node.addConnection(containedPos);
                        player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.routing.link.master"), true);
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return ActionResultType.SUCCESS;
                    } else if (pastNode.getMasterPos().equals(BlockPos.ORIGIN)) //pastNode is not connected to a master, but node is
                    {
                        TileEntity tile = world.getTileEntity(node.getMasterPos());
                        if (tile instanceof IMasterRoutingNode) {
                            IMasterRoutingNode master = (IMasterRoutingNode) tile;
                            master.addConnection(pos, containedPos);
                            master.addNodeToList(pastNode);
                            pastNode.connectMasterToRemainingNode(world, new LinkedList<>(), master);
                        }
                        pastNode.addConnection(pos);
                        node.addConnection(containedPos);
                        player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.routing.link"), true);
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return ActionResultType.SUCCESS;
                    } else if (node.getMasterPos().equals(BlockPos.ORIGIN)) //node is not connected to a master, but pastNode is
                    {
                        TileEntity tile = world.getTileEntity(pastNode.getMasterPos());
                        if (tile instanceof IMasterRoutingNode) {
                            IMasterRoutingNode master = (IMasterRoutingNode) tile;
                            master.addConnection(pos, containedPos);
                            master.addNodeToList(node);
                            node.connectMasterToRemainingNode(world, new LinkedList<>(), master);
                        }
                        pastNode.addConnection(pos);
                        node.addConnection(containedPos);
                        player.sendStatusMessage(new TranslationTextComponent("chat.bloodmagic.routing.link"), true);
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return ActionResultType.SUCCESS;
                    } else {
                        this.setBlockPos(stack, BlockPos.ORIGIN);
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }

        return ActionResultType.FAIL;
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "type=normal");
    }

    public BlockPos getBlockPos(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);
        return new BlockPos(stack.getTagCompound().getInteger(Constants.NBT.X_COORD), stack.getTagCompound().getInteger(Constants.NBT.Y_COORD), stack.getTagCompound().getInteger(Constants.NBT.Z_COORD));
    }

    public ItemStack setBlockPos(ItemStack stack, BlockPos pos) {
        NBTHelper.checkNBT(stack);
        CompoundNBT itemTag = stack.getTagCompound();
        itemTag.putInt(Constants.NBT.X_COORD, pos.getX());
        itemTag.putInt(Constants.NBT.Y_COORD, pos.getY());
        itemTag.putInt(Constants.NBT.Z_COORD, pos.getZ());
        return stack;
    }
}
