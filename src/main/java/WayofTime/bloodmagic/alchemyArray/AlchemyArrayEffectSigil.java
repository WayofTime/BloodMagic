package WayofTime.bloodmagic.alchemyArray;

import WayofTime.bloodmagic.iface.ISigil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class AlchemyArrayEffectSigil extends AlchemyArrayEffect {
    private final ISigil sigil;

    public AlchemyArrayEffectSigil(String key, ISigil sigil) {
        super(key);
        this.sigil = sigil;
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive) {
        //TODO: Need particles.
        if (sigil.hasArrayEffect()) {
            sigil.performArrayEffect(tile.getWorld(), tile.getPos());
        }

        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public AlchemyArrayEffect getNewCopy() {
        return new AlchemyArrayEffectSigil(key, sigil);
    }
}