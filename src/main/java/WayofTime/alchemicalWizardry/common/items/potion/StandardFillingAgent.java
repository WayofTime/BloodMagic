package WayofTime.alchemicalWizardry.common.items.potion;

public class StandardFillingAgent extends WeakFillingAgent
{
    public StandardFillingAgent()
    {
        super();
    }

    @Override
    public int getFilledAmountForPotionNumber(int potionEffects)
    {
        if (potionEffects == 0)
        {
            return 8;
        }

        if (potionEffects >= 1 && potionEffects <= 3)
        {
            return (int) (4 * (Math.pow(0.5f, potionEffects - 1) + 0.01f));
        }

        return 0;
    }
}
