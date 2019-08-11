package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.core.data.Binding;
import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.event.RitualEvent;
import WayofTime.bloodmagic.iface.IBindable;
import WayofTime.bloodmagic.item.ItemActivationCrystal;
import WayofTime.bloodmagic.ritual.AreaDescriptor;
import WayofTime.bloodmagic.ritual.EnumReaderBoundaries;
import WayofTime.bloodmagic.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.ritual.Ritual;
import WayofTime.bloodmagic.soul.DemonWillHolder;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import WayofTime.bloodmagic.tile.base.TileTicking;
import WayofTime.bloodmagic.util.ChatUtil;
import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.helper.*;
import com.google.common.base.Strings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.*;

public class TileMasterRitualStone extends TileTicking implements IMasterRitualStone {
    protected final Map<String, AreaDescriptor> modableRangeMap = new HashMap<>();
    private UUID owner;
    private SoulNetwork cachedNetwork;
    private boolean active;
    private boolean redstoned;
    private int activeTime;
    private int cooldown;
    private Ritual currentRitual;
    private EnumFacing direction = EnumFacing.NORTH;
    private boolean inverted;
    private List<EnumDemonWillType> currentActiveWillConfig = new ArrayList<>();

    @Override
    public void onUpdate() {
        if (getWorld().isRemote)
            return;

        if (isPowered() && isActive()) {
            active = false;
            redstoned = true;
            stopRitual(Ritual.BreakType.REDSTONE);
            return;
        }

        if (!isActive() && !isPowered() && isRedstoned() && getCurrentRitual() != null) {
            active = true;
            ItemStack crystalStack = NBTHelper.checkNBT(new ItemStack(RegistrarBloodMagicItems.ACTIVATION_CRYSTAL, 1, getCurrentRitual().getCrystalLevel()));
            BindableHelper.applyBinding(crystalStack, new Binding(owner, PlayerHelper.getUsernameFromUUID(owner)));
            activateRitual(crystalStack, null, getCurrentRitual());
            redstoned = false;
        }

        if (getCurrentRitual() != null && isActive()) {
            if (activeTime % getCurrentRitual().getRefreshTime() == 0)
                performRitual(getWorld(), getPos());

            activeTime++;
        }
    }

    @Override
    public void deserialize(NBTTagCompound tag) {
        owner = tag.hasUniqueId("owner") ? tag.getUniqueId("owner") : null;
        if (owner != null)
            cachedNetwork = NetworkHelper.getSoulNetwork(owner);
        currentRitual = BloodMagic.RITUAL_MANAGER.getRitual(tag.getString(Constants.NBT.CURRENT_RITUAL));
        if (currentRitual != null) {
            NBTTagCompound ritualTag = tag.getCompoundTag(Constants.NBT.CURRENT_RITUAL_TAG);
            if (!ritualTag.isEmpty()) {
                currentRitual.readFromNBT(ritualTag);
            }
        }
        active = tag.getBoolean(Constants.NBT.IS_RUNNING);
        activeTime = tag.getInteger(Constants.NBT.RUNTIME);
        direction = EnumFacing.VALUES[tag.getInteger(Constants.NBT.DIRECTION)];
        redstoned = tag.getBoolean(Constants.NBT.IS_REDSTONED);

        for (EnumDemonWillType type : EnumDemonWillType.values()) {
            if (tag.getBoolean("EnumWill" + type)) {
                currentActiveWillConfig.add(type);
            }
        }
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tag) {
        String ritualId = BloodMagic.RITUAL_MANAGER.getId(getCurrentRitual());
        if (owner != null)
            tag.setUniqueId("owner", owner);
        tag.setString(Constants.NBT.CURRENT_RITUAL, Strings.isNullOrEmpty(ritualId) ? "" : ritualId);
        if (currentRitual != null) {
            NBTTagCompound ritualTag = new NBTTagCompound();
            currentRitual.writeToNBT(ritualTag);
            tag.setTag(Constants.NBT.CURRENT_RITUAL_TAG, ritualTag);
        }
        tag.setBoolean(Constants.NBT.IS_RUNNING, isActive());
        tag.setInteger(Constants.NBT.RUNTIME, getActiveTime());
        tag.setInteger(Constants.NBT.DIRECTION, direction.getIndex());
        tag.setBoolean(Constants.NBT.IS_REDSTONED, redstoned);

        for (EnumDemonWillType type : currentActiveWillConfig) {
            tag.setBoolean("EnumWill" + type, true);
        }

        return tag;
    }

