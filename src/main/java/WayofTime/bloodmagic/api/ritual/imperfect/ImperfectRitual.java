package WayofTime.bloodmagic.api.ritual.imperfect;

import WayofTime.bloodmagic.api.BlockStack;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public abstract class ImperfectRitual {

    private final String name;
    private final BlockStack requiredBlock;
    private final int activationCost;
    private final boolean lightshow;

    public ImperfectRitual(String name, BlockStack requiredBlock, int activationCost) {
        this(name, requiredBlock, activationCost, false);
    }

    public abstract boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player);

    @Override
    public String toString() {
        return getName() + ":" + getRequiredBlock().toString() + "@" + getActivationCost();
    }
}
