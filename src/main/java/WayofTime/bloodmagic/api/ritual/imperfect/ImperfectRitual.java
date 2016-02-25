package WayofTime.bloodmagic.api.ritual.imperfect;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.bloodmagic.api.BlockStack;

/**
 * Abstract class for creating new imperfect rituals. ImperfectRituals need be
 * registered with
 * {@link WayofTime.bloodmagic.api.registry.ImperfectRitualRegistry#registerRitual(ImperfectRitual)}
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public abstract class ImperfectRitual
{

    private final String name;
    private final BlockStack requiredBlock;
    private final int activationCost;
    private final boolean lightshow;
    private final String unlocalizedName;

    /**
     * @param name
     *        - The name of the ritual
     * @param requiredBlock
     *        - The block required above the ImperfectRitualStone
     * @param activationCost
     *        - Base LP cost for activating the ritual
     */
    public ImperfectRitual(String name, BlockStack requiredBlock, int activationCost, String unlocalizedName)
    {
        this(name, requiredBlock, activationCost, false, unlocalizedName);
    }

    /**
     * Called when the player activates the ritual
     * {@link WayofTime.bloodmagic.tile.TileImperfectRitualStone#performRitual(World, BlockPos, ImperfectRitual, EntityPlayer)}
     * 
     * @param imperfectRitualStone
     *        - The {@link IImperfectRitualStone} that the ritual is bound to
     * @param player
     *        - The player activating the ritual
     * @return - Whether activation was successful
     */
    public abstract boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player);

    @Override
    public String toString()
    {
        return getName() + ":" + getRequiredBlock().toString() + "@" + getActivationCost();
    }
}
