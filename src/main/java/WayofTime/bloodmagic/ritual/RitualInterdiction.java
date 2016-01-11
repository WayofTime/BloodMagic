package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.*;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RitualInterdiction extends Ritual
{
    public static final String INTERDICTION_RANGE = "interdictionRange";

    public RitualInterdiction()
    {
        super("ritualInterdiction", 0, 1000, "ritual." + Constants.Mod.MODID + ".interdictionRitual");
        addBlockRange(INTERDICTION_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, 0, -2), 5));
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

        AreaDescriptor interdictionRange = getBlockRange(INTERDICTION_RANGE);

        for (EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, interdictionRange.getAABB(masterRitualStone.getBlockPos())))
        {
            if (entity instanceof EntityPlayer && (((EntityPlayer) entity).capabilities.isCreativeMode || PlayerHelper.getUUIDFromPlayer((EntityPlayer) entity).toString().equals(masterRitualStone.getOwner())))
                continue;

            double xDif = entity.posX - masterRitualStone.getBlockPos().getX();
            double yDif = entity.posY - masterRitualStone.getBlockPos().getY() + 1;
            double zDif = entity.posZ - masterRitualStone.getBlockPos().getZ();

            entity.setVelocity(0.1 * xDif, 0.1 * yDif, 0.1 * zDif);
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

        this.addCornerRunes(components, 1, 0, EnumRuneType.AIR);
        this.addParallelRunes(components, 1, 0, EnumRuneType.AIR);

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualInterdiction();
    }
}
