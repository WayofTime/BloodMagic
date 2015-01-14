package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.common.omega.OmegaParadigm;
import WayofTime.alchemicalWizardry.common.omega.OmegaRegistry;

public class TEMimicBlock extends TileEntity
{
    private ItemStack[] inv;
    public Reagent reagent;

    private int ticksRemaining;

    public TEMimicBlock()
    {
        this.inv = new ItemStack[1];

        ticksRemaining = 0;
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        super.onDataPacket(net, packet);
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList tagList = par1NBTTagCompound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
            int slot = tag.getByte("Slot");

            if (slot >= 0 && slot < inv.length)
            {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        ticksRemaining = par1NBTTagCompound.getInteger("ticksRemaining");
        reagent = ReagentRegistry.getReagentForKey(par1NBTTagCompound.getString("reagent"));
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inv.length; i++)
        {
            ItemStack stack = inv[i];

            if (inv[i] != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                inv[i].writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        par1NBTTagCompound.setTag("Inventory", itemList);
        par1NBTTagCompound.setInteger("ticksRemaining", ticksRemaining);
        par1NBTTagCompound.setString("reagent", ReagentRegistry.getKeyForReagent(reagent));
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        this.ticksRemaining--;

        if (this.ticksRemaining <= 0)
        {
            this.returnContainedBlock();
        }
    }

    public static boolean createMimicBlockAtLocation(World world, int x, int y, int z, int duration, Block block, int meta, Reagent reagent)
    {
        if (block == null)
        {
            return false;
        }

        if (world.getTileEntity(x, y, z) == null && world.isAirBlock(x, y, z))
        {
            ItemStack item = new ItemStack(block, 1, meta);

            world.setBlock(x, y, z, ModBlocks.blockMimic);
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TEMimicBlock)
            {
                ((TEMimicBlock) tile).setContainedItem(item);
                ((TEMimicBlock) tile).setDuration(duration);
                ((TEMimicBlock) tile).reagent = reagent;
                world.markBlockForUpdate(x, y, z);
                return true;
            }
        }

        return false;
    }
    
    public static boolean createMimicBlockAtLocation(World world, int x, int y, int z, int duration, Block block, int meta)
    {
    	return createMimicBlockAtLocation(world, x, y, z, duration, block, meta);
    }

    public void setDuration(int dur)
    {
        this.ticksRemaining = dur;
    }

    public void resetDuration(int dur)
    {
        if (this.ticksRemaining < dur)
        {
            this.ticksRemaining = dur;
        }
    }

    public void setContainedItem(ItemStack item)
    {
        this.inv[0] = item;
    }

    public void returnContainedBlock()
    {
//        ItemStack item = this.inv[0];
//        if (item != null)
//        {
//            if (item.getItem() instanceof ItemBlock)
//            {
//                Block block = ((ItemBlock) item.getItem()).field_150939_a;
//                int meta = item.getItemDamage();
//
//                if (block != null)
//                {
//                    this.worldObj.setBlock(xCoord, yCoord, zCoord, block, meta, 6);
//                }
//            }
//
//        } else
        {
            this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
    }
    
    public Block getBlock()
    {
    	ItemStack item = this.inv[0];
    	if(item != null)
    	{
    		if (item.getItem() instanceof ItemBlock)
	        {
	            Block block = ((ItemBlock) item.getItem()).field_150939_a;
	            return block;
	        }
    	}
    	return null;
    }
    
    public int getMetaOfMimic()
    {
    	ItemStack item = this.inv[0];
    	if(item != null)
    	{
    		return item.getItemDamage();
    	}
    	
    	return 0;
    }
    
	public boolean getBlockEffectWhileInside(Entity entity, int x, int y, int z)
	{
		if(reagent != null)
		{
			OmegaParadigm paradigm = OmegaRegistry.getParadigmForReagent(reagent);
			if(paradigm != null)
			{
				return paradigm.getBlockEffectWhileInside(entity, x, y, z);
			}
		}
		
		return false;
	}
}
