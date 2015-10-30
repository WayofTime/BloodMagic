package WayofTime.alchemicalWizardry.api.altar;

import lombok.Getter;

@Getter
public class AltarUpgrade {

    private int speedCount;

    public AltarUpgrade() {

    }

    // Adders

    public AltarUpgrade addSpeed() {
        speedCount++;
        return this;
    }
}
