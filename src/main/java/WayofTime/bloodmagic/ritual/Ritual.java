package WayofTime.bloodmagic.ritual;

import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * Abstract class for creating new rituals. Register your ritual by annotating it with {@link RitualRegister}
 */
public abstract class Ritual {

    protected final Map<String, AreaDescriptor> modableRangeMap = new HashMap<>();
    protected final Map<String, Integer> volumeRangeMap = new HashMap<>();
    protected final Map<String, Integer> horizontalRangeMap = new HashMap<>();
    protected final Map<String, Integer> verticalRangeMap = new HashMap<>();
    private final String name;
    private final int crystalLevel;
    private final int activationCost;
    private final RitualRenderer renderer;
    private final String unlocalizedName;

    public Ritual(String name, int crystalLevel, int activationCost, RitualRenderer renderer, String unlocalizedName) {
        this.name = name;
        this.crystalLevel = crystalLevel;
        this.activationCost = activationCost;
        this.renderer = renderer;
        this.unlocalizedName = unlocalizedName;
    }

    /**
     * @param name           - The name of the ritual
     * @param crystalLevel   - Required Activation Crystal tier
     * @param activationCost - Base LP cost for activating the ritual
     */
    public Ritual(String name, int crystalLevel, int activationCost, String unlocalizedName) {
        this(name, crystalLevel, activationCost, null, unlocalizedName);
    }

