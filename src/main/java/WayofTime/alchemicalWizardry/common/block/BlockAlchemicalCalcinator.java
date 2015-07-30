package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.api.items.interfaces.IReagentManipulator;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAlchemicalCalcinator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAlchemicalCalcinator extends BlockContainer
{
    public BlockAlchemicalCalcinator()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TEAlchemicalCalcinator();
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState blockState)
    {
        return true;
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        dropItems(world, blockPos);
        super.breakBlock(world, blockPos, blockState);
    }

    private void dropItems(World world, BlockPos blockPos)
    {
        Random rand = new Random();
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (!(tileEntity instanceof IInventory))
        {
            return;
        }

        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack item = inventory.getStackInSlot(i);

            if (item != null && item.stackSize > 0)
            {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;
                EntityItem entityItem = new EntityItem(world, blockPos.getX() + rx, blockPos.getY() + ry, blockPos.getZ() + rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

                if (item.hasTagCompound())
                {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                item.stackSize = 0;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TEAlchemicalCalcinator tileEntity = (TEAlchemicalCalcinator) world.getTileEntity(blockPos);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem() instanceof IReagentManipulator)
            {
                return false;
            }

            if (playerItem.getItem() instanceof IBloodOrb)
            {
                if (tileEntity.getStackInSlot(0) == null)
                {
                    ItemStack newItem = playerItem.copy();
                    newItem.stackSize = 1;
                    --playerItem.stackSize;
                    tileEntity.setInventorySlotContents(0, newItem);
                }
            } else if (tileEntity.getStackInSlot(1) == null)
            {
                ItemStack newItem = playerItem.copy();
                newItem.stackSize = 1;
                --playerItem.stackSize;
                tileEntity.setInventorySlotContents(1, newItem);
            }

        } else
        {
            if (tileEntity.getStackInSlot(1) != null)
            {
                player.inventory.addItemStackToInventory(tileEntity.getStackInSlot(1));
                tileEntity.setInventorySlotContents(1, null);
            } else if (tileEntity.getStackInSlot(0) != null)
            {
                player.inventory.addItemStackToInventory(tileEntity.getStackInSlot(0));
                tileEntity.setInventorySlotContents(0, null);
            }
        }

        tileEntity.getWorld().markBlockForUpdate(blockPos);

        return true;
    }
}