    @Override
    public boolean activateRitual(ItemStack activationCrystal, @Nullable EntityPlayer activator, Ritual ritual) {
        if (PlayerHelper.isFakePlayer(activator))
            return false;

        Binding binding = ((IBindable) activationCrystal.getItem()).getBinding(activationCrystal);
        if (binding != null && ritual != null) {
            if (activationCrystal.getItem() instanceof ItemActivationCrystal) {
                int crystalLevel = ((ItemActivationCrystal) activationCrystal.getItem()).getCrystalLevel(activationCrystal);
                if (RitualHelper.canCrystalActivate(ritual, crystalLevel)) {
                    if (!getWorld().isRemote) {
                        SoulNetwork network = NetworkHelper.getSoulNetwork(binding);

                        if (!isRedstoned() && network.getCurrentEssence() < ritual.getActivationCost() && (activator != null && !activator.capabilities.isCreativeMode)) {
                            activator.sendStatusMessage(new TextComponentTranslation("chat.bloodmagic.ritual.weak"), true);
                            return false;
                        }

                        if (currentRitual != null)
                            currentRitual.stopRitual(this, Ritual.BreakType.ACTIVATE);

                        RitualEvent.RitualActivatedEvent event = new RitualEvent.RitualActivatedEvent(this, binding.getOwnerId(), ritual, activator, activationCrystal, crystalLevel);

                        if (MinecraftForge.EVENT_BUS.post(event)) {
                            if (activator != null)
                                activator.sendStatusMessage(new TextComponentTranslation("chat.bloodmagic.ritual.prevent"), true);
                            return false;
                        }

                        if (ritual.activateRitual(this, activator, binding.getOwnerId())) {
                            if (!isRedstoned() && (activator != null && !activator.capabilities.isCreativeMode))
                                network.syphon(ticket(ritual.getActivationCost()));

                            if (activator != null)
                                activator.sendStatusMessage(new TextComponentTranslation("chat.bloodmagic.ritual.activate"), true);

                            this.active = true;
                            this.owner = binding.getOwnerId();
                            this.cachedNetwork = network;
                            this.currentRitual = ritual;

                            if (!checkBlockRanges(ritual.getModableRangeMap()))
                                addBlockRanges(ritual.getModableRangeMap());

                            notifyUpdate();
                            return true;
                        }
                    }

                    notifyUpdate();
                    return true;
                }
            }
        } else {
            if (activator != null)
                activator.sendStatusMessage(new TextComponentTranslation("chat.bloodmagic.ritual.notValid"), true);
        }

        return false;
    }

    @Override
    public void performRitual(World world, BlockPos pos) {
        if (!world.isRemote && getCurrentRitual() != null && BloodMagic.RITUAL_MANAGER.enabled(BloodMagic.RITUAL_MANAGER.getId(currentRitual), false)) {
            if (RitualHelper.checkValidRitual(getWorld(), getPos(), currentRitual, getDirection())) {
                Ritual ritual = getCurrentRitual();
                RitualEvent.RitualRunEvent event = new RitualEvent.RitualRunEvent(this, getOwner(), ritual);

                if (MinecraftForge.EVENT_BUS.post(event))
                    return;

                if (!checkBlockRanges(getCurrentRitual().getModableRangeMap()))
                    addBlockRanges(getCurrentRitual().getModableRangeMap());

                getCurrentRitual().performRitual(this);
            } else {
                stopRitual(Ritual.BreakType.BREAK_STONE);
            }
        }
    }

