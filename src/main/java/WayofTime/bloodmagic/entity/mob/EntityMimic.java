package WayofTime.bloodmagic.entity.mob;

import WayofTime.bloodmagic.block.BlockMimic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicBlocks;
import WayofTime.bloodmagic.entity.ai.EntityAIMimicReform;
import WayofTime.bloodmagic.tile.TileMimic;
import WayofTime.bloodmagic.util.StateUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityMimic extends EntityDemonBase {
    /**
     * Copy of EntitySpider's AI (should be pretty evident...)
     */
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntityMimic.class, DataSerializers.BYTE);

    public boolean dropItemsOnBreak = true;
    public NBTTagCompound tileTag = new NBTTagCompound();
    public int metaOfReplacedBlock = 0;
    public IBlockState stateOfReplacedBlock = Blocks.AIR.getDefaultState();
    public int playerCheckRadius = 5;

    public EntityMimic(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 0.9F);

        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityMimic.AISpiderAttack(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1));
        this.tasks.addTask(6, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(7, new EntityAIMimicReform(this));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityMimic.AISpiderTarget(this, EntityPlayer.class));
        this.targetTasks.addTask(3, new EntityMimic.AISpiderTarget(this, EntityIronGolem.class));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);

        tag.setBoolean("dropItemsOnBreak", dropItemsOnBreak);
        tag.setTag("tileTag", tileTag);
        tag.setInteger("metaOfReplacedBlock", metaOfReplacedBlock);
        tag.setInteger("playerCheckRadius", playerCheckRadius);
        tag.setString("stateOfReplacedBlock", stateOfReplacedBlock.toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);

        dropItemsOnBreak = tag.getBoolean("dropItemsOnBreak");
        tileTag = tag.getCompoundTag("tileTag");
        metaOfReplacedBlock = tag.getInteger("metaOfReplacedBlock");
        playerCheckRadius = tag.getInteger("playerCheckRadius");
        stateOfReplacedBlock = StateUtil.parseState(tag.getString("stateOfReplacedBlock"));
    }

    public ItemStack getMimicItemStack() {
        return this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
    }

    public void setMimicItemStack(ItemStack stack) {
        this.setItemStackToSlot(EntityEquipmentSlot.CHEST, stack);
    }

    public boolean spawnHeldBlockOnDeath(World world, BlockPos pos) {
        return world.isAirBlock(pos) && TileMimic.replaceMimicWithBlockActual(world, pos, getMimicItemStack(), tileTag, stateOfReplacedBlock);
    }

    public boolean spawnMimicBlockAtPosition(World world, BlockPos pos) {
        if (world.isAirBlock(pos)) {
            IBlockState mimicState = RegistrarBloodMagicBlocks.MIMIC.getStateFromMeta(BlockMimic.sentientMimicMeta);
            world.setBlockState(pos, mimicState, 3);
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileMimic) {
                TileMimic mimic = (TileMimic) tile;
                mimic.setReplacedState(this.stateOfReplacedBlock);
                mimic.tileTag = tileTag;
                mimic.setInventorySlotContents(0, getMimicItemStack());
                mimic.dropItemsOnBreak = dropItemsOnBreak;
                mimic.refreshTileEntity();
            }

            return true;
        }

        return false;
    }

    public void initializeMimic(ItemStack heldStack, NBTTagCompound tileTag, boolean dropItemsOnBreak, IBlockState stateOfReplacedBlock, int playerCheckRadius, BlockPos homePosition) {
        this.setMimicItemStack(heldStack);
        this.tileTag = tileTag;
        this.dropItemsOnBreak = dropItemsOnBreak;
        this.stateOfReplacedBlock = stateOfReplacedBlock;
        this.playerCheckRadius = playerCheckRadius;
        this.setHomePosAndDistance(homePosition, 2); //TODO: Save this.
    }

    public boolean reformIntoMimicBlock(BlockPos centerPos) {
        int horizontalRadius = 1;
        int verticalRadius = 1;

        for (int hR = 0; hR <= horizontalRadius; hR++) {
            for (int vR = 0; vR <= verticalRadius; vR++) {
                for (int i = -hR; i <= hR; i++) {
                    for (int k = -hR; k <= hR; k++) {
                        for (int j = -vR; j <= vR; j += 2 * vR + (vR > 0 ? 0 : 1)) {
                            if (!(Math.abs(i) == hR || Math.abs(k) == hR)) {
                                continue;
                            }

                            BlockPos newPos = centerPos.add(i, j, k);
                            if (spawnMimicBlockAtPosition(getEntityWorld(), newPos)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);

        if (!getEntityWorld().isRemote) {
            BlockPos centerPos = this.getPosition();

            int horizontalRadius = 1;
            int verticalRadius = 1;

            for (int hR = 0; hR <= horizontalRadius; hR++) {
                for (int vR = 0; vR <= verticalRadius; vR++) {
                    for (int i = -hR; i <= hR; i++) {
                        for (int k = -hR; k <= hR; k++) {
                            for (int j = -vR; j <= vR; j += 2 * vR + (vR > 0 ? 0 : 1)) {
                                if (!(Math.abs(i) == hR || Math.abs(k) == hR)) {
                                    continue;
                                }

                                BlockPos newPos = centerPos.add(i, j, k);
                                if (spawnHeldBlockOnDeath(getEntityWorld(), newPos)) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding
     * this one.
     */
    @Override
    public double getMountedYOffset() {
        return (double) (this.height * 0.5F);
    }

    /**
     * Returns new PathNavigateGround instance
     */
    @Override
    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateClimber(this, worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, (byte) 0);
//        this.dataManager.register(ITEMSTACK, null);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        if (!this.getEntityWorld().isRemote && this.getEntityWorld().getDifficulty() == EnumDifficulty.PEACEFUL) {
            if (reformIntoMimicBlock(this.getPosition())) {
                this.setDead();
            }
        }

        super.onUpdate();

        if (!this.getEntityWorld().isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15F, 1.0F);
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    /**
     * Sets the Entity inside a web block.
     */
    @Override
    public void setInWeb() {

    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
        return potioneffectIn.getPotion() != MobEffects.POISON && super.isPotionApplicable(potioneffectIn);
    }

    /**
     * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns
     * false. The WatchableObject is updated using setBesideClimableBlock.
     */
    public boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING) & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to
     * 0x01 if par1 is true or 0x00 if it is false.
     */
    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.dataManager.get(CLIMBING);

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataManager.set(CLIMBING, b0);
    }

    public float getEyeHeight() {
        return 0.65F;
    }

    static class AISpiderAttack extends EntityAIAttackMelee {
        public AISpiderAttack(EntityMimic spider) {
            super(spider, 1.0D, true);
        }

        @Override
        protected double getAttackReachSqr(EntityLivingBase attackTarget) {
            return (double) (4.0F + attackTarget.width);
        }
    }

    static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
        public AISpiderTarget(EntityMimic spider, Class<T> classTarget) {
            super(spider, classTarget, true);
        }
    }
}
