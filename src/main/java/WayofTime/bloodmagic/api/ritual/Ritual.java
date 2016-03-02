package WayofTime.bloodmagic.api.ritual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Abstract class for creating new rituals. Rituals need be registered with
 * {@link WayofTime.bloodmagic.api.registry.RitualRegistry#registerRitual(Ritual, String)}
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = { "modableRangeMap" })
@ToString
public abstract class Ritual
{
    public final ArrayList<RitualComponent> ritualComponents = new ArrayList<RitualComponent>();
    private final String name;
    private final int crystalLevel;
    private final int activationCost;
    private final RitualRenderer renderer;
    private final String unlocalizedName;

    protected final Map<String, AreaDescriptor> modableRangeMap = new HashMap<String, AreaDescriptor>();

    /**
     * @param name
     *        - The name of the ritual
     * @param crystalLevel
     *        - Required Activation Crystal tier
     * @param activationCost
     *        - Base LP cost for activating the ritual
     */
    public Ritual(String name, int crystalLevel, int activationCost, String unlocalizedName)
    {
        this(name, crystalLevel, activationCost, null, unlocalizedName);
    }

    public void readFromNBT(NBTTagCompound tag)
    {

    }

    public void writeToNBT(NBTTagCompound tag)
    {

    }

    /**
     * Called when the player attempts to activate the ritual.
     * 
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#activateRitual(ItemStack, EntityPlayer, Ritual)}
     * 
     * @param masterRitualStone
     *        - The {@link IMasterRitualStone} that the ritual is bound to
     * @param player
     *        - The activating player
     * @param owner
     *        - Owner of the crystal activating this ritual, or the current
     *        owner of the ritual if being reactivated.
     * @return - Whether activation was successful
     */
    public boolean activateRitual(IMasterRitualStone masterRitualStone, EntityPlayer player, String owner)
    {
        return true;
    }

    /**
     * Called every {@link #getRefreshTime()} ticks while active.
     * 
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#performRitual(World, BlockPos)}
     * 
     * @param masterRitualStone
     *        - The {@link IMasterRitualStone} that the ritual is bound to
     */
    public abstract void performRitual(IMasterRitualStone masterRitualStone);

    /**
     * Called when the ritual is stopped for a given {@link Ritual.BreakType}.
     * 
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#stopRitual(Ritual.BreakType)}
     * 
     * @param masterRitualStone
     *        - The {@link IMasterRitualStone} that the ritual is bound to
     * @param breakType
     *        - The type of break that caused the stoppage.
     */
    public void stopRitual(IMasterRitualStone masterRitualStone, BreakType breakType)
    {

    }

    /**
     * Used to set the amount of LP drained every {@link #getRefreshTime()}
     * ticks.
     * 
     * @return - The amount of LP drained per refresh
     */
    public abstract int getRefreshCost();

    /**
     * Used to set the refresh rate of the ritual. (How often
     * {@link #performRitual(IMasterRitualStone)} is called.
     * 
     * @return - How often to perform the effect in ticks.
     */
    public int getRefreshTime()
    {
        return 20;
    }

    public void addBlockRange(String range, AreaDescriptor defaultRange)
    {
        modableRangeMap.put(range, defaultRange);
    }

    /**
     * Used to grab the range of a ritual for a given effect.
     * 
     * @param range
     *        - Range that needs to be pulled.
     * @return -
     */
    public AreaDescriptor getBlockRange(String range)
    {
        if (modableRangeMap.containsKey(range))
        {
            return modableRangeMap.get(range);
        }

        return null;
    }

    /**
     * @return a list of {@link RitualComponent} for checking the ritual.
     */
    public abstract ArrayList<RitualComponent> getComponents();

    public void addRune(ArrayList<RitualComponent> components, int offset1, int y, int offset2, EnumRuneType rune)
    {
        components.add(new RitualComponent(new BlockPos(offset1, y, offset2), rune));
    }

    public void addOffsetRunes(ArrayList<RitualComponent> components, int offset1, int offset2, int y, EnumRuneType rune)
    {
        addRune(components, offset1, y, offset2, rune);
        addRune(components, offset2, y, offset1, rune);
        addRune(components, offset1, y, -offset2, rune);
        addRune(components, -offset2, y, offset1, rune);
        addRune(components, -offset1, y, offset2, rune);
        addRune(components, offset2, y, -offset1, rune);
        addRune(components, -offset1, y, -offset2, rune);
        addRune(components, -offset2, y, -offset1, rune);
    }

    public void addCornerRunes(ArrayList<RitualComponent> components, int offset, int y, EnumRuneType rune)
    {
        addRune(components, offset, y, offset, rune);
        addRune(components, offset, y, -offset, rune);
        addRune(components, -offset, y, -offset, rune);
        addRune(components, -offset, y, offset, rune);
    }

    public void addParallelRunes(ArrayList<RitualComponent> components, int offset, int y, EnumRuneType rune)
    {
        addRune(components, offset, y, 0, rune);
        addRune(components, -offset, y, 0, rune);
        addRune(components, 0, y, -offset, rune);
        addRune(components, 0, y, offset, rune);
    }

    public enum BreakType
    {
        REDSTONE,
        BREAK_MRS,
        BREAK_STONE,
        ACTIVATE,
        DEACTIVATE,
        EXPLOSION,
    }

    public abstract Ritual getNewCopy();
}
