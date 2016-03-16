package WayofTime.bloodmagic.ritual;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.AreaDescriptor;
import WayofTime.bloodmagic.api.ritual.EnumRuneType;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.ritual.RitualComponent;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;

public class RitualSpeed extends Ritual
{
    public static final String SPEED_RANGE = "sanicRange";

    public RitualSpeed()
    {
        super("ritualSpeed", 0, 1000, "ritual." + Constants.Mod.MODID + ".speedRitual");
        addBlockRange(SPEED_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-2, 1, -2), new BlockPos(2, 5, 2)));
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

        AreaDescriptor speedRange = getBlockRange(SPEED_RANGE);

        for (EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, speedRange.getAABB(masterRitualStone.getBlockPos())))
        {
            if (entity.isSneaking())
                continue;

            double motionY = 1.2;
            double speed = 3;
            EnumFacing direction = masterRitualStone.getDirection();

            entity.motionY = motionY;
            entity.fallDistance = 0;

            switch (direction)
            {
            case NORTH:
                entity.setVelocity(0, motionY, -speed);
                break;

            case SOUTH:
                entity.setVelocity(0, motionY, speed);
                break;

            case WEST:
                entity.setVelocity(-speed, motionY, 0);
                break;

            case EAST:
                entity.setVelocity(speed, motionY, 0);
                break;
            }

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
        return 5;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        this.addRune(components, 0, 0, -2, EnumRuneType.DUSK);
        this.addRune(components, 1, 0, -1, EnumRuneType.AIR);
        this.addRune(components, -1, 0, -1, EnumRuneType.AIR);
        for (int i = 0; i < 2; i++)
        {
            this.addRune(components, 2, 0, i, EnumRuneType.AIR);
            this.addRune(components, -2, 0, i, EnumRuneType.AIR);
        }

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualSpeed();
    }
}
