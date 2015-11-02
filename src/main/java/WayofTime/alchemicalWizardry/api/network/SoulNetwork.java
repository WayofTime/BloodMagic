package WayofTime.alchemicalWizardry.api.network;

import WayofTime.alchemicalWizardry.api.AlchemicalWizardryAPI;
import WayofTime.alchemicalWizardry.api.NBTHolder;
import WayofTime.alchemicalWizardry.api.event.SoulNetworkEvent;
import WayofTime.alchemicalWizardry.api.util.helper.PlayerHelper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

@Getter
@Setter
public class SoulNetwork extends WorldSavedData {

    private int currentEssence;
    private int maxOrb;
    private final EntityPlayer player;

    public SoulNetwork(String name) {
        super(name);

        currentEssence = 0;
        maxOrb = 0;
        player = PlayerHelper.getPlayerFromUsername(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        currentEssence = nbttagcompound.getInteger(NBTHolder.NBT_CURRENTESSENCE);
        maxOrb = nbttagcompound.getInteger(NBTHolder.NBT_MAXORB);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger(NBTHolder.NBT_CURRENTESSENCE, currentEssence);
        nbttagcompound.setInteger(NBTHolder.NBT_MAXORB, maxOrb);
    }

    public int syphon(int syphon) {
        if (getCurrentEssence() >= syphon) {
            setCurrentEssence(getCurrentEssence() - syphon);
            markDirty();
            return syphon;
        }

        return 0;
    }

    public boolean syphonAndDamage(int syphon) {
        SoulNetworkEvent.PlayerDrainNetworkEvent event = new SoulNetworkEvent.PlayerDrainNetworkEvent(getPlayer(), mapName, syphon);

        if (MinecraftForge.EVENT_BUS.post(event))
            return false;

        int drain = syphon(event.syphon);

        if (drain == 0 || event.shouldDamage)
            hurtPlayer(event.syphon);

        return event.getResult() != Event.Result.DENY;
    }

    public void hurtPlayer(int syphon) {
        if (syphon < 100 && syphon > 0) {
            if (!player.capabilities.isCreativeMode) {
                player.setHealth((player.getHealth() - 1));

                if (player.getHealth() <= 0.0005f)
                    player.onDeath(AlchemicalWizardryAPI.getDamageSource());
            }
        } else if (syphon >= 100) {
            if (!player.capabilities.isCreativeMode) {
                for (int i = 0; i < ((syphon + 99) / 100); i++) {
                    player.setHealth((player.getHealth() - 1));

                    if (player.getHealth() <= 0.0005f) {
                        player.onDeath(AlchemicalWizardryAPI.getDamageSource());
                        break;
                    }
                }
            }
        }
    }
}
