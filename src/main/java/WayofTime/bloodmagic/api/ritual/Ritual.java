package WayofTime.bloodmagic.api.ritual;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

@Getter
@RequiredArgsConstructor
public abstract class Ritual {

    private final String name;
    private final int crystalLevel;
    private final int activationCost;
    private final RitualRenderer renderer;

    public final ArrayList<RitualComponent> ritualComponents = new ArrayList<RitualComponent>();

    public Ritual(String name, int crystalLevel, int activationCost) {
        this(name, crystalLevel, activationCost, null);
    }

    public abstract boolean startRitual(IMasterRitualStone masterRitualStone, EntityPlayer player);

    public abstract void performEffect(IMasterRitualStone masterRitualStone);

    public abstract void onRitualBroken(IMasterRitualStone masterRitualStone, Ritual.BreakType breakType);

    public abstract int getRefreshCost();

    public abstract ArrayList<RitualComponent> getComponents();

    public void addOffsetRunes(ArrayList<RitualComponent> components, int offset1, int offset2, int y, EnumRuneType rune) {
        components.add(new RitualComponent(new BlockPos(offset1, y, offset2), rune));
        components.add(new RitualComponent(new BlockPos(offset2, y, offset1), rune));
        components.add(new RitualComponent(new BlockPos(offset1, y, -offset2), rune));
        components.add(new RitualComponent(new BlockPos(-offset2, y, offset1), rune));
        components.add(new RitualComponent(new BlockPos(-offset1, y, offset2), rune));
        components.add(new RitualComponent(new BlockPos(offset2, y, -offset1), rune));
        components.add(new RitualComponent(new BlockPos(-offset1, y, -offset2), rune));
        components.add(new RitualComponent(new BlockPos(-offset2, y, -offset1), rune));
    }

    public void addCornerRunes(ArrayList<RitualComponent> components, int offset, int y, EnumRuneType rune) {
        components.add(new RitualComponent(new BlockPos(offset, y, offset), rune));
        components.add(new RitualComponent(new BlockPos(offset, y, -offset), rune));
        components.add(new RitualComponent(new BlockPos(-offset, y, -offset), rune));
        components.add(new RitualComponent(new BlockPos(-offset, y, offset), rune));
    }

    public void addParallelRunes(ArrayList<RitualComponent> components, int offset, int y, EnumRuneType rune) {
        components.add(new RitualComponent(new BlockPos(offset, y, 0), rune));
        components.add(new RitualComponent(new BlockPos(-offset, y, 0), rune));
        components.add(new RitualComponent(new BlockPos(0, y, -offset), rune));
        components.add(new RitualComponent(new BlockPos(0, y, offset), rune));
    }

    public enum BreakType {
        REDSTONE,
        BREAK_MRS,
        BREAK_STONE,
        ACTIVATE,
        DEACTIVATE,
        EXPLOSION,
    }
}
