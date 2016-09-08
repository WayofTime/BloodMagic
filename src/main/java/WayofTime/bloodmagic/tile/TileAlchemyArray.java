package WayofTime.bloodmagic.tile;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.iface.IAlchemyArray;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;

public class TileAlchemyArray extends TileInventory implements ITickable, IAlchemyArray
{
    public boolean isActive = false;
    public int activeCounter = 0;

    @Getter
    @Setter
    public EnumFacing rotation = EnumFacing.HORIZONTALS[0];;

    private String key = "empty";
    private AlchemyArrayEffect arrayEffect;

    public TileAlchemyArray()
    {
        super(2, "alchemyArray");
    }

    public void onEntityCollidedWithBlock(IBlockState state, Entity entity)
    {
        if (arrayEffect != null)
        {
            arrayEffect.onEntityCollidedWithBlock(this, worldObj, pos, state, entity);
        }
    }

    @Override
    public void deserialize(NBTTagCompound tagCompound)
    {
        super.deserialize(tagCompound);
        this.isActive = tagCompound.getBoolean("isActive");
        this.activeCounter = tagCompound.getInteger("activeCounter");
        this.key = tagCompound.getString("key");
        this.rotation = EnumFacing.HORIZONTALS[tagCompound.getInteger(Constants.NBT.DIRECTION)];

        NBTTagCompound arrayTag = tagCompound.getCompoundTag("arrayTag");
        arrayEffect = AlchemyArrayRecipeRegistry.getAlchemyArrayEffect(key);
        if (arrayEffect != null)
        {
            arrayEffect.readFromNBT(arrayTag);
        }
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tagCompound)
    {
        super.serialize(tagCompound);
        tagCompound.setBoolean("isActive", isActive);
        tagCompound.setInteger("activeCounter", activeCounter);
        tagCompound.setString("key", "".equals(key) ? "empty" : key);
        tagCompound.setInteger(Constants.NBT.DIRECTION, rotation.getHorizontalIndex());

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
}
