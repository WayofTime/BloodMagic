package WayofTime.alchemicalWizardry.common.demonVillage.tileEntity;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;

public class BlockDemonChest extends BlockChest implements IBlockPortalNode
{
	public BlockDemonChest() 
	{
		super(0);
		this.setHardness(2.5F).setStepSound(soundTypeWood).setUnlocalizedName("demonChest");
		this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}
	
	@Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TEDemonChest();
    }
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TEDemonChest)
		{
			((TEDemonChest) tile).notifyPortalOfInteraction();
		}
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return true;
	}
}
