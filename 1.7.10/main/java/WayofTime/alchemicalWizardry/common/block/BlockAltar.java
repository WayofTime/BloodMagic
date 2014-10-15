package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.items.EnergyBattery;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHolding;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAltar extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    private static IIcon topIcon;
    @SideOnly(Side.CLIENT)
    private static IIcon sideIcon1;
    @SideOnly(Side.CLIENT)
    private static IIcon sideIcon2;
    @SideOnly(Side.CLIENT)
    private static IIcon bottomIcon;

    public BlockAltar()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("bloodAltar");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.topIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_Top");
        this.sideIcon1 = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_SideType1");
        this.sideIcon2 = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_SideType2");
        this.bottomIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_Bottom");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        switch (side)
        {
            case 0:
                return bottomIcon;
            case 1:
                return topIcon;
            default:
                return sideIcon2;
        }
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int meta)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TEAltar)
        {
            ItemStack stack = ((TEAltar) tile).getStackInSlot(0);

            if (stack != null && stack.getItem() instanceof EnergyBattery)
            {
                EnergyBattery bloodOrb = (EnergyBattery) stack.getItem();
                int maxEssence = bloodOrb.getMaxEssence();
                int currentEssence = bloodOrb.getCurrentEssence(stack);
                int level = currentEssence * 15 / maxEssence;
                return ((int) (Math.min(15, level))) % 16;
            }
        }

        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
    {
        TEAltar tileEntity = (TEAltar) world.getTileEntity(x, y, z);

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
                    world.markBlockForUpdate(x, y, z);
                } else
                {
                    tileEntity.sendChatInfoToPlayer(player);
                }

                return true;
            } else if (playerItem.getItem().equals(ModItems.itemSeerSigil))
            {
                if (player.worldObj.isRemote)
                {
                    world.markBlockForUpdate(x, y, z);
                } else
                {
                    tileEntity.sendMoreChatInfoToPlayer(player);
                }

                return true;
            } else if (playerItem.getItem().equals(ModItems.sigilOfHolding))
            {
                ItemStack item = ((SigilOfHolding) playerItem.getItem()).getCurrentItem(playerItem);

                if (item != null && item.getItem().equals(ModItems.divinationSigil))
                {
                    if (player.worldObj.isRemote)
                    {
                        world.markBlockForUpdate(x, y, z);
                    } else
                    {
                        tileEntity.sendChatInfoToPlayer(player);
                    }

                    return true;
                } else if (item != null && item.getItem().equals(ModItems.itemSeerSigil))
                {
                    if (player.worldObj.isRemote)
                    {
                        world.markBlockForUpdate(x, y, z);
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
        world.markBlockForUpdate(x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
    {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    private void dropItems(World world, int x, int y, int z)
    {
        Random rand = new Random();
        TileEntity tileEntity = world.getTileEntity(x, y, z);

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
                EntityItem entityItem = new EntityItem(world,
                        x + rx, y + ry, z + rz,
                        new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

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
    public boolean renderAsNormalBlock()
    {
        return false;
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
    public boolean hasTileEntity()
    {
        return true;
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        TEAltar tileEntity = (TEAltar) world.getTileEntity(x, y, z);

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
