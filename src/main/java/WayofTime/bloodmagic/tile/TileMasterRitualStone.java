package WayofTime.bloodmagic.tile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.api.event.RitualEvent;
import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.registry.RitualRegistry;
import WayofTime.bloodmagic.api.ritual.IMasterRitualStone;
import WayofTime.bloodmagic.api.ritual.Ritual;
import WayofTime.bloodmagic.api.util.helper.NBTHelper;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.api.util.helper.PlayerHelper;
import WayofTime.bloodmagic.api.util.helper.RitualHelper;
import WayofTime.bloodmagic.item.ItemActivationCrystal;
import WayofTime.bloodmagic.registry.ModItems;
import WayofTime.bloodmagic.util.ChatUtil;

import com.google.common.base.Strings;

@Getter
@NoArgsConstructor
public class TileMasterRitualStone extends TileEntity implements IMasterRitualStone, ITickable
{
    private String owner;
    private boolean active;
    private boolean redstoned;
    private int activeTime;
    private int cooldown;
    private Ritual currentRitual;
    @Setter
    private EnumFacing direction = EnumFacing.NORTH;

    @Override
    public void update()
    {
        if (worldObj.isRemote)
            return;

        if (getWorld().isBlockPowered(getPos()) && isActive())
        {
            active = false;
            redstoned = true;
            stopRitual(Ritual.BreakType.REDSTONE);
            return;
        }

        if (!isActive() && !getWorld().isBlockPowered(getPos()) && isRedstoned() && getCurrentRitual() != null)
        {
            active = true;
            ItemStack crystalStack = NBTHelper.checkNBT(new ItemStack(ModItems.activationCrystal, 1, getCurrentRitual().getCrystalLevel()));
            crystalStack.getTagCompound().setString(Constants.NBT.OWNER_UUID, getOwner());
            activateRitual(crystalStack, null, getCurrentRitual());
            redstoned = false;
        }

        if (getCurrentRitual() != null && isActive())
        {
            if (activeTime % getCurrentRitual().getRefreshTime() == 0)
                performRitual(getWorld(), getPos());

            activeTime++;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        owner = tag.getString(Constants.NBT.OWNER_UUID);
        currentRitual = RitualRegistry.getRitualForId(tag.getString(Constants.NBT.CURRENT_RITUAL));
        if (currentRitual != null)
        {
            NBTTagCompound ritualTag = tag.getCompoundTag(Constants.NBT.CURRENT_RITUAL_TAG);
            if (ritualTag != null)
            {
                currentRitual.readFromNBT(ritualTag);
            }
        }
        active = tag.getBoolean(Constants.NBT.IS_RUNNING);
        activeTime = tag.getInteger(Constants.NBT.RUNTIME);
        direction = EnumFacing.VALUES[tag.getInteger(Constants.NBT.DIRECTION)];
        redstoned = tag.getBoolean(Constants.NBT.IS_REDSTONED);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        String ritualId = RitualRegistry.getIdForRitual(getCurrentRitual());
        tag.setString(Constants.NBT.OWNER_UUID, Strings.isNullOrEmpty(getOwner()) ? "" : getOwner());
        tag.setString(Constants.NBT.CURRENT_RITUAL, Strings.isNullOrEmpty(ritualId) ? "" : ritualId);
        if (currentRitual != null)
        {
            NBTTagCompound ritualTag = new NBTTagCompound();
            currentRitual.writeToNBT(ritualTag);
            tag.setTag(Constants.NBT.CURRENT_RITUAL_TAG, ritualTag);
        }
        tag.setBoolean(Constants.NBT.IS_RUNNING, isActive());
        tag.setInteger(Constants.NBT.RUNTIME, getActiveTime());
        tag.setInteger(Constants.NBT.DIRECTION, direction.getIndex());
        tag.setBoolean(Constants.NBT.IS_REDSTONED, redstoned);
    }

    @Override
    public boolean activateRitual(ItemStack activationCrystal, EntityPlayer activator, Ritual ritual)
    {
        if (PlayerHelper.isFakePlayer(activator))
            return false;

        activationCrystal = NBTHelper.checkNBT(activationCrystal);
        String crystalOwner = activationCrystal.getTagCompound().getString(Constants.NBT.OWNER_UUID);

        if (!Strings.isNullOrEmpty(crystalOwner) && ritual != null)
        {
            if (activationCrystal.getItem() instanceof ItemActivationCrystal)
            {
                int crystalLevel = ((ItemActivationCrystal) activationCrystal.getItem()).getCrystalLevel(activationCrystal);
                if (RitualHelper.canCrystalActivate(ritual, crystalLevel))
                {
                    if (!getWorld().isRemote)
                    {
                        SoulNetwork network = NetworkHelper.getSoulNetwork(crystalOwner);

                        if (!isRedstoned() && network.getCurrentEssence() < ritual.getActivationCost() && !activator.capabilities.isCreativeMode)
                        {
                            ChatUtil.sendNoSpamUnloc(activator, "chat.BloodMagic.ritual.weak");
                            return false;
                        }

                        if (currentRitual != null)
                            currentRitual.stopRitual(this, Ritual.BreakType.ACTIVATE);

                        RitualEvent.RitualActivatedEvent event = new RitualEvent.RitualActivatedEvent(this, crystalOwner, ritual, activator, activationCrystal, crystalLevel);

                        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
                        {
                            ChatUtil.sendNoSpamUnloc(activator, "chat.BloodMagic.ritual.prevent");
                            return false;
                        }

                        if (ritual.activateRitual(this, activator, crystalOwner))
                        {
                            if (!isRedstoned() && !activator.capabilities.isCreativeMode)
                                network.syphon(ritual.getActivationCost());

                            ChatUtil.sendNoSpamUnloc(activator, "chat.BloodMagic.ritual.activate");

                            this.active = true;
                            this.owner = crystalOwner;
                            this.currentRitual = ritual;

                            return true;
                        }
                    }

                    return true;
                }
            }
        } else
        {
            ChatUtil.sendNoSpamUnloc(activator, "chat.BloodMagic.ritual.notValid");
        }

        return false;
    }

    @Override
    public void performRitual(World world, BlockPos pos)
    {
        if (!world.isRemote && getCurrentRitual() != null && RitualRegistry.ritualEnabled(getCurrentRitual()) && RitualHelper.checkValidRitual(getWorld(), getPos(), RitualRegistry.getIdForRitual(currentRitual), getDirection()))
        {
            RitualEvent.RitualRunEvent event = new RitualEvent.RitualRunEvent(this, getOwner(), getCurrentRitual());

            if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
                return;

            getCurrentRitual().performRitual(this);
        }
    }

    @Override
    public void stopRitual(Ritual.BreakType breakType)
    {
        if (!getWorld().isRemote && getCurrentRitual() != null)
        {
            RitualEvent.RitualStopEvent event = new RitualEvent.RitualStopEvent(this, getOwner(), getCurrentRitual(), breakType);

            if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY)
                return;

            getCurrentRitual().stopRitual(this, breakType);
            if (breakType != Ritual.BreakType.REDSTONE)
            {
                this.currentRitual = null;
                this.active = false;
                this.activeTime = 0;
            }
        }
    }

    @Override
    public int getCooldown()
    {
        return cooldown;
    }

    @Override
    public void setCooldown(int cooldown)
    {
        this.cooldown = cooldown;
    }

    @Override
    public void setActive(boolean active)
    {
        this.active = active;
    }

    @Override
    public EnumFacing getDirection()
    {
        return direction;
    }

    @Override
    public boolean areTanksEmpty()
    {
        return false;
    }

    @Override
    public int getRunningTime()
    {
        return activeTime;
    }

    @Override
    public String getOwner()
    {
        return owner;
    }

    @Override
    public World getWorld()
    {
        return super.getWorld();
    }

    @Override
    public BlockPos getPos()
    {
        return super.getPos();
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(pos, this.getBlockMetadata(), nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        super.onDataPacket(net, packet);
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public World getWorldObj()
    {
        return getWorld();
    }

    @Override
    public BlockPos getBlockPos()
    {
        return getPos();
    }
}
