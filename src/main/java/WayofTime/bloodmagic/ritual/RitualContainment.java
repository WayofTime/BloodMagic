package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.*;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RitualContainment extends Ritual
{
    public static final String CONTAINMENT_RANGE = "containmentRange";

    public RitualContainment()
    {
        super("ritualContainment", 0, 2000, "ritual." + Constants.Mod.MODID + ".containmentRitual");
        addBlockRange(CONTAINMENT_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-3, 0, -3), 7));
        setMaximumVolumeAndDistanceOfRange(CONTAINMENT_RANGE, 0, 5, 10);
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNausea();
            return;
        }

        AreaDescriptor containmentRange = getBlockRange(CONTAINMENT_RANGE);

        for (EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, containmentRange.getAABB(masterRitualStone.getBlockPos())))
        {
            if (entity instanceof EntityPlayer && (((EntityPlayer) entity).capabilities.isCreativeMode || PlayerHelper.getUUIDFromPlayer((EntityPlayer) entity).toString().equals(masterRitualStone.getOwner())))
                continue;

            double xDif = entity.posX - (masterRitualStone.getBlockPos().getX() + 0.5);
            double yDif = entity.posY - (masterRitualStone.getBlockPos().getY() + 2.5);
            double zDif = entity.posZ - (masterRitualStone.getBlockPos().getZ() + 0.5);

            entity.motionX = -0.05 * xDif;
            entity.motionY = -0.05 * yDif;
            entity.motionZ = -0.05 * zDif;
            entity.fallDistance = 0;
        }
    }

    @Override
    public int getRefreshTime()
    {
        return 1;
    }

    @Override
    public int getRefreshCost()
    {
        return 1;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addParallelRunes(components, 1, 0, EnumRuneType.EARTH);
        this.addCornerRunes(components, 2, 0, EnumRuneType.EARTH);
        this.addParallelRunes(components, 1, 5, EnumRuneType.EARTH);
        this.addCornerRunes(components, 2, 5, EnumRuneType.EARTH);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualContainment();
    }
}
