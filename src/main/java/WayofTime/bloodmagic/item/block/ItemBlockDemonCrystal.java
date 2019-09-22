package WayofTime.bloodmagic.item.block;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.tile.TileDemonCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;

public class ItemBlockDemonCrystal extends BlockItem {
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
    public boolean placeBlockAt(ItemStack stack, PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, BlockState newState) {
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
