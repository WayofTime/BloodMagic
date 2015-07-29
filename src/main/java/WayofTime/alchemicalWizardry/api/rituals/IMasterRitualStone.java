package WayofTime.alchemicalWizardry.api.rituals;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ISegmentedReagentHandler;

public interface IMasterRitualStone extends ISegmentedReagentHandler
{
    void performRitual(World world, BlockPos pos, String ritualID);

    String getOwner();

    void setCooldown(int newCooldown);

    int getCooldown();

    void setVar1(int newVar1);

    int getVar1();

    void setActive(boolean active);

    int getDirection();

    World getWorldObj();

    BlockPos getPosition();

    NBTTagCompound getCustomRitualTag();

    void setCustomRitualTag(NBTTagCompound tag);
    
    boolean areTanksEmpty();
    
    int getRunningTime();
    
    LocalRitualStorage getLocalStorage();
    
    void setLocalStorage(LocalRitualStorage storage);
}
