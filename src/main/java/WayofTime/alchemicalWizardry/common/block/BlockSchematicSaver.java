package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.common.tileEntity.TESchematicSaver;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockSchematicSaver extends BlockContainer
{
    public BlockSchematicSaver()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TESchematicSaver();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return false;
        }

        TESchematicSaver tileEntity = (TESchematicSaver) world.getTileEntity(blockPos);

        tileEntity.rightClickBlock();

        return false;
    }
}
