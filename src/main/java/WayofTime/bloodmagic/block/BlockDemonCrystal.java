package WayofTime.bloodmagic.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;

public class BlockDemonCrystal extends Block
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 6);

    public BlockDemonCrystal()
    {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));

        setUnlocalizedName(Constants.Mod.MODID + ".demonCrystal");
        setRegistryName(Constants.BloodMagicBlock.DEMON_CRYSTAL.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
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

//    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
//    {
//        return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
//    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer) state.getValue(AGE)).intValue();
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] { AGE });
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }

        int meta = getMetaFromState(state);
        int nextMeta = Math.min(meta + 1, 6);
        world.setBlockState(pos, this.getStateFromMeta(nextMeta));

        return true;
    }

//    @Override
//    public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
//    {
//        java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
//        int age = ((Integer) state.getValue(AGE)).intValue();
//        Random rand = world instanceof World ? ((World) world).rand : new Random();
//
//        if (age >= 7)
//        {
//            int k = 3 + fortune;
//
//            for (int i = 0; i < 3 + fortune; ++i)
//            {
//                if (rand.nextInt(15) <= age)
//                {
//                    ret.add(new ItemStack(this.getSeed(), 1, 0));
//                }
//            }
//        }
//        return ret;
//    }
}