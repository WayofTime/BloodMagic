package WayofTime.bloodmagic.entity.mob;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.entity.ai.EntityAIAttackRangedBow;

public class EntityCorruptedZombie extends EntityDemonBase
{
    @Getter
    @Setter
    protected EnumDemonWillType type = EnumDemonWillType.DEFAULT;

    private final EntityAIAttackRangedBow aiArrowAttack = new EntityAIAttackRangedBow(this, 1.0D, 20, 15.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.0D, false);

    private final int attackPriority = 3;

    public EntityCorruptedZombie(World worldIn)
    {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
//        ((PathNavigateGround) getNavigator()).setCanSwim(false);

        this.setCombatTask();
//        this.targetTasks.addTask(8, new EntityAINearestAttackableTarget<EntityMob>(this, EntityMob.class, 10, true, false, new TargetPredicate(this)));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
    }

    @Override
    public void setCombatTask()
    {
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItemMainhand();

            if (itemstack != null && itemstack.getItem() instanceof ItemBow)
            {
                int i = 20;

                if (this.worldObj.getDifficulty() != EnumDifficulty.HARD)
                {
                    i = 40;
                }

                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(attackPriority, this.aiArrowAttack);
            } else
            {
                this.tasks.addTask(attackPriority, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return this.isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
    }

    /**
     * Redone from EntityMob to prevent despawning on peaceful.
     */
    @Override
    public boolean attackEntityAsMob(Entity attackedEntity)
    {
        boolean flag = super.attackEntityAsMob(attackedEntity);

        if (flag)
        {
            //EMPTY

            return true;
        } else
        {
            return false;
        }
    }

    /**
     * @param toHeal
     * @return Amount of Will consumed from the Aura to heal
     */
    public double absorbWillFromAuraToHeal(double toHeal)
    {
        if (worldObj.isRemote)
        {
            return 0;
        }

        double healthMissing = this.getMaxHealth() - this.getHealth();
        if (healthMissing <= 0)
        {
            return 0;
        }

        double will = WorldDemonWillHandler.getCurrentWill(worldObj, getPosition(), getType());

        toHeal = Math.min(healthMissing, Math.min(toHeal, will / getWillToHealth()));
        if (toHeal > 0)
        {
            this.heal((float) toHeal);
            return WorldDemonWillHandler.drainWill(worldObj, getPosition(), getType(), toHeal * getWillToHealth(), true);
        }

        return 0;
    }

    public double getWillToHealth()
    {
        return 2;
    }

    @Override
    protected boolean canDespawn()
    {
        return !this.isTamed() && super.canDespawn();
    }

    public void onUpdate()
    {
        if (!this.worldObj.isRemote && this.ticksExisted % 20 == 0)
        {
            absorbWillFromAuraToHeal(2);
        }

        super.onUpdate();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);

        tag.setString(Constants.NBT.WILL_TYPE, type.toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);

        if (!tag.hasKey(Constants.NBT.WILL_TYPE))
        {
            type = EnumDemonWillType.DEFAULT;
        } else
        {
            type = EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE));
        }

        this.setCombatTask();
    }

    //TODO: Change to fit the given AI
    @Override
    public boolean shouldAttackEntity(EntityLivingBase attacker, EntityLivingBase owner)
    {
        if (!(attacker instanceof EntityCreeper) && !(attacker instanceof EntityGhast))
        {
            return super.shouldAttackEntity(attacker, owner);
        } else
        {
            return false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound()
    {
        return SoundEvents.ENTITY_COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_COW_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block)
    {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }
}