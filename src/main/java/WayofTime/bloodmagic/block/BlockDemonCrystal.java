package WayofTime.bloodmagic.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.IAltarManipulator;
import WayofTime.bloodmagic.api.iface.IAltarReader;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.api.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.item.ItemComponent;
import WayofTime.bloodmagic.item.ItemDemonCrystal;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.tile.TileDemonCrystal;
import WayofTime.bloodmagic.util.Utils;

public class BlockDemonCrystal extends BlockContainer
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 6);
    public static final PropertyEnum<EnumDemonWillType> TYPE = PropertyEnum.<EnumDemonWillType>create("type", EnumDemonWillType.class);
    public static final PropertyEnum<EnumFacing> ATTACHED = PropertyEnum.<EnumFacing>create("attached", EnumFacing.class);

    public BlockDemonCrystal()
    {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumDemonWillType.DEFAULT).withProperty(ATTACHED, EnumFacing.UP));

        setUnlocalizedName(Constants.Mod.MODID + ".demonCrystal");
        setRegistryName(Constants.BloodMagicBlock.DEMON_CRYSTAL.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side)
    {
        BlockPos offsetPos = pos.offset(side.getOpposite());
        IBlockState offsetState = world.getBlockState(offsetPos);
        Block offsetBlock = offsetState.getBlock();

        return offsetBlock.isSideSolid(world, offsetPos, side) && this.canPlaceBlockAt(world, pos);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        TileDemonCrystal tile = (TileDemonCrystal) world.getTileEntity(pos);
        EnumFacing placement = tile.getPlacement();
        BlockPos offsetPos = pos.offset(placement.getOpposite());
        IBlockState offsetState = world.getBlockState(offsetPos);
        Block offsetBlock = offsetState.getBlock();

        if (!offsetBlock.isSideSolid(world, offsetPos, placement))
        {
            world.setBlockToAir(pos);
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileDemonCrystal tile = (TileDemonCrystal) world.getTileEntity(pos);
        return state.withProperty(AGE, tile.getCrystalCountForRender()).withProperty(ATTACHED, tile.getPlacement());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list)
    {
        for (int i = 0; i < EnumDemonWillType.values().length; i++)
            list.add(new ItemStack(this, 1, i));
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
        return this.getDefaultState().withProperty(TYPE, EnumDemonWillType.values()[meta]);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumDemonWillType) state.getValue(TYPE)).ordinal();
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] { TYPE, AGE, ATTACHED });
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileDemonCrystal();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileDemonCrystal tile = (TileDemonCrystal) world.getTileEntity(pos);
        EnumDemonWillType type = state.getValue(TYPE);
        int number = tile.getCrystalCount();

        spawnAsEntity(world, pos, getItemStackDropped(type, number));
        world.removeTileEntity(pos);
    }

    public static ItemStack getItemStackDropped(EnumDemonWillType type, int crystalNumber)
    {
        ItemStack stack = null;
        switch (type)
        {
        case CORROSIVE:
            stack = ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_CORROSIVE);
            break;
        case DEFAULT:
            stack = ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DEFAULT);
            break;
        case DESTRUCTIVE:
            stack = ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_DESTRUCTIVE);
            break;
        case STEADFAST:
            stack = ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_STEADFAST);
            break;
        case VENGEFUL:
            stack = ItemDemonCrystal.getStack(ItemDemonCrystal.CRYSTAL_VENGEFUL);
            break;
        }

        stack.stackSize = crystalNumber;
        return stack;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }

        TileDemonCrystal crystal = (TileDemonCrystal) world.getTileEntity(pos);

        if (PlayerDemonWillHandler.getTotalDemonWill(EnumDemonWillType.DEFAULT, player) > 1024)
        {
            crystal.dropSingleCrystal();

            world.markBlockForUpdate(pos);
        }

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