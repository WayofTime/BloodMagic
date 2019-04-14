package WayofTime.bloodmagic.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.block.BlockMimic;
import WayofTime.bloodmagic.tile.TileMimic;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.item.block.base.ItemBlockEnum;

import WayofTime.bloodmagic.util.ChatUtil;

public class ItemBlockMimic extends ItemBlockEnum {
    public ItemBlockMimic(BlockEnum block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        //If not sneaking, do normal item use
        if (!player.isSneaking()) {
            return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        }

        //IF sneaking and player has permission, replace the targeted block
        if (player.canPlayerEdit(pos, facing, stack)) {
            //Store information about the block being replaced and its appropriate itemstack
            IBlockState replacedBlockstate = world.getBlockState(pos);
            Block replacedBlock = replacedBlockstate.getBlock();
            ItemStack replacedStack = replacedBlock.getItem(world, pos, replacedBlockstate);

            //Get the state for the mimic
            IBlockState mimicBlockstate = this.getBlock().getStateFromMeta(stack.getMetadata());


            //Check if the block can be replaced

            if (!canReplaceBlock(world, pos, replacedBlockstate)) {
                return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
            }

            //Check if the tile entity, if any, can be replaced
            TileEntity tileReplaced = world.getTileEntity(pos);
            if (!canReplaceTile(tileReplaced)) {
                return EnumActionResult.FAIL;
            }

            //If tile can be replaced, store info about the tile
            NBTTagCompound tileTag = getTagFromTileEntity(tileReplaced);
            if (tileReplaced != null) {
                NBTTagCompound voidTag = new NBTTagCompound();
                voidTag.setInteger("x", pos.getX());
                voidTag.setInteger("y", pos.getY());
                voidTag.setInteger("z", pos.getZ());
                tileReplaced.readFromNBT(voidTag);
            }

            //Remove one item from stack
            stack.shrink(1);


            //Replace the block
            world.setBlockState(pos, mimicBlockstate, 3);
            //Make placing sound
            SoundType soundtype = this.block.getSoundType();
            world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

            //Replace the tile entity
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileMimic) {
                TileMimic mimic = (TileMimic) tile;
                mimic.tileTag = tileTag;
                mimic.setReplacedState(replacedBlockstate);
                mimic.setInventorySlotContents(0, replacedStack);
                mimic.refreshTileEntity();

                if (player.capabilities.isCreativeMode) {
                    mimic.dropItemsOnBreak = false;
                }
            }
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;

    }

    public boolean canReplaceTile(TileEntity tile) {
        if (tile instanceof TileEntityChest) {
            return true;
        }

        return tile == null;
    }

    public boolean canReplaceBlock(World world, BlockPos pos, IBlockState state) {
        return state.getBlockHardness(world, pos) != -1.0F;
    }

    public NBTTagCompound getTagFromTileEntity(TileEntity tile) {
        NBTTagCompound tag = new NBTTagCompound();

        if (tile != null) {
            return tile.writeToNBT(tag);
        }

        return tag;
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}
