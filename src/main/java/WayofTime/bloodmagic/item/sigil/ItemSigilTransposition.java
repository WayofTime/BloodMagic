package WayofTime.bloodmagic.item.sigil;

import WayofTime.bloodmagic.api.impl.BloodMagicAPI;
import WayofTime.bloodmagic.util.BlockStack;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.iface.ISigil;
import WayofTime.bloodmagic.util.helper.NBTHelper;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import WayofTime.bloodmagic.util.helper.PlayerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

        if (tag.hasKey(Constants.NBT.CONTAINED_BLOCK_NAME) && tag.hasKey(Constants.NBT.CONTAINED_BLOCK_META)) {
            tooltip.add(" ");
            BlockStack blockStack = new BlockStack(Block.getBlockFromName(tag.getString(Constants.NBT.CONTAINED_BLOCK_NAME)), tag.getByte(Constants.NBT.CONTAINED_BLOCK_META));
            tooltip.add(blockStack.getItemStack().getDisplayName());
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        stack = NBTHelper.checkNBT(stack);
        NBTTagCompound tag = stack.getTagCompound();

        if (tag.hasKey(Constants.NBT.CONTAINED_BLOCK_NAME) && tag.hasKey(Constants.NBT.CONTAINED_BLOCK_META)) {
            BlockStack blockStack = new BlockStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString(Constants.NBT.CONTAINED_BLOCK_NAME))), tag.getByte(Constants.NBT.CONTAINED_BLOCK_META));
            if (blockStack.getItemStack() != null && blockStack.getItemStack().getItem() != null) //TODO: Figure out why it's a null item. This is a patchwork solution.
            {
                return super.getItemStackDisplayName(stack) + " (" + blockStack.getItemStack().getDisplayName() + ")";
            }

        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() instanceof ISigil.Holding)
            stack = ((Holding) stack.getItem()).getHeldItem(stack, player);
        if (PlayerHelper.isFakePlayer(player))
            return EnumActionResult.FAIL;

        stack = NBTHelper.checkNBT(stack);

        IBlockState state = world.getBlockState(blockPos);
        if (!world.isRemote) {
            if (BloodMagicAPI.INSTANCE.getBlacklist().getTransposition().contains(state))
                return EnumActionResult.FAIL;

            if (player.isSneaking() && (!stack.getTagCompound().hasKey(Constants.NBT.CONTAINED_BLOCK_NAME) || !stack.getTagCompound().hasKey(Constants.NBT.CONTAINED_BLOCK_META))) {
                if (state.getPlayerRelativeBlockHardness(player, world, blockPos) >= 0 && state.getBlockHardness(world, blockPos) >= 0) {
                    int cost = getLpUsed();

                    NBTTagCompound tileNBTTag = new NBTTagCompound();
                    String blockName = state.getBlock().getRegistryName().toString();
                    byte metadata = (byte) state.getBlock().getMetaFromState(state);

                    if (world.getTileEntity(blockPos) != null) {
                        cost *= 5;
                        world.getTileEntity(blockPos).writeToNBT(tileNBTTag);

                        if (world.getTileEntity(blockPos) instanceof TileEntityMobSpawner) {
                            cost *= 6;
                        }
                    }

                    stack.getTagCompound().setString(Constants.NBT.CONTAINED_BLOCK_NAME, blockName);
                    stack.getTagCompound().setByte(Constants.NBT.CONTAINED_BLOCK_META, metadata);
                    stack.getTagCompound().setTag(Constants.NBT.CONTAINED_TILE_ENTITY, tileNBTTag);

                    NetworkHelper.getSoulNetwork(getOwnerUUID(stack)).syphonAndDamage(player, cost);

                    world.removeTileEntity(blockPos);
                    world.setBlockToAir(blockPos);

                    return EnumActionResult.SUCCESS;
                }
            } else if (stack.getTagCompound().hasKey(Constants.NBT.CONTAINED_BLOCK_NAME) && stack.getTagCompound().hasKey(Constants.NBT.CONTAINED_BLOCK_META)) {
                IBlockState iblockstate = world.getBlockState(blockPos);
                Block block = iblockstate.getBlock();
                BlockStack blockToPlace = new BlockStack(Block.getBlockFromName(stack.getTagCompound().getString(Constants.NBT.CONTAINED_BLOCK_NAME)), stack.getTagCompound().getByte(Constants.NBT.CONTAINED_BLOCK_META));

                if (!block.isReplaceable(world, blockPos)) {
                    blockPos = blockPos.offset(side);
                }

                if (!stack.isEmpty() && player.canPlayerEdit(blockPos, side, stack) && world.mayPlace(blockToPlace.getBlock(), blockPos, false, side, player)) {
                    if (world.setBlockState(blockPos, blockToPlace.getState(), 3)) {
                        blockToPlace.getBlock().onBlockPlacedBy(world, blockPos, blockToPlace.getState(), player, blockToPlace.getItemStack());
//                        world.playSound((double) ((float) blockPos.getX() + 0.5F), (double) ((float) blockPos.getY() + 0.5F), (double) ((float) blockPos.getZ() + 0.5F), blockToPlace.getBlock().getStepSound().getPlaceSound(), (blockToPlace.getBlock().getStepSound().getVolume() + 1.0F) / 2.0F, blockToPlace.getBlock().getStepSound().getPitch() * 0.8F);

                        if (stack.getTagCompound().hasKey(Constants.NBT.CONTAINED_TILE_ENTITY) && blockToPlace.getBlock().hasTileEntity(blockToPlace.getState())) {
                            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(Constants.NBT.CONTAINED_TILE_ENTITY);
                            tag.setInteger("x", blockPos.getX());
                            tag.setInteger("y", blockPos.getY());
                            tag.setInteger("z", blockPos.getZ());
                            world.getTileEntity(blockPos).readFromNBT(tag);
                        }
                        world.notifyBlockUpdate(blockPos, state, state, 3);

                        stack.getTagCompound().removeTag(Constants.NBT.CONTAINED_BLOCK_NAME);
                        stack.getTagCompound().removeTag(Constants.NBT.CONTAINED_BLOCK_META);
                        stack.getTagCompound().removeTag(Constants.NBT.CONTAINED_TILE_ENTITY);

                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return EnumActionResult.FAIL;
    }
}
