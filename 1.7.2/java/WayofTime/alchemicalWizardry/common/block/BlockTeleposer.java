package WayofTime.alchemicalWizardry.common.block;

import java.util.Random;

import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.items.TelepositionFocus;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTeleposer extends BlockContainer
{
    @SideOnly(Side.CLIENT)
    private static IIcon topIcon;
    @SideOnly(Side.CLIENT)
    private static IIcon sideIcon1;
    @SideOnly(Side.CLIENT)
    private static IIcon sideIcon2;
    @SideOnly(Side.CLIENT)
    private static IIcon bottomIcon;

    public BlockTeleposer()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("bloodTeleposer");
        //func_111022_d("AlchemicalWizardry:blocks");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.topIcon = iconRegister.registerIcon("AlchemicalWizardry:Teleposer_Top");
        this.sideIcon1 = iconRegister.registerIcon("AlchemicalWizardry:Teleposer_Side");
        this.sideIcon2 = iconRegister.registerIcon("AlchemicalWizardry:Teleposer_Side");
        this.bottomIcon = iconRegister.registerIcon("AlchemicalWizardry:Teleposer_Side");
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
        TETeleposer tileEntity = (TETeleposer) world.getTileEntity(x, y, z);
        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem() instanceof TelepositionFocus)
            {
                if (playerItem.stackTagCompound == null)
                {
                    playerItem.setTagCompound(new NBTTagCompound());
                }

                NBTTagCompound itemTag = playerItem.stackTagCompound;
                itemTag.setInteger("xCoord", x);
                itemTag.setInteger("yCoord", y);
                itemTag.setInteger("zCoord", z);
                itemTag.setInteger("dimensionId", world.provider.dimensionId);
                return true;
            }
        }

        player.openGui(AlchemicalWizardry.instance, 1, world, x, y, z);
