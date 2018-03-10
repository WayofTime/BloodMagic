package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.altar.*;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.iface.IAltarReader;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.iface.IDocumentedBlock;
import WayofTime.bloodmagic.orb.BloodOrb;
import WayofTime.bloodmagic.orb.IBloodOrb;
import WayofTime.bloodmagic.tile.TileAltar;
import WayofTime.bloodmagic.util.Utils;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
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
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockAltar extends Block implements IVariantProvider, IDocumentedBlock, IBMBlock {
    public BlockAltar() {
        super(Material.ROCK);

        setUnlocalizedName(BloodMagic.MODID + ".altar");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        if (world.isRemote)
            return 0;

        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof TileAltar) {
            TileAltar altar = (TileAltar) tile;
            ItemStack orbStack = altar.getStackInSlot(0);

            if (world.getBlockState(pos.down()).getBlock() instanceof BlockDecorative) {
                if (orbStack.getItem() instanceof IBloodOrb && orbStack.getItem() instanceof IBindable) {
                    BloodOrb orb = ((IBloodOrb) orbStack.getItem()).getOrb(orbStack);
                    Binding binding = ((IBindable) orbStack.getItem()).getBinding(orbStack);
                    if (orb != null && binding != null) {
                        SoulNetwork soulNetwork = NetworkHelper.getSoulNetwork(binding);

                        int maxEssence = orb.getCapacity();
                        int currentEssence = soulNetwork.getCurrentEssence();
                        int level = currentEssence * 15 / maxEssence;
                        return Math.min(15, level) % 16;
                    }
                }
            } else {
                int maxEssence = altar.getCapacity();
                int currentEssence = altar.getCurrentBlood();
                int level = currentEssence * 15 / maxEssence;
                return Math.min(15, level) % 16;
            }
        }

        return 0;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileAltar altar = (TileAltar) world.getTileEntity(pos);

        if (altar == null || player.isSneaking())
            return false;

        ItemStack playerItem = player.getHeldItem(hand);

        if (playerItem.getItem() instanceof IAltarReader || playerItem.getItem() instanceof IAltarManipulator) {
            playerItem.getItem().onItemRightClick(world, player, hand);
            return true;
        }

        if (Utils.insertItemToTile(altar, player))
            altar.startCycle();
        else
            altar.setActive();

        world.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TileAltar)
            ((TileAltar) tile).dropItems();

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileAltar();
    }

    // IDocumentedBlock

    @Override
    public List<ITextComponent> getDocumentation(EntityPlayer player, World world, BlockPos pos, IBlockState state) {
        List<ITextComponent> docs = new ArrayList<>();
        IBloodAltar altar = ((IBloodAltar) world.getTileEntity(pos));
        Pair<BlockPos, ComponentType> missingBlock = AltarUtil.getFirstMissingComponent(world, pos, altar.getTier().toInt());
        if (missingBlock != null)
            docs.add(new TextComponentTranslation("chat.bloodmagic.altar.nextTier", new TextComponentTranslation(missingBlock.getRight().getKey()), Utils.prettifyBlockPosString(missingBlock.getLeft())));

        return docs;
    }

    @Override
    public ItemBlock getItem() {
        return new ItemBlock(this);
    }
}
