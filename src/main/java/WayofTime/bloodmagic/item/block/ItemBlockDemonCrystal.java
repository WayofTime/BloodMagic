package WayofTime.bloodmagic.item.block;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.tile.TileDemonCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;

public class ItemBlockDemonCrystal extends ItemBlock {
    public ItemBlockDemonCrystal(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack) + EnumDemonWillType.values()[stack.getItemDamage()].toString().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState)) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileDemonCrystal) {
                ((TileDemonCrystal) tile).setPlacement(side);
            }

            return true;
        }

        return false;
    }
}