//        this.swapBlocks(world, x, y+1, z, x, y+2, z);
//
//        world.markBlockForUpdate(x, y, z);
        //player.openGui(AlchemicalWizardry.instance, 0, world, x, y, z);
        //PacketDispatcher.sendPacketToServer(tileEntity.getDescriptionPacket());
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
                EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

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
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TETeleposer();
    }

    public static boolean swapBlocks(World worldI, World worldF, int xi, int yi, int zi, int xf, int yf, int zf)
    {
        //TODO Fix sapling duplication glitch
        TileEntity tileEntityI = worldI.getTileEntity(xi, yi, zi);
        TileEntity tileEntityF = worldF.getTileEntity(xf, yf, zf);
        TileEntity tileI;
        TileEntity tileF;
//    	ItemStack[] inv1 = new ItemStack[0];
//    	ItemStack[] inv2 = new ItemStack[0];
        NBTTagCompound nbttag1 = new NBTTagCompound();
        NBTTagCompound nbttag2 = new NBTTagCompound();

        if (tileEntityI != null)
        {
            //NBTTagCompound nbttag1 = new NBTTagCompound();
//    		tileEntityI.xCoord=xf;
//    		tileEntityI.yCoord=yf;
//    		tileEntityI.zCoord=zf;
            tileEntityI.writeToNBT(nbttag1);
            //tileEntityI.readFromNBT(new NBTTagCompound());
        }

//    	if(tileEntityI instanceof IInventory)
//    	{
//    		int size = ((IInventory)tileEntityI).getSizeInventory();
//    		inv1 = new ItemStack[size];
//    		for(int i=0; i<size; i++)
//    		{
//    			inv1[i] = ((IInventory)tileEntityI).getStackInSlot(i);
//    			((IInventory)tileEntityI).setInventorySlotContents(i, null);
//    		}
//    	}

        if (tileEntityF != null)
        {
//    		tileEntityF.xCoord=xi;
//    		tileEntityF.yCoord=yi;
//    		tileEntityF.zCoord=zi;
            tileEntityF.writeToNBT(nbttag2);
        }

//    	if(tileEntityF instanceof IInventory)
//    	{
//    		int size = ((IInventory)tileEntityF).getSizeInventory();
//    		inv2 = new ItemStack[size];
//    		for(int i=0; i<size; i++)
//    		{
//    			inv2[i] = ((IInventory)tileEntityF).getStackInSlot(i);
//    			((IInventory)tileEntityF).setInventorySlotContents(i, null);
//    		}
//    	}
        Block blockI = worldI.getBlock(xi, yi, zi);
        Block blockF = worldF.getBlock(xf, yf, zf);

//    	if(ThaumcraftApi.portableHoleBlackList.contains(idI)||ThaumcraftApi.portableHoleBlackList.contains(idF))
//    	{
//    		return false;
//    	}

        if (blockI.equals(Blocks.air) && blockF.equals(Blocks.air))
        {
            return false;
        }

        //if(Block.blocksList[idI] instanceof IPlantable || Block.blocksList[idF] instanceof IPlantable||Block.blocksList[idI] instanceof BlockMobSpawner || Block.blocksList[idF] instanceof BlockMobSpawner)
        if (blockI instanceof BlockMobSpawner || blockF instanceof BlockMobSpawner)
        {
            return false;
        }

        int metaI = worldI.getBlockMetadata(xi, yi, zi);
        int metaF = worldF.getBlockMetadata(xf, yf, zf);
        worldI.playSoundEffect(xi, yi, zi, "mob.endermen.portal", 1.0F, 1.0F);
        worldF.playSoundEffect(xf, yf, zf, "mob.endermen.portal", 1.0F, 1.0F);
        //CLEAR TILES
        Block finalBlock = blockF;

        if (finalBlock != null)
        {
            TileEntity tileToSet = finalBlock.createTileEntity(worldF, metaF);
//    		tileToSet.xCoord = xf;
//    		tileToSet.yCoord = yf;
//    		tileToSet.zCoord = zf;
            worldF.setTileEntity(xf, yf, zf, tileToSet);
        }

        Block initialBlock = blockI;

        if (initialBlock != null)
        {
            TileEntity tileToSet = initialBlock.createTileEntity(worldI, metaI);
//    		tileToSet.xCoord = xi;
//    		tileToSet.yCoord = yi;
//    		tileToSet.zCoord = zi;
            worldI.setTileEntity(xi, yi, zi, tileToSet);
        }

        //TILES CLEARED
//    	worldF.destroyBlock(xf, yf, zf, false);
//    	worldI.destroyBlock(xi, yi, zi, false);
//    	worldI.setBlockToAir(xi, yi, zi);
//    	worldF.setBlockToAir(xf, yf, zf);
        worldF.setBlock(xf, yf, zf, initialBlock, metaI, 3);

        if (tileEntityI != null)
        {
            TileEntity newTileEntityI = TileEntity.createAndLoadEntity(nbttag1);
            worldF.setTileEntity(xf, yf, zf, newTileEntityI);
            newTileEntityI.xCoord = xf;
            newTileEntityI.yCoord = yf;
            newTileEntityI.zCoord = zf;
        }

        worldI.setBlock(xi, yi, zi, finalBlock, metaF, 3);

        if (tileEntityF != null)
        {
            TileEntity newTileEntityF = TileEntity.createAndLoadEntity(nbttag2);
            worldI.setTileEntity(xi, yi, zi, newTileEntityF);
            newTileEntityF.xCoord = xi;
            newTileEntityF.yCoord = yi;
            newTileEntityF.zCoord = zi;
        }

//    	if(finalBlock!=null)
//    		finalBlock.onNeighborBlockChange(worldI, xi, yi, zi, AlchemicalWizardry.blockTeleposer.blockID);
//    	if(initialBlock!=null)
//    		initialBlock.onNeighborBlockChange(worldF, xf, yf, zf, AlchemicalWizardry.blockTeleposer.blockID);
//    	tileI = worldI.getBlockTileEntity(xi, yi, zi);
//    	tileF = worldF.getBlockTileEntity(xf, yf, zf);
//    	if(tileI!=null)
//    	{
//    		tileI.readFromNBT(nbttag2);
//    	}
//
//    	if(tileF!=null)
//    	{
//    		tileF.readFromNBT(nbttag1);
//    	}
//    	if(tileI instanceof IInventory && inv2.length>0)
//    	{
//    		for(int i=0;i<((IInventory)tileI).getSizeInventory();i++)
//    		{
//    			((IInventory)tileI).setInventorySlotContents(i, inv2[i]);
//    		}
//    	}
//
//    	if(tileF instanceof IInventory && inv1.length>0)
//    	{
//    		for(int i=0;i<((IInventory)tileF).getSizeInventory();i++)
//    		{
//    			((IInventory)tileF).setInventorySlotContents(i, inv1[i]);
//    		}
//    	}
//    	worldI.markBlockForUpdate(xi, yi, zi);
//    	worldF.markBlockForUpdate(xf, yf, zf);
        return true;
    }
}
