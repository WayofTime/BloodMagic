package WayofTime.alchemicalWizardry.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.fml.common.Optional;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.event.TeleposeEvent;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;
import WayofTime.alchemicalWizardry.common.items.TelepositionFocus;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
//import codechicken.multipart.MultipartHelper;
//import codechicken.multipart.TileMultipart;

public class BlockTeleposer extends BlockContainer
{
    public BlockTeleposer()
    {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem != null)
        {
            if (playerItem.getItem() instanceof TelepositionFocus)
            {
            	SoulNetworkHandler.checkAndSetItemPlayer(playerItem, player);
            	
                if (playerItem.getTagCompound() == null)
                {
                    playerItem.setTagCompound(new NBTTagCompound());
                }

                NBTTagCompound itemTag = playerItem.getTagCompound();
                itemTag.setInteger("xCoord", pos.getX());
                itemTag.setInteger("yCoord", pos.getY());
                itemTag.setInteger("zCoord", pos.getZ());
                itemTag.setInteger("dimensionId", world.provider.getDimensionId());
                return true;
            }
        }
        player.openGui(AlchemicalWizardry.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        dropItems(world, pos);
        super.breakBlock(world, pos, state);
    }

    private void dropItems(World world, BlockPos pos)
    {
        Random rand = new Random();
        TileEntity tileEntity = world.getTileEntity(pos);

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
                EntityItem entityItem = new EntityItem(world, pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

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
    
    public static boolean swapBlocks(Object caller, World worldI, World worldF, BlockPos posi, BlockPos posf)
    {
    	return swapBlocks(caller, worldI, worldF, posi, posf, true, 3);
    }
    
    public static boolean swapBlocksWithoutSound(Object caller, World worldI, World worldF, BlockPos posi, BlockPos posf)
    {
    	return swapBlocks(caller, worldI, worldF, posi, posf, false, 3);
    }

    public static boolean swapBlocks(Object caller, World worldI, World worldF, BlockPos posi, BlockPos posf, boolean doSound, int flag)
    {
        TileEntity tileEntityI = worldI.getTileEntity(posi);
        TileEntity tileEntityF = worldF.getTileEntity(posf);

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

        IBlockState stateI = worldI.getBlockState(posi);
        Block blockI = stateI.getBlock();
        IBlockState stateF = worldF.getBlockState(posf);
        Block blockF = stateF.getBlock();


        if (blockI.equals(Blocks.air) && blockF.equals(Blocks.air))
        {
            return false;
        }

        if (blockI instanceof BlockMobSpawner || blockF instanceof BlockMobSpawner || caller instanceof TEDemonPortal ? false : blockI instanceof BlockPortal || blockF instanceof BlockPortal)
        {
            return false;
        }
        
        TeleposeEvent evt = new TeleposeEvent(worldI, posi, stateI, worldF, posf, stateF);
        if (MinecraftForge.EVENT_BUS.post(evt))
            return false;
        
        if(doSound)
        {
        	worldI.playSoundEffect(posi.getX(), posi.getY(), posi.getZ(), "mob.endermen.portal", 1.0F, 1.0F);
            worldF.playSoundEffect(posf.getX(), posf.getY(), posf.getZ(), "mob.endermen.portal", 1.0F, 1.0F);
        }
        
        //CLEAR TILES
        Block finalBlock = blockF;

        if (finalBlock != null)
        {
            TileEntity tileToSet = finalBlock.createTileEntity(worldF, stateF);

            worldF.setTileEntity(posf, tileToSet);
        }

        if (blockI != null)
        {
            TileEntity tileToSet = blockI.createTileEntity(worldI, stateI);

            worldI.setTileEntity(posi, tileToSet);
        }

        //TILES CLEARED
        worldF.setBlockState(posf, stateI, flag);

        if (tileEntityI != null)
        {
            TileEntity newTileEntityI = TileEntity.createAndLoadEntity(nbttag1);
            
//            if(AlchemicalWizardry.isFMPLoaded && isMultipart(tileEntityI))
        	{
//        		newTileEntityI = createMultipartFromNBT(worldF, nbttag1);
        	}
                        
            worldF.setTileEntity(posf, newTileEntityI);
            
            newTileEntityI.setPos(posf);
//           if(AlchemicalWizardry.isFMPLoaded && isMultipart(tileEntityI))
        	{
//            	sendDescriptorOfTile(worldF, newTileEntityI);
        	} 
        }

        worldI.setBlockState(posi, stateF, flag);

        if (tileEntityF != null)
        {        	
            TileEntity newTileEntityF = TileEntity.createAndLoadEntity(nbttag2);
//            if(AlchemicalWizardry.isFMPLoaded && isMultipart(tileEntityF))
        	{
//        		newTileEntityF = createMultipartFromNBT(worldI, nbttag2);
        	}
            
            worldI.setTileEntity(posi, newTileEntityF);
            
            newTileEntityF.setPos(posi);
            
//            if(AlchemicalWizardry.isFMPLoaded && isMultipart(tileEntityF))
        	{
//            	sendDescriptorOfTile(worldI, newTileEntityF);
        	} 
        }

        return true;
    }
    
/*    @Optional.Method(modid = "ForgeMultipart")
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
    	MultipartHelper.sendDescPacket(world, tile);
    }
    */
}
