package WayofTime.bloodmagic.entity.mob;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.gson.Serializers;

public abstract class EntityAspectedDemonBase extends EntityDemonBase
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

    public double getMeleeResist()
    {
        return 0;
    }

    public double getProjectileResist()
    {
        return 0;
    }

    public double getMagicResist()
    {
        return 0;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        } else
        {
            float newAmount = amount;
            if (source.isProjectile())
            {
                newAmount *= MathHelper.clamp_double(1 - getProjectileResist(), 0, 1);
            } else
            {
                newAmount *= MathHelper.clamp_double(1 - getMeleeResist(), 0, 1);
            }

            if (source.isMagicDamage())
            {
                newAmount *= MathHelper.clamp_double(1 - getMagicResist(), 0, 1);
            }

            return super.attackEntityFrom(source, newAmount);
        }
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
