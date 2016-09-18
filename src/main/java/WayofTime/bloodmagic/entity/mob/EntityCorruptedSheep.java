package WayofTime.bloodmagic.entity.mob;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Maps;

public class EntityCorruptedSheep extends EntityAspectedDemonBase implements IShearable
{
    private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.<Byte>createKey(EntityCorruptedSheep.class, DataSerializers.BYTE);

    private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
    /**
     * Used to control movement as well as wool regrowth. Set to 40 on
     * handleHealthUpdate and counts down with each tick.
     */
    private int sheepTimer;
    private EntityAIEatGrass entityAIEatGrass; //TODO: Change to a new AI

    public static float[] getDyeRgb(EnumDyeColor dyeColor)
    {
        return (float[]) DYE_TO_RGB.get(dyeColor);
    }

    public EntityCorruptedSheep(World worldIn)
    {
        super(worldIn);
        this.setSize(0.9F, 1.3F);
    }

    protected void initEntityAI()
    {
        this.entityAIEatGrass = new EntityAIEatGrass(this);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.1D, Items.WHEAT, false));
        this.tasks.addTask(5, this.entityAIEatGrass);
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    protected void updateAITasks()
    {
        this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }

    @Override
    public void onLivingUpdate()
    {
        if (this.worldObj.isRemote)
        {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }

        super.onLivingUpdate();
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6);
    }

    public double getMeleeResist()
    {
        return 0.2;
    }

    public double getProjectileResist()
    {
        return 0.6;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(DYE_COLOR, Byte.valueOf((byte) 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 10)
        {
            this.sheepTimer = 40;
        } else
        {
            super.handleStatusUpdate(id);
        }
    }

    @SideOnly(Side.CLIENT)
    public float getHeadRotationPointY(float partialTick)
    {
        return this.sheepTimer <= 0 ? 0.0F : (this.sheepTimer >= 4 && this.sheepTimer <= 36 ? 1.0F : (this.sheepTimer < 4 ? ((float) this.sheepTimer - partialTick) / 4.0F : -((float) (this.sheepTimer - 40) - partialTick) / 4.0F));
    }

    @SideOnly(Side.CLIENT)
    public float getHeadRotationAngleX(float partialTick)
    {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36)
        {
            float f = ((float) (this.sheepTimer - 4) - partialTick) / 32.0F;
            return ((float) Math.PI / 5F) + ((float) Math.PI * 7F / 100F) * MathHelper.sin(f * 28.7F);
        } else
        {
            return this.sheepTimer > 0 ? ((float) Math.PI / 5F) : this.rotationPitch * 0.017453292F;
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Sheared", this.getSheared());
        compound.setByte("Color", (byte) this.getFleeceColor().getMetadata());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setSheared(compound.getBoolean("Sheared"));
        this.setFleeceColor(EnumDyeColor.byMetadata(compound.getByte("Color")));
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SHEEP_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound()
    {
        return SoundEvents.ENTITY_SHEEP_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SHEEP_DEATH;
    }

    @Override
    protected float getSoundPitch()
    {
        return super.getSoundPitch() * 0.5f;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }

    /**
     * Gets the wool color of this sheep.
     */
    public EnumDyeColor getFleeceColor()
    {
        return EnumDyeColor.byMetadata(((Byte) this.dataManager.get(DYE_COLOR)).byteValue() & 15);
    }

    /**
     * Sets the wool color of this sheep
     */
    public void setFleeceColor(EnumDyeColor color)
    {
        byte b0 = ((Byte) this.dataManager.get(DYE_COLOR)).byteValue();
        this.dataManager.set(DYE_COLOR, Byte.valueOf((byte) (b0 & 240 | color.getMetadata() & 15)));
    }

    /**
     * returns true if a sheeps wool has been sheared
     */
    public boolean getSheared()
    {
        return (((Byte) this.dataManager.get(DYE_COLOR)).byteValue() & 16) != 0;
    }

    /**
     * make a sheep sheared if set to true
     */
    public void setSheared(boolean sheared)
    {
        byte b0 = ((Byte) this.dataManager.get(DYE_COLOR)).byteValue();

        if (sheared)
        {
            this.dataManager.set(DYE_COLOR, Byte.valueOf((byte) (b0 | 16)));
        } else
        {
            this.dataManager.set(DYE_COLOR, Byte.valueOf((byte) (b0 & -17)));
        }
    }

    /**
     * Chooses a "vanilla" sheep color based on the provided random.
     */
    public static EnumDyeColor getRandomSheepColor(Random random)
    {
        int i = random.nextInt(100);
        return i < 5 ? EnumDyeColor.BLACK : (i < 10 ? EnumDyeColor.GRAY : (i < 15 ? EnumDyeColor.SILVER : (i < 18 ? EnumDyeColor.BROWN : (random.nextInt(500) == 0 ? EnumDyeColor.PINK : EnumDyeColor.WHITE))));
    }

    /**
     * This function applies the benefits of growing back wool and faster
     * growing up to the acting entity. (This function is used in the
     * AIEatGrass)
     */
    @Override
    public void eatGrassBonus()
    {
        this.setSheared(false);

        if (this.isChild())
        {
            //TODO: Heal
        }
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob
     * spawner, natural spawning etc, but not called when entity is reloaded
     * from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setFleeceColor(getRandomSheepColor(this.worldObj.rand));
        return livingdata;
    }

    @Override
    public float getEyeHeight()
    {
        return 0.95F * this.height;
    }

    static
    {
        DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[] { 1.0F, 1.0F, 1.0F });
        DYE_TO_RGB.put(EnumDyeColor.ORANGE, new float[] { 0.85F, 0.5F, 0.2F });
        DYE_TO_RGB.put(EnumDyeColor.MAGENTA, new float[] { 0.7F, 0.3F, 0.85F });
        DYE_TO_RGB.put(EnumDyeColor.LIGHT_BLUE, new float[] { 0.4F, 0.6F, 0.85F });
        DYE_TO_RGB.put(EnumDyeColor.YELLOW, new float[] { 0.9F, 0.9F, 0.2F });
        DYE_TO_RGB.put(EnumDyeColor.LIME, new float[] { 0.5F, 0.8F, 0.1F });
        DYE_TO_RGB.put(EnumDyeColor.PINK, new float[] { 0.95F, 0.5F, 0.65F });
        DYE_TO_RGB.put(EnumDyeColor.GRAY, new float[] { 0.3F, 0.3F, 0.3F });
        DYE_TO_RGB.put(EnumDyeColor.SILVER, new float[] { 0.6F, 0.6F, 0.6F });
        DYE_TO_RGB.put(EnumDyeColor.CYAN, new float[] { 0.3F, 0.5F, 0.6F });
        DYE_TO_RGB.put(EnumDyeColor.PURPLE, new float[] { 0.5F, 0.25F, 0.7F });
        DYE_TO_RGB.put(EnumDyeColor.BLUE, new float[] { 0.2F, 0.3F, 0.7F });
        DYE_TO_RGB.put(EnumDyeColor.BROWN, new float[] { 0.4F, 0.3F, 0.2F });
        DYE_TO_RGB.put(EnumDyeColor.GREEN, new float[] { 0.4F, 0.5F, 0.2F });
        DYE_TO_RGB.put(EnumDyeColor.RED, new float[] { 0.6F, 0.2F, 0.2F });
        DYE_TO_RGB.put(EnumDyeColor.BLACK, new float[] { 0.1F, 0.1F, 0.1F });
    }

    @Override
    public boolean isShearable(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos)
    {
        return !this.getSheared() && !this.isChild();
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        this.setSheared(true);
        int i = 1 + this.rand.nextInt(3);

        java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        for (int j = 0; j < i; ++j)
            ret.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, this.getFleeceColor().getMetadata()));

        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        return ret;
    }
}