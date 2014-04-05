package WayofTime.alchemicalWizardry.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpectralContainer extends BlockContainer
{
    public BlockSpectralContainer(int par1)
    {
        super(par1, Material.cloth);
        //setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setUnlocalizedName("blockSpectralContainer");
        this.setBlockBounds(0,0,0,0,0,0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:BlockBloodLight");
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {

    }

    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

	@Override
	public TileEntity createNewTileEntity(World world) 
	{
		return new TESpectralContainer();
	}
	
	@Override
	public boolean isBlockReplaceable(World par1World, int par2, int par3, int par4)
	{
		return true;
	}
}
