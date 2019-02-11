package WayofTime.bloodmagic.entity.projectile;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.soul.PlayerDemonWillHandler;
import WayofTime.bloodmagic.util.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

public class EntitySentientArrow extends EntityTippedArrow {
    public PotionType potion = PotionTypes.EMPTY;
    public double reimbursedAmountOnHit = 0;
    public EnumDemonWillType type = EnumDemonWillType.DEFAULT;
    public int currentLevel = 0;
    public ItemStack itemStack;
    public Class<? extends EntityArrow> specialArrowClass;
    public float[] destructiveExplosionRadius = {0.5f, 1, 1.5f, 2, 2.5f, 3, 3.5f};
    public int[] poisonDuration = {50, 100, 150, 80, 120, 160, 200};
    public int[] poisonLevel = {0, 0, 0, 1, 1, 1, 1};
    public int[] levitationDuration = {20, 40, 60, 80, 100, 120, 160};
    public int[] levitationLevel = {0, 0, 0, 1, 1, 1, 2};
    public int[] slownessDuration = {40, 60, 100, 150, 200, 250, 300};
    public int[] slownessLevel = {0, 0, 0, 1, 1, 1, 2};
    public EntityArrow specialEntity;
    public MethodHandle specialHitMH;
    public Method specialHit;

    public EntitySentientArrow(World worldIn) {
        super(worldIn);
    }

    public EntitySentientArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntitySentientArrow(World worldIn, EntityLivingBase shooter, EnumDemonWillType type, double reimburseAmount, int currentLevel) {
        super(worldIn, shooter);
        this.reimbursedAmountOnHit = reimburseAmount;
        this.type = type;
        this.currentLevel = currentLevel;
    }

    public EntitySentientArrow(World worldIn, EntityLivingBase shooter, EnumDemonWillType type, double reimburseAmount, int currentLevel, PotionType potion) {
        super(worldIn, shooter);
        this.reimbursedAmountOnHit = reimburseAmount;
        this.type = type;
        this.currentLevel = currentLevel;
        this.potion = potion;
    }

    public EntitySentientArrow(World worldIn, EntityLivingBase shooter, EnumDemonWillType type, double reimburseAmount, int currentLevel, ItemStack itemStack) {
        super(worldIn, shooter);
        this.reimbursedAmountOnHit = reimburseAmount;
        this.type = type;
        this.currentLevel = currentLevel;
        this.potion = PotionUtils.getPotionFromItem(itemStack);
    }

    public EntitySentientArrow(World worldIn, EntityLivingBase shooter, EnumDemonWillType type, double reimburseAmount, int currentLevel, EntityArrow specialArrow) {
        super(worldIn, shooter);
        this.reimbursedAmountOnHit = reimburseAmount;
        this.type = type;
        this.currentLevel = currentLevel;
        this.specialEntity = specialArrow;
        this.specialArrowClass = specialArrow.getClass();
    }

    public void reimbursePlayer(EntityLivingBase hitEntity, float damage) {
        if (this.shootingEntity instanceof EntityPlayer) {
            if (hitEntity.getEntityWorld().getDifficulty() != EnumDifficulty.PEACEFUL && !(hitEntity instanceof IMob)) {
                return;
            }

            PlayerDemonWillHandler.addDemonWill(type, (EntityPlayer) this.shootingEntity, reimbursedAmountOnHit * damage / 20f);
        }
    }

