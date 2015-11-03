package WayofTime.bloodmagic.tile;

import WayofTime.bloodmagic.api.NBTHolder;
import WayofTime.bloodmagic.api.event.RitualEvent;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.RitualHelper;
import WayofTime.bloodmagic.item.ItemActivationCrystal;
import WayofTime.bloodmagic.util.ChatUtil;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class TileMasterRitualStone extends TileEntity implements IMasterRitualStone, IUpdatePlayerListBox {

    public static final int REFRESH_TIME = 0;

    private String owner;
    private boolean active;
    private int activeTime;
    private int cooldown;
    private Ritual currentRitual;
    private EnumFacing direction;

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

    @Override
    public boolean activateRitual(ItemStack activationCrystal, EntityPlayer activator) {
        activationCrystal = NBTHolder.checkNBT(activationCrystal);
        String crystalOwner = activationCrystal.getTagCompound().getString(NBTHolder.NBT_OWNER);
        Ritual ritual = RitualRegistry.getRitualForId("");

        if (!Strings.isNullOrEmpty(crystalOwner) && ritual != null) {
            if (activationCrystal.getItem() instanceof ItemActivationCrystal) {
                int crystalLevel = ((ItemActivationCrystal) activationCrystal.getItem()).getCrystalLevel(activationCrystal);
                if (RitualHelper.canCrystalActivate(ritual, crystalLevel)) {

                    RitualEvent.RitualActivatedEvent event = new RitualEvent.RitualActivatedEvent(this, crystalOwner, ritual, activator, activationCrystal, crystalLevel);

                    if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY) {
                        ChatUtil.sendNoSpamUnloc(activator, "chat.BloodMagic.ritual.prevent");
                        return false;
                    }
                }
            }
        }

        return false;
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
    public void stopRitual() {

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
    public boolean areTanksEmpty() {
        return false;
    }

    @Override
    public int getRunningTime() {
        return activeTime;
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
