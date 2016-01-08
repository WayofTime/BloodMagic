package WayofTime.bloodmagic.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.TileSoulForge;
import WayofTime.bloodmagic.tile.TileTeleposer;

public class BlockSoulForge extends BlockContainer
{
    public BlockSoulForge()
    {
        super(Material.iron);

        setUnlocalizedName(Constants.Mod.MODID + ".soulForge");
        setHardness(2.0F);
        setResistance(5.0F);
        setStepSound(soundTypeMetal);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(BloodMagic.tabBloodMagic);
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
    public boolean isVisuallyOpaque()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.getTileEntity(pos) instanceof TileSoulForge)
        {
            player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileTeleposer)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileTeleposer) worldIn.getTileEntity(pos));
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileSoulForge();
    }
}
