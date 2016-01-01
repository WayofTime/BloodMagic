package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.ritual.*;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;

public class RitualTest extends Ritual
{
    public RitualTest()
    {
        super("ritualTest", 0, 1000, "ritual." + Constants.Mod.MODID + ".testRitual");
    }

    @Override
    public boolean activateRitual(IMasterRitualStone masterRitualStone, EntityPlayer player)
    {
        player.addChatComponentMessage(new ChatComponentText("ritual started"));
        return true;
    }

    @Override
    public void performRitual(IMasterRitualStone masterRitualStone)
    {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(masterRitualStone.getOwner());

        if (player != null)
            player.addChatComponentMessage(new ChatComponentText("effect performed"));
    }

    @Override
    public void stopRitual(IMasterRitualStone masterRitualStone, BreakType breakType)
    {
        EntityPlayer player = PlayerHelper.getPlayerFromUUID(masterRitualStone.getOwner());

        if (player != null)
            player.addChatComponentMessage(new ChatComponentText("ritual stopped - " + breakType.name()));
    }

    @Override
    public int getRefreshCost()
    {
        return 0;
    }

    @Override
    public ArrayList<RitualComponent> getComponents()
    {
        ArrayList<RitualComponent> components = new ArrayList<RitualComponent>();

        components.add(new RitualComponent(new BlockPos(1, 0, 1), EnumRuneType.AIR));
        components.add(new RitualComponent(new BlockPos(-1, 0, 1), EnumRuneType.EARTH));
        components.add(new RitualComponent(new BlockPos(1, 0, -1), EnumRuneType.WATER));
        components.add(new RitualComponent(new BlockPos(-1, 0, -1), EnumRuneType.FIRE));

        return components;
    }

    @Override
    public Ritual getNewCopy()
    {
        return new RitualTest();
    }
}
