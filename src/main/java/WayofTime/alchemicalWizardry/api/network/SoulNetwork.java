package WayofTime.alchemicalWizardry.api.network;

import WayofTime.alchemicalWizardry.api.NBTHolder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

@Getter
@Setter
public class SoulNetwork extends WorldSavedData {

    private int currentEssence;
    private int maxOrb;

    public SoulNetwork(String name) {
        super(name);

        currentEssence = 0;
        maxOrb = 0;
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
}
