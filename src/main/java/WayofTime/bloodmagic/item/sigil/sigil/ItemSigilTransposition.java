package WayofTime.bloodmagic.item.sigil.sigil;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api_impl.BloodMagicAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemSigilTransposition extends ItemSigil {

    public ItemSigilTransposition() {
        super(new SigilTransposition(), "transposition");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if (!stack.hasTagCompound())
            return;

        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey("stored"))
            return;

        IBlockState state = NBTUtil.readBlockState(tag.getCompoundTag("stored"));
        ItemStack blockStack = new ItemStack(state.getBlock().getItemDropped(state, null, 0), 1, state.getBlock().damageDropped(state));
        tooltip.add(blockStack.isEmpty() ? I18n.format(state.getBlock().getUnlocalizedName()) : blockStack.getDisplayName());
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (!stack.hasTagCompound())
            return super.getItemStackDisplayName(stack);

        NBTTagCompound tag = stack.getTagCompound();
        if (!tag.hasKey("stored"))
            return super.getItemStackDisplayName(stack);

        IBlockState state = NBTUtil.readBlockState(tag.getCompoundTag("stored"));
        ItemStack blockStack = new ItemStack(state.getBlock().getItemDropped(state, null, 0), 1, state.getBlock().damageDropped(state));
        String blockName = blockStack.isEmpty() ? net.minecraft.util.text.translation.I18n.translateToLocal(state.getBlock().getUnlocalizedName()) : blockStack.getDisplayName();

        return super.getItemStackDisplayName(stack) + " (" + blockName + ")";
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("stored"))
            return null;

        NBTTagCompound justTheState = stack.getTagCompound().copy();
        justTheState.getCompoundTag("stored").removeTag("tile");
        return justTheState;
    }

    public static class SigilTransposition implements ISigil {

        @Override
        public int getCost() {
            return 1000;
        }

        @Override
        public EnumActionResult onInteract(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nonnull EnumHand hand) {
            IBlockState state = world.getBlockState(pos);
            if (world.isRemote || BloodMagicAPI.INSTANCE.getBlacklist().getTransposition().contains(state))
                return EnumActionResult.FAIL;

            NBTTagCompound tag = stack.getTagCompound();
            if (player.isSneaking() && (!stack.hasTagCompound() || !tag.hasKey("stored"))) {
                if (state.getPlayerRelativeBlockHardness(player, world, pos) >= 0 && state.getBlockHardness(world, pos) >= 0) {
                    int cost = getCost();

                    NBTTagCompound stored = new NBTTagCompound();
                    NBTUtil.writeBlockState(stored, state);

                    TileEntity tile = world.getTileEntity(pos);
                    if (tile != null) {
                        NBTTagCompound tileTag = new NBTTagCompound();
                        tile.writeToNBT(tileTag);
                        stored.setTag("tile", tileTag);
                        cost *= 5;

                        if (tile instanceof TileEntityMobSpawner)
                            cost *= 6;
                    }

                    tag.setTag("stored", stored);

                    NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).syphonAndDamage(player, cost);
                    world.setBlockToAir(pos);
                    world.removeTileEntity(pos);
                }
            } else if (stack.hasTagCompound() && stack.getTagCompound().hasKey("stored")) {
                IBlockState placeAtState = world.getBlockState(pos);

                // If the block we clicked isn't replaceable, try to offset to the side we clicked on
                if (!placeAtState.getBlock().isReplaceable(world, pos))
                    placeAtState = world.getBlockState(pos = pos.offset(side));

                // If we still can't place here, just cancel out.
                if (!placeAtState.getBlock().isReplaceable(world, pos))
                    return EnumActionResult.FAIL;

                NBTTagCompound stored = stack.getTagCompound().getCompoundTag("stored");
                IBlockState toPlace = NBTUtil.readBlockState(stored);
                if (player.canPlayerEdit(pos, side, stack) && world.mayPlace(toPlace.getBlock(), pos, false, side, player)) {
                    ItemStack placeStack = new ItemStack(state.getBlock().getItemDropped(state, null, 0), 1, state.getBlock().damageDropped(state));
                    toPlace.getBlock().onBlockPlacedBy(world, pos, toPlace, player, placeStack);

                    if (stored.hasKey("tile")) {
                        NBTTagCompound tileTag = stored.getCompoundTag("tile");
                        tileTag.setInteger("x", pos.getX());
                        tileTag.setInteger("y", pos.getY());
                        tileTag.setInteger("z", pos.getZ());
                        world.getTileEntity(pos).readFromNBT(tileTag);
                    }

                    world.notifyBlockUpdate(pos, state, state, 3);

                    stack.getTagCompound().removeTag("stored");
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }
}
