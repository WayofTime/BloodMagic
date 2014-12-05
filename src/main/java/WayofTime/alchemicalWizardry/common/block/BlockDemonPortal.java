package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class BlockDemonPortal extends BlockContainer
{
    public BlockDemonPortal()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("demonPortal");
    }
    
    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
    {
    	TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TEDemonPortal)
		{
			((TEDemonPortal) tile).notifyPortalOfBreak();
		}
		
    	super.onBlockHarvested(world, x, y, z, meta, player);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TEDemonPortal();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float what, float these, float are)
    {
        if (world.isRemote)
        {
            return false;
        }

        TEDemonPortal tileEntity = (TEDemonPortal) world.getTileEntity(x, y, z);

        tileEntity.rightClickBlock(player, side);

        return false;
    }
}
