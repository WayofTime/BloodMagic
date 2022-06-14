package wayoftime.bloodmagic.ritual.types;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.network.SetClientVelocityPacket;
import wayoftime.bloodmagic.ritual.AreaDescriptor;
import wayoftime.bloodmagic.ritual.EnumRuneType;
import wayoftime.bloodmagic.ritual.IMasterRitualStone;
import wayoftime.bloodmagic.ritual.Ritual;
import wayoftime.bloodmagic.ritual.RitualComponent;
import wayoftime.bloodmagic.ritual.RitualRegister;

@RitualRegister("jumping")
public class RitualJumping extends Ritual
{
	public static final String JUMP_RANGE = "jumpRange";
	public static final String JUMP_POWER = "jumpPower";

	public RitualJumping()
	{
		super("ritualJump", 0, 5000, "ritual." + BloodMagic.MODID + ".jumpRitual");
		addBlockRange(JUMP_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 1, -1), 3, 1, 3));
		setMaximumVolumeAndDistanceOfRange(JUMP_RANGE, 0, 5, 5);
		addBlockRange(JUMP_POWER, new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 1, 5, 1));
		setMaximumVolumeAndDistanceOfRange(JUMP_POWER, 0, 1, 100);
	}

	@Override
	public void performRitual(IMasterRitualStone masterRitualStone)
	{
		Level world = masterRitualStone.getWorldObj();
		int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

		if (currentEssence < getRefreshCost())
		{
			masterRitualStone.getOwnerNetwork().causeNausea();
			return;
		}

		int maxEffects = currentEssence / getRefreshCost();
		int totalEffects = 0;

		AreaDescriptor jumpRange = masterRitualStone.getBlockRange(JUMP_RANGE);
		List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, jumpRange.getAABB(masterRitualStone.getMasterBlockPos()));
		for (LivingEntity entity : entities)
		{
			if (totalEffects >= maxEffects)
			{
				break;
			}

			double motionY = masterRitualStone.getBlockRange(JUMP_POWER).getHeight() * 0.3;

			entity.fallDistance = 0;
			if (entity.isShiftKeyDown())
			{
				continue;
			}

			Vec3 motion = entity.getDeltaMovement();

			double motionX = motion.x();
			double motionZ = motion.z();

			totalEffects++;

			entity.setDeltaMovement(motionX, motionY, motionZ);
			if (entity instanceof ServerPlayer)
			{
				BloodMagic.packetHandler.sendTo(new SetClientVelocityPacket(motionX, motionY, motionZ), (ServerPlayer) entity);
			}
		}

		masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
	}

	@Override
	public int getRefreshTime()
	{
		return 1;
	}

	@Override
	public int getRefreshCost()
	{
		return getBlockRange(JUMP_POWER).getHeight();
	}

	@Override
	public void gatherComponents(Consumer<RitualComponent> components)
	{
		for (int i = -1; i <= 1; i++) addCornerRunes(components, 1, i, EnumRuneType.AIR);
	}

	@Override
	public Ritual getNewCopy()
	{
		return new RitualJumping();
	}
}
