package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.IDemonWillGem;
import WayofTime.bloodmagic.api.soul.IDiscreteDemonWill;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileDemonCrucible;
import WayofTime.bloodmagic.util.Utils;

public class BlockDemonCrucible extends BlockContainer implements IVariantProvider
{
    public BlockDemonCrucible()
    {
        super(Material.rock);

        setUnlocalizedName(Constants.Mod.MODID + ".demonCrucible");
        setRegistryName(Constants.BloodMagicBlock.DEMON_CRUCIBLE.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);

//        setBlockBounds(0.3F, 0F, 0.3F, 0.72F, 1F, 0.72F);
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque()
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileDemonCrucible();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileDemonCrucible crucible = (TileDemonCrucible) world.getTileEntity(pos);

        if (crucible == null || player.isSneaking())
            return false;

        if (heldItem != null)
        {
            if (!(heldItem.getItem() instanceof IDiscreteDemonWill) && !(heldItem.getItem() instanceof IDemonWillGem))
            {
                return false;
            }
        }

        Utils.insertItemToTile(crucible, player);

        world.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        TileDemonCrucible tile = (TileDemonCrucible) world.getTileEntity(blockPos);
        if (tile != null)
            tile.dropItems();

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "normal"));
        return ret;
    }
}
