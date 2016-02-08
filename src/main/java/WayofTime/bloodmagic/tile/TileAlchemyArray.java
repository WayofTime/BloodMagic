package WayofTime.bloodmagic.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;

public class TileAlchemyArray extends TileInventory implements ITickable
{
    public boolean isActive = false;
    public int activeCounter = 0;

    public TileAlchemyArray()
    {
        super(2, "alchemyArray");
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        this.isActive = tagCompound.getBoolean("isActive");
        this.activeCounter = tagCompound.getInteger("activeCounter");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("isActive", isActive);
        tagCompound.setInteger("activeCounter", activeCounter);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public void update()
    {
        if (isActive && attemptCraft())
        {
            activeCounter++;
        } else
        {
            isActive = false;
            activeCounter = 0;
        }
    }

    public boolean attemptCraft()
    {
        AlchemyArrayEffect effect = AlchemyArrayRecipeRegistry.getAlchemyArrayEffect(this.getStackInSlot(0), this.getStackInSlot(1));
        if (effect != null)
        {
            isActive = true;

            if (effect.update(this, this.activeCounter))
            {
                this.decrStackSize(0, 1);
                this.decrStackSize(1, 1);
                this.worldObj.setBlockToAir(getPos());
            }

            return true;
        }

        return false;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(pos, this.getBlockMetadata(), nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        super.onDataPacket(net, packet);
        readFromNBT(packet.getNbtCompound());
    }
}
