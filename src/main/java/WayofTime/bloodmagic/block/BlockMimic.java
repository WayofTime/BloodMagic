package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.base.BlockStringContainer;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.tile.TileMimic;

public class BlockMimic extends BlockStringContainer implements IVariantProvider
{
    public static final String[] names = { "nohitbox" };

    public BlockMimic()
    {
        super(Material.ROCK, names);

        setUnlocalizedName(Constants.Mod.MODID + ".mimic.");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 0);
        setLightOpacity(15);
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World world, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos)
    {
        TileMimic tileMimic = (TileMimic) world.getTileEntity(pos);
        if (tileMimic != null && tileMimic.getStackInSlot(0) != null)
        {
            Block mimicBlock = Block.getBlockFromItem(tileMimic.getStackInSlot(0).getItem());
            if (mimicBlock == null)
            {
                return FULL_BLOCK_AABB;
            }
            IBlockState mimicState = mimicBlock.getStateFromMeta(tileMimic.getStackInSlot(0).getItemDamage());
            if (mimicBlock != this)
            {
                return mimicState.getSelectedBoundingBox(world, pos);
            }
        }

        return FULL_BLOCK_AABB;
    }

    @Override
    public int getLightOpacity(IBlockState state)
    {
        //Overriden for now so that in the future I don't have to.
        return this.lightOpacity;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        if (state.getBlock() == this)
        {
            return super.getMetaFromState(state);
        }

        return state.getBlock().getMetaFromState(state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileMimic mimic = (TileMimic) world.getTileEntity(pos);

        if (mimic == null)
            return false;

        return mimic.onBlockActivated(world, pos, state, player, hand, heldItem, side);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileMimic)
        {
            TileMimic mimic = (TileMimic) tile;
            ItemStack stack = mimic.getStackInSlot(0);
            if (stack != null && stack.getItem() instanceof ItemBlock)
            {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                IBlockState mimicState = block.getStateFromMeta(mimic.metaOfReplacedBlock);
                if (block != this)
                {
                    if (block.getRenderType(mimicState) == EnumBlockRenderType.ENTITYBLOCK_ANIMATED)
                    {
                        return ModBlocks.bloodLight.getDefaultState(); //Small and invisible-ish, basically this is returned in order to not render over the animated block (TESR)
                    }

                    return block.getActualState(mimicState, world, pos);
                }
            }
        }
        return state;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TileMimic)
        {
            TileMimic TileMimic = (TileMimic) world.getTileEntity(blockPos);
            if (TileMimic != null)
                TileMimic.dropItems();
        }

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < names.length; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + names[i]));
        return ret;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileMimic();
    }
}
