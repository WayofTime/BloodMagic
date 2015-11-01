package WayofTime.alchemicalWizardry.api.ritual.imperfect;

import WayofTime.alchemicalWizardry.api.BlockStack;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;

@RequiredArgsConstructor
public abstract class ImperfectRitual {

    private final BlockStack requiredBlock;
    private final int activationCost;
    private final boolean lightshow;

    public ImperfectRitual(BlockStack requiredBlock, int activationCost) {
        this(requiredBlock, activationCost, false);
    }

    public abstract boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player);
}
