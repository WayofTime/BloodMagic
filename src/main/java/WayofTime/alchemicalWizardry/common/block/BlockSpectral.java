package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSpectral extends BlockContainer
{
    public BlockSpectral()
    {
        super(Material.rock);
        this.setBlockName("spectralBlock");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:SpectralBlock");
    }

    @Override
    public boolean isOpaqueCube()
    {
        Block d;
        return false;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
    {
        Block block = p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_);

        if (p_149646_1_.getBlockMetadata(p_149646_2_, p_149646_3_, p_149646_4_) != p_149646_1_.getBlockMetadata(p_149646_2_ - Facing.offsetsXForSide[p_149646_5_], p_149646_3_ - Facing.offsetsYForSide[p_149646_5_], p_149646_4_ - Facing.offsetsZForSide[p_149646_5_]))
        {
            return true;
        }

        if (block == this)
        {
            return false;
        }

        return block != this && super.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_);
    }

    @SideOnly(Side.CLIENT)
    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
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
                world.setBlock(x, y, z, ((ItemBlock) (playerItem.getItem())).field_150939_a, playerItem.getItemDamage(), 3);

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
