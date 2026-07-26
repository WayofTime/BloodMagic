package wayoftime.bloodmagic.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.common.registries.BloodMagicEntityTypes;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeMeteor;
import wayoftime.bloodmagic.util.Constants;

public class EntityMeteor extends Entity
{
	private ItemStack containedStack = ItemStack.EMPTY;
	protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(EntityMeteor.class, EntityDataSerializers.BLOCK_POS);
	public int time;

	public EntityMeteor(EntityType<EntityMeteor> entityType, Level level)
	{
		super(entityType, level);
	}

	public EntityMeteor(Level worldIn, double x, double y, double z)
	{
		this(BloodMagicEntityTypes.METEOR.getEntityType(), worldIn);
		this.setPos(x, y, z);
		this.setDeltaMovement(Vec3.ZERO);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.setStartPos(this.blockPosition());
	}

	public void setStartPos(BlockPos p_31960_) {
		this.entityData.set(DATA_START_POS, p_31960_);
	}

	public void setContainedStack(ItemStack stack)
	{
		this.containedStack = stack;
	}

	public ItemStack getContainedStack() {
		return containedStack;
	}

	public void tick() {
		++this.time;
		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
		}

		this.move(MoverType.SELF, this.getDeltaMovement());
		if (!this.level().isClientSide) {
			BlockPos pos = this.blockPosition();

			BlockState blockstate = this.level().getBlockState(pos);
			if (!blockstate.is(Blocks.MOVING_PISTON)) {
				if (!FallingBlock.isFree(this.level().getBlockState(pos.below()))) {
					RecipeMeteor recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getMeteor(this.level(), containedStack);
					if (recipe != null)
					{
						recipe.spawnMeteorInWorld(this.level(), pos);
					}
					this.discard();
				}
			}
		}

		this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	protected Entity.MovementEmission getMovementEmission() {
		return Entity.MovementEmission.NONE;
	}

	public boolean isPickable() {
		return false;
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound)
	{
		compound.put(Constants.NBT.ITEM, containedStack.save(new CompoundTag()));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tagCompound)
	{
		CompoundTag tag = tagCompound.getCompound(Constants.NBT.ITEM);
		containedStack = ItemStack.of(tag);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_START_POS, BlockPos.ZERO);
	}
}