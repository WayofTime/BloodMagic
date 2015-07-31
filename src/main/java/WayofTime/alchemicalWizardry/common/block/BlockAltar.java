package WayofTime.alchemicalWizardry.common.block;

import java.util.Random;

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
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.IAltarManipulator;
import WayofTime.alchemicalWizardry.common.items.Orb;
import WayofTime.alchemicalWizardry.common.items.sigil.holding.SigilOfHolding;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAltar extends BlockContainer
{
    public BlockAltar()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos blockPos)
    {
        TileEntity tile = world.getTileEntity(blockPos);

        if (tile instanceof TEAltar)
        {
            ItemStack stack = ((TEAltar) tile).getStackInSlot(0);

            if (stack != null && stack.getItem() instanceof Orb)
            {
                Orb bloodOrb = (Orb) stack.getItem();
                int maxEssence = bloodOrb.getMaxEssence();
                int currentEssence = bloodOrb.getCurrentEssence(stack);
                int level = currentEssence * 15 / maxEssence;
                return Math.min(15, level) % 16;
            }
        }

        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TEAltar tileEntity = (TEAltar) world.getTileEntity(blockPos);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem().equals(ModItems.divinationSigil))
            {
                if (player.worldObj.isRemote)
                {
                    world.markBlockForUpdate(blockPos);
                } else
                {
                    tileEntity.sendChatInfoToPlayer(player);
                }

                return true;
            } else if (playerItem.getItem().equals(ModItems.itemSeerSigil))
            {
                if (player.worldObj.isRemote)
                {
                    world.markBlockForUpdate(blockPos);
                } else
                {
                    tileEntity.sendMoreChatInfoToPlayer(player);
                }

                return true;
            }else if(playerItem.getItem() instanceof IAltarManipulator)
            {
            	return false;
            }
            else if (playerItem.getItem().equals(ModItems.sigilOfHolding))
            {
                ItemStack item = SigilOfHolding.getCurrentSigil(playerItem);

                if (item != null && item.getItem().equals(ModItems.divinationSigil))
                {
                    if (player.worldObj.isRemote)
                    {
                        world.markBlockForUpdate(blockPos);
                    } else
                    {
                        tileEntity.sendChatInfoToPlayer(player);
                    }

                    return true;
                } else if (item != null && item.getItem().equals(ModItems.itemSeerSigil))
                {
                    if (player.worldObj.isRemote)
                    {
                        world.markBlockForUpdate(blockPos);
                    } else
                    {
                        tileEntity.sendMoreChatInfoToPlayer(player);
                    }

                    return true;
                }
            }
        }

        if (tileEntity.getStackInSlot(0) == null && playerItem != null)
        {
            ItemStack newItem = playerItem.copy();
            newItem.stackSize = 1;
            --playerItem.stackSize;
            tileEntity.setInventorySlotContents(0, newItem);
            tileEntity.startCycle();
        } else if (tileEntity.getStackInSlot(0) != null && playerItem == null)
        {
            player.inventory.addItemStackToInventory(tileEntity.getStackInSlot(0));
            tileEntity.setInventorySlotContents(0, null);
            tileEntity.setActive();
        }
        world.markBlockForUpdate(blockPos);
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
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState blockState, Random rand)
    {
        TEAltar tileEntity = (TEAltar) world.getTileEntity(blockPos);

        if (!tileEntity.isActive())
        {
            return;
        }

        if (rand.nextInt(3) != 0)
        {
            return;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TEAltar();
    }
}
