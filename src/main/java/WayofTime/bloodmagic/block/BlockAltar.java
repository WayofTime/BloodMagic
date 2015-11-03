package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.iface.IAltarReader;
import WayofTime.bloodmagic.tile.TileAltar;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockAltar extends BlockContainer {

    public BlockAltar() {
        super(Material.rock);

        setUnlocalizedName(BloodMagic.MODID + ".altar");
        setCreativeTab(BloodMagic.tabBloodMagic);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileAltar();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileAltar altar = (TileAltar) world.getTileEntity(pos);

        if (altar == null || player.isSneaking())
        {
            return false;
        }

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem() instanceof IAltarReader)
            {
                playerItem.getItem().onItemRightClick(playerItem, world, player);
                return true;
            }
        }

        if (altar.getStackInSlot(0) == null && playerItem != null)
        {
            ItemStack newItem = playerItem.copy();
            newItem.stackSize = 1;
            --playerItem.stackSize;
            altar.setInventorySlotContents(0, newItem);
            altar.startCycle();
        }
        else if (altar.getStackInSlot(0) != null && playerItem == null)
        {
            player.inventory.addItemStackToInventory(altar.getStackInSlot(0));
            altar.setInventorySlotContents(0, null);
            altar.setActive();
        }
        world.markBlockForUpdate(pos);
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
        TileAltar tileAltar = (TileAltar) world.getTileEntity(blockPos);
        if (tileAltar != null)
            tileAltar.dropItems();

        super.breakBlock(world, blockPos, blockState);
    }

}
