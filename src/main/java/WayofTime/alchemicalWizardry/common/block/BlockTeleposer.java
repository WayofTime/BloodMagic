package WayofTime.alchemicalWizardry.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockPortal;
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
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.items.TelepositionFocus;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import codechicken.multipart.MultipartHelper;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.common.Optional;
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
            	SoulNetworkHandler.checkAndSetItemOwner(playerItem, player);
            	
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
        TileEntity tileEntityI = worldI.getTileEntity(xi, yi, zi);
        TileEntity tileEntityF = worldF.getTileEntity(xf, yf, zf);
        TileEntity tileI;
        TileEntity tileF;

        NBTTagCompound nbttag1 = new NBTTagCompound();
        NBTTagCompound nbttag2 = new NBTTagCompound();

        if (tileEntityI != null)
        {
            tileEntityI.writeToNBT(nbttag1);
        }

        if (tileEntityF != null)
        {
            tileEntityF.writeToNBT(nbttag2);
        }

        Block blockI = worldI.getBlock(xi, yi, zi);
        Block blockF = worldF.getBlock(xf, yf, zf);


        if (blockI.equals(Blocks.air) && blockF.equals(Blocks.air))
        {
            return false;
        }

        if (blockI instanceof BlockMobSpawner || blockF instanceof BlockMobSpawner || blockI instanceof BlockPortal || blockF instanceof BlockPortal)
        {
            return false;
        }

        int metaI = worldI.getBlockMetadata(xi, yi, zi);
        int metaF = worldF.getBlockMetadata(xf, yf, zf);
        
        //TODO Teleposer event
        
        worldI.playSoundEffect(xi, yi, zi, "mob.endermen.portal", 1.0F, 1.0F);
        worldF.playSoundEffect(xf, yf, zf, "mob.endermen.portal", 1.0F, 1.0F);
        
        
        
        //CLEAR TILES
        Block finalBlock = blockF;

        if (finalBlock != null)
        {
            TileEntity tileToSet = finalBlock.createTileEntity(worldF, metaF);

            worldF.setTileEntity(xf, yf, zf, tileToSet);
        }

        Block initialBlock = blockI;

        if (initialBlock != null)
        {
            TileEntity tileToSet = initialBlock.createTileEntity(worldI, metaI);

            worldI.setTileEntity(xi, yi, zi, tileToSet);
        }

        //TILES CLEARED
        worldF.setBlock(xf, yf, zf, initialBlock, metaI, 3);

        if (tileEntityI != null)
        {
            TileEntity newTileEntityI = TileEntity.createAndLoadEntity(nbttag1);
            
            if(AlchemicalWizardry.isFMPLoaded && isMultipart(tileEntityI))
        	{
        		newTileEntityI = createMultipartFromNBT(worldF, nbttag1);
        	}
                        
            worldF.setTileEntity(xf, yf, zf, newTileEntityI);
            
            newTileEntityI.xCoord = xf;
            newTileEntityI.yCoord = yf;
            newTileEntityI.zCoord = zf;
            
            if(AlchemicalWizardry.isFMPLoaded && isMultipart(tileEntityI))
        	{
            	sendDescriptorOfTile(worldF, newTileEntityI);
        	} 
        }

        worldI.setBlock(xi, yi, zi, finalBlock, metaF, 3);

        if (tileEntityF != null)
        {        	
            TileEntity newTileEntityF = TileEntity.createAndLoadEntity(nbttag2);
            if(AlchemicalWizardry.isFMPLoaded && isMultipart(tileEntityF))
        	{
        		newTileEntityF = createMultipartFromNBT(worldI, nbttag2);
        	}
            
            worldI.setTileEntity(xi, yi, zi, newTileEntityF);
            
            newTileEntityF.xCoord = xi;
            newTileEntityF.yCoord = yi;
            newTileEntityF.zCoord = zi;
            
            if(AlchemicalWizardry.isFMPLoaded && isMultipart(tileEntityF))
        	{
            	sendDescriptorOfTile(worldI, newTileEntityF);
        	} 
        }

        return true;
    }
    
    @Optional.Method(modid = "ForgeMultipart")
    public static boolean isMultipart(TileEntity tile)
    {
    	return tile instanceof TileMultipart;
    }
    
    @Optional.Method(modid = "ForgeMultipart")
    public static TileEntity createMultipartFromNBT(World world, NBTTagCompound tag)
    {
    	return MultipartHelper.createTileFromNBT(world, tag);
    }
    
    @Optional.Method(modid = "ForgeMultipart")
    public static void sendDescriptorOfTile(World world, TileEntity tile)
    {
    	MultipartHelper.sendDescPacket(world, (TileMultipart)tile);
    }
}
