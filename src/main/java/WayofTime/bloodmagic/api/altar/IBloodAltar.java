package WayofTime.bloodmagic.api.altar;

public interface IBloodAltar {

    int getCapacity();

    int getCurrentBlood();

    EnumAltarTier getTier();

    int getProgress();

    float getSacrificeMultiplier();

    float getSelfSacrificeMultiplier();

    float getOrbMultiplier();

    float getDislocationMultiplier();

    int getBufferCapacity();

    void sacrificialDaggerCall(int amount, boolean b);

    void startCycle();

    void checkTier();
    
    boolean isActive();
    
    void setActive();
    
    int fillMainTank(int amount);

    /**
     * Will set the altar to initiate a cooldown cycle after it crafts before starting to craft again, giving the user time to interact with the altar.
     * This can only be set while the altar is not active.
     *
     * @param cooldown - How long the cooldown should last
     */
    void requestPauseAfterCrafting(int cooldown);
}
