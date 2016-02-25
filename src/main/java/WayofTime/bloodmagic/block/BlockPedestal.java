package WayofTime.bloodmagic.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockStringContainer;
import WayofTime.bloodmagic.tile.TilePlinth;
import WayofTime.bloodmagic.util.Utils;

public class BlockPedestal extends BlockStringContainer
{
    public static String[] names = { "pedestal", "plinth" };

    public BlockPedestal()
    {
        super(Material.rock, names);

        setUnlocalizedName(Constants.Mod.MODID + ".");
        setRegistryName(Constants.BloodMagicBlock.PEDESTAL.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        switch (getMetaFromState(state))
        {
        case 0:
        {
            // TileEntity plinth = world.getTileEntity(pos);
            //
            // if (plinth!= null && plinth instanceof TilePlinth) {
            // Utils.insertItemToTile((TilePlinth) plinth, player);
            // }
        }

        case 1:
        {
            TileEntity plinth = world.getTileEntity(pos);

            if (plinth == null || player.isSneaking())
                return false;

            if (plinth instanceof TilePlinth)
            {
                Utils.insertItemToTile((TilePlinth) plinth, player);
                return true;
            }
        }
        }

        world.markBlockForUpdate(pos);
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, BlockPos pos)
    {
        IBlockState state = blockAccess.getBlockState(pos);

        if (getMetaFromState(state) == 0)
            setBlockBounds(0.5F - 0.3125F, 0.0F, 0.5F - 0.3125F, 0.5F + 0.3125F, 0.6F, 0.5F + 0.3125F);
        else if (getMetaFromState(state) == 1)
            setBlockBounds(0.1F, 0.0F, 0.1F, 1.0F - 0.1F, 0.8F, 1.0F - 0.1F);

    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return meta == 0 ? null : new TilePlinth();
    }
}
