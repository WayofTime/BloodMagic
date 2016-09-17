package WayofTime.bloodmagic.entity.mob;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.gson.Serializers;

public class EntityAspectedDemonBase extends EntityDemonBase
{
    protected static final DataParameter<EnumDemonWillType> TYPE = EntityDataManager.<EnumDemonWillType>createKey(EntityAspectedDemonBase.class, Serializers.WILL_TYPE_SERIALIZER);

    public EntityAspectedDemonBase(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(TYPE, EnumDemonWillType.DEFAULT);
    }

    public EnumDemonWillType getType()
    {
        return this.dataManager.get(TYPE);
    }

    public void setType(EnumDemonWillType type)
    {
        this.dataManager.set(TYPE, type);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);

        tag.setString(Constants.NBT.WILL_TYPE, this.getType().toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);

        if (!tag.hasKey(Constants.NBT.WILL_TYPE))
        {
            setType(EnumDemonWillType.DEFAULT);
        } else
        {
            setType(EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE)));
        }
    }
}
