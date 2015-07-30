package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.common.items.BlankSpell;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellTable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockSpellTable extends BlockContainer
{
    public BlockSpellTable()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TESpellTable tileEntity = (TESpellTable) world.getTileEntity(blockPos);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem() instanceof BlankSpell)
            {
                if (playerItem.getTagCompound() == null)
                {
                    playerItem.setTagCompound(new NBTTagCompound());
                }

                NBTTagCompound itemTag = playerItem.getTagCompound();
                itemTag.setInteger("xCoord", blockPos.getX());
                itemTag.setInteger("yCoord", blockPos.getY());
                itemTag.setInteger("zCoord", blockPos.getZ());
                itemTag.setInteger("dimensionId", world.provider.getDimensionId());
                return true;
            }
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metaMaybe)
    {
        return new TESpellTable();
    }
}
