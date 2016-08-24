package WayofTime.bloodmagic.tile;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import WayofTime.bloodmagic.block.BlockMimic;
import WayofTime.bloodmagic.entity.mob.EntityMimic;
import WayofTime.bloodmagic.registry.ModBlocks;
import WayofTime.bloodmagic.util.Utils;

public class TileMimic extends TileInventory implements ITickable
{
    private static Field _blockMetadata = ReflectionHelper.findField(TileEntity.class, "blockMetadata", "field_145847_g");

    public boolean dropItemsOnBreak = true;
    public NBTTagCompound tileTag = new NBTTagCompound();
    public TileEntity mimicedTile = null;
    public int metaOfReplacedBlock = 0;

    public int spawnRadius = 5;

    public TileMimic()
    {
        super(1, "mimic");
    }

    @Override
    public void update()
    {
        if (this.getBlockMetadata() == BlockMimic.sentientMimicMeta && !(mimicedTile instanceof IInventory))
        {
            AxisAlignedBB bb = new AxisAlignedBB(this.getPos()).expand(spawnRadius, spawnRadius, spawnRadius);
            List<EntityPlayer> playerList = worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb);

            for (EntityPlayer player : playerList)
            {
                if (!player.capabilities.isCreativeMode && Utils.canEntitySeeBlock(worldObj, player, getPos()))
                {
                    spawnMimicEntity(player);
                    break;
                }
            }
        }

    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side)
    {
        if (performSpecialAbility(player))
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
        this.refreshTileEntity();

        if (player.capabilities.isCreativeMode)
        {
            dropItemsOnBreak = getStackInSlot(0) == null;
        }

        world.notifyBlockUpdate(pos, state, state, 3);
        return true;
    }

    public boolean performSpecialAbility(EntityPlayer player)
    {
        switch (this.getBlockMetadata())
        {
        case BlockMimic.sentientMimicMeta:
            return spawnMimicEntity(player);
        }
        return false;
    }

    public boolean spawnMimicEntity(EntityPlayer target)
    {
        if (this.getStackInSlot(0) == null || worldObj.isRemote)
        {
            return false;
        }

        EntityMimic mimicEntity = new EntityMimic(worldObj);
        mimicEntity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);

        mimicEntity.initializeMimic(getStackInSlot(0), tileTag, dropItemsOnBreak, metaOfReplacedBlock);
        tileTag = null;
        mimicedTile = null;
        this.setInventorySlotContents(0, null);

        worldObj.spawnEntityInWorld(mimicEntity);
        if (target != null)
        {
            mimicEntity.setAttackTarget(target);
        }

        worldObj.setBlockToAir(pos);

        return true;
    }

    public void refreshTileEntity()
    {
        if (mimicedTile != null)
        {
            dropMimicedTileInventory();
        }
        mimicedTile = getTileFromStackWithTag(worldObj, pos, getStackInSlot(0), tileTag, metaOfReplacedBlock);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        dropItemsOnBreak = tag.getBoolean("dropItemsOnBreak");
        tileTag = tag.getCompoundTag("tileTag");
        metaOfReplacedBlock = tag.getInteger("metaOfReplacedBlock");
        mimicedTile = getTileFromStackWithTag(worldObj, pos, getStackInSlot(0), tileTag, metaOfReplacedBlock);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setBoolean("dropItemsOnBreak", dropItemsOnBreak);
        tag.setTag("tileTag", tileTag);
        tag.setInteger("metaOfReplacedBlock", metaOfReplacedBlock);

        return tag;
    }

    public static void replaceMimicWithBlockActual(TileMimic mimic)
    {
        World world = mimic.getWorld();
        BlockPos pos = mimic.getPos();

        replaceMimicWithBlockActual(world, pos, mimic.getStackInSlot(0), mimic.tileTag, mimic.metaOfReplacedBlock);
    }

    public static boolean replaceMimicWithBlockActual(World world, BlockPos pos, ItemStack stack, NBTTagCompound tileTag, int replacedMeta)
    {
        if (stack != null && stack.getItem() instanceof ItemBlock)
        {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            IBlockState state = block.getStateFromMeta(replacedMeta);
            if (world.setBlockState(pos, state, 3))
            {
                TileEntity tile = world.getTileEntity(pos);
                if (tile != null)
                {
                    tileTag.setInteger("x", pos.getX());
                    tileTag.setInteger("y", pos.getY());
                    tileTag.setInteger("z", pos.getZ());
                    tile.readFromNBT(tileTag);
                }

                return true;
            }
        }

        return false;
    }

    public static TileEntity getTileFromStackWithTag(World world, BlockPos pos, ItemStack stack, NBTTagCompound tag, int replacementMeta)
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

                try
                {
                    _blockMetadata.setInt(tile, replacementMeta);
                } catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }

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

        dropMimicedTileInventory();
    }

    public void dropMimicedTileInventory()
    {
        if (!worldObj.isRemote && mimicedTile instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(getWorld(), getPos(), (IInventory) mimicedTile);
        }
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return slot == 0 && dropItemsOnBreak;
    }
}