    @Override
    public void stopRitual(Ritual.BreakType breakType) {
        if (!getWorld().isRemote && getCurrentRitual() != null) {
            RitualEvent.RitualStopEvent event = new RitualEvent.RitualStopEvent(this, getOwner(), getCurrentRitual(), breakType);

            if (MinecraftForge.EVENT_BUS.post(event))
                return;

            getCurrentRitual().stopRitual(this, breakType);
            if (breakType != Ritual.BreakType.REDSTONE) {
                this.currentRitual = null;
                this.active = false;
                this.activeTime = 0;
            }
            notifyUpdate();
        }
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public EnumFacing getDirection() {
        return direction;
    }

    public void setDirection(EnumFacing direction) {
        this.direction = direction;
    }

    @Override
    public boolean areTanksEmpty() {
        return false;
    }

    @Override
    public int getRunningTime() {
        return activeTime;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public SoulNetwork getOwnerNetwork() {
        return cachedNetwork;
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    @Override
    public BlockPos getPos() {
        return super.getPos();
    }

    @Override
    public World getWorldObj() {
        return getWorld();
    }

    @Override
    public BlockPos getBlockPos() {
        return getPos();
    }

    @Override
    public String getNextBlockRange(String range) {
        if (this.currentRitual != null) {
            return this.currentRitual.getNextBlockRange(range);
        }

        return "";
    }

    @Override
    public void provideInformationOfRitualToPlayer(EntityPlayer player) {
        if (this.currentRitual != null) {
            ChatUtil.sendNoSpam(player, this.currentRitual.provideInformationOfRitualToPlayer(player));
        }
    }

    @Override
    public void provideInformationOfRangeToPlayer(EntityPlayer player, String range) {
        if (this.currentRitual != null && this.currentRitual.getListOfRanges().contains(range)) {
            ChatUtil.sendNoSpam(player, this.currentRitual.provideInformationOfRangeToPlayer(player, range));
        }
    }

    @Override
    public void setActiveWillConfig(EntityPlayer player, List<EnumDemonWillType> typeList) {
        this.currentActiveWillConfig = typeList;
    }

    @Override
    public EnumReaderBoundaries setBlockRangeByBounds(EntityPlayer player, String range, BlockPos offset1, BlockPos offset2) {
        AreaDescriptor descriptor = this.getBlockRange(range);
        DemonWillHolder holder = WorldDemonWillHandler.getWillHolder(world, getBlockPos());

        EnumReaderBoundaries modificationType = currentRitual.canBlockRangeBeModified(range, descriptor, this, offset1, offset2, holder);
        if (modificationType == EnumReaderBoundaries.SUCCESS)
            descriptor.modifyAreaByBlockPositions(offset1, offset2);

        return modificationType;
    }

    @Override
    public List<EnumDemonWillType> getActiveWillConfig() {
        return new ArrayList<>(currentActiveWillConfig);
    }

    @Override
    public void provideInformationOfWillConfigToPlayer(EntityPlayer player, List<EnumDemonWillType> typeList) {
        //There is probably an easier way to make expanded chat messages
        if (typeList.size() >= 1) {
            Object[] translations = new TextComponentTranslation[typeList.size()];
            StringBuilder constructedString = new StringBuilder("%s");

            for (int i = 1; i < typeList.size(); i++) {
                constructedString.append(", %s");
            }

            for (int i = 0; i < typeList.size(); i++) {
                translations[i] = new TextComponentTranslation("tooltip.bloodmagic.currentBaseType." + typeList.get(i).name.toLowerCase());
            }

            ChatUtil.sendNoSpam(player, new TextComponentTranslation("ritual.bloodmagic.willConfig.set", new TextComponentTranslation(constructedString.toString(), translations)));
        } else {
            ChatUtil.sendNoSpam(player, new TextComponentTranslation("ritual.bloodmagic.willConfig.void"));
        }
    }

    public boolean isPowered() {
        if (inverted)
            return !getWorld().isBlockPowered(getPos());

        return getWorld().isBlockPowered(getPos());
    }

    public SoulNetwork getCachedNetwork() {
        return cachedNetwork;
    }

    public void setCachedNetwork(SoulNetwork cachedNetwork) {
        this.cachedNetwork = cachedNetwork;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isRedstoned() {
        return redstoned;
    }

    public void setRedstoned(boolean redstoned) {
        this.redstoned = redstoned;
    }

    public int getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(int activeTime) {
        this.activeTime = activeTime;
    }

    public Ritual getCurrentRitual() {
        return currentRitual;
    }

    public void setCurrentRitual(Ritual currentRitual) {
        this.currentRitual = currentRitual;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public List<EnumDemonWillType> getCurrentActiveWillConfig() {
        return currentActiveWillConfig;
    }

    public void setCurrentActiveWillConfig(List<EnumDemonWillType> currentActiveWillConfig) {
        this.currentActiveWillConfig = currentActiveWillConfig;
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

    @Override
    public void addBlockRange(String range, AreaDescriptor defaultRange) {
        modableRangeMap.putIfAbsent(range, defaultRange.copy());
    }

    @Override
    public void addBlockRanges(Map<String, AreaDescriptor> blockRanges) {
        for (Map.Entry<String, AreaDescriptor> entry : blockRanges.entrySet()) {
            modableRangeMap.putIfAbsent(entry.getKey(), entry.getValue().copy());
        }
    }

    @Override
    public void setBlockRange(String range, AreaDescriptor defaultRange) {
        modableRangeMap.put(range, defaultRange.copy());
    }

    @Override
    public void setBlockRanges(Map<String, AreaDescriptor> blockRanges) {
        for (Map.Entry<String, AreaDescriptor> entry : blockRanges.entrySet()) {
            modableRangeMap.put(entry.getKey(), entry.getValue().copy());
        }
    }

    public boolean checkBlockRanges(Map<String, AreaDescriptor> blockRanges) {
        for (Map.Entry<String, AreaDescriptor> entry : blockRanges.entrySet()) {
            if (modableRangeMap.get(entry.getKey()) == null)
                return false;
        }
        return true;
    }
}
