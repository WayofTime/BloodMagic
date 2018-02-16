package WayofTime.bloodmagic.ritual.data.imperfect;

import WayofTime.bloodmagic.util.BlockStack;
import WayofTime.bloodmagic.core.registry.ImperfectRitualRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Abstract class for creating new imperfect rituals. ImperfectRituals need be
 * registered with
 * {@link ImperfectRitualRegistry#registerRitual(ImperfectRitual)}
 */
public abstract class ImperfectRitual {

    private final String name;
    private final BlockStack requiredBlock;
    private final int activationCost;
    private final boolean lightshow;
    private final String unlocalizedName;

    public ImperfectRitual(String name, BlockStack requiredBlock, int activationCost, boolean lightshow, String unlocalizedName) {
        this.name = name;
        this.requiredBlock = requiredBlock;
        this.activationCost = activationCost;
        this.lightshow = lightshow;
        this.unlocalizedName = unlocalizedName;
    }

    /**
     * @param name           - The name of the ritual
     * @param requiredBlock  - The block required above the ImperfectRitualStone
     * @param activationCost - Base LP cost for activating the ritual
     */
    public ImperfectRitual(String name, BlockStack requiredBlock, int activationCost, String unlocalizedName) {
        this(name, requiredBlock, activationCost, false, unlocalizedName);
    }

    /**
     * Called when the player activates the ritual
     * {@link WayofTime.bloodmagic.tile.TileImperfectRitualStone#performRitual(World, net.minecraft.util.math.BlockPos, ImperfectRitual, EntityPlayer)}
     *
     * @param imperfectRitualStone - The {@link IImperfectRitualStone} that the ritual is bound to
     * @param player               - The player activating the ritual
     * @return - Whether activation was successful
     */
    public abstract boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player);

    public String getName() {
        return name;
    }

    public BlockStack getRequiredBlock() {
        return requiredBlock;
    }

    public int getActivationCost() {
        return activationCost;
    }

    public boolean isLightshow() {
        return lightshow;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    @Override
    public String toString() {
        return getName() + ":" + getRequiredBlock().toString() + "@" + getActivationCost();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImperfectRitual)) return false;

        ImperfectRitual that = (ImperfectRitual) o;

        if (activationCost != that.activationCost) return false;
        if (lightshow != that.lightshow) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (requiredBlock != null ? !requiredBlock.equals(that.requiredBlock) : that.requiredBlock != null)
            return false;
        return unlocalizedName != null ? unlocalizedName.equals(that.unlocalizedName) : that.unlocalizedName == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (requiredBlock != null ? requiredBlock.hashCode() : 0);
        result = 31 * result + activationCost;
        result = 31 * result + (lightshow ? 1 : 0);
        result = 31 * result + (unlocalizedName != null ? unlocalizedName.hashCode() : 0);
        return result;
    }
}
