package WayofTime.alchemicalWizardry.api.ritual;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

@Getter
@RequiredArgsConstructor
public class Ritual {

    private final String name;
    private final int crystalLevel;
    private final int activationCost;
    private final RitualEffect ritualEffect;
    private final RitualRenderer renderer;

    public Ritual(String name, int crystalLevel, int activationCost, RitualEffect ritualEffect) {
        this(name, crystalLevel, activationCost, ritualEffect, null);
    }

    public ArrayList<RitualComponent> getComponents() {
        return this.getRitualEffect().getComponents();
    }

    public void performEffect(IMasterRitualStone masterRitualStone) {
        if (ritualEffect != null && RitualRegistry.ritualEnabled(this))
            ritualEffect.performEffect(masterRitualStone);
    }

    public boolean startRitual(IMasterRitualStone masterRitualStone, EntityPlayer player) {
        return ritualEffect != null && RitualRegistry.ritualEnabled(this) && ritualEffect.startRitual(masterRitualStone, player);
    }

    public void onBreak(IMasterRitualStone masterRitualStone, BreakType breakType) {
        if (ritualEffect != null && RitualRegistry.ritualEnabled(this))
            ritualEffect.onRitualBroken(masterRitualStone, breakType);
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
