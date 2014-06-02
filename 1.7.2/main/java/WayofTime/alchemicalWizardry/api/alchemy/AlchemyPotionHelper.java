package WayofTime.alchemicalWizardry.api.alchemy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AlchemyPotionHelper
{
    private int potionID;
    private int tickDuration;
    private int concentration;
    private int durationFactor;

    public AlchemyPotionHelper(int potionID, int tickDuration, int concentration, int durationFactor)
    {
        this.potionID = potionID;
        this.tickDuration = tickDuration;
        this.concentration = concentration;
        this.durationFactor = durationFactor;
    }

    public void setConcentration(int concentration)
    {
        this.concentration = concentration;
    }

    public void setDurationFactor(int durationFactor)
    {
        this.durationFactor = durationFactor;
    }

    public int getPotionID()
    {
        return this.potionID;
    }

    public int getTickDuration()
    {
        return this.tickDuration;
    }

    public int getConcentration()
    {
        return this.concentration;
    }

    public int getdurationFactor()
    {
        return this.durationFactor;
    }

    public PotionEffect getPotionEffect()
    {
        if (potionID == Potion.heal.id || potionID == Potion.harm.id)
        {
            return (new PotionEffect(potionID, 1, concentration));
        }

        return (new PotionEffect(potionID, (int) (tickDuration * Math.pow(0.5f, concentration) * Math.pow(8.0f / 3.0f, durationFactor)), concentration));
    }

    public static AlchemyPotionHelper readEffectFromNBT(NBTTagCompound tagCompound)
    {
        return new AlchemyPotionHelper(tagCompound.getInteger("potionID"), tagCompound.getInteger("tickDuration"), tagCompound.getInteger("concentration"), tagCompound.getInteger("durationFactor"));
    }

    public static NBTTagCompound setEffectToNBT(AlchemyPotionHelper aph)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setInteger("potionID", aph.getPotionID());
        tagCompound.setInteger("tickDuration", aph.getTickDuration());
        tagCompound.setInteger("concentration", aph.getConcentration());
        tagCompound.setInteger("durationFactor", aph.getdurationFactor());
        return tagCompound;
    }
}
