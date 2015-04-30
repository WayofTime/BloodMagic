package WayofTime.alchemicalWizardry.common.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TECrucible;

public class BlockCrucible extends BlockContainer
{
	public BlockCrucible() 
	{
		super(Material.anvil);
		this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		this.setBlockName("blockCrucible");
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.8F, 0.6F);        
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
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TECrucible();
	}
	
	@Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        if (rand.nextInt(3) != 0)
        {
        	TECrucible tile = (TECrucible)world.getTileEntity(x, y, z);
            tile.spawnClientParticle(world, x, y, z, rand);
        }
    }
}
