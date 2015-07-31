package WayofTime.alchemicalWizardry.common.items.potion;

public class EnhancedFillingAgent extends WeakFillingAgent
{
    public EnhancedFillingAgent()
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
        {
            switch (potionEffects)
            {
                case 1:
                    return 6;

                case 2:
                    return 4;

                case 3:
                    return 3;

                case 4:
                    return 2;

                case 5:
                    return 2;
            }
        }
        return 0;
    }
}
