package WayofTime.bloodmagic.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.block.BlockInversionPillar;
import WayofTime.bloodmagic.registry.ModBlocks;

public class ItemBlockInversionPillar extends ItemBlock
{
    public ItemBlockInversionPillar(Block block)
    {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
        if (!isBlockPillarBase(world.getBlockState(pos.offset(EnumFacing.DOWN)), newState) || !isBlockPillarCap(world.getBlockState(pos.offset(EnumFacing.UP)), newState))
        {
            return false;
        }

        return super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
    }

    public boolean isBlockPillarBase(IBlockState baseState, IBlockState newState)
    {
        Block block = baseState.getBlock();
        if (block == ModBlocks.INVERSION_PILLAR_END)
        {
            int baseMeta = block.getMetaFromState(baseState);
            int pillarMeta = newState.getBlock().getMetaFromState(newState);

            if (pillarMeta == baseMeta / 2 && baseMeta % 2 == 0)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isBlockPillarCap(IBlockState capState, IBlockState newState)
    {
        Block block = capState.getBlock();
        if (block == ModBlocks.INVERSION_PILLAR_END)
        {
            int capMeta = block.getMetaFromState(capState);
            int pillarMeta = newState.getBlock().getMetaFromState(newState);

            if (pillarMeta == capMeta / 2 && capMeta % 2 == 1)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + BlockInversionPillar.names[stack.getItemDamage()];
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }
}
