package WayofTime.bloodmagic.entity.mob;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityDemonBase extends EntityCreature implements IEntityOwnable {
    protected static final DataParameter<Byte> TAMED = EntityDataManager.createKey(EntityDemonBase.class, DataSerializers.BYTE);
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityDemonBase.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityDemonBase(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(TAMED, (byte) 0);
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    }

    public void setCombatTask() {

    }

    @Override
    public boolean isPotionApplicable(PotionEffect effect) {
        return super.isPotionApplicable(effect);
    }

    @Override
    public void onLivingUpdate() {
        this.updateArmSwingProgress();

        super.onLivingUpdate();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return !this.isEntityInvulnerable(source) && super.attackEntityFrom(source, amount);
    }

    /**
     * Redone from EntityMob to prevent despawning on peaceful.
     */
    @Override
    public boolean attackEntityAsMob(Entity attackedEntity) {
        float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (attackedEntity instanceof EntityLivingBase) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) attackedEntity).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = attackedEntity.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag) {
            if (i > 0) {
                ((EntityLivingBase) attackedEntity).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0) {
                attackedEntity.setFire(j * 4);
            }

            if (attackedEntity instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) attackedEntity;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD) {
                    float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1) {
                        entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                        this.getEntityWorld().setEntityState(entityplayer, (byte) 30);
                    }
                }
            }

            this.applyEnchantments(this, attackedEntity);
        }

        return flag;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        super.setItemStackToSlot(slotIn, stack);

        if (!this.getEntityWorld().isRemote && slotIn == EntityEquipmentSlot.MAINHAND) {
            this.setCombatTask();
        }
    }

    public boolean isStationary() {
        return false;
    }

    public boolean absorbExplosion(Explosion explosion) {
        return false;
    }

    public void performEmergencyHeal(double toHeal) {
        this.heal((float) toHeal);

        if (getEntityWorld() instanceof WorldServer) {
            WorldServer server = (WorldServer) getEntityWorld();
            server.spawnParticle(EnumParticleTypes.HEART, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 7, 0.2, 0.2, 0.2, 0);
        }
    }

    public boolean shouldEmergencyHeal() {
        return this.getHealth() < this.getMaxHealth() * 0.5;
    }

    @Override
    protected boolean canDespawn() {
        return !this.isTamed() && super.canDespawn();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);

        if (this.getOwnerId() == null) {
            tag.setString("OwnerUUID", "");
        } else {
            tag.setString("OwnerUUID", this.getOwnerId().toString());
        }

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);

        String s;

        if (tag.hasKey("OwnerUUID", 8)) {
            s = tag.getString("OwnerUUID");
        } else {
            String s1 = tag.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }

        if (!s.isEmpty()) {
            try {
                this.setOwnerId(UUID.fromString(s));
                this.setTamed(true);
            } catch (Throwable var4) {
                this.setTamed(false);
            }
        }

        this.setCombatTask();
    }

    //TODO: Change to fit the given AI
    public boolean shouldAttackEntity(EntityLivingBase attacker, EntityLivingBase owner) {
        if (!(attacker instanceof EntityCreeper) && !(attacker instanceof EntityGhast)) {
            if (attacker instanceof IEntityOwnable) {
                IEntityOwnable entityOwnable = (IEntityOwnable) attacker;

                if (entityOwnable.getOwner() == owner) {
                    return false;
                }
            }

            return !(attacker instanceof EntityPlayer && owner instanceof EntityPlayer && !((EntityPlayer) owner).canAttackPlayer((EntityPlayer) attacker)) && (!(attacker instanceof EntityHorse) || !((EntityHorse) attacker).isTame());
        } else {
            return false;
        }
    }

    public void attackEntityWithRangedAttack(EntityLivingBase target, float velocity) {

    }

    public boolean isTamed() {
        return (this.dataManager.get(TAMED) & 4) != 0;
    }

    public void setTamed(boolean tamed) {
        byte b0 = this.dataManager.get(TAMED);

        if (tamed) {
            this.dataManager.set(TAMED, (byte) (b0 | 4));
        } else {
            this.dataManager.set(TAMED, (byte) (b0 & -5));
        }

//        this.setupTamedAI();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COW_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return getHurtSound();
    }

    protected SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COW_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public UUID getOwnerId() {
        return (this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
    }

    public void setOwnerId(UUID uuid) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(uuid));
    }

    @Override
    public EntityLivingBase getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.getEntityWorld().getPlayerEntityByUUID(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public void setOwner(EntityPlayer player) {
        setOwnerId(player.getUniqueID());
    }

    public class TargetPredicate implements Predicate<EntityMob> {
        EntityDemonBase entity;

        public TargetPredicate(EntityDemonBase entity) {
            this.entity = entity;
        }

        @Override
        public boolean apply(EntityMob input) {
            return entity.shouldAttackEntity(input, this.entity.getOwner());
        }
    }
}