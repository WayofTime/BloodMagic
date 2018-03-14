package WayofTime.bloodmagic.entity.mob;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.entity.ai.EntityAIEatAndCorruptBlock;
import WayofTime.bloodmagic.entity.ai.EntityAIProtectAlly;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EntityCorruptedSheep extends EntityAspectedDemonBase implements IShearable {
    private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntityCorruptedSheep.class, DataSerializers.BYTE);

    private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
    public static int maxProtectionCooldown = 90 * 20; //90 second cooldown

    static {
        DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[]{1.0F, 1.0F, 1.0F});
        DYE_TO_RGB.put(EnumDyeColor.ORANGE, new float[]{0.85F, 0.5F, 0.2F});
        DYE_TO_RGB.put(EnumDyeColor.MAGENTA, new float[]{0.7F, 0.3F, 0.85F});
        DYE_TO_RGB.put(EnumDyeColor.LIGHT_BLUE, new float[]{0.4F, 0.6F, 0.85F});
        DYE_TO_RGB.put(EnumDyeColor.YELLOW, new float[]{0.9F, 0.9F, 0.2F});
        DYE_TO_RGB.put(EnumDyeColor.LIME, new float[]{0.5F, 0.8F, 0.1F});
        DYE_TO_RGB.put(EnumDyeColor.PINK, new float[]{0.95F, 0.5F, 0.65F});
        DYE_TO_RGB.put(EnumDyeColor.GRAY, new float[]{0.3F, 0.3F, 0.3F});
        DYE_TO_RGB.put(EnumDyeColor.SILVER, new float[]{0.6F, 0.6F, 0.6F});
        DYE_TO_RGB.put(EnumDyeColor.CYAN, new float[]{0.3F, 0.5F, 0.6F});
        DYE_TO_RGB.put(EnumDyeColor.PURPLE, new float[]{0.5F, 0.25F, 0.7F});
        DYE_TO_RGB.put(EnumDyeColor.BLUE, new float[]{0.2F, 0.3F, 0.7F});
        DYE_TO_RGB.put(EnumDyeColor.BROWN, new float[]{0.4F, 0.3F, 0.2F});
        DYE_TO_RGB.put(EnumDyeColor.GREEN, new float[]{0.4F, 0.5F, 0.2F});
        DYE_TO_RGB.put(EnumDyeColor.RED, new float[]{0.6F, 0.2F, 0.2F});
        DYE_TO_RGB.put(EnumDyeColor.BLACK, new float[]{0.1F, 0.1F, 0.1F});
    }

    private final int attackPriority = 3;
    public int protectionCooldown = 0;
    /**
     * Used to control movement as well as wool regrowth. Set to 40 on
     * handleHealthUpdate and counts down with each tick.
     */
    private int sheepTimer;
    private int castTimer = 0;
    private EntityAIEatAndCorruptBlock entityAIEatGrass;
    private EntityAIProtectAlly entityAIProtectAlly;
    private EntityAIAttackMelee aiAttackOnCollide;

    public EntityCorruptedSheep(World world) {
        this(world, EnumDemonWillType.DEFAULT);
    }

    public EntityCorruptedSheep(World world, EnumDemonWillType type) {
        super(world);
        this.setSize(0.9F, 1.3F);

        this.setType(type);
    }

    protected void initEntityAI() {
        this.entityAIEatGrass = new EntityAIEatAndCorruptBlock(this);
        this.entityAIProtectAlly = new EntityAIProtectAlly(this);

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, entityAIProtectAlly);
        this.tasks.addTask(5, this.entityAIEatGrass);
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));

        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 10, true, false, new EntityAspectedDemonBase.TeamAttackPredicate(this)));
    }

    @Override
    public void setCombatTask() {
        if (aiAttackOnCollide != null) {
            this.tasks.removeTask(aiAttackOnCollide);
        }

        aiAttackOnCollide = new EntityAIAttackMelee(this, this.getBaseSprintModifier(getType()), false);
        this.tasks.addTask(attackPriority, aiAttackOnCollide);
    }

    @Override
    protected void updateAITasks() {
        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
        this.castTimer = this.entityAIProtectAlly.getCastTimer();
        super.updateAITasks();
    }

    @Override
    public void onLivingUpdate() {
        if (this.getEntityWorld().isRemote) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
            this.castTimer = Math.max(0, castTimer - 1);
            if (this.castTimer == 70) {
                this.playSound(this.getHurtSound(), this.getSoundVolume() * 2, this.getSoundPitch());
            }
        }

        this.protectionCooldown = Math.max(0, this.protectionCooldown - 1);

        super.onLivingUpdate();
    }

    public boolean canProtectAlly(EntityLivingBase entity) {
        return this.protectionCooldown <= 0 && entity.getHealth() < entity.getMaxHealth() && !entity.isPotionActive(MobEffects.RESISTANCE);
    }

    public boolean applyProtectionToAlly(EntityLivingBase entity) {
        if (canProtectAlly(entity)) {
            entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 20 * 20, 3));
            this.protectionCooldown = maxProtectionCooldown;
        }

        return false;
    }

    @Override
    public double getBaseHP(EnumDemonWillType type) {
        return super.getBaseHP(type) * 0.75;
    }

    @Override
    public double getBaseMeleeDamage(EnumDemonWillType type) {
        return super.getBaseMeleeDamage(type) * 0.75;
    }

    @Override
    public double getBaseSpeed(EnumDemonWillType type) {
        return super.getBaseSpeed(type);
    }

    @Override
    public double getBaseSprintModifier(EnumDemonWillType type) {
        return super.getBaseSprintModifier(type);
    }

    @Override
    public double getBaseKnockbackResist(EnumDemonWillType type) {
        return super.getBaseKnockbackResist(type) + 0.2;
    }

    @Override
    public double getMeleeResist() {
        return 0.2;
    }

    @Override
    public double getProjectileResist() {
        return 0.6;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(DYE_COLOR, (byte) 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 10) {
            this.sheepTimer = 40;
        } else if (id == 53) {
            this.castTimer = 100;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    @SideOnly(Side.CLIENT)
    public float getHeadRotationPointY(float partialTick) {
        return this.sheepTimer <= 0 ? 0.0F : (this.sheepTimer >= 4 && this.sheepTimer <= 36 ? 1.0F : (this.sheepTimer < 4 ? ((float) this.sheepTimer - partialTick) / 4.0F : -((float) (this.sheepTimer - 40) - partialTick) / 4.0F));
    }

    @SideOnly(Side.CLIENT)
    public float getHeadRotationAngleX(float partialTick) {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
            float f = ((float) (this.sheepTimer - 4) - partialTick) / 32.0F;
            return ((float) Math.PI / 5F) + ((float) Math.PI * 7F / 100F) * MathHelper.sin(f * 28.7F);
        } else {
            return this.sheepTimer > 0 ? ((float) Math.PI / 5F) : this.rotationPitch * 0.017453292F;
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setBoolean("Sheared", this.getSheared());
        tag.setByte("Color", (byte) this.getFleeceColor().getMetadata());
        tag.setInteger("protection", this.protectionCooldown);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setSheared(tag.getBoolean("Sheared"));
        this.setFleeceColor(EnumDyeColor.byMetadata(tag.getByte("Color")));
        this.protectionCooldown = tag.getInteger("protection");
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.5f;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }

    /**
     * Gets the wool color of this sheep.
     */
    public EnumDyeColor getFleeceColor() {
        return EnumDyeColor.byMetadata(this.dataManager.get(DYE_COLOR) & 15);
    }

    /**
     * Sets the wool color of this sheep
     */
    public void setFleeceColor(EnumDyeColor color) {
        byte b0 = this.dataManager.get(DYE_COLOR);
        this.dataManager.set(DYE_COLOR, (byte) (b0 & 240 | color.getMetadata() & 15));
    }

    /**
     * returns true if a sheeps wool has been sheared
     */
    public boolean getSheared() {
        return (this.dataManager.get(DYE_COLOR) & 16) != 0;
    }

    /**
     * make a sheep sheared if set to true
     */
    public void setSheared(boolean sheared) {
        byte b0 = this.dataManager.get(DYE_COLOR);

        if (sheared) {
            this.dataManager.set(DYE_COLOR, (byte) (b0 | 16));
        } else {
            this.dataManager.set(DYE_COLOR, (byte) (b0 & -17));
        }
    }

    /**
     * This function applies the benefits of growing back wool and faster
     * growing up to the acting entity. (This function is used in the
     * AIEatGrass)
     */
    @Override
    public void eatGrassBonus() {
        this.setSheared(false);

        if (this.isChild()) {
            this.heal(3);
        }
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob
     * spawner, natural spawning etc, but not called when entity is reloaded
     * from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setFleeceColor(getRandomSheepColor(this.getEntityWorld().rand));
        return livingdata;
    }

    @Override
    public float getEyeHeight() {
        return 0.95F * this.height;
    }

    @Override
    public boolean isShearable(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos) {
        return !this.getSheared() && !this.isChild();
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        this.setSheared(true);
        int i = 1 + this.rand.nextInt(3);

        List<ItemStack> ret = new ArrayList<>();
        for (int j = 0; j < i; ++j)
            ret.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, this.getFleeceColor().getMetadata()));

        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        return ret;
    }

    public int getCastTimer() {
        return castTimer;
    }

    public static float[] getDyeRgb(EnumDyeColor dyeColor) {
        return DYE_TO_RGB.get(dyeColor);
    }

    /**
     * Chooses a "vanilla" sheep color based on the provided random.
     */
    public static EnumDyeColor getRandomSheepColor(Random random) {
        int i = random.nextInt(100);
        return i < 5 ? EnumDyeColor.BLACK : (i < 10 ? EnumDyeColor.GRAY : (i < 15 ? EnumDyeColor.SILVER : (i < 18 ? EnumDyeColor.BROWN : (random.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE))));
    }
}