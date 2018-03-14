package WayofTime.bloodmagic.entity.mob;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.gson.Serializers;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Locale;

public abstract class EntityAspectedDemonBase extends EntityDemonBase {
    protected static final DataParameter<EnumDemonWillType> TYPE = EntityDataManager.createKey(EntityAspectedDemonBase.class, Serializers.WILL_TYPE_SERIALIZER);

    public EntityAspectedDemonBase(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TYPE, EnumDemonWillType.DEFAULT);
    }

    public double getMeleeResist() {
        return 0;
    }

    public double getProjectileResist() {
        return 0;
    }

    public double getMagicResist() {
        return 0;
    }

    public double getBaseHP(EnumDemonWillType type) {
        double baseHP = 40;

        switch (type) {
            case DEFAULT:
                break;
            case CORROSIVE:
                break;
            case DESTRUCTIVE:
                break;
            case VENGEFUL:
                baseHP *= 0.8;
                break;
            case STEADFAST:
                baseHP *= 1.25;
                break;
        }

        return baseHP;
    }

    public double getBaseMeleeDamage(EnumDemonWillType type) {
        double baseDamage = 8;

        switch (type) {
            case DEFAULT:
                break;
            case CORROSIVE:
                baseDamage *= 0.8;
                break;
            case DESTRUCTIVE:
                baseDamage *= 1.5;
                break;
            case VENGEFUL:
                baseDamage *= 0.8;
                break;
            case STEADFAST:
                baseDamage *= 0.6;
                break;
        }

        return baseDamage;
    }

    public double getBaseSpeed(EnumDemonWillType type) {
        double baseSpeed = 0.27;

        switch (type) {
            case DEFAULT:
                break;
            case CORROSIVE:
                break;
            case DESTRUCTIVE:
                break;
            case VENGEFUL:
                baseSpeed *= 1.3;
                break;
            case STEADFAST:
                break;
        }

        return baseSpeed;
    }

    public double getBaseSprintModifier(EnumDemonWillType type) {
        double baseSprint = 1;

        switch (type) {
            case DEFAULT:
                break;
            case CORROSIVE:
                break;
            case DESTRUCTIVE:
                break;
            case VENGEFUL:
                baseSprint *= 1.2;
                break;
            case STEADFAST:
                break;
        }

        return baseSprint;
    }

    public double getBaseKnockbackResist(EnumDemonWillType type) {
        double baseKnockback = 0;

        switch (type) {
            case DEFAULT:
                break;
            case CORROSIVE:
                break;
            case DESTRUCTIVE:
                break;
            case VENGEFUL:
                break;
            case STEADFAST:
                baseKnockback += 0.35;
                break;
        }

        return baseKnockback;
    }

    public void applyEntityAttributes(EnumDemonWillType type) {
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getBaseHP(type));
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getBaseSpeed(type));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.getBaseMeleeDamage(type));
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(this.getBaseKnockbackResist(type));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else {
            float newAmount = amount;
            if (source.isProjectile()) {
                newAmount *= MathHelper.clamp(1 - getProjectileResist(), 0, 1);
            } else {
                newAmount *= MathHelper.clamp(1 - getMeleeResist(), 0, 1);
            }

            if (source.isMagicDamage()) {
                newAmount *= MathHelper.clamp(1 - getMagicResist(), 0, 1);
            }

            return super.attackEntityFrom(source, newAmount);
        }
    }

    public EnumDemonWillType getType() {
        return this.dataManager.get(TYPE);
    }

    public void setType(EnumDemonWillType type) {
        this.dataManager.set(TYPE, type);
        this.applyEntityAttributes(type);
        this.setCombatTask();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);

        tag.setString(Constants.NBT.WILL_TYPE, this.getType().toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);

        if (!tag.hasKey(Constants.NBT.WILL_TYPE)) {
            setType(EnumDemonWillType.DEFAULT);
        } else {
            setType(EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ENGLISH)));
        }
    }

    //Returns true if the inputted mob is on the same team.
    public static class WillTypePredicate implements Predicate<EntityLivingBase> {
        private final EnumDemonWillType type;

        public WillTypePredicate(EnumDemonWillType type) {
            this.type = type;
        }

        //Returns true if this mob is the same type.
        @Override
        public boolean apply(EntityLivingBase input) {
            if (input instanceof EntityAspectedDemonBase) {
                if (((EntityAspectedDemonBase) input).getType() == type) {
                    return true;
                }
            }

            return false;
        }
    }

    public class TeamAttackPredicate implements Predicate<EntityLivingBase> {
        private final EntityAspectedDemonBase demon;

        public TeamAttackPredicate(EntityAspectedDemonBase demon) {
            this.demon = demon;
        }

        //Returns true if this mob can attack the inputted mob.
        @Override
        public boolean apply(EntityLivingBase input) {
            if (input instanceof EntityAspectedDemonBase) {
                if (((EntityAspectedDemonBase) input).getType() == demon.getType()) {
                    return false;
                }
            }

            return input != null;
        }
    }
}
