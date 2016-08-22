package WayofTime.bloodmagic.tile;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.util.Utils;

public class TileMimic extends TileInventory
{
    public boolean dropItemsOnBreak = true;
    public NBTTagCompound tileTag = new NBTTagCompound();
    public TileEntity mimicedTile = null;

    public TileMimic()
    {
        super(1, "mimic");
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side)
    {
        if (performSpecialAbility())
        {
            return true;
        }

        if (player.isSneaking())
            return false;

        if (player.getHeldItem(hand) != null && player.getHeldItem(hand).getItem() == new ItemStack(ModBlocks.mimic).getItem())
            return false;

        if (getStackInSlot(0) != null && player.getHeldItem(hand) != null)
            return false;

        if (!dropItemsOnBreak && !player.capabilities.isCreativeMode)
            return false;

        Utils.insertItemToTile(this, player);
        mimicedTile = getTileFromStackWithTag(worldObj, pos, getStackInSlot(0), tileTag);

        if (player.capabilities.isCreativeMode)
        {
            dropItemsOnBreak = getStackInSlot(0) == null;
        }

        world.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    public boolean performSpecialAbility()
    {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        dropItemsOnBreak = tag.getBoolean("dropItemsOnBreak");
        tileTag = tag.getCompoundTag("tileTag");
        mimicedTile = getTileFromStackWithTag(worldObj, pos, getStackInSlot(0), tileTag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setBoolean("dropItemsOnBreak", dropItemsOnBreak);
        tag.setTag("tileTag", tileTag);

        return tag;
    }

    public static void replaceMimicWithBlockActual(TileMimic mimic)
    {
        World world = mimic.getWorld();
        BlockPos pos = mimic.getPos();

        replaceMimicWithBlockActual(world, pos, mimic.getStackInSlot(0), mimic.tileTag);
    }

    public static boolean replaceMimicWithBlockActual(World world, BlockPos pos, ItemStack stack, NBTTagCompound tileTag)
    {
        if (stack != null && stack.getItem() instanceof ItemBlock)
        {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            IBlockState state = block.onBlockPlaced(world, pos, EnumFacing.UP, 0, 0, 0, stack.getItemDamage(), null);
            if (world.setBlockState(pos, state, 3))
            {
                TileEntity tile = world.getTileEntity(pos);
                if (tile != null)
                {
                    tileTag.setInteger("x", pos.getX());
                    tileTag.setInteger("y", pos.getY());
                    tileTag.setInteger("z", pos.getZ());
                    tile.readFromNBT(tileTag);

                    return true;
                }
            }
        }

        return false;
    }

    public static TileEntity getTileFromStackWithTag(World world, BlockPos pos, ItemStack stack, NBTTagCompound tag)
    {
        if (stack != null && stack.getItem() instanceof ItemBlock)
        {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            if (block instanceof ITileEntityProvider)
            {
                TileEntity tile = ((ITileEntityProvider) block).createNewTileEntity(world, stack.getItemDamage());

                if (tag != null)
                {
                    NBTTagCompound copyTag = tag.copy();
                    copyTag.setInteger("x", pos.getX());
                    copyTag.setInteger("y", pos.getY());
                    copyTag.setInteger("z", pos.getZ());
                    tile.readFromNBT(copyTag);
                }

                tile.setWorldObj(world);

                return tile;
            }
        }

        return null;
    }

    @Override
    public void dropItems()
    {
        if (dropItemsOnBreak)
        {
            InventoryHelper.dropInventoryItems(getWorld(), getPos(), this);
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return slot == 0 && dropItemsOnBreak;
    }
}