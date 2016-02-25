package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;

public class RitualContainment extends Ritual
{
    public static final String CONTAINMENT_RANGE = "containmentRange";

    public RitualContainment()
    {
        super("ritualContainment", 0, 2000, "ritual." + Constants.Mod.MODID + ".containmentRitual");
        addBlockRange(CONTAINMENT_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-3, 0, -3), 7));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorldObj();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner());
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
        {
            network.causeNauseaToPlayer();
            return;
        }

        AreaDescriptor containmentRange = getBlockRange(CONTAINMENT_RANGE);

        for (EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, containmentRange.getAABB(masterRitualStone.getBlockPos())))
        {
            if (entity instanceof EntityPlayer && (((EntityPlayer) entity).capabilities.isCreativeMode || PlayerHelper.getUUIDFromPlayer((EntityPlayer) entity).toString().equals(masterRitualStone.getOwner())))
                continue;

            double xDif = entity.posX - masterRitualStone.getBlockPos().getX() + 0.5;
            double yDif = entity.posY - masterRitualStone.getBlockPos().getY() + 3;
            double zDif = entity.posZ - masterRitualStone.getBlockPos().getZ() + 0.5;

            entity.setVelocity(-0.05 * xDif, -0.05 * yDif, -0.05 * zDif);
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
