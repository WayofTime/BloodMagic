package WayofTime.alchemicalWizardry.api.spell;

public class SpellEnhancement
{
    public static final int POWER = 0;
    public static final int EFFICIENCY = 1;
    public static final int POTENCY = 2;

    private int state = this.POWER;

    protected SpellEnhancement(int state)
    {
        this.state = state;
    }

    public int getState()
    {
        return this.state;
    }
}
