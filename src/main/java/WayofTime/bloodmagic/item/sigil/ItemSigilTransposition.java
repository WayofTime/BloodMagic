package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSigilTransposition extends ItemSigilBase {

    public ItemSigilTransposition() {
        super("transposition", 1000);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);

        if (!stack.hasTagCompound())
            return;
        NBTTagCompound tag = stack.getTagCompound();

        if (tag.hasKey("stored")) {
            tooltip.add(" ");
            tooltip.add(tag.getCompoundTag("stored").getString("display"));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (!stack.hasTagCompound())
            return super.getItemStackDisplayName(stack);

        NBTTagCompound tag = stack.getTagCompound();
        if (tag.hasKey("stored"))
            return super.getItemStackDisplayName(stack) + " (" + tag.getCompoundTag("stored").getString("display") + ")";

        return super.getItemStackDisplayName(stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return EnumActionResult.FAIL;

        IBlockState state = world.getBlockState(blockPos);
        if (!world.isRemote) {
            if (BloodMagicAPI.INSTANCE.getBlacklist().getTransposition().contains(state))
                return EnumActionResult.FAIL;

            if (player.isSneaking() && stack.hasTagCompound() && !stack.getTagCompound().hasKey("stored")) {
                if (state.getPlayerRelativeBlockHardness(player, world, blockPos) >= 0 && state.getBlockHardness(world, blockPos) >= 0) {
                    int cost = getLpUsed();

                    NBTTagCompound stored = new NBTTagCompound();
                    stored.setTag("state", NBTUtil.writeBlockState(new NBTTagCompound(), state));
                    stored.setString("display", state.getBlock().getPickBlock(state, null, world, blockPos, player).getDisplayName());
                    if (state.getBlock().hasTileEntity(state)) {
                        TileEntity tile = world.getTileEntity(blockPos);
                        if (tile != null) {
                            cost *= 5;
                            stored.setTag("tileData", tile.writeToNBT(new NBTTagCompound()));

                            if (world.getTileEntity(blockPos) instanceof TileEntityMobSpawner)
                                cost *= 6;
                        }
                    }

                    stack.getTagCompound().setTag("stored", stored);
                    NetworkHelper.getSoulNetwork(getBinding(stack)).syphonAndDamage(player, SoulTicket.ITEM(stack, world, player, cost));
                    world.removeTileEntity(blockPos);
                    world.setBlockToAir(blockPos);
                    return EnumActionResult.SUCCESS;
                }
            } else if (stack.hasTagCompound() && stack.getTagCompound().hasKey("stored")) {
                IBlockState worldState = world.getBlockState(blockPos);
                NBTTagCompound storedTag = stack.getTagCompound().getCompoundTag("stored");
                IBlockState storedState = NBTUtil.readBlockState(storedTag.getCompoundTag("state"));
                NBTTagCompound tileData = storedTag.hasKey("tileData") ? storedTag.getCompoundTag("tileData") : null;

                if (!worldState.getBlock().isReplaceable(world, blockPos))
                    blockPos = blockPos.offset(side);

                if (!stack.isEmpty() && player.canPlayerEdit(blockPos, side, stack) && world.mayPlace(storedState.getBlock(), blockPos, false, side, player)) {
                    if (world.setBlockState(blockPos, storedState, 3)) {
                        storedState.getBlock().onBlockPlacedBy(world, blockPos, storedState, player, ItemStack.EMPTY);

                        if (tileData != null) {
                            tileData.setInteger("x", blockPos.getX());
                            tileData.setInteger("y", blockPos.getY());
                            tileData.setInteger("z", blockPos.getZ());
                            TileEntity worldTile = world.getTileEntity(blockPos);
                            if (worldTile != null)
                                worldTile.readFromNBT(tileData);
                        }

                        world.notifyBlockUpdate(blockPos, state, state, 3);
                        stack.getTagCompound().removeTag("stored");
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return EnumActionResult.FAIL;
    }

    // We only want to send the display name to the client rather than the bloated tag with tile data and such
    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("stored"))
            return super.getNBTShareTag(stack);

        NBTTagCompound shareTag = stack.getTagCompound().copy();
        NBTTagCompound storedTag = shareTag.getCompoundTag("stored");
        storedTag.removeTag("state");
        storedTag.removeTag("stored");

        return shareTag;
    }
}
