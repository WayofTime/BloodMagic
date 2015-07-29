package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockSpectral extends BlockContainer
{
    public BlockSpectral()
    {
        super(Material.rock);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, BlockPos blockPos, EnumFacing side)
    {
        Block block = p_149646_1_.getBlockState(blockPos).getBlock();

        if (p_149646_1_.getBlockState(blockPos) != p_149646_1_.getBlockState(blockPos.add(-side.getFrontOffsetX(), -side.getFrontOffsetY(), -side.getFrontOffsetZ())))
        {
            return true;
        }

        if (block == this)
        {
            return false;
        }

        return block != this && super.shouldSideBeRendered(p_149646_1_, blockPos, side);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (player.isSneaking())
        {
            return false;
        }

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem() instanceof ItemBlock)
            {
                world.addBlockEvent(blockPos, ((ItemBlock) playerItem.getItem()).getBlock(), playerItem.getItemDamage(), 3);

                if (!player.capabilities.isCreativeMode)
                {
                    playerItem.stackSize--;
                }

                return true;
            } else
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TESpectralBlock();
    }
}
