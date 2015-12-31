package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.ritual.*;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.network.BloodMagicPacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualJumping extends Ritual
{
    public static final String JUMP_RANGE = "jumpRange";

    public RitualJumping()
    {
        super("ritualJump", 0, 1000, "ritual." + Constants.Mod.MODID + ".jumpRitual");
        addBlockRange(JUMP_RANGE, new AreaDescriptor.Rectangle(new BlockPos(-1, 1, -1), new BlockPos(1, 2, 1)));
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        World world = masterRitualStone.getWorld();
        SoulNetwork network = NetworkHelper.getSoulNetwork(masterRitualStone.getOwner(), world);
        int currentEssence = network.getCurrentEssence();

        if (currentEssence < getRefreshCost())
            return;

        AreaDescriptor jumpRange = getBlockRange(JUMP_RANGE);
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, jumpRange.getAABB(masterRitualStone.getPos()));
        if (entities != null)
        {
            for (EntityLivingBase entity : entities)
            {
                if (entity.isSneaking())
                    continue;

                double motionY = 1.5;

                if (entity instanceof EntityPlayer && entity instanceof EntityPlayerMP)
                {
                    //TODO Packet handlers if needed
//                    BloodMagicPacketHandler.INSTANCE.sendTo();
                    ((EntityPlayerMP) entity).motionY = motionY;
                    ((EntityPlayerMP) entity).fallDistance = 0;
                }
                else
                {
                    entity.motionY = motionY;
                    entity.fallDistance = 0;
                }
            }
        }
    }

    @Override
    public int getRefreshTime()
    {
        return 20;
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

        for (int i = -1; i <= 1; i++)
            this.addCornerRunes(components, 1, i, EnumRuneType.AIR);

        return components;
    }
}
