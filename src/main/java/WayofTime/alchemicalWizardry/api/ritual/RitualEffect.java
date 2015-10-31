package WayofTime.alchemicalWizardry.api.ritual;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

// TODO - Return after Reagents are done
public abstract class RitualEffect {

    public boolean startRitual(IMasterRitualStone masterRitualStone, EntityPlayer player) {
        return true;
    }

    public void onRitualBroken(IMasterRitualStone masterRitualStone, Ritual.BreakType breakType) {

    }

    public LocalRitualStorage getNewLocalStorage() {
        return new LocalRitualStorage();
    }

    public void addOffsetRunes(ArrayList<RitualComponent> ritualList, int offset1, int offset2, int y, EnumRuneType rune) {
        ritualList.add(new RitualComponent(new BlockPos(offset1, y, offset2), rune));
        ritualList.add(new RitualComponent(new BlockPos(offset2, y, offset1), rune));
        ritualList.add(new RitualComponent(new BlockPos(offset1, y, -offset2), rune));
        ritualList.add(new RitualComponent(new BlockPos(-offset2, y, offset1), rune));
        ritualList.add(new RitualComponent(new BlockPos(-offset1, y, offset2), rune));
        ritualList.add(new RitualComponent(new BlockPos(offset2, y, -offset1), rune));
        ritualList.add(new RitualComponent(new BlockPos(-offset1, y, -offset2), rune));
        ritualList.add(new RitualComponent(new BlockPos(-offset2, y, -offset1), rune));
    }

    public void addCornerRunes(ArrayList<RitualComponent> ritualList, int offset, int y, EnumRuneType rune) {
        ritualList.add(new RitualComponent(new BlockPos(offset, y, offset), rune));
        ritualList.add(new RitualComponent(new BlockPos(offset, y, -offset), rune));
        ritualList.add(new RitualComponent(new BlockPos(-offset, y, -offset), rune));
        ritualList.add(new RitualComponent(new BlockPos(-offset, y, offset), rune));
    }

    public void addParallelRunes(ArrayList<RitualComponent> ritualList, int offset, int y, EnumRuneType rune) {
        ritualList.add(new RitualComponent(new BlockPos(offset, y, 0), rune));
        ritualList.add(new RitualComponent(new BlockPos(-offset, y, 0), rune));
        ritualList.add(new RitualComponent(new BlockPos(0, y, -offset), rune));
        ritualList.add(new RitualComponent(new BlockPos(0, y, offset), rune));
    }

    public abstract void performEffect(IMasterRitualStone masterRitualStone);

    public abstract int getRefreshCost();

    public abstract ArrayList<RitualComponent> getComponents();

}
