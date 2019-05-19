package WayofTime.bloodmagic.ritual.types;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ritual.*;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

@RitualRegister("jumping")
public class RitualJumping extends Ritual {
    public static final String JUMP_RANGE = "jumpRange";
    public static final String JUMP_POWER = "jumpPower";

    public RitualJumping() {
        super("ritualJump", 0, 5000, "ritual." + BloodMagic.MODID + ".jumpRitual");
        addBlockRange(JUMP_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 1, -1), 3, 1, 3));
        setMaximumVolumeAndDistanceOfRange(JUMP_RANGE, 0, 5, 5);
        addBlockRange(JUMP_POWER, new AreaDescriptor.Rectangle(new BlockPos(0, 0, 0), 0, 5, 0));
        setMaximumVolumeAndDistanceOfRange(JUMP_POWER, 0, 0, 100);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        int maxEffects = currentEssence / getRefreshCost();
        int totalEffects = 0;

        AreaDescriptor jumpRange = masterRitualStone.getBlockRange(JUMP_RANGE);
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, jumpRange.getAABB(masterRitualStone.getBlockPos()));
        for (EntityLivingBase entity : entities) {
            if (totalEffects >= maxEffects) {
                break;
            }

            double motionY = masterRitualStone.getBlockRange(JUMP_POWER).getHeight() * 0.3;

            entity.fallDistance = 0;
            if (entity.isSneaking()) {
                continue;
            }

            entity.motionY = motionY;
            totalEffects++;

            if (entity instanceof EntityPlayer) {
                Utils.setPlayerSpeedFromServer((EntityPlayer) entity, entity.motionX, entity.motionY, entity.motionZ);
            }
        }

        masterRitualStone.getOwnerNetwork().syphon(masterRitualStone.ticket(getRefreshCost() * totalEffects));
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return getBlockRange(JUMP_POWER).getHeight();
    }

    @Override
    public void gatherComponents(Consumer<RitualComponent> components) {
        for (int i = -1; i <= 1; i++)
            addCornerRunes(components, 1, i, EnumRuneType.AIR);
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualJumping();
    }
}
