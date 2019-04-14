package WayofTime.bloodmagic.entity.mob;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.entity.ai.EntityAIAttackRangedBow;
import WayofTime.bloodmagic.entity.ai.EntityAIFollowOwner;
import WayofTime.bloodmagic.entity.ai.*;
import WayofTime.bloodmagic.entity.ai.EntityAIOwnerHurtByTarget;
import WayofTime.bloodmagic.entity.ai.EntityAIOwnerHurtTarget;
import WayofTime.bloodmagic.item.soul.ItemSentientBow;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntitySentientSpecter extends EntityDemonBase {
    private final EntityAIAttackRangedBow aiArrowAttack = new EntityAIAttackRangedBow(this, 1.0D, 20, 15.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.0D, false);
    private final int attackPriority = 3;
    protected EnumDemonWillType type = EnumDemonWillType.DESTRUCTIVE;
    protected boolean wasGivenSentientArmour = false;

    public EntitySentientSpecter(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
//        ((PathNavigateGround) getNavigator()).setCanSwim(false);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRetreatToHeal<>(this, EntityCreature.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(attackPriority, aiAttackOnCollide);
        this.tasks.addTask(4, new EntityAIGrabEffectsFromOwner(this, 2.0D, 1.0F));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));

        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityMob.class, true));

        this.targetTasks.addTask(4, new EntityAIHurtByTargetIgnoreTamed(this, false));

        this.setCombatTask();
