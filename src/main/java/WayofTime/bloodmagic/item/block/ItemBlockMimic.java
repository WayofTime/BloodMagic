package WayofTime.bloodmagic.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.tile.TileMimic;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.item.block.base.ItemBlockEnum;

public class ItemBlockMimic extends ItemBlockEnum {
    public ItemBlockMimic(BlockEnum block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public ActionResultType onItemUse(PlayerEntity player, World world, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        //If not sneaking, do normal item use
        if (!player.isSneaking()) {
            return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        }

        //IF sneaking and player has permission, replace the targeted block
        if (player.canPlayerEdit(pos, facing, stack)) {
            //Store information about the block being replaced and its appropriate itemstack
            BlockState replacedBlockstate = world.getBlockState(pos);
            Block replacedBlock = replacedBlockstate.getBlock();
            ItemStack replacedStack = replacedBlock.getItem(world, pos, replacedBlockstate);

            //Get the state for the mimic
            BlockState mimicBlockstate = this.getBlock().getStateFromMeta(stack.getMetadata());


            //Check if the block can be replaced

            if (!canReplaceBlock(world, pos, replacedBlockstate)) {
                return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
            }

            //Check if the tile entity, if any, can be replaced
            TileEntity tileReplaced = world.getTileEntity(pos);
            if (!canReplaceTile(tileReplaced)) {
                return ActionResultType.FAIL;
            }

            //If tile can be replaced, store info about the tile
            CompoundNBT tileTag = getTagFromTileEntity(tileReplaced);
            if (tileReplaced != null) {
                CompoundNBT voidTag = new CompoundNBT();
                voidTag.putInt("x", pos.getX());
                voidTag.putInt("y", pos.getY());
                voidTag.putInt("z", pos.getZ());
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
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;

    }

    public boolean canReplaceTile(TileEntity tile) {
        if (tile instanceof ChestTileEntity) {
            return true;
        }

        return tile == null;
    }

    public boolean canReplaceBlock(World world, BlockPos pos, BlockState state) {
        return state.getBlockHardness(world, pos) != -1.0F;
    }

    public CompoundNBT getTagFromTileEntity(TileEntity tile) {
        CompoundNBT tag = new CompoundNBT();

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
