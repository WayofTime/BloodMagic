package WayofTime.bloodmagic.tile;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;

public class TileAlchemyArray extends TileInventory implements ITickable
{
    public boolean isActive = false;
    public int activeCounter = 0;

    private String key = "empty";
    private AlchemyArrayEffect arrayEffect;

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
        this.key = tagCompound.getString("key");

        NBTTagCompound arrayTag = tagCompound.getCompoundTag("arrayTag");
        arrayEffect = AlchemyArrayRecipeRegistry.getAlchemyArrayEffect(key);
        if (arrayEffect != null)
        {
            arrayEffect.readFromNBT(arrayTag);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("isActive", isActive);
        tagCompound.setInteger("activeCounter", activeCounter);
        tagCompound.setString("key", "".equals(key) ? "empty" : key);

        NBTTagCompound arrayTag = new NBTTagCompound();
        if (arrayEffect != null)
        {
            arrayEffect.writeToNBT(arrayTag);
        }
        tagCompound.setTag("arrayTag", arrayTag);

        return tagCompound;
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
            arrayEffect = null;
            key = "empty";
        }
    }

    /**
     * This occurs when the block is destroyed.
     */
    @Override
    public void dropItems()
    {
        super.dropItems();
        if (arrayEffect != null)
        {

        }
    }

    public boolean attemptCraft()
    {
        AlchemyArrayEffect effect = AlchemyArrayRecipeRegistry.getAlchemyArrayEffect(this.getStackInSlot(0), this.getStackInSlot(1));
        if (effect != null)
        {
            if (arrayEffect == null)
            {
                arrayEffect = effect;
                key = effect.getKey();
            } else
            {
                String effectKey = effect.getKey();
                if (effectKey.equals(key))
                {
                    //Good! Moving on.
                } else
                {
                    //Something has changed, therefore we have to move our stuffs.
                    //TODO: Add an AlchemyArrayEffect.onBreak(); ?
                    arrayEffect = effect;
                    key = effect.getKey();
                }
            }
        } else
        {
            return false;
        }

        if (arrayEffect != null)
        {
            isActive = true;

            if (arrayEffect.update(this, this.activeCounter))
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
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new SPacketUpdateTileEntity(pos, this.getBlockMetadata(), nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
    {
        super.onDataPacket(net, packet);
        readFromNBT(packet.getNbtCompound());
    }
}
