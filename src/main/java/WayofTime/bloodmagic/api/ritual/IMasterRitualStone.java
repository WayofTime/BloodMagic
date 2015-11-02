package WayofTime.bloodmagic.api.ritual;

import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IMasterRitualStone extends IImperfectRitualStone {

    void performRitual(World world, BlockPos pos, Ritual ritual);

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
