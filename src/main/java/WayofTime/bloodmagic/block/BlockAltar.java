package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.altar.BloodAltar;
import WayofTime.bloodmagic.api.altar.EnumAltarComponent;
import WayofTime.bloodmagic.api.altar.IBloodAltar;
import WayofTime.bloodmagic.api.iface.IDocumentedBlock;
import WayofTime.bloodmagic.item.sigil.ItemSigilHolding;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.altar.IAltarManipulator;
import WayofTime.bloodmagic.api.iface.IAltarReader;
import WayofTime.bloodmagic.api.iface.IBindable;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.orb.IBloodOrb;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.Utils;

import com.google.common.base.Strings;

public class BlockAltar extends BlockContainer implements IVariantProvider, IDocumentedBlock
{
    public BlockAltar()
    {
        super(Material.ROCK);

        setUnlocalizedName(Constants.Mod.MODID + ".altar");
        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos)
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
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean causesSuffocation()
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
        return new TileAltar();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileAltar altar = (TileAltar) world.getTileEntity(pos);

        if (altar == null || player.isSneaking())
            return false;

        ItemStack playerItem = player.inventory.getCurrentItem();

        if (playerItem != null)
        {
            if (playerItem.getItem() instanceof IAltarReader || playerItem.getItem() instanceof IAltarManipulator)
            {
                playerItem.getItem().onItemRightClick(playerItem, world, player, hand);
                return true;
            }
        }

        if (Utils.insertItemToTile(altar, player))
            altar.startCycle();
        else
            altar.setActive();

        world.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TileAltar)
        {
            TileAltar tileAltar = (TileAltar) world.getTileEntity(blockPos);
            if (tileAltar != null)
                tileAltar.dropItems();
        }

        super.breakBlock(world, blockPos, blockState);
    }

    // IVariantProvider

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "normal"));
        return ret;
    }

    // IDocumentedBlock

    @Override
    public List<ITextComponent> getDocumentation(EntityPlayer player, World world, BlockPos pos, IBlockState state)
    {
        List<ITextComponent> docs = new ArrayList<ITextComponent>();
        IBloodAltar altar = ((IBloodAltar) world.getTileEntity(pos));
        Pair<BlockPos, EnumAltarComponent> missingBlock = BloodAltar.getAltarMissingBlock(world, pos, altar.getTier().toInt());
        if (missingBlock != null)
            docs.add(new TextComponentTranslation("chat.BloodMagic.altar.nextTier", new TextComponentTranslation(missingBlock.getRight().getKey()), Utils.prettifyBlockPosString(missingBlock.getLeft())));

        return docs;
    }
}
