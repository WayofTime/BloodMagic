package WayofTime.bloodmagic.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.TileIncenseAltar;

public class BlockIncenseAltar extends BlockContainer
{
    public BlockIncenseAltar()
    {
        super(Material.rock);

        setUnlocalizedName(Constants.Mod.MODID + ".incenseAltar");
        setRegistryName(Constants.BloodMagicBlock.INCENSE_ALTAR.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileIncenseAltar();
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        TileIncenseAltar TileIncenseAltar = (TileIncenseAltar) world.getTileEntity(blockPos);
        if (TileIncenseAltar != null)
            TileIncenseAltar.dropItems();

        super.breakBlock(world, blockPos, blockState);
    }
}