//        this.targetTasks.addTask(8, new EntityAINearestAttackableTarget<EntityMob>(this, EntityMob.class, 10, true, false, new TargetPredicate(this)));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
    }

    @Override
    public void setCombatTask() {
        if (!this.getEntityWorld().isRemote) {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItemMainhand();

            if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemBow) {
                int i = 20;

                if (this.getEntityWorld().getDifficulty() != EnumDifficulty.HARD) {
                    i = 40;
                }

                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(attackPriority, this.aiArrowAttack);
            } else {
                this.tasks.addTask(attackPriority, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public boolean isPotionApplicable(PotionEffect effect) {
        Potion potion = effect.getPotion();

        //Specter cannot be healed by normal means
        return !(potion == MobEffects.REGENERATION || potion == MobEffects.INSTANT_HEALTH) && super.isPotionApplicable(effect);
    }

    public boolean canStealEffectFromOwner(EntityLivingBase owner, PotionEffect effect) {
        return effect.getPotion().isBadEffect() && this.type == EnumDemonWillType.CORROSIVE;
    }

    public boolean canStealEffectFromOwner(EntityLivingBase owner) {
        if (this.type != EnumDemonWillType.CORROSIVE) {
            return false;
        }

        for (PotionEffect eff : owner.getActivePotionEffects()) {
            if (canStealEffectFromOwner(owner, eff)) {
                return true;
            }
        }

        return false;
    }

    public boolean stealEffectsFromOwner(EntityLivingBase owner) {
        if (this.type != EnumDemonWillType.CORROSIVE) {
            return false;
        }

        boolean hasStolenEffect = false;

        List<PotionEffect> removedEffects = new ArrayList<>();

        for (PotionEffect eff : owner.getActivePotionEffects()) {
            if (canStealEffectFromOwner(owner, eff)) {
                removedEffects.add(eff);
                hasStolenEffect = true;
            }
        }

        for (PotionEffect eff : removedEffects) {
            owner.removePotionEffect(eff.getPotion());
            this.addPotionEffect(eff);
        }

        return hasStolenEffect;
    }

    public boolean applyNegativeEffectsToAttacked(EntityLivingBase attackedEntity, float percentTransmitted) {
        boolean hasProvidedEffect = false;
        List<PotionEffect> removedEffects = new ArrayList<>();
        for (PotionEffect eff : this.getActivePotionEffects()) {
            if (eff.getPotion().isBadEffect() && attackedEntity.isPotionApplicable(eff)) {
                if (!attackedEntity.isPotionActive(eff.getPotion())) {
                    removedEffects.add(eff);
                    hasProvidedEffect = true;
                } else {
                    PotionEffect activeEffect = attackedEntity.getActivePotionEffect(eff.getPotion());
                    if (activeEffect.getAmplifier() < eff.getAmplifier() || activeEffect.getDuration() < eff.getDuration() * percentTransmitted) {
                        removedEffects.add(eff);
                        hasProvidedEffect = true;
                    }
                }
            }
        }

        for (PotionEffect eff : removedEffects) {
            if (!attackedEntity.isPotionActive(eff.getPotion())) {
                PotionEffect newEffect = new PotionEffect(eff.getPotion(), (int) (eff.getDuration() * percentTransmitted), eff.getAmplifier(), eff.getIsAmbient(), eff.doesShowParticles());
                attackedEntity.addPotionEffect(newEffect);

                PotionEffect newSentientEffect = new PotionEffect(eff.getPotion(), (int) (eff.getDuration() * (1 - percentTransmitted)), eff.getAmplifier(), eff.getIsAmbient(), eff.doesShowParticles());
                this.removePotionEffect(eff.getPotion());
                this.addPotionEffect(newSentientEffect);
            } else {
                PotionEffect activeEffect = attackedEntity.getActivePotionEffect(eff.getPotion());

                PotionEffect newEffect = new PotionEffect(eff.getPotion(), (int) (eff.getDuration() * percentTransmitted), eff.getAmplifier(), activeEffect.getIsAmbient(), activeEffect.doesShowParticles());
                attackedEntity.addPotionEffect(newEffect);

                PotionEffect newSentientEffect = new PotionEffect(eff.getPotion(), (int) (eff.getDuration() * (1 - percentTransmitted)), eff.getAmplifier(), eff.getIsAmbient(), eff.doesShowParticles());
                this.removePotionEffect(eff.getPotion());
                this.addPotionEffect(newSentientEffect);
            }
        }

        return hasProvidedEffect;
    }

    public List<PotionEffect> getPotionEffectsForArrowRemovingDuration(float percentTransmitted) {
        List<PotionEffect> arrowEffects = new ArrayList<>();

        if (type != EnumDemonWillType.CORROSIVE) {
            return arrowEffects;
        }

        List<PotionEffect> removedEffects = new ArrayList<>();
        for (PotionEffect eff : this.getActivePotionEffects()) {
            if (eff.getPotion().isBadEffect()) {
                removedEffects.add(eff);
            }
        }

        for (PotionEffect eff : removedEffects) {
            PotionEffect newEffect = new PotionEffect(eff.getPotion(), (int) (eff.getDuration() * percentTransmitted), eff.getAmplifier(), eff.getIsAmbient(), eff.doesShowParticles());
            arrowEffects.add(newEffect);

            PotionEffect newSentientEffect = new PotionEffect(eff.getPotion(), (int) (eff.getDuration() * (1 - percentTransmitted)), eff.getAmplifier(), eff.getIsAmbient(), eff.doesShowParticles());
            this.removePotionEffect(eff.getPotion());
            this.addPotionEffect(newSentientEffect);
        }

        return arrowEffects;
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
        boolean flag = super.attackEntityAsMob(attackedEntity);

        if (flag) {
            if (this.type == EnumDemonWillType.CORROSIVE && attackedEntity instanceof EntityLivingBase) {
//                ((EntityLivingBase) attackedEntity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 200));
                applyNegativeEffectsToAttacked((EntityLivingBase) attackedEntity, 1);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);

        if (!getEntityWorld().isRemote && !getHeldItemMainhand().isEmpty()) {
            this.entityDropItem(getHeldItemMainhand(), 0);
        }
    }

    @Override
    public boolean isStationary() {
        return false;
    }

    @Override
    public boolean absorbExplosion(Explosion explosion) {
        if (this.type == EnumDemonWillType.DESTRUCTIVE) {
            this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 600, 1));

            explosion.doExplosionB(true);

            return true;
        }

        return false;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (this.isTamed() && player.equals(this.getOwner()) && hand == EnumHand.MAIN_HAND) {
            if (stack.isEmpty() && player.isSneaking()) //Should return to the entity
            {
                if (!getEntityWorld().isRemote) {
                    if (!getHeldItemMainhand().isEmpty()) {
                        this.entityDropItem(getHeldItemMainhand(), 0);
                    }

                    if (!getHeldItemOffhand().isEmpty()) {
                        this.entityDropItem(getHeldItemOffhand(), 0);
                    }

                    if (wasGivenSentientArmour) {
                        this.entityDropItem(new ItemStack(RegistrarBloodMagicItems.SENTIENT_ARMOUR_GEM), 0);
                    }

                    this.setDead();
                }
            }
        }

        return super.processInteract(player, hand);
    }

    public boolean isEntityInvulnerable(DamageSource source) {
        return super.isEntityInvulnerable(source) && (this.type == EnumDemonWillType.DESTRUCTIVE && source.isExplosion());
    }

    @Override
    public void performEmergencyHeal(double toHeal) {
        this.heal((float) toHeal);

        if (getEntityWorld() instanceof WorldServer) {
            WorldServer server = (WorldServer) getEntityWorld();
            server.spawnParticle(EnumParticleTypes.HEART, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, 7, 0.2, 0.2, 0.2, 0);
        }
    }

    /**
     * @param toHeal
     * @return Amount of Will consumed from the Aura to heal
     */
    public double absorbWillFromAuraToHeal(double toHeal) {
        if (getEntityWorld().isRemote) {
            return 0;
        }

        double healthMissing = this.getMaxHealth() - this.getHealth();
        if (healthMissing <= 0) {
            return 0;
        }

        double will = WorldDemonWillHandler.getCurrentWill(getEntityWorld(), getPosition(), getType());

        toHeal = Math.min(healthMissing, Math.min(toHeal, will / getWillToHealth()));
        if (toHeal > 0) {
            this.heal((float) toHeal);
            return WorldDemonWillHandler.drainWill(getEntityWorld(), getPosition(), getType(), toHeal * getWillToHealth(), true);
        }

        return 0;
    }

    public double getWillToHealth() {
        return 2;
    }

    @Override
    protected boolean canDespawn() {
        return !this.isTamed() && super.canDespawn();
    }

    public void onUpdate() {
        if (!this.getEntityWorld().isRemote && this.ticksExisted % 20 == 0) {
            absorbWillFromAuraToHeal(2);
        }

        super.onUpdate();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);

        tag.setString(Constants.NBT.WILL_TYPE, type.toString());

        tag.setBoolean("sentientArmour", wasGivenSentientArmour);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);

        if (!tag.hasKey(Constants.NBT.WILL_TYPE)) {
            type = EnumDemonWillType.DEFAULT;
        } else {
            type = EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ENGLISH));
        }

        wasGivenSentientArmour = tag.getBoolean("sentientArmour");

        this.setCombatTask();
    }

    //TODO: Change to fit the given AI
    @Override
    public boolean shouldAttackEntity(EntityLivingBase attacker, EntityLivingBase owner) {
        if (!(attacker instanceof EntityCreeper) && !(attacker instanceof EntityGhast)) {
            return super.shouldAttackEntity(attacker, owner);
        }
        return false;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float velocity) {
        ItemStack heldStack = this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        if (!heldStack.isEmpty() && heldStack.getItem() == RegistrarBloodMagicItems.SENTIENT_BOW) {
            EntityTippedArrow arrowEntity = ((ItemSentientBow) heldStack.getItem()).getArrowEntity(getEntityWorld(), heldStack, target, this, velocity);
            if (arrowEntity != null) {
                List<PotionEffect> effects = getPotionEffectsForArrowRemovingDuration(0.2f);
                for (PotionEffect eff : effects) {
                    arrowEntity.addEffect(eff);
                }

                this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
                this.getEntityWorld().spawnEntity(arrowEntity);
            }
        } else {
            EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.getEntityWorld(), this); //TODO: Change to an arrow created by the Sentient Bow
            double d0 = target.posX - this.posX;
            double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - entitytippedarrow.posY;
            double d2 = target.posZ - this.posZ;
            double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
            entitytippedarrow.shoot(d0, d1 + d3 * 0.2, d2, 1.6F, 0); //TODO: Yes, it is an accurate arrow. Don't be hatin'
            int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
            int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);
            entitytippedarrow.setDamage((double) (velocity * 2.0F) + this.rand.nextGaussian() * 0.25D + (double) ((float) this.getEntityWorld().getDifficulty().getId() * 0.11F));

            if (i > 0) {
                entitytippedarrow.setDamage(entitytippedarrow.getDamage() + (double) i * 0.5D + 0.5D);
            }

            if (j > 0) {
                entitytippedarrow.setKnockbackStrength(j);
            }

            boolean burning = this.isBurning();
            burning = burning || EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, this) > 0;

            if (burning) {
                entitytippedarrow.setFire(100);
            }

            if (true) //TODO: Add potion effects to the arrows
            {
                entitytippedarrow.addEffect(new PotionEffect(MobEffects.SLOWNESS, 600));
            }

            this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
            this.getEntityWorld().spawnEntity(entitytippedarrow);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COW_AMBIENT;
    }

    @Override
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

    public EnumDemonWillType getType() {
        return type;
    }

    public void setType(EnumDemonWillType type) {
        this.type = type;
    }

    public boolean isWasGivenSentientArmour() {
        return wasGivenSentientArmour;
    }

    public void setWasGivenSentientArmour(boolean wasGivenSentientArmour) {
        this.wasGivenSentientArmour = wasGivenSentientArmour;
    }
}