    @Override
    protected void arrowHit(EntityLivingBase living) {
        int amp = -1;
        switch (type) {
            case CORROSIVE:
                if (this.potion != null)
                    for (PotionEffect i : this.potion.getEffects()) {
                        if (i.getEffectName().equals("poison")) {
                            amp = i.getAmplifier();
                            continue;
                        }
                        living.addPotionEffect(new PotionEffect(i.getPotion(), i.getDuration(), i.getAmplifier()));
                    }
                living.addPotionEffect(new PotionEffect(MobEffects.POISON, currentLevel >= 0 ? (amp > -1 && poisonLevel[currentLevel] != amp) ? poisonDuration[currentLevel] / 2 : poisonDuration[currentLevel] : 0, currentLevel >= 0 ? (amp > -1) ? Math.max(poisonLevel[currentLevel], amp) + 1 : poisonLevel[currentLevel] : 0));
                break;
            case DEFAULT:
                if (this.potion != null)
                    for (PotionEffect i : this.potion.getEffects()) {
                        living.addPotionEffect(new PotionEffect(i.getPotion(), i.getDuration(), i.getAmplifier()));
                    }
                break;
            case DESTRUCTIVE:
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, currentLevel >= 0 ? destructiveExplosionRadius[currentLevel] : 0, false);
                createPotionFromArrow(living);
                break;
            case STEADFAST:
                if (this.potion != null)
                    for (PotionEffect i : this.potion.getEffects()) {
                        if (i.getEffectName().equals("levitation")) {
                            amp = i.getAmplifier();
                            continue;
                        }
                        living.addPotionEffect(new PotionEffect(i.getPotion(), i.getDuration(), i.getAmplifier()));
                    }
                living.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, currentLevel >= 0 ? (amp > -1 && levitationLevel[currentLevel] != amp) ? levitationDuration[currentLevel] / 2 : levitationDuration[currentLevel] : 0, currentLevel >= 0 ? (amp > -1) ? Math.max(levitationLevel[currentLevel], amp) + 1 : levitationLevel[currentLevel] : 0));
                break;
            case VENGEFUL:
                if (this.potion != null)
                    for (PotionEffect i : this.potion.getEffects()) {
                        if (i.getEffectName().equals("slowness")) {
                            amp = i.getAmplifier();
                            continue;
                        }
                        living.addPotionEffect(new PotionEffect(i.getPotion(), i.getDuration(), i.getAmplifier()));
                    }
                living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, currentLevel >= 0 ? (amp > -1 && slownessLevel[currentLevel] != amp) ? slownessDuration[currentLevel] / 2 : slownessDuration[currentLevel] : 0, currentLevel >= 0 ? (amp > -1) ? Math.max(slownessLevel[currentLevel], amp) + 1 : slownessLevel[currentLevel] : 0));
                break;
            default:
                break;
        }
        if (this.specialArrowClass != null) {
            try {
                this.specialHit = this.specialArrowClass.getMethod("arrowHit", EntityLivingBase.class);
                this.specialHitMH = MethodHandles.lookup().unreflect(this.specialHit).bindTo(this.specialEntity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (this.specialHitMH != null)
                        this.specialHitMH.invoke(living);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.specialArrowClass != null) {
            if (!this.world.isRemote) {
                this.specialEntity.posX = this.posX;
                this.specialEntity.posY = this.posY;
                this.specialEntity.posZ = this.posZ;

                this.specialEntity.onUpdate();
                if (this.inGround) {
                    this.specialEntity.setDead();
                }
            }
        }
        switch (type) {
            case DESTRUCTIVE:
                if (this.potion != null) {
                    this.spawnPotionParticles(2);
                }
                if (!this.world.isRemote && this.inGround) {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, currentLevel >= 0 ? destructiveExplosionRadius[currentLevel] : 0, false);
                    if (this.potion != null && this.specialArrowClass == null) {
                        createPotionFromArrow(null);
                    }
                    this.setDead();
                }
                break;
            case CORROSIVE:
                this.spawnPotionParticles(2);

                break;
            case DEFAULT:
                if (this.potion != null) {
                    this.spawnPotionParticles(2);
                }
                break;
            case STEADFAST:
                if (this.potion != null) {
                    this.spawnPotionParticles(2);
                }
                break;
            case VENGEFUL:
                if (this.potion != null) {
                    this.spawnPotionParticles(2);
                }
                break;
            default:
                break;
        }


    }

    //TODO: Potion splash (for destructive will fired tipped arrows) currently does not have a visual effect.
    private void createPotionFromArrow(EntityLivingBase living) {
        if (this.potion != null) {
            float radius = currentLevel >= 0 ? destructiveExplosionRadius[currentLevel] : 0;
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(radius * 2, radius, radius * 2);
            List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

            if (!list.isEmpty()) {
                for (EntityLivingBase entitylivingbase : list) {
                    if (entitylivingbase.canBeHitWithPotion()) {
                        double d0 = this.getDistanceSq(entitylivingbase);

                        if (d0 < 16.0D) {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                            if (entitylivingbase == living) {
                                d1 = 1.0D;
                            }

                            for (PotionEffect potioneffect : this.potion.getEffects()) {
                                Potion potion = potioneffect.getPotion();

                                if (potion.isInstant()) {
                                    potion.affectEntity(this, this.shootingEntity, entitylivingbase, potioneffect.getAmplifier(), d1);
                                } else {
                                    int i = (int) (d1 * (double) potioneffect.getDuration() + 0.5D);

                                    if (i > 20) {
                                        entitylivingbase.addPotionEffect(new PotionEffect(potion, i, potioneffect.getAmplifier(), potioneffect.getIsAmbient(), potioneffect.doesShowParticles()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);

        tag.setDouble("reimbursement", reimbursedAmountOnHit);
        tag.setInteger("currentLevel", currentLevel);
        tag.setString(Constants.NBT.WILL_TYPE, type.toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);

        reimbursedAmountOnHit = tag.getDouble("reimbursement");
        type = EnumDemonWillType.valueOf(tag.getString(Constants.NBT.WILL_TYPE).toUpperCase(Locale.ENGLISH));
        currentLevel = tag.getInteger("currentLevel");
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }

    public void spawnPotionParticles(int particleCount) {
        int i = this.getColor();

        if (i != -1 && particleCount > 0) {
            double d0 = (double) (i >> 16 & 255) / 255.0D;
            double d1 = (double) (i >> 8 & 255) / 255.0D;
            double d2 = (double) (i >> 0 & 255) / 255.0D;

            for (int j = 0; j < particleCount; ++j) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, d0, d1, d2);
            }
        }
    }
}
