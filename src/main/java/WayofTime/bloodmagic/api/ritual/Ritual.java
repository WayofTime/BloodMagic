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
public abstract class Ritual {

	public final ArrayList<RitualComponent> ritualComponents = new ArrayList<RitualComponent>();
	private final String name;
	private final int crystalLevel;
	private final int activationCost;
	private final RitualRenderer renderer;
	private final String unlocalizedName;

	private final Map<String, BlockPos[]> modableRangeMap = new HashMap<String, BlockPos[]>();

	/**
	 * @param name
	 *            - The name of the ritual
	 * @param crystalLevel
	 *            - Required Activation Crystal tier
	 * @param activationCost
	 *            - Base LP cost for activating the ritual
	 */
	public Ritual(String name, int crystalLevel, int activationCost, String unlocalizedName) {
		this(name, crystalLevel, activationCost, null, unlocalizedName);
	}

	/**
	 * Called when the player attempts to activate the ritual.
	 * 
	 * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#activateRitual(ItemStack, EntityPlayer, Ritual)}
	 * 
	 * @param masterRitualStone
	 *            - The {@link IMasterRitualStone} that the ritual is bound to
	 * @param player
	 *            - The activating player
	 * @return - Whether activation was successful
	 */
	public boolean activateRitual(IMasterRitualStone masterRitualStone, EntityPlayer player) {
		return true;
	}

	/**
	 * Called every {@link #getRefreshTime()} ticks while active.
	 * 
	 * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#performRitual(World, BlockPos)}
	 * 
	 * @param masterRitualStone
	 *            - The {@link IMasterRitualStone} that the ritual is bound to
	 */
	public abstract void performRitual(IMasterRitualStone masterRitualStone);

	/**
	 * Called when the ritual is stopped for a given {@link BreakType}.
	 * 
	 * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#stopRitual(BreakType)}
	 * 
	 * @param masterRitualStone
	 *            - The {@link IMasterRitualStone} that the ritual is bound to
	 * @param breakType
	 *            - The type of break that caused the stoppage.
	 */
	public void stopRitual(IMasterRitualStone masterRitualStone, BreakType breakType) {

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
	public int getRefreshTime() {
		return 20;
	}

	public void addBlockRange(String range, BlockPos[] defaultRange) {
		modableRangeMap.put(range, defaultRange);
	}

	/**
	 * Used to grab the range of a ritual for a given effect. The order of the
	 * blockPos array is: bottom corner, top corner.
	 * 
	 * @param range
	 *            - Range that needs to be pulled.
	 * @return - The range of the ritual effect. Array must be of size 2 and
	 *         have non-null values, with the first BlockPos having the lower
	 *         offset values and the second BlockPos having the higher offset
	 *         values
	 */
	public BlockPos[] getBlockRange(String range) {
		if (modableRangeMap.containsKey(range)) {
			return modableRangeMap.get(range);
		}
		
		return new BlockPos[] { new BlockPos(0, 0, 0), new BlockPos(0, 0, 0) };
	}

	/**
	 * @return a list of {@link RitualComponent} for checking the ritual.
	 */
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
		REDSTONE, BREAK_MRS, BREAK_STONE, ACTIVATE, DEACTIVATE, EXPLOSION,
	}
}
