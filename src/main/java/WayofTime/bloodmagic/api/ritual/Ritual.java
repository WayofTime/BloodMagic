package WayofTime.bloodmagic.api.ritual;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public abstract class Ritual {

    public final ArrayList<RitualComponent> ritualComponents = new ArrayList<RitualComponent>();
    private final String name;
    private final int crystalLevel;
    private final int activationCost;
    private final RitualRenderer renderer;

    public Ritual(String name, int crystalLevel, int activationCost) {
        this(name, crystalLevel, activationCost, null);
    }

	public boolean activateRitual(IMasterRitualStone masterRitualStone, EntityPlayer player) {
		return true;
	}

    public abstract void performRitual(IMasterRitualStone masterRitualStone);

	public void stopRitual(IMasterRitualStone masterRitualStone, BreakType breakType) {
		
	}

    public abstract int getRefreshCost();

    public int getRefreshTime() {
        return 20;
    }

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
