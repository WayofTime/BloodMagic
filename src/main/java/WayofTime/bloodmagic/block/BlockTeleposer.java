package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.util.helper.BindableHelper;
import WayofTime.bloodmagic.item.ItemTelepositionFocus;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockTeleposer extends BlockContainer {

    public BlockTeleposer() {
        super(Material.rock);

        setCreativeTab(BloodMagic.tabBloodMagic);
        setUnlocalizedName(Constants.Mod.MODID + ".teleposer");
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null && playerItem.getItem() instanceof ItemTelepositionFocus) {
            BindableHelper.checkAndSetItemOwner(playerItem, player);

            ((ItemTelepositionFocus) playerItem.getItem()).setBlockPos(playerItem, world, pos);
            return true;
        }

//        player.openGui(AlchemicalWizardry.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
