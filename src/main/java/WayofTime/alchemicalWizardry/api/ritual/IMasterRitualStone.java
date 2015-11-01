package WayofTime.alchemicalWizardry.api.ritual;

import WayofTime.alchemicalWizardry.api.ritual.imperfect.IImperfectRitualStone;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IMasterRitualStone extends IImperfectRitualStone {

    void performRitual(World world, BlockPos pos, String ritualID);

    void setCooldown(int cooldown);

    int getCooldown();

    void setActive(boolean active);

    EnumFacing getDirection();

    NBTTagCompound getCustomRitualTag();

    void setCustomRitualTag(NBTTagCompound tag);

    boolean areTanksEmpty();

    int getRunningTime();

    LocalRitualStorage getLocalStorage();

    void setLocalStorage(LocalRitualStorage storage);
}
