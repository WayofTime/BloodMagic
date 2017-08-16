package WayofTime.bloodmagic.api.altar;

public class AltarUpgrade {
    private int speedCount;
    private int efficiencyCount;
    private int sacrificeCount;
    private int selfSacrificeCount;
    private int displacementCount;
    private int capacityCount;
    private int orbCapacityCount;
    private int betterCapacityCount;
    private int accelerationCount;
    private int chargingCount;

    public AltarUpgrade(int speedCount, int efficiencyCount, int sacrificeCount, int selfSacrificeCount, int displacementCount, int capacityCount, int orbCapacityCount, int betterCapacityCount, int accelerationCount, int chargingCount) {
        this.speedCount = speedCount;
        this.efficiencyCount = efficiencyCount;
        this.sacrificeCount = sacrificeCount;
        this.selfSacrificeCount = selfSacrificeCount;
        this.displacementCount = displacementCount;
        this.capacityCount = capacityCount;
        this.orbCapacityCount = orbCapacityCount;
        this.betterCapacityCount = betterCapacityCount;
        this.accelerationCount = accelerationCount;
        this.chargingCount = chargingCount;
    }

    public AltarUpgrade() {
    }

    // Adders

    public AltarUpgrade addSpeed() {
        speedCount++;
        return this;
    }

    public AltarUpgrade addEfficiency() {
        efficiencyCount++;
        return this;
    }

    public AltarUpgrade addSacrifice() {
        sacrificeCount++;
        return this;
    }

    public AltarUpgrade addSelfSacrifice() {
        selfSacrificeCount++;
        return this;
    }

    public AltarUpgrade addDisplacement() {
        displacementCount++;
        return this;
    }

    public AltarUpgrade addCapacity() {
        capacityCount++;
        return this;
    }

    public AltarUpgrade addOrbCapacity() {
        orbCapacityCount++;
        return this;
    }

    public AltarUpgrade addBetterCapacity() {
        betterCapacityCount++;
        return this;
    }

    public AltarUpgrade addAcceleration() {
        accelerationCount++;
        return this;
    }

    public AltarUpgrade addCharging() {
        chargingCount++;
        return this;
    }

    public int getSpeedCount() {
        return speedCount;
    }

    public int getEfficiencyCount() {
        return efficiencyCount;
    }

    public int getSacrificeCount() {
        return sacrificeCount;
    }

    public int getSelfSacrificeCount() {
        return selfSacrificeCount;
    }

    public int getDisplacementCount() {
        return displacementCount;
    }

    public int getCapacityCount() {
        return capacityCount;
    }

    public int getOrbCapacityCount() {
        return orbCapacityCount;
    }

    public int getBetterCapacityCount() {
        return betterCapacityCount;
    }

    public int getAccelerationCount() {
        return accelerationCount;
    }

    public int getChargingCount() {
        return chargingCount;
    }
}