    public void readFromNBT(NBTTagCompound tag) {
        NBTTagList tags = tag.getTagList("areas", 10);
        if (tags.isEmpty()) {
            return;
        }

        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound newTag = tags.getCompoundTagAt(i);
            String rangeKey = newTag.getString("key");

            NBTTagCompound storedTag = newTag.getCompoundTag("area");
            AreaDescriptor desc = this.getBlockRange(rangeKey);
            if (desc != null) {
                desc.readFromNBT(storedTag);
            }
        }
    }

    public void writeToNBT(NBTTagCompound tag) {
        NBTTagList tags = new NBTTagList();

        for (Entry<String, AreaDescriptor> entry : modableRangeMap.entrySet()) {
            NBTTagCompound newTag = new NBTTagCompound();
            newTag.setString("key", entry.getKey());
            NBTTagCompound storedTag = new NBTTagCompound();

            entry.getValue().writeToNBT(storedTag);

            newTag.setTag("area", storedTag);

            tags.appendTag(newTag);
        }

        tag.setTag("areas", tags);
    }

    /**
     * Called when the player attempts to activate the ritual.
     * <p>
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#activateRitual(ItemStack, EntityPlayer, Ritual)}
     *
     * @param masterRitualStone - The {@link IMasterRitualStone} that the ritual is bound to
     * @param player            - The activating player
     * @param owner             - Owner of the crystal activating this ritual, or the current
     *                          owner of the ritual if being reactivated.
     * @return - Whether activation was successful
     */
    public boolean activateRitual(IMasterRitualStone masterRitualStone, EntityPlayer player, UUID owner) {
        return true;
    }

    /**
     * Called every {@link #getRefreshTime()} ticks while active.
     * <p>
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#performRitual(World, BlockPos)}
     *
     * @param masterRitualStone - The {@link IMasterRitualStone} that the ritual is bound to
     */
    public abstract void performRitual(IMasterRitualStone masterRitualStone);

    /**
     * Called when the ritual is stopped for a given {@link Ritual.BreakType}.
     * <p>
     * {@link WayofTime.bloodmagic.tile.TileMasterRitualStone#stopRitual(Ritual.BreakType)}
     *
     * @param masterRitualStone - The {@link IMasterRitualStone} that the ritual is bound to
     * @param breakType         - The type of break that caused the stoppage.
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

    public void addBlockRange(String range, AreaDescriptor defaultRange) {
        modableRangeMap.put(range, defaultRange);
    }

    /**
     * Used to grab the range of a ritual for a given effect.
     *
     * @param range - Range that needs to be pulled.
     * @return -
     */
    public AreaDescriptor getBlockRange(String range) {
        if (modableRangeMap.containsKey(range)) {
            return modableRangeMap.get(range);
        }

        return null;
    }

    public List<String> getListOfRanges() {
        return new ArrayList<>(modableRangeMap.keySet());
    }

    public String getNextBlockRange(String range) {
        List<String> rangeList = getListOfRanges();

        if (rangeList.isEmpty()) {
            return "";
        }

        if (!rangeList.contains(range)) {
            return rangeList.get(0);
        }

        boolean hasMatch = false;

        for (String rangeCheck : rangeList) {
            if (hasMatch) {
                return rangeCheck;
            } else if (rangeCheck.equals(range)) {
                hasMatch = true;
            }
        }

        return rangeList.get(0);
    }

    public EnumReaderBoundaries canBlockRangeBeModified(String range, AreaDescriptor descriptor, IMasterRitualStone master, BlockPos offset1, BlockPos offset2, DemonWillHolder holder) {
        List<EnumDemonWillType> willConfig = master.getActiveWillConfig();
        int maxVolume = getMaxVolumeForRange(range, willConfig, holder);
        int maxVertical = getMaxVerticalRadiusForRange(range, willConfig, holder);
        int maxHorizontal = getMaxHorizontalRadiusForRange(range, willConfig, holder);

        return (maxVolume <= 0 || descriptor.getVolumeForOffsets(offset1, offset2) <= maxVolume) ? descriptor.isWithinRange(offset1, offset2, maxVertical, maxHorizontal) ? EnumReaderBoundaries.SUCCESS : EnumReaderBoundaries.NOT_WITHIN_BOUNDARIES : EnumReaderBoundaries.VOLUME_TOO_LARGE;
    }

    protected void setMaximumVolumeAndDistanceOfRange(String range, int volume, int horizontalRadius, int verticalRadius) {
        volumeRangeMap.put(range, volume);
        horizontalRangeMap.put(range, horizontalRadius);
        verticalRangeMap.put(range, verticalRadius);
    }

    protected boolean checkDescriptorIsWithinRange(AreaDescriptor descriptor, int maxVolume, int maxHorizontal, int maxVertical) {
        return descriptor.getVolume() <= maxVolume && descriptor.isWithinRange(maxVertical, maxHorizontal);
    }

    public int getMaxVolumeForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder) {
        return volumeRangeMap.get(range);
    }

    public int getMaxVerticalRadiusForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder) {
        return verticalRangeMap.get(range);
    }

    public int getMaxHorizontalRadiusForRange(String range, List<EnumDemonWillType> activeTypes, DemonWillHolder holder) {
        return horizontalRangeMap.get(range);
    }

    public ITextComponent getErrorForBlockRangeOnFail(EntityPlayer player, String range, IMasterRitualStone master, BlockPos offset1, BlockPos offset2) {
        AreaDescriptor descriptor = this.getBlockRange(range);
        if (descriptor == null) {
            return new TextComponentTranslation("ritual.bloodmagic.blockRange.tooBig", "?");
        }

        List<EnumDemonWillType> willConfig = master.getActiveWillConfig();
        DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(master.getWorldObj(), master.getBlockPos());

        int maxVolume = this.getMaxVolumeForRange(range, willConfig, holder);
        int maxVertical = this.getMaxVerticalRadiusForRange(range, willConfig, holder);
        int maxHorizontal = this.getMaxHorizontalRadiusForRange(range, willConfig, holder);

        if (maxVolume > 0 && descriptor.getVolumeForOffsets(offset1, offset2) > maxVolume) {
            return new TextComponentTranslation("ritual.bloodmagic.blockRange.tooBig", maxVolume);
        } else {
            return new TextComponentTranslation("ritual.bloodmagic.blockRange.tooFar", maxVertical, maxHorizontal);
        }
    }

    public ITextComponent[] provideInformationOfRitualToPlayer(EntityPlayer player) {
        return new ITextComponent[]{new TextComponentTranslation(this.getTranslationKey() + ".info")};
    }

    public ITextComponent provideInformationOfRangeToPlayer(EntityPlayer player, String range) {
        if (getListOfRanges().contains(range)) {
            return new TextComponentTranslation(this.getTranslationKey() + "." + range + ".info");
        } else {
            return new TextComponentTranslation("ritual.bloodmagic.blockRange.noRange");
        }
    }

    public abstract void gatherComponents(Consumer<RitualComponent> components);

    protected final void addRune(Consumer<RitualComponent> components, int offset1, int y, int offset2, EnumRuneType rune) {
        components.accept(new RitualComponent(new BlockPos(offset1, y, offset2), rune));
    }

    protected final void addOffsetRunes(Consumer<RitualComponent> components, int offset1, int offset2, int y, EnumRuneType rune) {
        addRune(components, offset1, y, offset2, rune);
        addRune(components, offset2, y, offset1, rune);
        addRune(components, offset1, y, -offset2, rune);
        addRune(components, -offset2, y, offset1, rune);
        addRune(components, -offset1, y, offset2, rune);
        addRune(components, offset2, y, -offset1, rune);
        addRune(components, -offset1, y, -offset2, rune);
        addRune(components, -offset2, y, -offset1, rune);
    }

    protected final void addCornerRunes(Consumer<RitualComponent> components, int offset, int y, EnumRuneType rune) {
        addRune(components, offset, y, offset, rune);
        addRune(components, offset, y, -offset, rune);
        addRune(components, -offset, y, -offset, rune);
        addRune(components, -offset, y, offset, rune);
    }

    protected final void addParallelRunes(Consumer<RitualComponent> components, int offset, int y, EnumRuneType rune) {
        addRune(components, offset, y, 0, rune);
        addRune(components, -offset, y, 0, rune);
        addRune(components, 0, y, -offset, rune);
        addRune(components, 0, y, offset, rune);
    }

    public double getWillRespectingConfig(World world, BlockPos pos, EnumDemonWillType type, List<EnumDemonWillType> willConfig) {
        return willConfig.contains(type) ? WorldDemonWillHandler.getCurrentWill(world, pos, type) : 0;
    }

    public abstract Ritual getNewCopy();

    public String getName() {
        return name;
    }

    public int getCrystalLevel() {
        return crystalLevel;
    }

    public int getActivationCost() {
        return activationCost;
    }

    public RitualRenderer getRenderer() {
        return renderer;
    }

    public String getTranslationKey() {
        return unlocalizedName;
    }

    public Map<String, AreaDescriptor> getModableRangeMap() {
        return modableRangeMap;
    }

    public Map<String, Integer> getVolumeRangeMap() {
        return volumeRangeMap;
    }

    public Map<String, Integer> getHorizontalRangeMap() {
        return horizontalRangeMap;
    }

    public Map<String, Integer> getVerticalRangeMap() {
        return verticalRangeMap;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("crystalLevel", crystalLevel)
                .append("activationCost", activationCost)
                .append("renderer", renderer)
                .append("unlocalizedName", unlocalizedName)
                .append("modableRangeMap", modableRangeMap)
                .append("volumeRangeMap", volumeRangeMap)
                .append("horizontalRangeMap", horizontalRangeMap)
                .append("verticalRangeMap", verticalRangeMap)
                .append("refreshTime", getRefreshTime())
                .append("listOfRanges", getListOfRanges())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ritual)) return false;

        Ritual ritual = (Ritual) o;

        if (crystalLevel != ritual.crystalLevel) return false;
        if (activationCost != ritual.activationCost) return false;
        if (name != null ? !name.equals(ritual.name) : ritual.name != null) return false;
        return unlocalizedName != null ? unlocalizedName.equals(ritual.unlocalizedName) : ritual.unlocalizedName == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + crystalLevel;
        result = 31 * result + activationCost;
        result = 31 * result + (unlocalizedName != null ? unlocalizedName.hashCode() : 0);
        return result;
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
