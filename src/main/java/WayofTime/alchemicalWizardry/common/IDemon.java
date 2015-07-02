package WayofTime.alchemicalWizardry.common;

public interface IDemon
{
    void setSummonedConditions();

    boolean isAggro();

    void setAggro(boolean aggro);
    
    boolean getDoesDropCrystal();
    
    void setDropCrystal(boolean crystal);
}
