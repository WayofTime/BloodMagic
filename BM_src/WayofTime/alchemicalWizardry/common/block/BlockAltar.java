package WayofTime.alchemicalWizardry.common.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.items.EnergyBattery;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHolding;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAltar extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    private static Icon topIcon;
    @SideOnly(Side.CLIENT)
    private static Icon sideIcon1;
    @SideOnly(Side.CLIENT)
    private static Icon sideIcon2;
    @SideOnly(Side.CLIENT)
    private static Icon bottomIcon;
    public BlockAltar(int id)
    {
        super(id, Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setUnlocalizedName("blockAltar");
        //func_111022_d("AlchemicalWizardry:blocks");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.topIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_Top");
        this.sideIcon1 = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_SideType1");
        this.sideIcon2 = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_SideType2");
        this.bottomIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodAltar_Bottom");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        switch (side)
        {
            case 0:
                return bottomIcon;

            case 1:
                return topIcon;

                //case 2: return sideIcon1;
                //case 3: return sideIcon1;
                //case 4: return sideIcon2;
                //case 5: return sideIcon2;
            default:
                return sideIcon2;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
    {
        TEAltar tileEntity = (TEAltar)world.getBlockTileEntity(x, y, z);

//        world.scheduleBlockUpdate(x, y, z, this.blockID, 0);

        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem().itemID == AlchemicalWizardry.divinationSigil.itemID)
            {
                if (player.worldObj.isRemote)
                {
                    PacketDispatcher.sendPacketToServer(PacketHandler.getAltarPacket(x, y, z));
                }

                return true;
            }
            else if (playerItem.getItem().itemID == AlchemicalWizardry.sigilOfHolding.itemID)
            {
                ItemStack item = ((SigilOfHolding)playerItem.getItem()).getCurrentItem(playerItem);

                if (item != null && item.getItem().itemID == AlchemicalWizardry.divinationSigil.itemID)
                {
                    if (player.worldObj.isRemote)
                    {
                        PacketDispatcher.sendPacketToServer(PacketHandler.getAltarPacket(x, y, z));
                    }

                    return true;
                }
            }
        }

        if (tileEntity.getStackInSlot(0) == null && playerItem != null)
        {
            ItemStack newItem = playerItem.copy();
            newItem.stackSize = 1;
//            if(newItem.getMaxDamage()==0)
//            {
//            	newItem.setItemDamage(0);
//            }
            --playerItem.stackSize;
            tileEntity.setInventorySlotContents(0, newItem);
            tileEntity.startCycle();
        }
        else if (tileEntity.getStackInSlot(0) != null && playerItem == null)
        {
            /**stub method
             * Add the item that is in the slot to the player's inventory, and
             * then set the slot to null.
             */
            player.inventory.addItemStackToInventory(tileEntity.getStackInSlot(0));
            tileEntity.setInventorySlotContents(0, null);
            tileEntity.setActive();
        }

        world.markBlockForUpdate(x, y, z);
        //player.openGui(AlchemicalWizardry.instance, 0, world, x, y, z);
        //PacketDispatcher.sendPacketToServer(tileEntity.getDescriptionPacket());
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    private void dropItems(World world, int x, int y, int z)
    {
        Random rand = new Random();
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

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
                                                       new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

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
    public TileEntity createNewTileEntity(World world)
    {
        return new TEAltar();
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
        TEAltar tileEntity = (TEAltar)world.getBlockTileEntity(x, y, z);

        if (!tileEntity.isActive())
        {
            return;
        }

        if (rand.nextInt(3) != 0)
        {
            return;
        }
    }

//    @Override
//    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
//    {
//        return 1;
//    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        TileEntity tile = par1IBlockAccess.getBlockTileEntity(par2, par3, par4);

        if (tile instanceof TEAltar)
        {
//    		if(tile.worldObj.isRemote)
//    		{
//    			return 0;
//    		}
            ItemStack stack = ((TEAltar)tile).getStackInSlot(0);

            if (stack != null && stack.getItem() instanceof EnergyBattery)
            {
                EnergyBattery bloodOrb = (EnergyBattery)stack.getItem();
                int maxEssence = bloodOrb.getMaxEssence();
                int currentEssence = bloodOrb.getCurrentEssence(stack);
                int level = currentEssence * 15 / maxEssence;
                return((int)(Math.min(15, level))) % 16;
            }
        }

        return 0;
    }
}
