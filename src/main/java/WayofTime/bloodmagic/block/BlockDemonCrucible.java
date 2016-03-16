package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.client.IVariantProvider;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.IDemonWillGem;
import WayofTime.bloodmagic.api.soul.IDiscreteDemonWill;
import WayofTime.bloodmagic.tile.TileDemonCrucible;
import WayofTime.bloodmagic.util.Utils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

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
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileDemonCrucible();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileDemonCrucible crucible = (TileDemonCrucible) world.getTileEntity(pos);

        if (crucible == null || player.isSneaking())
            return false;

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (!(playerItem.getItem() instanceof IDiscreteDemonWill) && !(playerItem.getItem() instanceof IDemonWillGem))
            {
                return false;
            }
        }

        Utils.insertItemToTile(crucible, player);

        world.markBlockForUpdate(pos);
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
