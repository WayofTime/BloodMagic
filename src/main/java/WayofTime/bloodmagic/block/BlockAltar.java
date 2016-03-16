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
import WayofTime.bloodmagic.api.altar.IAltarManipulator;
import WayofTime.bloodmagic.api.iface.IAltarReader;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.Utils;

import com.google.common.base.Strings;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class BlockAltar extends BlockContainer implements IVariantProvider
{
    public BlockAltar()
    {
        super(Material.rock);

        setUnlocalizedName(Constants.Mod.MODID + ".altar");
        setRegistryName(Constants.BloodMagicBlock.ALTAR.getRegName());
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos pos)
    {
        if (world.isRemote)
            return 0;

        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof TileAltar)
        {
            TileAltar altar = (TileAltar) tile;
            ItemStack orbStack = altar.getStackInSlot(0);

            if (world.getBlockState(pos.down()).getBlock() instanceof BlockBloodStoneBrick)
            {
                if (orbStack != null && orbStack.getItem() instanceof IBloodOrb && orbStack.getItem() instanceof IBindable)
                {
                    IBloodOrb bloodOrb = (IBloodOrb) orbStack.getItem();
                    IBindable bindable = (IBindable) orbStack.getItem();
                    if (!Strings.isNullOrEmpty(bindable.getOwnerUUID(orbStack)))
                    {
                        SoulNetwork soulNetwork = NetworkHelper.getSoulNetwork(bindable.getOwnerUUID(orbStack));

                        int maxEssence = bloodOrb.getMaxEssence(orbStack.getItemDamage());
                        int currentEssence = soulNetwork.getCurrentEssence();
                        int level = currentEssence * 15 / maxEssence;
                        return Math.min(15, level) % 16;
                    }
                }
            } else
            {
                int maxEssence = altar.getCapacity();
                int currentEssence = altar.getCurrentBlood();
                int level = currentEssence * 15 / maxEssence;
                return Math.min(15, level) % 16;
            }
        }

        return 0;
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
        return new TileAltar();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileAltar altar = (TileAltar) world.getTileEntity(pos);

        if (altar == null || player.isSneaking())
            return false;

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem() instanceof IAltarReader || playerItem.getItem() instanceof IAltarManipulator)
            {
                playerItem.getItem().onItemRightClick(playerItem, world, player);
                return true;
            }
        }

        if (Utils.insertItemToTile(altar, player))
            altar.startCycle();
        else
            altar.setActive();

        world.markBlockForUpdate(pos);
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        TileAltar tileAltar = (TileAltar) world.getTileEntity(blockPos);
        if (tileAltar != null)
            tileAltar.dropItems();

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
