package WayofTime.alchemicalWizardry.api.tile;

/**
 * Created by Pokefenn.
 */
public interface IBloodAltar
{
    int getCapacity();

    int getCurrentBlood();

    int getTier();

    int getProgress();

    float getSacrificeMultiplier();

    float getSelfSacrificeMultiplier();

    float getOrbMultiplier();

    float getDislocationMultiplier();

    int getBufferCapacity();

	void sacrificialDaggerCall(int amount, boolean b);

	void startCycle();
	
	/**
	 * Will set the altar to initiate a cooldown cycle after it crafts before starting to craft again, giving the user time to interact with the altar.
	 * This can only be set while the altar is not active.
	 * @param amount
	 */
	void requestPauseAfterCrafting(int amount);
	
	void addToDemonBloodDuration(int dur);
	
	boolean hasDemonBlood();
	
	void decrementDemonBlood();
}
