package WayofTime.bloodmagic.altar;

public interface IBloodAltar {
    int getCapacity();

    int getCurrentBlood();

    AltarTier getTier();

    int getProgress();

    float getSacrificeMultiplier();

    float getSelfSacrificeMultiplier();

    float getOrbMultiplier();

    float getDislocationMultiplier();

    float getConsumptionMultiplier();

    float getConsumptionRate();

    int getChargingRate();

    int getChargingFrequency();

    int getTotalCharge();

    int getLiquidRequired();

    int getBufferCapacity();

    void sacrificialDaggerCall(int amount, boolean isSacrifice);

    void startCycle();

    void checkTier();

    boolean isActive();

    void setActive();

    int fillMainTank(int amount);

    /**
     * Will set the altar to initiate a cooldown cycle after it crafts before
     * starting to craft again, giving the user time to interact with the altar.
     * This can only be set while the altar is not active.
     *
     * @param cooldown - How long the cooldown should last
     */
    void requestPauseAfterCrafting(int cooldown);
}
