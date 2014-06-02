package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

public class AltarUpgradeComponent
{
    private int speedUpgrades;
    private int efficiencyUpgrades;
    private int sacrificeUpgrades;
    private int selfSacrificeUpgrades;
    private int displacementUpgrades;
    private int altarCapacitiveUpgrades;
    private int orbCapacitiveUpgrades;

    public AltarUpgradeComponent()
    {
        speedUpgrades = 0;
        efficiencyUpgrades = 0;
        sacrificeUpgrades = 0;
        selfSacrificeUpgrades = 0;
        displacementUpgrades = 0;
        altarCapacitiveUpgrades = 0;
        orbCapacitiveUpgrades = 0;
    }

    public void addSpeedUpgrade()
    {
        speedUpgrades++;
    }

    public void addEfficiencyUpgrade()
    {
        efficiencyUpgrades++;
    }

    public void addSacrificeUpgrade()
    {
        sacrificeUpgrades++;
    }

    public void addSelfSacrificeUpgrade()
    {
        selfSacrificeUpgrades++;
    }

    public void addDisplacementUpgrade()
    {
        displacementUpgrades++;
    }

    public void addaltarCapacitiveUpgrade()
    {
        altarCapacitiveUpgrades++;
    }

    public void addorbCapacitiveUpgrade()
    {
        orbCapacitiveUpgrades++;
    }

    public int getSpeedUpgrades()
    {
        return speedUpgrades;
    }

    public int getEfficiencyUpgrades()
    {
        return efficiencyUpgrades;
    }

    public int getSacrificeUpgrades()
    {
        return sacrificeUpgrades;
    }

    public int getSelfSacrificeUpgrades()
    {
        return selfSacrificeUpgrades;
    }

    public int getDisplacementUpgrades()
    {
        return displacementUpgrades;
    }

    public int getAltarCapacitiveUpgrades()
    {
        return this.altarCapacitiveUpgrades;
    }

    public int getOrbCapacitiveUpgrades()
    {
        return this.orbCapacitiveUpgrades;
    }
}
