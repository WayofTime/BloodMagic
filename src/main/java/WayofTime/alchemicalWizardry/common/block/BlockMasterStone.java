package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.common.items.ActivationCrystal;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockMasterStone extends BlockContainer
{
    public BlockMasterStone()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player)
    {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TEMasterStone)
        {
            ((TEMasterStone) tile).useOnRitualBroken();
        }

        super.onBlockHarvested(world, blockPos, blockState, player);
    }
    
    @Override
    public void onBlockDestroyedByExplosion(World world, BlockPos blockPos, Explosion explosion)
    {
    	super.onBlockDestroyedByExplosion(world, blockPos, explosion);
    	TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TEMasterStone)
        {
            ((TEMasterStone) tile).useOnRitualBrokenExplosion();
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TEMasterStone tileEntity = (TEMasterStone) world.getTileEntity(blockPos);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem == null)
        {
            return false;
        }

        Item item = playerItem.getItem();

        if (!(item instanceof ActivationCrystal))
        {
            return false;
        }

        ActivationCrystal acItem = (ActivationCrystal) item;
//        tileEntity.setOwner(acItem.getOwnerName(playerItem));
        tileEntity.activateRitual(world, acItem.getCrystalLevel(playerItem), playerItem, player, ActivationCrystal.getOwnerName(playerItem));
        world.markBlockForUpdate(blockPos);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TEMasterStone();
    }
}
