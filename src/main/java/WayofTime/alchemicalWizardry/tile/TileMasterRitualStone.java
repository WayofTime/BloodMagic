package WayofTime.alchemicalWizardry.tile;

import WayofTime.alchemicalWizardry.api.NBTHolder;
import WayofTime.alchemicalWizardry.api.event.RitualEvent;
import WayofTime.alchemicalWizardry.api.network.SoulNetwork;
import WayofTime.alchemicalWizardry.api.registry.RitualRegistry;
import WayofTime.alchemicalWizardry.api.ritual.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.ritual.LocalRitualStorage;
import WayofTime.alchemicalWizardry.api.ritual.Ritual;
import WayofTime.alchemicalWizardry.api.util.helper.NetworkHelper;
import WayofTime.alchemicalWizardry.item.ItemActivationCrystal;
import WayofTime.alchemicalWizardry.util.ChatUtil;
import com.google.common.base.Strings;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

@Getter
public class TileMasterRitualStone extends TileEntity implements IMasterRitualStone, IUpdatePlayerListBox {

    public static final int REFRESH_TIME = 0;

    private String owner;
    private boolean active;
    private int activeTime;
    private int cooldown;
    private Ritual currentRitual;
    private EnumFacing direction;

    public LocalRitualStorage storage;
    public NBTTagCompound customTag;

    public TileMasterRitualStone() {

    }

    public void readClientNBT(NBTTagCompound tag) {
        currentRitual = RitualRegistry.getRitualForId(tag.getString(NBTHolder.NBT_CURRENTRITUAL));
        active = tag.getBoolean(NBTHolder.NBT_RUNNING);
        activeTime = tag.getInteger(NBTHolder.NBT_RUNTIME);
    }

    public void writeClientNBT(NBTTagCompound tag) {
        tag.setString(NBTHolder.NBT_CURRENTRITUAL, RitualRegistry.getIdForRitual(currentRitual));
        tag.setBoolean(NBTHolder.NBT_RUNNING, active);
        tag.setInteger(NBTHolder.NBT_RUNTIME, activeTime);
    }

    @Override
    public void update() {

    }

    public void activateRitual(ItemStack activationCrystal, EntityPlayer activator) {
        activationCrystal = NBTHolder.checkNBT(activationCrystal);
        String crystalOwner = activationCrystal.getTagCompound().getString(NBTHolder.NBT_OWNER);
        Ritual ritual = null;

        if (!Strings.isNullOrEmpty(crystalOwner)) {
            if (activationCrystal.getItem() instanceof ItemActivationCrystal) {
                int crystalLevel = ((ItemActivationCrystal) activationCrystal.getItem()).getCrystalLevel(activationCrystal);

                RitualEvent.RitualActivatedEvent event = new RitualEvent.RitualActivatedEvent(this, crystalOwner, ritual, activator, activationCrystal, crystalLevel);

                if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY) {
                    ChatUtil.sendNoSpamUnloc(activator, "chat.AlchemicalWizardry.ritual.prevent");
                    return;
                }
            }
        }
    }

    @Override
    public void performRitual(World world, BlockPos pos, Ritual ritual) {
        if (ritual != null && RitualRegistry.ritualEnabled(ritual)) {
            SoulNetwork network = NetworkHelper.getSoulNetwork(getOwner(), getWorld());
            network.syphonAndDamage(ritual.getRefreshCost());
            ritual.performEffect(this);
        }
    }

    @Override
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public EnumFacing getDirection() {
        return direction;
    }

    @Override
    public NBTTagCompound getCustomRitualTag() {
        return customTag;
    }

    @Override
    public void setCustomRitualTag(NBTTagCompound tag) {
        this.customTag = tag;
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
    public LocalRitualStorage getLocalStorage() {
        return storage;
    }

    @Override
    public void setLocalStorage(LocalRitualStorage storage) {
        this.storage = storage;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    @Override
    public BlockPos getPos() {
        return super.getPos();
    }
}
