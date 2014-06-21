package WayofTime.alchemicalWizardry.api.tile;

/**
 * Created by Pokefenn.
 */
public interface IBloodAltar
{

    public int getCapacity();

    public int getCurrentBlood();

    public int getTier();

    public int getProgress();

    public float getSacrificeMultiplier();

    public float getSelfSacrificeMultiplier();

    public float getOrbMultiplier();

    public float getDislocationMultiplier();

    public int getBufferCapacity();
}
