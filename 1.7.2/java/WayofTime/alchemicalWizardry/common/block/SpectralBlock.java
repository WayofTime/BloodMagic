package WayofTime.alchemicalWizardry.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SpectralBlock extends Block
{
    public SpectralBlock()
    {
        super(Material.rock);
        this.setBlockName("spectralBlock");
    }

//	@Override
//	public int tickRate(World par1World)
//    {
//        return 10;
//    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        //if(!par1World.isRemote)
        par1World.setBlockToAir(par2, par3, par4);
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
        return false;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
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
        //TEAltar tileEntity = (TEAltar)world.getBlockTileEntity(x, y, z);
        if (player.isSneaking())
        {
            return false;
        }

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem() instanceof ItemBlock)
            {
                world.setBlock(x, y, z, ((ItemBlock)(playerItem.getItem())).field_150939_a, playerItem.getItemDamage(), 3);

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
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        par1World.scheduleBlockUpdate(par2, par3, par4, this, 100);
    }
}
