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

	public void sacrificialDaggerCall(int amount, boolean b);

	public void startCycle();
	
	/**
	 * Will set the altar to initiate a cooldown cycle after it crafts before starting to craft again, giving the user time to interact with the altar.
	 * This can only be set while the altar is not active.
	 * @param amount
	 */
	public void requestPauseAfterCrafting(int amount);
	
	public void addToDemonBloodDuration(int dur);
	
	public boolean hasDemonBlood();
	
	public void decrementDemonBlood();
}
