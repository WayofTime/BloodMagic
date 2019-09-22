package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.command.sub.SubCommandTeleposer;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;
import WayofTime.bloodmagic.tile.TileTeleposer;
import WayofTime.bloodmagic.util.Constants;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTeleposer extends ContainerBlock implements IVariantProvider, IBMBlock {
    public BlockTeleposer() {
        super(Material.ROCK);

        setCreativeTab(BloodMagic.TAB_BM);
        setTranslationKey(BloodMagic.MODID + ".teleposer");
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
        ItemStack playerItem = player.getHeldItem(hand);

        if (playerItem.getItem() instanceof ItemTelepositionFocus)
            ((ItemTelepositionFocus) playerItem.getItem()).setBlockPos(playerItem, world, pos);
        else if (world.getTileEntity(pos) instanceof TileTeleposer)
            player.openGui(BloodMagic.instance, Constants.Gui.TELEPOSER_GUI, world, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, BlockState blockState) {
        TileTeleposer tileTeleposer = (TileTeleposer) world.getTileEntity(blockPos);
        if (tileTeleposer != null) {
            tileTeleposer.dropItems();
            SubCommandTeleposer.teleposerSet.remove(tileTeleposer);
        }

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTeleposer();
    }

    @Override
    public BlockItem getItem() {
        return new BlockItem(this);
    }
}
