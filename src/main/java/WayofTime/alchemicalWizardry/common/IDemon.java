package WayofTime.alchemicalWizardry.common;

public interface IDemon
{
    public abstract void setSummonedConditions();

    public boolean isAggro();

    public void setAggro(boolean aggro);
    
    public boolean getDoesDropCrystal();
    
    public void setDropCrystal(boolean crystal);
}
