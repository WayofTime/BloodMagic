package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.ritual.*;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RitualInterdiction extends Ritual {
    public static final String INTERDICTION_RANGE = "interdictionRange";

    public RitualInterdiction() {
        super("ritualInterdiction", 0, 1000, "ritual." + BloodMagic.MODID + ".interdictionRitual");
        addBlockRange(INTERDICTION_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, 0, -2), 5));
        setMaximumVolumeAndDistanceOfRange(INTERDICTION_RANGE, 0, 10, 10);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone) {
        World world = masterRitualStone.getWorldObj();
        int currentEssence = masterRitualStone.getOwnerNetwork().getCurrentEssence();

        if (currentEssence < getRefreshCost()) {
            masterRitualStone.getOwnerNetwork().causeNausea();
            return;
        }

        AreaDescriptor interdictionRange = getBlockRange(INTERDICTION_RANGE);

        for (EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, interdictionRange.getAABB(masterRitualStone.getBlockPos()))) {
            if (entity instanceof EntityPlayer && (((EntityPlayer) entity).capabilities.isCreativeMode || PlayerHelper.getUUIDFromPlayer((EntityPlayer) entity).toString().equals(masterRitualStone.getOwner())))
                continue;

            double xDif = entity.posX - (masterRitualStone.getBlockPos().getX() + 0.5);
            double yDif = entity.posY - masterRitualStone.getBlockPos().getY() + 1;
            double zDif = entity.posZ - (masterRitualStone.getBlockPos().getZ() + 0.5);

            entity.motionX = 0.1 * xDif;
            entity.motionY = 0.1 * yDif;
            entity.motionZ = 0.1 * zDif;
            entity.fallDistance = 0;

            if (entity instanceof EntityPlayer) {
                entity.velocityChanged = true;
            }
        }
    }

    @Override
    public int getRefreshTime() {
        return 1;
    }

    @Override
    public int getRefreshCost() {
        return 1;
    }

    @Override
    public ArrayList<RitualComponent> getComponents() {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addCornerRunes(components, 1, 0, EnumRuneType.AIR);
        this.addParallelRunes(components, 1, 0, EnumRuneType.AIR);

        return components;
    }

    @Override
    public Ritual getNewCopy() {
        return new RitualInterdiction();
    }
}
